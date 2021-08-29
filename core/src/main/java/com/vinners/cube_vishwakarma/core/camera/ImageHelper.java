package com.vinners.cube_vishwakarma.core.camera;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ImageHelper {

    public static final byte ROTATE_CW = 1;
    public static final byte ROTATE_ACW = 2;

    public static final byte REAR_CAMERA = 3;
    public static final byte FRONT_CAMERA = 4;


    /**
     * Rotates image
     *
     * @param fileBytes
     * @param phoneOrientationDegrees
     * @param cameraUsed
     * @return
     */
    public static byte[] rotateImageIfRequired(byte[] fileBytes, int phoneOrientationDegrees, byte cameraUsed) {

        if ((phoneOrientationDegrees + "").equalsIgnoreCase(""))
            return fileBytes;

        byte[] data = null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.length);

        ByteArrayOutputStream outputStream = null;

        try {
            if (cameraUsed == REAR_CAMERA) {
                switch (phoneOrientationDegrees) {
                    case 0:
                        bitmap = rotateImage(bitmap, 360);
                        break;
                    case 90:
                        bitmap = rotateImage(bitmap, 270);
                        break;
                    case 180:
                        bitmap = rotateImage(bitmap, 180);
                        break;
                    case 270:
                        bitmap = rotateImage(bitmap, 90);
                        break;
                }
            } else if (cameraUsed == FRONT_CAMERA) {
                switch (phoneOrientationDegrees) {
                    case 0:
                        bitmap = rotateImage(bitmap, 0);
                        break;
                    case 90:
                        bitmap = rotateImage(bitmap, 90);
                        break;
                    case 180:
                        bitmap = rotateImage(bitmap, 180);
                        break;
                    case 270:
                        bitmap = rotateImage(bitmap, 270);
                        break;
                }
            }
            outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            data = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // Intentionally blank
            }
        }

        return data;
    }

    public static void showClickedImage(Context context, Bitmap imageClicked) {

        if (imageClicked == null)
            return;

        android.app.Dialog builder = new android.app.Dialog(context);

        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(imageClicked);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }


    public static void showClickedImage(Context context, String imagePath) {

        android.app.Dialog builder = new android.app.Dialog(context);

        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(context);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();

    }

    public static boolean saveTo(String fileFullPath, Bitmap bitmapToSave) {

        FileOutputStream out = null;
        File file = new File(fileFullPath);
        try {
            out = new FileOutputStream(file);
            bitmapToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * Rotates the image using postRotate on a matrix
     *
     * @param img    image to roatate
     * @param degree Degree by which image should be rotated
     * @return rotated image
     */
    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public static boolean rotateImage90degreesPresentAt(String imagePath, byte rotateClockwiseOrAntiClockwise) {
        try {
            Bitmap imageToRotate = BitmapFactory.decodeFile(imagePath);
            if (imageToRotate == null)
                return false;

            if (rotateClockwiseOrAntiClockwise == ROTATE_CW)
                imageToRotate = rotateImage(imageToRotate, -90);
            else if (rotateClockwiseOrAntiClockwise == ROTATE_ACW)
                imageToRotate = rotateImage(imageToRotate, 90);

            saveTo(imagePath, imageToRotate);
            imageToRotate.recycle();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static File createImageFile(String directoryName) {

        // Create a unique file name for image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "IMG_" + timeStamp;

        return createImageFile(directoryName, imageFileName, "jpg");
    }


    public static File createImageFile(String directoryName, String imageName) {
        return createImageFile(directoryName, imageName, "jpg");
    }

    public static File createImageFile(File parentFile) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File imageFile = new File(parentFile, imageFileName + "." + "jpg");
        return imageFile;
    }

    public static File createPdfFile(String directoryName, String fileNameWithExtension) {

        // Create a unique file name for image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "IMG_" + timeStamp;

        // Getting a reference to Target storage directory (AuditCanonImages)
        File storageDir = Environment
                .getExternalStoragePublicDirectory(directoryName);

        // /Creating directory if not made already
        storageDir.mkdirs();
        File image = new File(storageDir, fileNameWithExtension);
        return image;
    }

    public static File createImageFileInRootDirectory(String directoryName, @Nullable String imageName, @NonNull String fileFormat) {

        // Getting a reference to Target storage directory (AuditCanonImages)
        File storageDir = Environment.getExternalStoragePublicDirectory(directoryName);
        File sub = new File(storageDir, getTodaysDateInDDMMYYYY());

        sub.mkdirs();

        File image = new File(sub, imageName + "." + fileFormat);
        return image;
    }

    public static File createImageFile(String directoryName, @Nullable String imageName, @NonNull String fileFormat) {

        // Getting a reference to Target storage directory (AuditCanonImages)
        File storageDir = Environment
                .getExternalStoragePublicDirectory(directoryName);

        // Creating directory if not made already
        storageDir.mkdirs();

        File image = new File(storageDir, imageName + "." + fileFormat);

        return image;
    }

    public static void copyAndClose(InputStream inputStream, FileOutputStream fileOutputStream) throws IOException {

        try {
            byte[] fileReader = new byte[inputStream.available()];

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(fileReader, 0, read);
            }
            fileOutputStream.flush();

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }


    public static Bitmap scaleImageForThumbNail(Bitmap imageToScale) {

        ByteArrayOutputStream bytearrayoutputstream;
        byte[] BYTE;

        final long targetArea = 76800;
        final int targetHeightPortrait = 320;
        final int targetHeightLandscape = 240;

        float targetWidthPortrait;
        float targetWidthLandscape;

        long currentArea = 0;
        float currentToTargetRatio;

        if (imageToScale == null) {
            return null;
        } else {
            bytearrayoutputstream = new ByteArrayOutputStream();
            currentArea = imageToScale.getWidth() * imageToScale.getHeight();

            if (currentArea < targetArea)
                return imageToScale;
            else {
                Bitmap tempBitmap;
                if (imageToScale.getHeight() > imageToScale.getWidth()) {
                    //Height is more than width hence picture was clicked in portrait mode
                    currentToTargetRatio = (float) imageToScale.getHeight() / targetHeightPortrait;
                    targetWidthPortrait = imageToScale.getWidth() / currentToTargetRatio;
                    tempBitmap = Bitmap.createScaledBitmap(imageToScale, (int) targetWidthPortrait, targetHeightPortrait, false);
                } else {
                    //Height is more than width hence picture was clicked in landscape mode
                    currentToTargetRatio = (float) imageToScale.getWidth() / targetHeightLandscape;
                    targetWidthLandscape = imageToScale.getHeight() / currentToTargetRatio;
                    tempBitmap = Bitmap.createScaledBitmap(imageToScale, targetHeightLandscape, (int) targetWidthLandscape, false);

                }

                tempBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytearrayoutputstream);

                BYTE = bytearrayoutputstream.toByteArray();

                return BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
            }

        }

    }


    public static Bitmap scaleImageAtOptimum(Bitmap imageToScale) {

        ByteArrayOutputStream bytearrayoutputstream;
        byte[] BYTE;

        final long targetArea = 1228800;
        final int targetHeightPortrait = 1280;
        final int targetHeightLandscape = 960;

        float targetWidthPortrait;
        float targetWidthLandscape;

        long currentArea = 0;
        float currentToTargetRatio;

        if (imageToScale == null) {
            return null;
        } else {
            bytearrayoutputstream = new ByteArrayOutputStream();
            currentArea = imageToScale.getWidth() * imageToScale.getHeight();

            if (currentArea < targetArea)
                return imageToScale;
            else {
                Bitmap tempBitmap;
                if (imageToScale.getHeight() > imageToScale.getWidth()) {
                    //Height is more than width hence picture was clicked in portrait mode
                    currentToTargetRatio = (float) imageToScale.getHeight() / targetHeightPortrait;
                    targetWidthPortrait = imageToScale.getWidth() / currentToTargetRatio;
                    tempBitmap = Bitmap.createScaledBitmap(imageToScale, (int) targetWidthPortrait, targetHeightPortrait, false);
                } else {
                    //Height is more than width hence picture was clicked in landscape mode
                    currentToTargetRatio = (float) imageToScale.getWidth() / targetHeightLandscape;
                    targetWidthLandscape = imageToScale.getHeight() / currentToTargetRatio;
                    tempBitmap = Bitmap.createScaledBitmap(imageToScale, targetHeightLandscape, (int) targetWidthLandscape, false);

                }

                tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytearrayoutputstream);

                BYTE = bytearrayoutputstream.toByteArray();

                return BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
            }

        }

    }

    public static String getRealPathFromURI(Context c, Uri contentURI) {
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


    public static Bitmap stampImageWithText(Bitmap bitmap, ArrayList<String> arrayList, String checkboxText) {
        int i;
        float f;
        double height = (double) bitmap.getHeight();
        float imageHeight = bitmap.getHeight();
        float imageWidth = bitmap.getWidth();


        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();

        if (imageHeight > imageWidth) {
            i = (int) (height * 0.030d);
            f = (float) i;
            paint.setTextSize(f);
        } else {
            i = (int) (height * 0.080d);
            f = (float) i;
            paint.setTextSize(f);
        }
//        final int height= bitmap.getHeight() ;
        final int width= bitmap.getWidth();
        float h= (float) height;
        float w= (float) width;
        paint.setColor(Color.parseColor("#F88017"));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(bitmap, 0.0f, 5.0f, null);
        Paint ractPaint = new Paint();
        ractPaint.setStyle(Paint.Style.STROKE);
        ractPaint.setAntiAlias(true);
        ractPaint.setColor(Color.parseColor("#F88017"));
        canvas.drawRoundRect(new RectF(5.0f,
                imageHeight - 5.0f * f,
                imageWidth - 5.0f,
                (imageHeight - 5.0f * f) + 4.7f * f), 6, 6, ractPaint);
        float f2 = f;

        for (int k = 0; k < arrayList.size(); k++) {
            if (k == 0) {
                canvas.drawText(arrayList.get(k), 10.0f, f2, paint);
            } else if (k == 1) {
                if (checkboxText.equals("Before")) {
                    paint.setColor(Color.RED);
                } else if (checkboxText.equals("Working")) {
                    paint.setColor(Color.YELLOW);
                } else if (checkboxText.equals("After")) {
                    paint.setColor(Color.parseColor("#F88017"));
                }
                canvas.drawText(arrayList.get(k), 10.0f, (imageHeight + 1.5f * f2 - 3.8f * f), paint);
            } else {
                paint.setColor(Color.parseColor("#F88017"));
                canvas.drawText(arrayList.get(k), 40.0f, (imageHeight + 1.5f * f2 - 5.3f * f), paint);
                f2 += f;
            }
        }
        return createBitmap;
    }

    public static String getTodaysDateInDDMMYYYY() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        return simpleDateFormat.format(newCalendar.getTime());
    }
}

