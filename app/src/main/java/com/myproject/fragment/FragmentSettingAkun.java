package com.myproject.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.myproject.R;
import com.myproject.activity.DashboardKepalaDesa;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.util.Constants;
import com.myproject.util.FunctionUtil;

import java.util.List;

public class FragmentSettingAkun extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView accountSetting, setting_btn_logout, more_help;

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    public FragmentSettingAkun() {}

    public static FragmentSettingAkun newInstance() {
        return newInstance("","");
    }

    public static FragmentSettingAkun newInstance(String param1, String param2) {
        FragmentSettingAkun fragment = new FragmentSettingAkun();
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
        accountSetting = (TextView) view.findViewById(R.id.accountSetting);
        more_help = (TextView) view.findViewById(R.id.more_help);
        setting_btn_logout = (TextView) view.findViewById(R.id.setting_btn_logout);
        setting_btn_logout.setVisibility(View.GONE);
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
        accountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUpdatePassword fragment = new FragmentUpdatePassword();
                DashboardKepalaDesa dashboard = DashboardKepalaDesa.instance;
                dashboard.pushFragmentDashboard(fragment);
            }
        });
        more_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FunctionUtil.isConnected(getBaseActivity())) {
                    DashboardKepalaDesa dashboard = DashboardKepalaDesa.instance;
                    DetailContentFragment detail = new DetailContentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.URL, "http://waru.pamekasankab.go.id/index.php");
                    bundle.putString(Constants.TITLE, more_help.getText().toString());
                    detail.setArguments(bundle);
                    dashboard.pushFragmentDashboard(detail);
                } else {
                    getBaseActivity().showAlertDialog(getResources().getString(R.string.alert), getResources().getString(R.string.error_connection_offline));
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
        return "Lainnya";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_lainnya;
    }

    @Override
    public void onResume() {
        super.onResume();
//        DashboardKepalaDesa.instance.showLeftMenu();
    }
}
