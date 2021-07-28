package com.vinners.cube_vishwakarma.core.camera.integrator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.vinners.cube_vishwakarma.core.camera.ImageCallback;
import com.vinners.cube_vishwakarma.core.camera.ImageFormats;
import com.vinners.cube_vishwakarma.core.camera.ImageHelper;
import com.vinners.cube_vishwakarma.core.camera.ImagesSizes;
import com.vinners.cube_vishwakarma.core.camera.RequestSource;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Camera Intent Integrator simply removes the boiler plate code that need to generate
 * while using native camera app to click images
 *
 * @author Himanshu
 * @Example
 */
public class CameraIntegrator extends Integrator {

    /**
     * Activity Context
     */
    private Activity activityReference;


    private Fragment fragmentReference;

    /**
     * File instance on which we will be operating
     */
    private File mFile;

    /**
     * This Value can be given
     */
    private String imagePath;
    private String imageDirectoryName;
    private String imageName;
    private String imageFormat;

    /**
     * Required Size of the image
     * this can be one of the {@link ImagesSizes}
     */
    private int requiredImageSize;


    /**
     * @param activityReference path where the new image we clicked should be stored
     */
    public CameraIntegrator(Activity activityReference) {
        super(activityReference);
        this.activityReference = activityReference;
    }


    public CameraIntegrator(Fragment fragmentReference) {
        super(fragmentReference.getContext());
        this.fragmentReference = fragmentReference;
        this.activityReference = fragmentReference.getActivity();
    }

    /**
     * Setting the absolute path where image will be stored
     *
     * @param imagePath
     */
    public void setImagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Setting the file where image will be stores
     *
     * @param mFile
     */
    public void setFile(File mFile) {
        this.mFile = mFile;
    }

    public void setImageDirectoryName(String directoryName) {
        this.imageDirectoryName = directoryName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageFormat(@ImageFormats.ImageFormat String format) {
        this.imageFormat = format;
    }

    public void setRequiredImageSize(@ImagesSizes.ImageSize int requiredImageSize) {
        this.requiredImageSize = requiredImageSize;
    }

    /**
     * Opens Camera for capturing image
     */
    public void initiateCapture() throws IOException {

        //Lets create a file if user hasn't created one where image will be stored
        if (imagePath != null)
            mFile = new File(imagePath);
        else if (imageDirectoryName != null && imageName == null && imageFormat == null)
            mFile = ImageHelper.createImageFile(imageDirectoryName);
        else if (imageDirectoryName != null && imageName != null && imageFormat == null)
            mFile = ImageHelper.createImageFile(imageDirectoryName, imageName);
        else if (imageDirectoryName != null && imageName != null && imageFormat != null)
            mFile = ImageHelper.createImageFile(imageDirectoryName, imageName, imageFormat);

        if (mFile == null)
            throw new IOException("Details Where image should be stored are not provided, you can call ");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activityReference.getPackageManager()) != null) {

            // Continue only if the File was successfully created
            if (mFile != null) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
                }else {
                    File file = new File(Uri.fromFile(mFile).getPath());
                    Uri photoUri = FileProvider.getUriForFile(activityReference.getApplicationContext(), activityReference.getPackageName() + ".provider", file);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }


                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (fragmentReference == null)
                    activityReference.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                else
                    fragmentReference.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }


    @Override
    public void parseResults(int requestCode, int resultCode, Intent data, ImageCallback resultCallback) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (mFile != null) {
                getParsedBitmapResult(mFile, null, RequestSource.SOURCE_CAMERA, requiredImageSize, resultCallback);
            }
        }
    }


}

