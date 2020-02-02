package com.myproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.myproject.activity.BaseActivity;
import com.myproject.activity.DashboardActivity;
import com.myproject.activity.DashboardKepalaDesa;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.fragment.LoginFragment;
import com.myproject.util.FunctionUtil;

public class StartActivity extends BaseActivity {
    AsyncTask waitingTask;
    boolean isPushToMain;
    private String notifModelName;
    private boolean isFromPushNotif;
    final public static String PUSH_NOTIF_MODEL_NAME = "PUSH_NOTIF_MODEL_NAME";
    final public static String IS_FROM_PUSH_NOTIF = "IS_FROM_PUSH_NOTIF";
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isPushToMain = false;
        if (getIntent().getExtras() != null) {
            isFromPushNotif = getIntent().getExtras().getBoolean(IS_FROM_PUSH_NOTIF);
            notifModelName = getIntent().getExtras().getString(PUSH_NOTIF_MODEL_NAME);
            if (notifModelName != null && notifModelName.contains("TES") && isFromPushNotif) {
                replaceFragmentwithTag(R.id.container, LoginFragment.newInstance(), false, "LOGIN");
            }
        }
        super.onCreate(savedInstanceState);
        runApps();
    }

    private void runApps() {
        if(FunctionUtil.hasPermissions(this, PERMISSIONS)){
            loading();
        }else{
            ActivityCompat.requestPermissions(this, PERMISSIONS, 0);
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if(FunctionUtil.hasPermissions(this, PERMISSIONS)){
                    loading();
                }else {
                    StartActivity.this.finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(waitingTask != null){
            try{
                if(waitingTask.getStatus() ==  AsyncTask.Status.RUNNING){
                    waitingTask.cancel(true);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void loading(){
        if(isPushToMain == false) {

            isPushToMain = true;
            waitingTask = new AsyncTask<Void, Integer, String>() {
                @Override
                protected String doInBackground(Void... voids) {

                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "";
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    String email = APP.getStringPref(StartActivity.this, Preference.TOKEN);
                    chekTokenAtApp(email);
                }
            };

            waitingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        }
    }

    @Override
    public void setUICallbacks() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void updateUI() {
        /*setLeftIcon(0);
        setRightIcon2(0);
        setRightIcon(0);
        showDisplayLogoTitle(false);
        changeHomeToolbarBackground(false);*/
    }

    private void chekTokenAtApp(String email){
        if(email.isEmpty()){
            replaceFragmentwithTag(R.id.container, LoginFragment.newInstance(), false, "LOGIN");
        } else{
            String role = APP.getStringPref(StartActivity.this, Preference.ROLE);
            if (role.equals("1")){
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(this, DashboardKepalaDesa.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
