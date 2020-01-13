package com.myproject.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit.mime.TypedFile;

public class FunctionUtil {

    private static NetworkInfo networkInfo;

    public static boolean checkStringNumber(String data){
        String newData = StringUtil.checkNullString(data);
        newData = newData.replaceAll(",","");
        newData = newData.replaceAll("\\.","");

        try
        {
            double d = Double.parseDouble(newData);
        }
        catch(Exception nfe)
        {
           // nfe.printStackTrace();
            return false;
        }
        return true;
    }

    /*
    public static void initFacebookConfig(){
        String fb_app_id = String.valueOf(R.string.app_id);
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(fb_app_id)
                .setNamespace("medictrust")
                .setPermissions(Constants.permissions)
                .build();

        SimpleFacebook.setConfiguration(configuration);
    }
    */

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                 return contentUri.getPath();
            }
            else{
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = connectivity.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
            return true;
        }
        return false;

    }

    public static int getDiffYears(Date first, Date last) {
        try{
            Calendar a = getCalendar(first);
            Calendar b = getCalendar(last);
            int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
            if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
                diff--;
            }
            return diff;
        }
        catch(Exception e){
            e.printStackTrace();
            return 1;
        }
    }


    public static double getDiffYearsDay(Date first, Date last) {
        try{
            long duration  = last.getTime() - first.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

            long years = diffInDays/365;

            DecimalFormat decimalFormat = new DecimalFormat("0.##");
            String ages = decimalFormat.format(years);

            return (Double.parseDouble(ages));
        }
        catch(Exception e){
            e.printStackTrace();
            return 1.0;
        }
    }


    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static boolean isVideoFile(String file){
        if(file.contains(".mp4")||file.contains(".3gp")||file.contains(".webm")||file.contains(".ts")||file.contains(".mov")||file.contains(".avi")){
            return true;
        }
        return false;
    }

    public static boolean isVideoFile2(String file){
        if(file.equalsIgnoreCase("mp4")||file.equalsIgnoreCase("3gp")||file.equalsIgnoreCase("webm")||file.equalsIgnoreCase("ts")||file.equalsIgnoreCase("mov")||file.equalsIgnoreCase("avi")||file.equalsIgnoreCase("video")){
            return true;
        }
        return false;
    }

    public static TypedFile makeVideoTypedFile(String videoPath) {
        if(videoPath.contains(".mp4")) {
            return new TypedFile("video/mp4", new File(videoPath));
        } else if(videoPath.contains(".3gp")) {
            return new TypedFile("video/3gpp", new File(videoPath));
        } else if(videoPath.contains(".webm")) {
            return new TypedFile("video/webm", new File(videoPath));
        } else if(videoPath.contains(".mov")) {
            return new TypedFile("video/quicktime", new File(videoPath));
        } else if(videoPath.contains(".avi")) {
            return new TypedFile("video/x-msvideo", new File(videoPath));
        } else if(videoPath.contains(".ts")) {
            return new TypedFile("video/MP2T", new File(videoPath));
        } else {
            return new TypedFile("video/*", new File(videoPath));
        }
    }

    public static TypedFile makeTypedFile(String imagePath) {
        return new TypedFile("image/jpg", new File(imagePath));
    }

    public static TypedFile makePDFTypedFile(String imagePath) {
        return new TypedFile("application/pdf", new File(imagePath));
    }

    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                activity.INPUT_METHOD_SERVICE);
        if(imm!=null && activity.getCurrentFocus()!=null)
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static ImageCropper croppingImage(Activity activity, Uri imageUri) {
        ImageCropper imageCrop = new ImageCropper(activity);
        imageCrop.setOutput(160, 160);
        imageCrop.setScale(true);
        imageCrop.setAspect(1, 1);
        imageCrop.setData(imageUri);
        imageCrop.create();
        return imageCrop;
    }

    public static int dp2px(Context ctx, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public static int getVersionCode(Context context) {
        int v = 0;
        try {
            v = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return v;
    }

    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, "");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see MediaStore.Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH,thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

}
