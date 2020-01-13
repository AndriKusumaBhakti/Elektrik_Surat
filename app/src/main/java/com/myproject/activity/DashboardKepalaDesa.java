package com.myproject.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.myproject.R;
import com.myproject.StartActivity;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.fragment.BaseFragment;
import com.myproject.fragment.FragmentPengumuman;
import com.myproject.fragment.FragmentScanQr;
import com.myproject.fragment.FragmentSettingAkun;
import com.myproject.fragment.LoadingDialog;
import com.myproject.util.Constants;
import com.myproject.util.FunctionUtil;
import com.myproject.util.StringUtil;

import java.util.List;

public class DashboardKepalaDesa extends BaseActivity {
    public static DashboardKepalaDesa instance;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView nav_header_textView;
    private Toolbar toolbar;
    private int navIndex = 0;
    private Bundle bundle = new Bundle();
    private Boolean cekNav = false;
    View headerView;
    ImageView nav_header_imageView;
    private LinearLayout log_out;
    private IntentIntegrator qrScanner;
    private String notifModelName;
    private boolean isFromPushNotif;
    final public static String PUSH_NOTIF_MODEL_NAME = "PUSH_NOTIF_MODEL_NAME";
    final public static String IS_FROM_PUSH_NOTIF = "IS_FROM_PUSH_NOTIF";

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        if (savedInstanceState == null){
            cekNav = true;
        }
        if (getIntent().getExtras() != null) {
            isFromPushNotif = getIntent().getExtras().getBoolean(IS_FROM_PUSH_NOTIF);
            notifModelName = getIntent().getExtras().getString(PUSH_NOTIF_MODEL_NAME);
            if (notifModelName != null && notifModelName.contains("TEST") && isFromPushNotif) {
                replaceFragmentwithTag(R.id.container, FragmentPengumuman.newInstance(), false, "HOME");
            }
        }
    }

    @Override
    public void initView() {
        requestAppPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0, 0);
        account = new Account(getApplication());
        profiles = account.getAllLanguage();
        for(int i = 0; i<profiles.size(); i++){
            if ((profiles.get(i).getNik()).equals(APP.getStringPref(getApplication(), Preference.NIK))){
                accountEntity = profiles.get(i);
            }
        }
        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        nav_header_textView = (TextView) headerView.findViewById(R.id.nav_header_textView);
        if (!StringUtil.checkNullString(accountEntity.getNama()).isEmpty()) {
            nav_header_textView.setText(accountEntity.getNama());
        }
        nav_header_imageView = (ImageView) headerView.findViewById(R.id.nav_header_imageView);
        log_out = (LinearLayout) findViewById(R.id.log_out);
        setActionBarTitle("Request Surat");
        navigationView.getMenu().getItem(1).setChecked(true);
        replaceFragmentwithTag(R.id.container, FragmentPengumuman.newInstance(), false, "Beranda");
        nav_header_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.getMenu().getItem(1).setChecked(true);
                replaceFragmentwithTag(R.id.container, FragmentPengumuman.newInstance(), false, "Beranda");
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.getMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(false);
                navigationView.getMenu().getItem(2).setChecked(false);
                drawer.closeDrawer(GravityCompat.START);
                logoutOn();
            }
        });
        qrScanner = new IntentIntegrator(this);
        qrScanner.setBeepEnabled(false);
    }

    @Override
    public void setUICallbacks() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
                if (id == R.id.nav_home) {
                    item.setChecked(true);
                    navIndex = 0;
                    setActionBarTitle("Scan QR Code");
                    qrScanner.setPrompt(getResources().getString(R.string.hintscanQr));
                    qrScanner.initiateScan();
                }else if (id == R.id.nav_search) {
                    item.setChecked(true);
                    navIndex = 1;
                    setActionBarTitle("Request Surat");
                    replaceFragmentwithTag(R.id.container, FragmentPengumuman.newInstance(), false, "Request Surat");
                }else if (id == R.id.setting_account) {
                    item.setChecked(true);
                    navIndex = 2;
                    setActionBarTitle("Pengaturan Akun");
                    replaceFragmentwithTag(R.id.container, FragmentSettingAkun.newInstance(), false, "Setting Akun");
                }
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.dashboard_kepala_desa;
    }

    @Override
    public void updateUI() {

    }

    private void logoutOn(){
        if (FunctionUtil.isConnected(getApplication())) {
            final LoadingDialog dialog = new LoadingDialog();
            Bundle b = new Bundle();
            b.putString(LoadingDialog.DIALOG_TITLE, getResources().getString(R.string.confirmation));
            b.putString(LoadingDialog.DIALOG_CONTENT, getResources().getString(R.string.are_you_sure_logout));
            b.putString(LoadingDialog.LEFT_BUTTON, getResources().getString(R.string.yes));
            b.putString(LoadingDialog.RIGHT_BUTTON, getResources().getString(R.string.cancel));
            b.putBoolean(LoadingDialog.LOADING_ICON, false);
            b.putBoolean(LoadingDialog.CLOSE_BTN, true);
            dialog.setArguments(b);
            dialog.setListener(new LoadingDialog.LoadingDialogListener() {
                @Override
                public void onLeftButtonClick() {
                    APP.setPreference(getApplication(), Preference.TOKEN,"");
                    APP.setPreference(getApplication(), Preference.ROLE,"");
                    account.deleteAllInterval();
                    Intent intent = new Intent(getApplication(), StartActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onRightButtonClick() {

                }
            });
            dialog.show(this.getFragmentManager(), LoadingDialog.class.getName());
        } else {
            showAlertDialog(getResources().getString(R.string.alert), getResources().getString(R.string.error_connection_offline));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            FragmentScanQr fragment = new FragmentScanQr();
            DashboardKepalaDesa dashboard = DashboardKepalaDesa.instance;
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

    public void showLeftMenu() {
        drawer.openDrawer(GravityCompat.START);
    }

    public void pushFragmentDashboard(BaseFragment fragment) {
        hideLeftMenu();
        pushFragmentToHome(fragment);
    }

    public void hideLeftMenu() {
        drawer.closeDrawers();
        navigationView.setVisibility(View.GONE);
    }

    public void pushFragmentToHome(BaseFragment fragment){
        replaceFragment(R.id.container, "Beranda", fragment, true);
    }

    public void pushFragment(BaseFragment fragment){
        replaceFragment(R.id.container, "Hasil Scan QR", fragment, false);
    }
}
