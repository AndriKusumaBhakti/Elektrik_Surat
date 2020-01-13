package com.myproject.util;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.myproject.aplication.APP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private PowerManager.WakeLock mWakeLock;
    ProgressDialog mProgressDialog;
    String nama_file;

    public DownloadTask(Context context, ProgressDialog mProgressDialog, String nama_file) {
        this.context = context;
        this.mProgressDialog = mProgressDialog;
        this.nama_file = nama_file;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            int fileLength = connection.getContentLength();

            input = connection.getInputStream();
            output = new FileOutputStream("/sdcard/Download/"+this.nama_file+".pdf");

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                if (fileLength > 0)
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        if (result != null) {
            APP.log("Download error:" + result);
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "File berhasil di download", Toast.LENGTH_SHORT).show();
            File file = new File(Environment.getExternalStorageDirectory()+"/Download/"+this.nama_file+".pdf");
            if (file.exists()) {
                viewPdf(file);
            }else{
                Toast.makeText(context, "Gagal membuka file", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void viewPdf(File path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        APP.log("Download error:" + path.toString());
        intent.setDataAndType(FileProvider.getUriForFile(context,"com.myproject.provider",path), "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
        }
    }
}
