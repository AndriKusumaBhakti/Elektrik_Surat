package com.myproject.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.myproject.R;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.util.StringUtil;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class FragmentDetailProfil extends BaseFragment implements View.OnClickListener{
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    ExpandableLayout exp_biodata, exp_pendidikan, exp_pekerjaan;
    TextView profile_name, nik, name, alamat, ttl, status, kwg, pendi, kerja;
    TextView biodata, pendidikan, pekerjaan;
    ImageView profile_home_avatar;
    private static final int REQUEST_CODE_IMAGE = 10001;

    public FragmentDetailProfil() {}

    public static FragmentDetailProfil newInstance() {
        return newInstance("","");
    }

    public static FragmentDetailProfil newInstance(String param1, String param2) {
        FragmentDetailProfil fragment = new FragmentDetailProfil();
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
        exp_biodata = (ExpandableLayout) view.findViewById(R.id.exp_biodata);
        exp_pendidikan = (ExpandableLayout) view.findViewById(R.id.exp_pendidikan);
        exp_pekerjaan = (ExpandableLayout) view.findViewById(R.id.exp_pekerjaan);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        nik = (TextView) view.findViewById(R.id.nik);
        name = (TextView) view.findViewById(R.id.name);
        alamat = (TextView) view.findViewById(R.id.alamat);
        ttl = (TextView) view.findViewById(R.id.ttl);
        status = (TextView) view.findViewById(R.id.status);
        kwg = (TextView) view.findViewById(R.id.kwg);
        pendi = (TextView) view.findViewById(R.id.pendi);
        kerja = (TextView) view.findViewById(R.id.kerja);
        biodata = (TextView) view.findViewById(R.id.biodata);
        pendidikan = (TextView) view.findViewById(R.id.pendidikan);
        pekerjaan = (TextView) view.findViewById(R.id.pekerjaan);
        profile_home_avatar = (ImageView) view.findViewById(R.id.profile_home_avatar);
        profile_home_avatar.setOnClickListener(this);
        if (!StringUtil.checkNullString(accountEntity.getNik()).isEmpty()){
            nik.setText(accountEntity.getNik());
        }
        if (!StringUtil.checkNullString(accountEntity.getNama()).isEmpty()){
            name.setText(accountEntity.getNama());
            profile_name.setText(accountEntity.getNama());
        }else{
            profile_name.setVisibility(View.INVISIBLE);
        }
        if (!StringUtil.checkNullString(accountEntity.getAlamat()).isEmpty()){
            alamat.setText(accountEntity.getAlamat());
        }
        String ttlahir = null;
        if (!StringUtil.checkNullString(accountEntity.getTempat()).isEmpty()){
            ttlahir = accountEntity.getNik();
        }
        if (ttlahir != null) {
            if (!StringUtil.checkNullString(accountEntity.getTanggal()).isEmpty()) {
                ttlahir = ttlahir + ", " + accountEntity.getTanggal();
            }
            ttl.setText(ttlahir);
        }else{
            if (!StringUtil.checkNullString(accountEntity.getTanggal()).isEmpty()) {
                ttl.setText(accountEntity.getTanggal());
            }
        }
        if (!StringUtil.checkNullString(accountEntity.getStatus()).isEmpty()){
            status.setText(accountEntity.getStatus());
        }
        if (!StringUtil.checkNullString(accountEntity.getKewarganegaraan()).isEmpty()){
            kwg.setText(accountEntity.getKewarganegaraan());
        }
        if (!StringUtil.checkNullString(accountEntity.getPendidikan()).isEmpty()){
            pendi.setText(accountEntity.getPendidikan());
        }
        if (!StringUtil.checkNullString(accountEntity.getPekerjaan()).isEmpty()){
            kerja.setText(accountEntity.getPekerjaan());
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
        biodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exp_biodata.isExpanded()){
                    exp_biodata.collapse(true);
                }else{
                    exp_biodata.expand(true);
                    exp_pendidikan.collapse(true);
                    exp_pekerjaan.collapse(true);
                }
            }
        });
        pendidikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exp_pendidikan.isExpanded()){
                    exp_pendidikan.collapse(true);
                }else{
                    exp_pendidikan.expand(true);
                    exp_biodata.collapse(true);
                    exp_pekerjaan.collapse(true);
                }
            }
        });
        pekerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exp_pekerjaan.isExpanded()){
                    exp_pekerjaan.collapse(true);
                }else{
                    exp_pekerjaan.expand(true);
                    exp_biodata.collapse(true);
                    exp_pendidikan.collapse(true);
                }
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
        return "Detail Profil";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_detail_profil;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_home_avatar){

        }
    }
}
