package com.myproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.myproject.R;
import com.myproject.aplication.APP;
import com.myproject.fragment.BaseFragment;
import com.myproject.fragment.FragmentBeranda;
import com.myproject.fragment.FragmentFileSaya;
import com.myproject.fragment.FragmentJenisSurat;
import com.myproject.fragment.FragmentLainnya;
import com.myproject.fragment.FragmentScanQr;
import com.myproject.fragment.FragmentScanQrPenduduk;
import com.myproject.util.Constants;
import com.myproject.util.FunctionUtil;

import java.lang.ref.WeakReference;

public class DashboardActivity extends BaseActivity {
    private Toolbar toolbar;
    private AHBottomNavigation bottomNavigation;

    public static DashboardActivity instance;
    private int currentSelectedTab;
    private WeakReference<Activity> wrActivity;
    private String notifModelName;
    private boolean isFromPushNotif;
    final static String CURRENT_INDEX = "CURRENT_INDEX";
    final static String ALREADY_SYNCING = "ALREADY_SYNCING";

    final public static String PUSH_NOTIF_MODEL_NAME = "PUSH_NOTIF_MODEL_NAME";
    final public static String IS_FROM_PUSH_NOTIF = "IS_FROM_PUSH_NOTIF";
    final public static String IS_FROM_LOADING_PAGE = "IS_FROM_LOADING_PAGE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wrActivity = new WeakReference<Activity>(this);
        instance = this;
        if (getIntent().getExtras() != null) {
            isFromPushNotif = getIntent().getExtras().getBoolean(IS_FROM_PUSH_NOTIF);
            notifModelName = getIntent().getExtras().getString(PUSH_NOTIF_MODEL_NAME);
            if (notifModelName != null && notifModelName.contains("TEST") && isFromPushNotif) {
                replaceFragmentwithTag(R.id.container, FragmentBeranda.newInstance(), false, "HOME");
            }
        }
    }

    public void showBottomMenu() {
        bottomNavigation.setVisibility(View.VISIBLE);
    }

    public void hideBottomMenu() {
        bottomNavigation.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        requestAppPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0, 0);
        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        bottomNavigation= (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        setSupportActionBar(toolbar);

        this.createNavigationItems();

        replaceFragmentwithTag(R.id.container, FragmentBeranda.newInstance(), false, "HOME");
    }

    private void createNavigationItems()
    {
        //Initialize Item
        AHBottomNavigationItem home = new AHBottomNavigationItem(getResources().getString(R.string.beranda), R.drawable.icon_home);
        AHBottomNavigationItem absenMasuk = new AHBottomNavigationItem(getResources().getString(R.string.jenis_surat), R.drawable.icon_request_surat);
        AHBottomNavigationItem absenKeluar=new AHBottomNavigationItem(getResources().getString(R.string.file_saya), R.drawable.icon_file_saya);
        AHBottomNavigationItem more=new AHBottomNavigationItem(getResources().getString(R.string.lainnya), R.drawable.icon_more);

        //Add Item to Navigation Bar
        bottomNavigation.addItem(home);
        bottomNavigation.addItem(absenMasuk);
        bottomNavigation.addItem(absenKeluar);
        bottomNavigation.addItem(more);

        //set properties
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        //set current item
        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavigation.setAccentColor(Color.parseColor("#4285F4"));
        bottomNavigation.setInactiveColor(Color.parseColor("#231f20"));

        bottomNavigation.setBehaviorTranslationEnabled(false);
    }


    @Override
    public void setUICallbacks() {
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        currentSelectedTab = 0;
                        showBottomMenu();
                        replaceFragmentwithTag(R.id.container, FragmentBeranda.newInstance(), false, "HOME");
                        break;
                    case 1:
                        currentSelectedTab = 1;
                        showBottomMenu();
                        replaceFragmentwithTag(R.id.container, FragmentJenisSurat.newInstance(), false, "JENIS SURAT");
                        break;
                    case 2:
                        currentSelectedTab = 2;
                        showBottomMenu();
                        replaceFragmentwithTag(R.id.container, FragmentFileSaya.newInstance(), false, "FILE SAYA");
                        break;
                    case 3:
                        currentSelectedTab = 3;
                        showBottomMenu();
                        replaceFragmentwithTag(R.id.container, FragmentLainnya.newInstance(), false, "MORE");
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.dashboard_activity;
    }

    @Override
    public void updateUI() {

    }

    public void pushFragmentDashboard(BaseFragment fragment) {
        hideBottomMenu();
        if (currentSelectedTab == 0) {
            pushFragmentToHome(fragment);
        } else if (currentSelectedTab == 1) {
            pushFragmentToAbsenMasuk(fragment);
        } else if (currentSelectedTab == 2) {
            pushFragmentToAbsenKeluar(fragment);
        } else if (currentSelectedTab == 3) {
            pushFragmentToMore(fragment);
        }
    }

    public void pushFragmentToHome(BaseFragment fragment){
        replaceFragment(R.id.container, "Home", fragment, true);
    }
    public void pushFragmentToAbsenMasuk(BaseFragment fragment) {
        replaceFragment(R.id.container, "Jenis Surat", fragment, true);
    }
    public void pushFragmentToAbsenKeluar(BaseFragment fragment) {
        replaceFragment(R.id.container, "File Saya", fragment, true);
    }
    public void pushFragmentToMore(BaseFragment fragment) {
        replaceFragment(R.id.container, "Lainnya", fragment, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            FragmentScanQrPenduduk fragment = new FragmentScanQrPenduduk();
            DashboardActivity dashboard = DashboardActivity.instance;
            Bundle bundle = new Bundle();
            if (result.getContents() != null) {
                bundle.putString(Constants.QR_CODE, result.getContents());
            }else{
                bundle.putString(Constants.QR_CODE, "");
            }
            fragment.setArguments(bundle);
            dashboard.pushFragmentDashboard(fragment);
        }
    }
}
