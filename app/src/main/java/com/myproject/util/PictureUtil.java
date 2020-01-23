package com.myproject.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.myproject.aplication.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureUtil {

    final public static int MAX_IMAGE_DIMENSION = 720;

    public static File BitmapToFile(Bitmap bm, String dir, String name) {
        File file = new File(dir, name);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Uri getImageRotated(Context context, Uri f, String filePictures) {
        try {
            Bitmap finalData = getCorrectlyOrientedImage(context, f, filePictures);

            String newImages = "Md_crop_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + Config.CACHE_FOLDER);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File fileImage = new File(dir,newImages);

            String imagePath = fileImage.getPath();

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);

            if (imageType.equalsIgnoreCase("png")) {
                finalData.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg")) {
                finalData.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }

            fOut.flush();
            fOut.close();

            finalData.recycle();
            return Uri.fromFile(fileImage);

            /*
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), finalData, "cropsData", null);
            finalData.recycle();
            return Uri.parse(path);
            */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Bitmap getBitmapFromUri(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);

        srcBitmap = BitmapFactory.decodeStream(is);
        is.close();

        return srcBitmap;
    }


    protected static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri, String filePicture) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri,filePicture);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }


    protected static int getOrientation(Context context, Uri photoUri, String filePictures) {
        if(filePictures.equals("")){
            try{
                Cursor cursor = context.getContentResolver().query(photoUri,
                        new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

                if (cursor.getCount() != 1) {
                    return -1;
                }

                cursor.moveToFirst();
                return cursor.getInt(0);
            }
            catch(Exception e){
                e.printStackTrace();
                return 0;
            }

        }
        else{
            File file = new File(filePictures);
            try{
                ExifInterface exif = new ExifInterface(file.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                int angle = 0;

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }
                return angle;
            }
            catch(Exception e){
                e.printStackTrace();
                return 0;
            }
        }
    }


}
