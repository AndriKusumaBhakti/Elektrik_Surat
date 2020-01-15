package com.myproject.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FragmentFormTambahan extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

    private CardView psik, sib, sktm, skbna, skbni, skbtl, sit, skkem, skkk, skpot, skpn;
    private Button btn_simpan;
    private String key, id_surat;
    Calendar caalendarTgl_benar, calenderTgl_meninggal, calenderTgl_nikah_psg, calenderTgl_lhr_psg;
    DatePickerDialog.OnDateSetListener dateTgl_benar, dateTgl_meninggal, dateTgl_nikah_psg, dateTgl_lhr_psg;
    TimePickerDialog.OnTimeSetListener timeJam_nikah_psg;

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

        nama_acara_ = (TextInputLayout) view.findViewById(R.id.nama_acara_);
        tmp_kerja_ = (TextInputLayout) view.findViewById(R.id.tmp_kerja_);
        anak_dari_ = (TextInputLayout) view.findViewById(R.id.anak_dari_);
        nama_benar_ = (TextInputLayout) view.findViewById(R.id.nama_benar_);
        nik_benar_ = (TextInputLayout) view.findViewById(R.id.nik_benar_);
        anak_dari2_ = (TextInputLayout) view.findViewById(R.id.anak_dari2_);
        nama_pondok_ = (TextInputLayout) view.findViewById(R.id.nama_pondok_);
        alasan_ = (TextInputLayout) view.findViewById(R.id.alasan_);
        nik_meninggal_ = (TextInputLayout) view.findViewById(R.id.nik_meninggal_);
        hari_meninggal_ = (TextInputLayout) view.findViewById(R.id.hari_meninggal_);
        tmp_meninggal_ = (TextInputLayout) view.findViewById(R.id.tmp_meninggal_);
        penyebab_ = (TextInputLayout) view.findViewById(R.id.penyebab_);
        hub_pelapor_ = (TextInputLayout) view.findViewById(R.id.hub_pelapor_);
        atas_nama_ = (TextInputLayout) view.findViewById(R.id.atas_nama_);
        jenis_merk_ = (TextInputLayout) view.findViewById(R.id.jenis_merk_);
        tipe_ = (TextInputLayout) view.findViewById(R.id.tipe_);
        tahun_ = (TextInputLayout) view.findViewById(R.id.tahun_);
        tahun_buat_ = (TextInputLayout) view.findViewById(R.id.tahun_buat_);
        no_mesin_ = (TextInputLayout) view.findViewById(R.id.no_mesin_);
        no_rangka_ = (TextInputLayout) view.findViewById(R.id.no_rangka_);
        no_polisi_ = (TextInputLayout) view.findViewById(R.id.no_polisi_);
        nama_anak_ = (TextInputLayout) view.findViewById(R.id.nama_anak_);
        nik_anak_ = (TextInputLayout) view.findViewById(R.id.nik_anak_);
        asal_sklah_ = (TextInputLayout) view.findViewById(R.id.asal_sklah_);
        penghasilan_ = (TextInputLayout) view.findViewById(R.id.penghasilan_);
        kmps_bersangkutan_ = (TextInputLayout) view.findViewById(R.id.kmps_bersangkutan_);
        bin_binti_ = (TextInputLayout) view.findViewById(R.id.bin_binti_);
        ayah_ = (TextInputLayout) view.findViewById(R.id.ayah_);
        ibu_ = (TextInputLayout) view.findViewById(R.id.ibu_);
        kwg_psg_ = (TextInputLayout) view.findViewById(R.id.kwg_psg_);
        pkerjaan_psg_ = (TextInputLayout) view.findViewById(R.id.pkerjaan_psg_);
        bin_binti_psg_ = (TextInputLayout) view.findViewById(R.id.bin_binti_psg_);
        alamat_psg_ = (TextInputLayout) view.findViewById(R.id.alamat_psg_);
        ayah_psg_ = (TextInputLayout) view.findViewById(R.id.ayah_psg_);
        ibu_psg_ = (TextInputLayout) view.findViewById(R.id.ibu_psg_);
        wali_nkh_psg_ = (TextInputLayout) view.findViewById(R.id.wali_nkh_psg_);
        mas_kawin_psg_ = (TextInputLayout) view.findViewById(R.id.mas_kawin_psg_);
        nama_psg_ = (TextInputLayout) view.findViewById(R.id.nama_psg_);
        jk_psg_ = (TextInputLayout) view.findViewById(R.id.jk_psg_);
        tmp_lhr_psg_ = (TextInputLayout) view.findViewById(R.id.tmp_lhr_psg_);

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
                skkem.setVisibility(View.VISIBLE);
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

    private Boolean validasiForm(){
        boolean status = true;
        if (psik.getVisibility() == View.VISIBLE){
            if (nama_acara.getText().toString().isEmpty()) {
                nama_acara_.setErrorEnabled(true);
                nama_acara_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                nama_acara_.setError(null);
            }
        }
        if (sib.getVisibility() == View.VISIBLE){
            if (tmp_kerja.getText().toString().isEmpty()) {
                tmp_kerja_.setErrorEnabled(true);
                tmp_kerja_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                tmp_kerja_.setError(null);
            }
        }
        if (sktm.getVisibility() == View.VISIBLE){
            if (anak_dari.getText().toString().isEmpty()) {
                anak_dari_.setErrorEnabled(true);
                anak_dari_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                anak_dari_.setError(null);
            }
        }
        if (skbna.getVisibility() == View.VISIBLE){
            if (nama_benar.getText().toString().isEmpty()) {
                nama_benar_.setErrorEnabled(true);
                nama_benar_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                nama_benar_.setError(null);
            }
        }
        if (skbni.getVisibility() == View.VISIBLE){
            if (nik_benar.getText().toString().isEmpty()) {
                nik_benar_.setErrorEnabled(true);
                nik_benar_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                nik_benar_.setError(null);
            }
        }
        if (skbtl.getVisibility() == View.VISIBLE){
            if (anak_dari2.getText().toString().isEmpty()) {
                anak_dari2_.setErrorEnabled(true);
                anak_dari2_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                anak_dari2_.setError(null);
            }
        }
        if (sit.getVisibility() == View.VISIBLE){
            if (nama_pondok.getText().toString().isEmpty()) {
                nama_pondok_.setErrorEnabled(true);
                nama_pondok_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                nama_pondok_.setError(null);
            }
            if (alasan.getText().toString().isEmpty()) {
                alasan_.setErrorEnabled(true);
                alasan_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                alasan_.setError(null);
            }
        }
        if (skkem.getVisibility() == View.VISIBLE){
            if (nik_meninggal.getText().toString().isEmpty()) {
                nik_meninggal_.setErrorEnabled(true);
                nik_meninggal_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                nik_meninggal_.setError(null);
            }
            if (hari_meninggal.getText().toString().isEmpty()) {
                hari_meninggal_.setErrorEnabled(true);
                hari_meninggal_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                hari_meninggal_.setError(null);
            }
            if (tmp_meninggal.getText().toString().isEmpty()) {
                tmp_meninggal_.setErrorEnabled(true);
                tmp_meninggal_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                tmp_meninggal_.setError(null);
            }
            if (penyebab.getText().toString().isEmpty()) {
                penyebab_.setErrorEnabled(true);
                penyebab_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                penyebab_.setError(null);
            }
            if (hub_pelapor.getText().toString().isEmpty()) {
                hub_pelapor_.setErrorEnabled(true);
                hub_pelapor_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                hub_pelapor_.setError(null);
            }
        }
        if (skkk.getVisibility() == View.VISIBLE){
            if (atas_nama.getText().toString().isEmpty()) {
                atas_nama_.setErrorEnabled(true);
                atas_nama_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                atas_nama_.setError(null);
            }
            if (jenis_merk.getText().toString().isEmpty()) {
                jenis_merk_.setErrorEnabled(true);
                jenis_merk_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                jenis_merk_.setError(null);
            }
            if (tipe.getText().toString().isEmpty()) {
                tipe_.setErrorEnabled(true);
                tipe_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                tipe_.setError(null);
            }
            if (tahun.getText().toString().isEmpty()) {
                tahun_.setErrorEnabled(true);
                tahun_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                tahun_.setError(null);
            }
            if (tahun_buat.getText().toString().isEmpty()) {
                tahun_buat_.setErrorEnabled(true);
                tahun_buat_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                tahun_buat_.setError(null);
            }
            if (no_mesin.getText().toString().isEmpty()) {
                no_mesin_.setErrorEnabled(true);
                no_mesin_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                no_mesin_.setError(null);
            }
            if (no_rangka.getText().toString().isEmpty()) {
                no_rangka_.setErrorEnabled(true);
                no_rangka_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                no_rangka_.setError(null);
            }
            if (no_polisi.getText().toString().isEmpty()) {
                no_polisi_.setErrorEnabled(true);
                no_polisi_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                no_polisi_.setError(null);
            }
        }
        if (skpot.getVisibility() == View.VISIBLE){
            if (nama_anak.getText().toString().isEmpty()) {
                nama_anak_.setErrorEnabled(true);
                nama_anak_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                nama_anak_.setError(null);
            }
            if (nik_anak.getText().toString().isEmpty()) {
                nik_anak_.setErrorEnabled(true);
                nik_anak_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                nik_anak_.setError(null);
            }
            if (asal_sklah.getText().toString().isEmpty()) {
                asal_sklah_.setErrorEnabled(true);
                asal_sklah_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                asal_sklah_.setError(null);
            }
            if (penghasilan.getText().toString().isEmpty()) {
                penghasilan_.setErrorEnabled(true);
                penghasilan_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                penghasilan_.setError(null);
            }
            if (kmps_bersangkutan.getText().toString().isEmpty()) {
                kmps_bersangkutan_.setErrorEnabled(true);
                kmps_bersangkutan_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                kmps_bersangkutan_.setError(null);
            }
        }
        if (skpn.getVisibility() == View.VISIBLE){
            if (bin_binti.getText().toString().isEmpty()) {
                bin_binti_.setErrorEnabled(true);
                bin_binti_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                bin_binti_.setError(null);
            }
            if (ayah.getText().toString().isEmpty()) {
                ayah_.setErrorEnabled(true);
                ayah_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                ayah_.setError(null);
            }
            if (ibu.getText().toString().isEmpty()) {
                ibu_.setErrorEnabled(true);
                ibu_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                ibu_.setError(null);
            }
            if (nama_psg.getText().toString().isEmpty()) {
                nama_psg_.setErrorEnabled(true);
                nama_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                nama_psg_.setError(null);
            }
            if (jk_psg.getText().toString().isEmpty()) {
                jk_psg_.setErrorEnabled(true);
                jk_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                jk_psg_.setError(null);
            }
            if (tmp_lhr_psg.getText().toString().isEmpty()) {
                tmp_lhr_psg_.setErrorEnabled(true);
                tmp_lhr_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                tmp_lhr_psg_.setError(null);
            }
            if (kwg_psg.getText().toString().isEmpty()) {
                kwg_psg_.setErrorEnabled(true);
                kwg_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                kwg_psg_.setError(null);
            }
            if (pkerjaan_psg.getText().toString().isEmpty()) {
                pkerjaan_psg_.setErrorEnabled(true);
                pkerjaan_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                pkerjaan_psg_.setError(null);
            }
            if (bin_binti_psg.getText().toString().isEmpty()) {
                bin_binti_psg_.setErrorEnabled(true);
                bin_binti_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                bin_binti_psg_.setError(null);
            }
            if (alamat_psg.getText().toString().isEmpty()) {
                alamat_psg_.setErrorEnabled(true);
                alamat_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                alamat_psg_.setError(null);
            }
            if (ayah_psg.getText().toString().isEmpty()) {
                ayah_psg_.setErrorEnabled(true);
                ayah_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                ayah_psg_.setError(null);
            }
            if (ibu_psg.getText().toString().isEmpty()) {
                ibu_psg_.setErrorEnabled(true);
                ibu_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                ibu_psg_.setError(null);
            }
            if (wali_nkh_psg.getText().toString().isEmpty()) {
                wali_nkh_psg_.setErrorEnabled(true);
                wali_nkh_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                wali_nkh_psg_.setError(null);
            }
            if (mas_kawin_psg.getText().toString().isEmpty()) {
                mas_kawin_psg_.setErrorEnabled(true);
                mas_kawin_psg_.setError(getResources().getString(R.string.data_required));
                status = false;
            } else {
                mas_kawin_psg_.setError(null);
            }
        }
        return status;
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
                if (validasiForm()){
                    ArrayList<HashMap<String, String>> data = new ArrayList<>();
                    HashMap<String, String> result =  new HashMap<>();
                    if (psik.getVisibility() == View.VISIBLE){
                        result.put("nama_acara", nama_acara.getText().toString().trim());
                        data.add(result);
                    }else if (sib.getVisibility() == View.VISIBLE){
                        result.put("tempat_kerja", tmp_kerja.getText().toString().trim());
                        data.add(result);
                    }else if (sktm.getVisibility() == View.VISIBLE){
                        result.put("anak_dari", anak_dari.getText().toString().trim());
                        data.add(result);
                    }else if (skbna.getVisibility() == View.VISIBLE){
                        result.put("nama_benar", nama_benar.getText().toString().trim());
                        data.add(result);
                    }else if (skbni.getVisibility() == View.VISIBLE){
                        result.put("nik_benar", nik_benar.getText().toString().trim());
                        data.add(result);
                    }else if (skbtl.getVisibility() == View.VISIBLE){
                        result.put("anak_dari", anak_dari2.getText().toString().trim());
                        data.add(result);
                        result.put("tgl_benar", tgl_benar.getText().toString().trim());
                        data.add(result);
                    }else if (sit.getVisibility() == View.VISIBLE){
                        result.put("nama_pondok", nama_pondok.getText().toString().trim());
                        data.add(result);
                        result.put("alasan", alasan.getText().toString().trim());
                        data.add(result);
                    }else if (skkem.getVisibility() == View.VISIBLE){
                        result.put("nik_meninggal", nik_meninggal.getText().toString().trim());
                        data.add(result);
                        result.put("hari_mati", hari_meninggal.getText().toString().trim());
                        data.add(result);
                        result.put("tgl_mati", tgl_meninggal.getText().toString().trim());
                        data.add(result);
                        result.put("bertempat", tmp_meninggal.getText().toString().trim());
                        data.add(result);
                        result.put("penyebab", penyebab.getText().toString().trim());
                        data.add(result);
                        result.put("hubungan_pelapor", hub_pelapor.getText().toString().trim());
                        data.add(result);
                    }else if (skkk.getVisibility() == View.VISIBLE){
                        result.put("atas_nama", atas_nama.getText().toString().trim());
                        data.add(result);
                        result.put("jenis_merk", jenis_merk.getText().toString().trim());
                        data.add(result);
                        result.put("type", tipe.getText().toString().trim());
                        data.add(result);
                        result.put("tahun", tahun.getText().toString().trim());
                        data.add(result);
                        result.put("tahun_pembuatan", tahun_buat.getText().toString().trim());
                        data.add(result);
                        result.put("no_mesin", no_mesin.getText().toString().trim());
                        data.add(result);
                        result.put("no_rangka", no_rangka.getText().toString().trim());
                        data.add(result);
                        result.put("no_polisi", no_polisi.getText().toString().trim());
                        data.add(result);
                    }else if (skpot.getVisibility() == View.VISIBLE){
                        result.put("nama_anak", nama_anak.getText().toString().trim());
                        data.add(result);
                        result.put("nik_anak", nik_anak.getText().toString().trim());
                        data.add(result);
                        result.put("asal_sekolah", asal_sklah.getText().toString().trim());
                        data.add(result);
                        result.put("penghasilan", penghasilan.getText().toString().trim());
                        data.add(result);
                        result.put("kampus_bersangkutan", kmps_bersangkutan.getText().toString().trim());
                        data.add(result);
                    }else if (skpn.getVisibility() == View.VISIBLE){
                        result.put("bin_binti", bin_binti.getText().toString().trim());
                        data.add(result);
                        result.put("ayah", ayah.getText().toString().trim());
                        data.add(result);
                        result.put("ibu", ibu.getText().toString().trim());
                        data.add(result);
                        result.put("nama_pasangan", nama_psg.getText().toString().trim());
                        data.add(result);
                        result.put("jenis_kelamin_pasangan", jk_psg.getText().toString().trim());
                        data.add(result);
                        result.put("tempat_lahir_pasangan", tmp_lhr_psg.getText().toString().trim());
                        data.add(result);
                        result.put("tanggal_lahir_pasangan", tgl_lhr_psg.getText().toString().trim());
                        data.add(result);
                        result.put("kewarganegaraan_pasangan", kwg_psg.getText().toString().trim());
                        data.add(result);
                        result.put("pekerjaan_pasangan", pkerjaan_psg.getText().toString().trim());
                        data.add(result);
                        result.put("bin_binti_pasangan", bin_binti_psg.getText().toString().trim());
                        data.add(result);
                        result.put("alamat_penduduk_pasangan", alamat_psg.getText().toString().trim());
                        data.add(result);
                        result.put("ayah_pasangan", ayah_psg.getText().toString().trim());
                        data.add(result);
                        result.put("ibu_pasangan", ibu_psg.getText().toString().trim());
                        data.add(result);
                        result.put("tgl_nikah", tgl_nikah_psg.getText().toString().trim());
                        data.add(result);
                        result.put("jam_nikah", jam_nikah_psg.getText().toString().trim());
                        data.add(result);
                        result.put("wali_nikah", wali_nkh_psg.getText().toString().trim());
                        data.add(result);
                        result.put("mas_kawin", mas_kawin_psg.getText().toString().trim());
                        data.add(result);
                    }
                    sendData(data);
                }else{
                    getBaseActivity().showAlertDialog("Pesan", "Data tidak boleh ada yang kosong");
                }
            }
        });
        caalendarTgl_benar = Calendar.getInstance();
        dateTgl_benar = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                caalendarTgl_benar.set(Calendar.YEAR, year);
                caalendarTgl_benar.set(Calendar.MONTH, monthOfYear);
                caalendarTgl_benar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tgl_benar.setText(sdf.format(caalendarTgl_benar.getTime()));
            }
        };
        tgl_benar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateTgl_benar, caalendarTgl_benar
                        .get(Calendar.YEAR), caalendarTgl_benar.get(Calendar.MONTH),
                        caalendarTgl_benar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        calenderTgl_meninggal = Calendar.getInstance();
        dateTgl_meninggal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calenderTgl_meninggal.set(Calendar.YEAR, year);
                calenderTgl_meninggal.set(Calendar.MONTH, monthOfYear);
                calenderTgl_meninggal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tgl_meninggal.setText(sdf.format(calenderTgl_meninggal.getTime()));
            }
        };
        tgl_meninggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateTgl_meninggal, calenderTgl_meninggal
                        .get(Calendar.YEAR), calenderTgl_meninggal.get(Calendar.MONTH),
                        calenderTgl_meninggal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        calenderTgl_nikah_psg = Calendar.getInstance();
        dateTgl_nikah_psg = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calenderTgl_nikah_psg.set(Calendar.YEAR, year);
                calenderTgl_nikah_psg.set(Calendar.MONTH, monthOfYear);
                calenderTgl_nikah_psg.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tgl_nikah_psg.setText(sdf.format(calenderTgl_nikah_psg.getTime()));
            }
        };
        tgl_nikah_psg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateTgl_nikah_psg, calenderTgl_nikah_psg
                        .get(Calendar.YEAR), calenderTgl_nikah_psg.get(Calendar.MONTH),
                        calenderTgl_nikah_psg.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        calenderTgl_lhr_psg = Calendar.getInstance();
        dateTgl_lhr_psg = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calenderTgl_lhr_psg.set(Calendar.YEAR, year);
                calenderTgl_lhr_psg.set(Calendar.MONTH, monthOfYear);
                calenderTgl_lhr_psg.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tgl_lhr_psg.setText(sdf.format(calenderTgl_lhr_psg.getTime()));
            }
        };
        tgl_lhr_psg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateTgl_lhr_psg, calenderTgl_lhr_psg
                        .get(Calendar.YEAR), calenderTgl_lhr_psg.get(Calendar.MONTH),
                        calenderTgl_lhr_psg.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeJam_nikah_psg = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                jam_nikah_psg.setText(String.valueOf(hourOfDay+":"+minute));
            }
        };
        jam_nikah_psg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), timeJam_nikah_psg, 00, 00, true).show();
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
    }

    @Override
    public String getPageTitle() {
        return "Form Tambahan";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_form_tambahan;
    }

    private void sendData(ArrayList<HashMap<String, String>> data){
        LoadingDialog loading = new LoadingDialog();
        loading.show(getBaseActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
        SendRequestIsi surat = new SendRequestIsi();
        surat.setMethod("createSurat");
        surat.setIdjenissurat(id_surat);
        surat.setNik_penduduk(String.valueOf(accountEntity.getNik()));
        surat.setValue(data);

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
