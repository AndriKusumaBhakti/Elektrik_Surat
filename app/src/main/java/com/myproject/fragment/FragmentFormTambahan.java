package com.myproject.fragment;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;
import com.myproject.R;
import com.myproject.api.TaskCreateSurat;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.ModelResponse;
import com.myproject.model.SendRequestIsi;
import com.myproject.model.request.RequestAddSurat;

import java.util.Calendar;
import java.util.List;

public class FragmentFormTambahan extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    private CardView psik, sib, sktm, skbna, skbni, skbtl, sit, skkem, skkk, skpot, skpn;
    private Button btn_simpan;
    private String key, id_surat;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText nama_acara, tmp_kerja, anak_dari, nama_benar, nik_benar, anak_dari2,
            nama_pondok, alasan, nik_meninggal, hari_meninggal, tmp_meninggal, penyebab, hub_pelapor
            , atas_nama, jenis_merk, tipe, tahun, tahun_buat, no_mesin, no_rangka, no_polisi, nama_anak, nik_anak
            , asal_sklah, penghasilan, kmps_bersangkutan, bin_binti, ayah, ibu, kwg_psg, pkerjaan_psg, bin_binti_psg
            , alamat_psg, ayah_psg, ibu_psg, wali_nkh_psg, mas_kawin_psg, nama_psg, jk_psg, tmp_lhr_psg;
    TextInputLayout nama_acara_, tmp_kerja_, anak_dari_, nama_benar_, nik_benar_, anak_dari2_,
            nama_pondok_, alasan_, nik_meninggal_, hari_meninggal_, tmp_meninggal_, penyebab_, hub_pelapor_
            , atas_nama_, jenis_merk_, tipe_, tahun_, tahun_buat_, no_mesin_, no_rangka_, no_polisi_,
            nama_anak_, nik_anak_, asal_sklah_, penghasilan_, kmps_bersangkutan_, bin_binti_, ayah_, ibu_, kwg_psg_
            , pkerjaan_psg_, bin_binti_psg_, alamat_psg_, ayah_psg_, ibu_psg_, wali_nkh_psg_, mas_kawin_psg_, nama_psg_, jk_psg_, tmp_lhr_psg_;
    TextView tgl_benar, tgl_meninggal, tgl_nikah_psg, jam_nikah_psg, tgl_lhr_psg;

    public FragmentFormTambahan() {}

    public static FragmentFormTambahan newInstance() {
        return newInstance("","");
    }

    public static FragmentFormTambahan newInstance(String param1, String param2) {
        FragmentFormTambahan fragment = new FragmentFormTambahan();
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
        key = getArguments().getString("key");
        id_surat = getArguments().getString("id_surat");
    }

    @Override
    public void initView(View view) {
        psik = (CardView) view.findViewById(R.id.psik);
        sib = (CardView) view.findViewById(R.id.sib);
        sktm = (CardView) view.findViewById(R.id.sktm);
        skbna = (CardView) view.findViewById(R.id.skbna);
        skbni = (CardView) view.findViewById(R.id.skbni);
        skbtl = (CardView) view.findViewById(R.id.skbtl);
        sit = (CardView) view.findViewById(R.id.sit);
        skkem = (CardView) view.findViewById(R.id.skkem);
        skkk = (CardView) view.findViewById(R.id.skkk);
        skpot = (CardView) view.findViewById(R.id.skpot);
        skpn = (CardView) view.findViewById(R.id.skpn);
        btn_simpan = (Button) view.findViewById(R.id.btn_simpan);

        nama_acara = (EditText) view.findViewById(R.id.nama_acara);
        tmp_kerja = (EditText) view.findViewById(R.id.tmp_kerja);
        anak_dari = (EditText) view.findViewById(R.id.anak_dari);
        nama_benar = (EditText) view.findViewById(R.id.nama_benar);
        nik_benar = (EditText) view.findViewById(R.id.nik_benar);
        anak_dari2 = (EditText) view.findViewById(R.id.anak_dari2);
        nama_pondok = (EditText) view.findViewById(R.id.nama_pondok);
        alasan = (EditText) view.findViewById(R.id.alasan);
        nik_meninggal = (EditText) view.findViewById(R.id.nik_meninggal);
        hari_meninggal = (EditText) view.findViewById(R.id.hari_meninggal);
        tmp_meninggal = (EditText) view.findViewById(R.id.tmp_meninggal);
        penyebab = (EditText) view.findViewById(R.id.penyebab);
        hub_pelapor = (EditText) view.findViewById(R.id.hub_pelapor);
        atas_nama = (EditText) view.findViewById(R.id.atas_nama);
        jenis_merk = (EditText) view.findViewById(R.id.jenis_merk);
        tipe = (EditText) view.findViewById(R.id.tipe);
        tahun = (EditText) view.findViewById(R.id.tahun);
        tahun_buat = (EditText) view.findViewById(R.id.tahun_buat);
        no_mesin = (EditText) view.findViewById(R.id.no_mesin);
        no_rangka = (EditText) view.findViewById(R.id.no_rangka);
        no_polisi = (EditText) view.findViewById(R.id.no_polisi);
        nama_anak = (EditText) view.findViewById(R.id.nama_anak);
        nik_anak = (EditText) view.findViewById(R.id.nik_anak);
        asal_sklah = (EditText) view.findViewById(R.id.asal_sklah);
        penghasilan = (EditText) view.findViewById(R.id.penghasilan);
        kmps_bersangkutan = (EditText) view.findViewById(R.id.kmps_bersangkutan);
        bin_binti = (EditText) view.findViewById(R.id.bin_binti);
        ayah = (EditText) view.findViewById(R.id.ayah);
        ibu = (EditText) view.findViewById(R.id.ibu);
        kwg_psg = (EditText) view.findViewById(R.id.kwg_psg);
        pkerjaan_psg = (EditText) view.findViewById(R.id.pkerjaan_psg);
        bin_binti_psg = (EditText) view.findViewById(R.id.bin_binti_psg);
        alamat_psg = (EditText) view.findViewById(R.id.alamat_psg);
        ayah_psg = (EditText) view.findViewById(R.id.ayah_psg);
        ibu_psg = (EditText) view.findViewById(R.id.ibu_psg);
        wali_nkh_psg = (EditText) view.findViewById(R.id.wali_nkh_psg);
        mas_kawin_psg = (EditText) view.findViewById(R.id.mas_kawin_psg);
        nama_psg = (EditText) view.findViewById(R.id.nama_psg);
        jk_psg = (EditText) view.findViewById(R.id.jk_psg);
        tmp_lhr_psg = (EditText) view.findViewById(R.id.tmp_lhr_psg);

        tgl_benar = (TextView) view.findViewById(R.id.tgl_benar);
        tgl_meninggal = (TextView) view.findViewById(R.id.tgl_meninggal);
        tgl_nikah_psg = (TextView) view.findViewById(R.id.tgl_nikah_psg);
        jam_nikah_psg = (TextView) view.findViewById(R.id.jam_nikah_psg);
        tgl_lhr_psg = (TextView) view.findViewById(R.id.tgl_lhr_psg);
        switch (key){
            case "psik":
                psik.setVisibility(View.VISIBLE);
                break;
            case "sib":
                sib.setVisibility(View.VISIBLE);
                break;
            case "sktm":
                sktm.setVisibility(View.VISIBLE);
                break;
            case "skbna":
                skbna.setVisibility(View.VISIBLE);
                break;
            case "skbni":
                skbni.setVisibility(View.VISIBLE);
                break;
            case "skbtl":
                skbtl.setVisibility(View.VISIBLE);
                break;
            case "sit":
                sit.setVisibility(View.VISIBLE);
                break;
            case "skkem":
                sit.setVisibility(View.VISIBLE);
                break;
            case "skkk":
                skkk.setVisibility(View.VISIBLE);
                break;
            case "skpot":
                skpot.setVisibility(View.VISIBLE);
                break;
            default:
                skpn.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                getFragmentManager().popBackStack();
            }

            @Override
            public void onRightIconClick() {

            }

            @Override
            public void onRight2IconClick() {

            }
        });
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
    }

    @Override
    public void updateUI() {
        getBaseActivity().setLeftIcon(R.drawable.back_white);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(0);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(true);
    }

    @Override
    public String getPageTitle() {
        return "Form Tambahan";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_form_tambahan;
    }

    private void sendData(){
        LoadingDialog loading = new LoadingDialog();
        loading.show(getBaseActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
        SendRequestIsi surat = new SendRequestIsi();

        TaskCreateSurat task = new TaskCreateSurat(getBaseActivity()) {
            @Override
            protected void onSuccess(ModelResponse response) {
                removeTask(this);
                if (loading != null){
                    loading.dismiss();
                }
                if (response.getStatus()){
                    getBaseActivity().showAlertDialog("Pesan", "Permohonan sedang di proses");
                    getFragmentManager().popBackStack();
                }else{
                    getBaseActivity().showAlertDialog("Pesan", response.getMessage());
                }
            }

            @Override
            protected void onFailed(String message) {
                removeTask(this);
                if (loading != null){
                    loading.dismiss();
                }
                getBaseActivity().showAlertDialog("Pesan", message);
            }
        };
        registerTask(task);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, surat);
    }
}
