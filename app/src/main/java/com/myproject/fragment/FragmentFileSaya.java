package com.myproject.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.myproject.R;
import com.myproject.activity.DashboardActivity;
import com.myproject.adapter.AdapterJenisSurat;
import com.myproject.adapter.AdapterSuratSaya;
import com.myproject.api.TaskSuratSaya;
import com.myproject.aplication.APP;
import com.myproject.aplication.Config;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.ModelJenisSurat;
import com.myproject.model.ModelSuratSaya;
import com.myproject.model.request.RequestJenisSurat;
import com.myproject.model.response.ResponseFileSurat;
import com.myproject.util.CircleTransform;
import com.myproject.util.DownloadTask;

import java.util.ArrayList;
import java.util.List;

public class FragmentFileSaya extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ViewAnimator selectProfileAnimator;
    private boolean isShowSelectProfile;
    private TextView name_user;
    private TextView alamat_uset;
    private TextView ttl_user;

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    private ArrayList<ModelSuratSaya> list;
    private AdapterSuratSaya adapter;

    private ProgressBar loading;
    private LinearLayout emptyData;
    private RecyclerView list_data;
    private MaterialRefreshLayout refresh_data;
    private LinearLayoutManager mLayoutManager;
    private ImageView barAvatar;

    ProgressDialog mProgressDialog;

    public FragmentFileSaya() {}

    public static FragmentFileSaya newInstance() {
        return newInstance("","");
    }

    public static FragmentFileSaya newInstance(String param1, String param2) {
        FragmentFileSaya fragment = new FragmentFileSaya();
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
        selectProfileAnimator = (ViewAnimator) view.findViewById(R.id.select_profile_animator);
        barAvatar = (ImageView) view.findViewById(R.id.barAvatar);
        name_user = (TextView) view.findViewById(R.id.name_user);
        alamat_uset = (TextView) view.findViewById(R.id.alamat_uset);
        ttl_user = (TextView) view.findViewById(R.id.ttl_user);
        refresh_data = (MaterialRefreshLayout) view.findViewById(R.id.refresh_data);
        emptyData = (LinearLayout) view.findViewById(R.id.emptyData);
        list_data = (RecyclerView) view.findViewById(R.id.list_data);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        getAllData();
    }

    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                //processAnimateSelectorProfile();
                getFragmentManager().popBackStack();
            }

            @Override
            public void onRightIconClick() {

            }

            @Override
            public void onRight2IconClick() {

            }
        });
        refresh_data.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAllData();
                        materialRefreshLayout.finishRefresh();
                    }
                }, 3000);
                materialRefreshLayout.finishRefreshLoadMore();
            }

            @Override
            public void onfinish() {
            }
        });
    }

    @Override
    public void updateUI() {
        getBaseActivity().setLeftIcon(R.drawable.back_white);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(0);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(true);
        getBaseActivity().setLeftView(accountEntity.getFoto());
    }

    @Override
    public String getPageTitle() {
        return "File Saya";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_file_saya;
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
        //DashboardActivity.instance.showBottomMenu();
    }

    private void getAllData(){
        RequestJenisSurat request = new RequestJenisSurat();
        request.setMethod("reqSurat");
        request.setNik_penduduk(accountEntity.getNik());
        list = new ArrayList<>();
        adapter = new AdapterSuratSaya(getActivity(), list, new MyAdapterListener());
        TaskSuratSaya task = new TaskSuratSaya(getBaseActivity()) {
            @Override
            protected void onSuccess(ResponseFileSurat response) {
                removeTask(this);
                loading.setVisibility(View.GONE);
                if (response.getStatus()){
                    if (response.getResult() != null || response.getResult().size()>0) {
                        for (int i = 0; i < response.getResult().size(); i++) {
                            list.add(response.getResult().get(i));
                        }
                        list_data.setVisibility(View.VISIBLE);
                        emptyData.setVisibility(View.GONE);
                        mLayoutManager = new LinearLayoutManager(getBaseActivity());
                        list_data.setHasFixedSize(true);
                        list_data.setLayoutManager(mLayoutManager);
                        list_data.setAdapter(adapter);
                    }else{
                        getBaseActivity().showAlertDialog("Pesan", response.getMessage());
                    }
                }else{
                    list_data.setVisibility(View.GONE);
                    emptyData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onFailed(String message) {
                removeTask(this);
                loading.setVisibility(View.GONE);
                list_data.setVisibility(View.GONE);
                emptyData.setVisibility(View.VISIBLE);
                getBaseActivity().showAlertDialog("Pesan", message);
            }
        };
        registerTask(task);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
    }

    private class MyAdapterListener implements AdapterSuratSaya.ClickListener{
        @Override
        public void onDownload(ModelSuratSaya item) {
            if (item.getApprove().equals("1")){
                downloadFile(item);
            }else{
                getBaseActivity().showAlertDialog("Pesan", "Tunggu kepala desa menyetujui permintaan anda");
            }
        }
    }

    private void downloadFile(ModelSuratSaya item){
        mProgressDialog = new ProgressDialog(getBaseActivity());
        mProgressDialog.setMessage(item.getJenis_surat());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(getBaseActivity(), mProgressDialog, item.getJenis_surat());
        downloadTask.execute(item.getLink_surat());

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }
}
