package com.myproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.myproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    Context context;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    private boolean isImageViewer;

    private Bitmap bitmap;

    public ImageLoader(Context context) {
        this.context = context;
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
        isImageViewer = false;
    }

    public ImageLoader(Context context, boolean isImageViewer) {
        this.context = context;
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
        this.isImageViewer = isImageViewer;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    int stub_id = R.mipmap.ic_launcher;

    public void DisplayImage(String url, int loader, ImageView imageView) {


        if(url.contains("www.")||url.contains("http")||url.contains("localhost")){
            stub_id = loader;
            imageViews.put(imageView, url);
            Bitmap bitmap = memoryCache.get(url);
            if (bitmap != null) {
                this.bitmap = bitmap;
                imageView.setImageBitmap(bitmap);
            } else {
                queuePhoto(url, imageView);
                imageView.setImageResource(loader);
            }
        }
        else{
            DisplayLocalImage(url,loader,imageView);
        }


    }

    public void DisplayUri(Uri uri, int loader, ImageView imageView) {
        stub_id = loader;
        Bitmap bitmap = decodesUri(uri);
        if (bitmap != null) {
            this.bitmap = bitmap;
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(loader);
        }
    }

    public void DisplayLocalImage(String path, int loader, ImageView imageView) {

        stub_id = loader;
        imageViews.put(imageView, path);
        Bitmap bitmap = memoryCache.get(path);
        if (bitmap != null) {
            this.bitmap = bitmap;
            imageView.setImageBitmap(bitmap);
        } else {
            Bitmap bitmaps = getLocalBitmap(path);

            if (bitmaps != null) {
                this.bitmap = bitmaps;
                memoryCache.put(path, bitmaps);
                imageView.setImageBitmap(bitmaps);
            }
            else {
                imageView.setImageResource(loader);
            }
        }
    }


    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    public Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);

        // from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        // from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            FunctionUtil.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public Bitmap getLocalBitmap(String path) {
        File f = new File(path);

        // from SD cache
        Bitmap b = decodeFile(f);
        if (b != null){
            return b;
        }

        return null;

    }

    public static Bitmap decodeResource(Resources res, int resId){
        try {
            boolean scaling = true;
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeResource(res, resId, o);

            final int REQUIRED_SIZE = 250;

            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (scaling) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeResource(res, resId, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            boolean scaling = true;
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = isImageViewer?1000:250;


            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (scaling) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    // decodes image and scales it to reduce memory consumption
    private Bitmap decodesUri(Uri uri){
        try {
            boolean scaling = true;

            // decode image size

            InputStream is = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, o);
            is.close();

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = isImageViewer?1000:250;


            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (scaling) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            Bitmap srcBitmap;
            // decode with inSampleSize
            is = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            //return BitmapFactory.decodeStream(is, null, o2);
            srcBitmap = BitmapFactory.decodeStream(is, null, o2);
            is.close();

            return srcBitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public Bitmap decodePhotoFile(File f) {
        try {
            boolean scaling = true;
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 500;


            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (scaling) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Uri decodeUriFile(Uri f) {
        try {

            Bitmap FinalData = getCorrectlyOrientedImage(this.context, f);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            FinalData.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.context.getContentResolver(), FinalData, "cropsData", null);

            return Uri.parse(path);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


    public static int getOrientation(Context context, Uri photoUri) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PHOTO_INFO, 0);
        String filePictures = prefs.getString(Constants.PHOTOPATH, "");

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


    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;

        rotatedWidth = dbo.outWidth;
        rotatedHeight = dbo.outHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }


        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > 720 || rotatedHeight > 720) {
            boolean scaling = true;
            final int REQUIRED_SIZE = 720;
            int width_tmp = dbo.outWidth, height_tmp = dbo.outHeight;
            int scale = 1;
            while (scaling) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            srcBitmap = BitmapFactory.decodeStream(is, null, o2);
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


    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
