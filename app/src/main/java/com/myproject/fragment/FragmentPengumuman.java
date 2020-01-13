package com.myproject.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.myproject.R;
import com.myproject.activity.DashboardKepalaDesa;
import com.myproject.adapter.AdapterRequestSurat;
import com.myproject.api.TaskAllSurat;
import com.myproject.api.TaskApproveSurat;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.ModelRequestSurat;
import com.myproject.model.ModelResponse;
import com.myproject.model.request.RequestJenisSurat;
import com.myproject.model.response.ResponseAllSurat;
import com.myproject.util.DownloadTask;

import java.util.ArrayList;
import java.util.List;

public class FragmentPengumuman extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressBar loading;
    private LinearLayout emptyData;
    private RecyclerView list_data;
    private MaterialRefreshLayout refresh_data;
    private ArrayList<ModelRequestSurat> list;
    private AdapterRequestSurat adapter;
    private LinearLayoutManager mLayoutManager;

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    ProgressDialog mProgressDialog;

    public FragmentPengumuman() {}

    public static FragmentPengumuman newInstance() {
        return newInstance("","");
    }

    public static FragmentPengumuman newInstance(String param1, String param2) {
        FragmentPengumuman fragment = new FragmentPengumuman();
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
        refresh_data = (MaterialRefreshLayout) view.findViewById(R.id.refresh_data);
        emptyData = (LinearLayout) view.findViewById(R.id.emptyData);
        list_data = (RecyclerView) view.findViewById(R.id.list_data);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        getAllRequest();
    }

    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                DashboardKepalaDesa dashboard = DashboardKepalaDesa.instance;
                dashboard.showLeftMenu();
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
                        getAllRequest();
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
        getBaseActivity().setLeftIcon(R.drawable.view_record_white);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(0);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(true);
    }

    @Override
    public String getPageTitle() {
        return "Request Surat";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_pengumuman;
    }

    private void getAllRequest(){
        RequestJenisSurat model = new RequestJenisSurat();
        model.setMethod("reqAllSurat");
        model.setNik_penduduk(accountEntity.getNik());
        list = new ArrayList<>();
        adapter = new AdapterRequestSurat(getActivity(), list, new MyAdapterListener());
        TaskAllSurat task = new TaskAllSurat(getBaseActivity()) {
            @Override
            protected void onSuccess(ResponseAllSurat response) {
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
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);
    }

    private class MyAdapterListener implements AdapterRequestSurat.ClickListener{
        @Override
        public void onApprove(ModelRequestSurat item) {
            if (item.getStatus_surat().equals("1")){
                APP.log(item.getLink_surat());
                downloadFile(item);
            }else {
                final LoadingDialog dialogLoading = new LoadingDialog();
                dialogLoading.show(getBaseActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
                RequestJenisSurat model = new RequestJenisSurat();
                model.setMethod("reqApprove");
                model.setNik_penduduk(item.getNik_penduduk());
                model.setId_surat(item.getId_surat());
                TaskApproveSurat task = new TaskApproveSurat(getBaseActivity()) {
                    @Override
                    protected void onSuccess(ModelResponse response) {
                        removeTask(this);
                        if (dialogLoading != null) {
                            dialogLoading.dismiss();
                        }
                        if (response.getStatus()) {
                            getAllRequest();
                        } else {
                            getBaseActivity().showAlertDialog("Pesan", response.getMessage());
                        }
                    }

                    @Override
                    protected void onFailed(String message) {
                        removeTask(this);
                        if (dialogLoading != null) {
                            dialogLoading.dismiss();
                        }
                        getBaseActivity().showAlertDialog("Pesan", message);
                    }
                };
                registerTask(task);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);
            }
        }
    }

    private void downloadFile(ModelRequestSurat item){
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
