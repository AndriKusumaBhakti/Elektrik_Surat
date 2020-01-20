package com.myproject.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.myproject.R;
import com.myproject.activity.DashboardKepalaDesa;
import com.myproject.api.TaskScanQrCode;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.ModelRequestSurat;
import com.myproject.model.request.ModelQrCode;
import com.myproject.model.response.ResponseScanQr;
import com.myproject.util.Constants;
import com.myproject.util.DownloadTask;
import com.myproject.util.StringUtil;

import java.util.List;

public class FragmentScanQr extends BaseFragment{
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    private String qr_code;

    private LinearLayout link_surat, emptyData;
    TextView nik, name, alamat, ttl, status, kwg, keterangan, nama_kades, nama_dusun, no_surat, tgl_approve;
    private TextView nama_surat;
    private ScrollView mainLayout;
    LoadingDialog loadingScan = new LoadingDialog();
    ProgressDialog mProgressDialog;
    String link_ = null;
    String nama_ = null;

    public FragmentScanQr() {}

    public static FragmentScanQr newInstance() {
        return newInstance("","");
    }

    public static FragmentScanQr newInstance(String param1, String param2) {
        FragmentScanQr fragment = new FragmentScanQr();
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
        if(getArguments()!=null) {
            qr_code = getArguments().getString(Constants.QR_CODE);
        }
    }

    @Override
    public void initView(View view) {
        link_surat = (LinearLayout) view.findViewById(R.id.link_surat);
        emptyData = (LinearLayout) view.findViewById(R.id.emptyData);
        mainLayout = (ScrollView) view.findViewById(R.id.mainLayout);
        nama_surat = (TextView) view.findViewById(R.id.nama_surat);
        nik = (TextView) view.findViewById(R.id.nik);
        name = (TextView) view.findViewById(R.id.name);
        alamat = (TextView) view.findViewById(R.id.alamat);
        ttl = (TextView) view.findViewById(R.id.ttl);
        status = (TextView) view.findViewById(R.id.status);
        kwg = (TextView) view.findViewById(R.id.kwg);
        keterangan = (TextView) view.findViewById(R.id.keterangan);
        nama_kades = (TextView) view.findViewById(R.id.nama_kades);
        nama_dusun = (TextView) view.findViewById(R.id.nama_dusun);
        no_surat = (TextView) view.findViewById(R.id.no_surat);
        tgl_approve = (TextView) view.findViewById(R.id.tgl_approve);
        scanQRCode();
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
        nama_surat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (link_ != null && nama_ != null) {
                    downloadFileSurat(link_, nama_);
                }else{
                    getBaseActivity().showAlertDialog("Pesan", "Link surat tidak ditemukan");
                }
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
        return "Hasil QR Code";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_scan_qr;
    }

    @Override
    public void onResume() {
        super.onResume();
//        DashboardKepalaDesa.instance.showLeftMenu();
    }

    private void scanQRCode(){
        loadingScan.show(getBaseActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
        if (!StringUtil.checkNullString(qr_code).isEmpty()){
            ModelQrCode model = new ModelQrCode();
            model.setMethod("getScanQR");
            model.setQrcode(qr_code);
            TaskScanQrCode task = new TaskScanQrCode(getBaseActivity()) {
                @Override
                protected void onSuccess(ResponseScanQr response) {
                    removeTask(this);
                    if (loadingScan != null){
                        loadingScan.dismiss();
                    }
                    if (response.getStatus()){
                        mainLayout.setVisibility(View.VISIBLE);
                        emptyData.setVisibility(View.GONE);
                        if (response.getResult() != null){
                            nama_surat.setText(response.getResult().getJenissurat().getJenis_surat());
                            if (!StringUtil.checkNullString(response.getResult().getPenduduk().getNik_penduduk()).isEmpty()){
                                nik.setText(response.getResult().getPenduduk().getNik_penduduk());
                            }
                            if (!StringUtil.checkNullString(response.getResult().getPenduduk().getNama_penduduk()).isEmpty()){
                                name.setText(response.getResult().getPenduduk().getNama_penduduk());
                            }
                            if (!StringUtil.checkNullString(response.getResult().getPenduduk().getAlamat_penduduk()).isEmpty()){
                                alamat.setText(response.getResult().getPenduduk().getAlamat_penduduk());
                            }
                            String ttlahir = null;
                            if (!StringUtil.checkNullString(response.getResult().getPenduduk().getTempat_lahir()).isEmpty()){
                                ttlahir = response.getResult().getPenduduk().getTempat_lahir();
                            }
                            if (ttlahir != null) {
                                if (!StringUtil.checkNullString(response.getResult().getPenduduk().getTanggal_lahir()).isEmpty()) {
                                    ttlahir = ttlahir + ", " + response.getResult().getPenduduk().getTanggal_lahir();
                                }
                                ttl.setText(ttlahir);
                            }else{
                                if (!StringUtil.checkNullString(response.getResult().getPenduduk().getTanggal_lahir()).isEmpty()) {
                                    ttl.setText(response.getResult().getPenduduk().getTanggal_lahir());
                                }
                            }
                            if (!StringUtil.checkNullString(response.getResult().getPenduduk().getStatus()).isEmpty()){
                                status.setText(response.getResult().getPenduduk().getStatus());
                            }
                            if (!StringUtil.checkNullString(response.getResult().getPenduduk().getKewarganegaraan()).isEmpty()){
                                kwg.setText(response.getResult().getPenduduk().getKewarganegaraan());
                            }
                            if (!StringUtil.checkNullString(response.getResult().getQrcode().getNama_kades()).isEmpty()){
                                nama_kades.setText(response.getResult().getQrcode().getNama_kades());
                            }
                            if (!StringUtil.checkNullString(response.getResult().getDusun().getNama_dusun()).isEmpty()){
                                nama_dusun.setText(response.getResult().getDusun().getNama_dusun());
                            }
                            if (!StringUtil.checkNullString(response.getResult().getSurat().getId_surat()).isEmpty()){
                                no_surat.setText(response.getResult().getSurat().getId_surat());
                            }
                            if (!StringUtil.checkNullString(response.getResult().getSurat().getTgl_approve()).isEmpty()){
                                tgl_approve.setText(response.getResult().getSurat().getTgl_approve());
                            }
                            /*String ket = null;
                            if (!StringUtil.checkNullString(response.getResult().getQrcode().getKeperluan()).isEmpty()){
                                ket = "Keperluan    : "+response.getResult().getQrcode().getKeperluan()+"\n";
                            }
                            if (ket == null){
                                if (!StringUtil.checkNullString(response.getResult().getQrcode().getKeterangan()).isEmpty()) {
                                    ket = "Keterangan   : " + response.getResult().getQrcode().getKeterangan() + "\n";
                                }
                            }else {
                                if (!StringUtil.checkNullString(response.getResult().getQrcode().getKeterangan()).isEmpty()) {
                                    ket = ket + "Keterangan : " + response.getResult().getQrcode().getKeterangan() + "\n";
                                }
                            }
                            if (ket == null){
                                if (!StringUtil.checkNullString(response.getResult().getQrcode().getBerlaku()).isEmpty()) {
                                    ket = "Berlaku  : " + response.getResult().getQrcode().getBerlaku() + "\n";
                                }
                            }else {
                                if (!StringUtil.checkNullString(response.getResult().getQrcode().getBerlaku()).isEmpty()) {
                                    ket = ket + "Berlaku    : " + response.getResult().getQrcode().getBerlaku() + "\n";
                                }
                            }
                            keterangan.setText(ket);*/
                            link_ = response.getResult().getSurat().getLink_surat();
                            nama_ = response.getResult().getJenissurat().getJenis_surat();
                        }
                    }else{
                        getBaseActivity().showAlertDialog("Pesan", response.getMessage());
                        mainLayout.setVisibility(View.GONE);
                        emptyData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                protected void onFailed(String message) {
                    removeTask(this);
                    if (loadingScan != null){
                        loadingScan.dismiss();
                    }
                    getBaseActivity().showAlertDialog("Pesan", message);
                    mainLayout.setVisibility(View.GONE);
                    emptyData.setVisibility(View.VISIBLE);
                }
            };
            registerTask(task);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);
        }else{
            mainLayout.setVisibility(View.GONE);
            emptyData.setVisibility(View.VISIBLE);
        }
    }

    private void downloadFileSurat(String link_surat, String jenis_surat){
        mProgressDialog = new ProgressDialog(getBaseActivity());
        mProgressDialog.setMessage(jenis_surat);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(getBaseActivity(), mProgressDialog, jenis_surat);
        downloadTask.execute(link_surat);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }
}
