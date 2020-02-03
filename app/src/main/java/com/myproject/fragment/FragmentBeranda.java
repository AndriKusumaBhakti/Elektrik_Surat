package com.myproject.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.myproject.R;
import com.myproject.activity.DashboardActivity;
import com.myproject.api.TaskSuratSaya;
import com.myproject.aplication.APP;
import com.myproject.aplication.Config;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.request.RequestJenisSurat;
import com.myproject.model.response.ResponseFileSurat;
import com.myproject.util.CircleTransform;
import com.myproject.util.Constants;

import java.util.List;

public class FragmentBeranda extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ViewAnimator selectProfileAnimator;
    private boolean isShowSelectProfile;
    private TextView name_user;
    private TextView alamat_uset;
    private TextView ttl_user;
    private TextView profile_home_name;
    private LinearLayout lprofile;
    private TextView rememberNow;
    private TextView statusRember;
    private ImageView profile_home_avatar, barAvatar;

    private IntentIntegrator qrScanner;

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;
    LoadingDialog loadingData = new LoadingDialog();

    public FragmentBeranda() {}

    public static FragmentBeranda newInstance() {
        return newInstance("","");
    }

    public static FragmentBeranda newInstance(String param1, String param2) {
        FragmentBeranda fragment = new FragmentBeranda();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(getBaseActivity());
        profiles = account.getAllLanguage();
        for(int i = 0; i<profiles.size(); i++){
            if ((profiles.get(i).getNik()).equals(APP.getStringPref(getBaseActivity(), Preference.NIK))){
                accountEntity = profiles.get(i);
            }
        }
    }

    @Override
    public void initView(View view) {
        profile_home_avatar = (ImageView) view.findViewById(R.id.profile_home_avatar);
        barAvatar = (ImageView) view.findViewById(R.id.barAvatar);
        selectProfileAnimator = (ViewAnimator) view.findViewById(R.id.select_profile_animator);
        name_user = (TextView) view.findViewById(R.id.name_user);
        alamat_uset = (TextView) view.findViewById(R.id.alamat_uset);
        ttl_user = (TextView) view.findViewById(R.id.ttl_user);

        rememberNow = (TextView) view.findViewById(R.id.rememberNow);
        statusRember = (TextView) view.findViewById(R.id.statusRember);

        profile_home_name = (TextView) view.findViewById(R.id.profile_home_name);
        profile_home_name.setText(accountEntity.getNama());
        lprofile = (LinearLayout) view.findViewById(R.id.lprofile);
        getDataSurat();
        qrScanner = new IntentIntegrator(getBaseActivity());
        qrScanner.setBeepEnabled(false);
    }

    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                /*processAnimateSelectorProfile();*/
                getFragmentManager().popBackStack();
            }

            @Override
            public void onRightIconClick() {
                /*qrScanner.setPrompt(getResources().getString(R.string.hintscanQr));
                qrScanner.initiateScan();*/
                getDataSurat();
            }

            @Override
            public void onRight2IconClick() {
                /*getDataSurat();*/
            }
        });
        lprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDetailProfil fragment = new FragmentDetailProfil();
                DashboardActivity dashboard = DashboardActivity.instance;
                dashboard.pushFragmentDashboard(fragment);
            }
        });
    }

    @Override
    public void updateUI() {
        getBaseActivity().setLeftIcon(R.drawable.back_white);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(R.drawable.icon_reload_white);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(true);
        getBaseActivity().setLeftView(accountEntity.getFoto());

        Glide.with(getActivity())
                .load(Config.getUrlFoto()+accountEntity.getFoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .transform(new CircleTransform(getBaseActivity()))
                .into(profile_home_avatar);
    }

    @Override
    public String getPageTitle() {
        return "Beranda";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_beranda;
    }

    private void processAnimateSelectorProfile(){
        name_user.setText(accountEntity.getNama());
        alamat_uset.setText(accountEntity.getAlamat());
        ttl_user.setText(accountEntity.getTempat()+", "+accountEntity.getTanggal());
        Glide.with(getActivity())
                .load(Config.getUrlFoto()+accountEntity.getFoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .transform(new CircleTransform(getBaseActivity()))
                .into(barAvatar);

        final Animation inAnim = AnimationUtils.loadAnimation(getBaseActivity(), R.anim.slide_in_top);
        final Animation outAnim = AnimationUtils.loadAnimation(getBaseActivity(), R.anim.slide_in_bottom);

        selectProfileAnimator.setInAnimation(inAnim);
        selectProfileAnimator.setOutAnimation(outAnim);

        if(isShowSelectProfile) {
            isShowSelectProfile = false;
            selectProfileAnimator.setAnimation(outAnim);
            selectProfileAnimator.setVisibility(View.GONE);
        }else{
            isShowSelectProfile = true;
            selectProfileAnimator.setAnimation(inAnim);
            selectProfileAnimator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        account = new Account(getBaseActivity());
        profiles = account.getAllLanguage();
        for(int i = 0; i<profiles.size(); i++){
            if ((profiles.get(i).getNik()).equals(APP.getStringPref(getBaseActivity(), Preference.NIK))){
                accountEntity = profiles.get(i);
            }
        }
        updateUI();
        //DashboardActivity.instance.showBottomMenu();
    }

    private void getDataSurat(){
        loadingData.setCancelLable(true);
        loadingData.show(getBaseActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
        RequestJenisSurat request = new RequestJenisSurat();
        request.setMethod("reqSurat");
        request.setNik_penduduk(accountEntity.getNik());
        TaskSuratSaya task = new TaskSuratSaya(getBaseActivity()) {
            @Override
            protected void onSuccess(ResponseFileSurat response) {
                removeTask(this);
                if(isFragmentSafety()) {
                    if (loadingData.isResumed()) {
                        loadingData.dismiss();
                    }
                    if (response.getStatus()){
                        if (response.getResult().size()>0){
                            rememberNow.setText(response.getResult().get(0).getJenis_surat());
                            if (response.getResult().get(0).getApprove().equals("1")){
                                statusRember.setText("Disetujui");
                            }else{
                                statusRember.setText("Tunda");
                            }
                        }
                    }else{
                        statusRember.setText("status");
                        rememberNow.setText("Tidak ada pemberitahuan");
                        Toast.makeText(getBaseActivity(), response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            protected void onFailed(String message) {
                removeTask(this);
                if(isFragmentSafety()) {
                    if (loadingData.isResumed()) {
                        loadingData.dismiss();
                    }
                    statusRember.setText("status");
                    rememberNow.setText("Tidak ada pemberitahuan");
                    getBaseActivity().showAlertDialog("Pesan", message);
                }
            }
        };
        registerTask(task);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
    }
}
