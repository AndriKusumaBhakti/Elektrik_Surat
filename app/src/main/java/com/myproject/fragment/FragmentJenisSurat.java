package com.myproject.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.myproject.R;
import com.myproject.activity.DashboardActivity;
import com.myproject.adapter.AdapterJenisSurat;
import com.myproject.api.TaskCreateSurat;
import com.myproject.api.TaskJenisSurat;
import com.myproject.api.TaskRequestJenisSurat;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.ModelJenisSurat;
import com.myproject.model.ModelResponse;
import com.myproject.model.request.RequestAddSurat;
import com.myproject.model.request.RequestJenisSurat;
import com.myproject.model.request.RequestSurat;
import com.myproject.model.response.ResponseJenisSurat;
import com.myproject.model.response.ResponseRequestSurat;

import java.util.ArrayList;
import java.util.List;

public class FragmentJenisSurat extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<ModelJenisSurat> list;
    private AdapterJenisSurat adapter;

    private ProgressBar loading;
    private LinearLayout emptyData;
    private RecyclerView list_data;
    private MaterialRefreshLayout refresh_data;
    private LinearLayoutManager mLayoutManager;
    LoadingDialog loadingRequest = new LoadingDialog();

    ViewAnimator selectProfileAnimator;
    private boolean isShowSelectProfile;
    private TextView name_user;
    private TextView alamat_uset;
    private TextView ttl_user;

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    public FragmentJenisSurat() {}

    public static FragmentJenisSurat newInstance() {
        return newInstance("","");
    }

    public static FragmentJenisSurat newInstance(String param1, String param2) {
        FragmentJenisSurat fragment = new FragmentJenisSurat();
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
                processAnimateSelectorProfile();
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
        getBaseActivity().setLeftIcon(R.drawable.no_user);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(0);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(true);
    }

    @Override
    public String getPageTitle() {
        return "Jenis Surat";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_jenis_surat;
    }

    private void getAllData(){
        RequestJenisSurat request = new RequestJenisSurat();
        request.setMethod("reqJenisSurat");
        request.setNik_penduduk(APP.getStringPref(getBaseActivity(), Preference.NIK));
        list = new ArrayList<>();
        adapter = new AdapterJenisSurat(getActivity(), list, new MyAdapterListener());
        TaskJenisSurat task = new TaskJenisSurat(getBaseActivity()) {
            @Override
            protected void onSuccess(ResponseJenisSurat response) {
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

    private class MyAdapterListener implements AdapterJenisSurat.ClickListener{
        @Override
        public void onDownload(ModelJenisSurat item) {
            if (item.getStatus_surat().equals("0")){
                getBaseActivity().showAlertDialog("Pesan", "File belum siap untuk dibuat");
            }else{
                loadingRequest.show(getBaseActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
                RequestSurat model = new RequestSurat();
                model.setId_jenis(item.getId_jenis_surat());
                model.setMethod("reqIsiSurat");
                TaskRequestJenisSurat task = new TaskRequestJenisSurat(getBaseActivity()) {
                    @Override
                    protected void onSuccess(ResponseRequestSurat response) {
                        removeTask(this);
                        if (response.getResult().size()>0){
                            if (loadingRequest != null){
                                loadingRequest.dismiss();
                            }
                            /*final DialogInput dialogInput = new DialogInput();
                            dialogInput.setTitleAndContent(FragmentJenisSurat.newInstance(), "Form tambahan", response.getResult(), item.getId_jenis_surat());
                            dialogInput.show(getBaseActivity().getFragmentManager(), DialogInput.class.getName());*/
                            FragmentFormTambahan fragment = new FragmentFormTambahan();
                            DashboardActivity dashboard = DashboardActivity.instance;
                            Bundle bundle = new Bundle();
                            bundle.putString("key", response.getKey());
                            bundle.putString("id_surat", item.getId_jenis_surat());
                            fragment.setArguments(bundle);
                            dashboard.pushFragmentDashboard(fragment);
                        }else{
                            getSendData(item.getId_jenis_surat());
                        }
                    }

                    @Override
                    protected void onFailed(String message) {
                        removeTask(this);
                        if (loadingRequest != null){
                            loadingRequest.dismiss();
                        }
                        getBaseActivity().showAlertDialog("Pesan", message);
                    }
                };
                registerTask(task);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);
            }
        }
    }

    private void getSendData(String id_jenis_surat){
        RequestAddSurat surat = new RequestAddSurat();
        surat.setMethod("createSurat");
        surat.setId_jenis_surat(id_jenis_surat);
        surat.setNik_penduduk(APP.getStringPref(getActivity(), Preference.NIK));
        TaskCreateSurat task = new TaskCreateSurat(getBaseActivity()) {
            @Override
            protected void onSuccess(ModelResponse response) {
                removeTask(this);
                if (loadingRequest != null){
                    loadingRequest.dismiss();
                }
                if (response.getStatus()){
                    getBaseActivity().showAlertDialog("Pesan", "Permohonan sedang di proses");
                }else{
                    getBaseActivity().showAlertDialog("Pesan", response.getMessage());
                }
            }

            @Override
            protected void onFailed(String message) {
                removeTask(this);
                if (loadingRequest != null){
                    loadingRequest.dismiss();
                }
                getBaseActivity().showAlertDialog("Pesan", message);
            }
        };
        registerTask(task);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, surat);
    }

    private void processAnimateSelectorProfile(){
        name_user.setText(accountEntity.getNama());
        alamat_uset.setText(accountEntity.getAlamat());
        ttl_user.setText(accountEntity.getTempat()+", "+accountEntity.getTanggal());

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
        DashboardActivity.instance.showBottomMenu();
    }
}
