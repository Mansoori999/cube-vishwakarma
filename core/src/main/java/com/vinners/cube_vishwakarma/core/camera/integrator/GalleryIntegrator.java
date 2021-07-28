package com.vinners.cube_vishwakarma.core.camera.integrator;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;

import com.vinners.cube_vishwakarma.core.camera.ImageCallback;
import com.vinners.cube_vishwakarma.core.camera.ImagesSizes;
import com.vinners.cube_vishwakarma.core.camera.RequestSource;

import java.io.File;


/**
 * Created by Himanshu on 4/30/2018.
 */

public class GalleryIntegrator extends Integrator {

    /**
     * Activity Context
     */
    private Activity activityRef;

    /**
     * Refrence of calling Activity
     */
    private Fragment fragmentReference;
    /**
     * File instance on which we will be operating
     */
    private File mFile;
    private String imageDirectoryName;


    /**
     * Required Size of the image
     * this can be one of the {@link com.lighting.camera.ImagesSizes.ImageSize}
     */
    private int requiredImageSize;

    /**
     * @param activityRef path where the new image we clicked should be stored
     */
    public GalleryIntegrator(Activity activityRef) {
        super(activityRef);
        this.activityRef = activityRef;
    }

    public GalleryIntegrator(Fragment fragmentReference) {
        super(fragmentReference.getActivity());
        this.fragmentReference = fragmentReference;
        this.activityRef = fragmentReference.getActivity();
    }

    public void setRequiredImageSize(@ImagesSizes.ImageSize int requiredImageSize) {
        this.requiredImageSize = requiredImageSize;
    }


    public void setImageDirectoryName(String directoryName) {
        this.imageDirectoryName = directoryName;
    }

    /**
     * Initiates Image Pick Process
     *
     * @throws ActivityNotFoundException if There is no application to pick image
     */
    public void initiateImagePick() throws ActivityNotFoundException {

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        if (fragmentReference == null)
            activityRef.startActivityForResult(intent, REQUEST_IMAGE_PICK);
        else if (fragmentReference != null)
            fragmentReference.startActivityForResult(intent, REQUEST_IMAGE_PICK);


    }

    @Override
    public void parseResults(int requestCode, int resultCode, Intent data, ImageCallback resultCallback) {

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                String imagePath = getRealPathFromURI(activityRef, selectedImageUri);
                if (imagePath != null) {
                    mFile = new File(imagePath);
                    getParsedBitmapResult(mFile, imageDirectoryName, RequestSource.SOURCE_GALLERY, requiredImageSize, resultCallback);
                }
            }
        }

    }





    /**
     * Parses #contentURI and returns Absolute path of image
     *
     * @param c
     * @param contentURI
     * @return
     */
    private String getRealPathFromURI(Context c, Uri contentURI) {
        Cursor cursor = c.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }

}
