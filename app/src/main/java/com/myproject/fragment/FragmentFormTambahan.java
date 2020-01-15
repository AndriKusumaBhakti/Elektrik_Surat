package com.myproject.fragment;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.cardview.widget.CardView;

import com.myproject.R;
import com.myproject.api.TaskCreateSurat;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.ModelResponse;
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
        RequestAddSurat surat = new RequestAddSurat();

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
