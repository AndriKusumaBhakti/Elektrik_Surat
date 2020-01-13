package com.myproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.myproject.R;
import com.myproject.StartActivity;
import com.myproject.activity.DashboardActivity;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.util.Constants;
import com.myproject.util.FunctionUtil;

import java.util.List;

public class FragmentLainnya extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView accountSetting, setting_btn_logout, more_help;
    ViewAnimator selectProfileAnimator;
    private boolean isShowSelectProfile;
    private TextView name_user;
    private TextView alamat_uset;
    private TextView ttl_user;

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    public FragmentLainnya() {}

    public static FragmentLainnya newInstance() {
        return newInstance("","");
    }

    public static FragmentLainnya newInstance(String param1, String param2) {
        FragmentLainnya fragment = new FragmentLainnya();
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
        setting_btn_logout = (TextView) view.findViewById(R.id.setting_btn_logout);
        accountSetting = (TextView) view.findViewById(R.id.accountSetting);
        more_help = (TextView) view.findViewById(R.id.more_help);
        selectProfileAnimator = (ViewAnimator) view.findViewById(R.id.select_profile_animator);
        name_user = (TextView) view.findViewById(R.id.name_user);
        alamat_uset = (TextView) view.findViewById(R.id.alamat_uset);
        ttl_user = (TextView) view.findViewById(R.id.ttl_user);
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
        setting_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutOn();
            }
        });
        accountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUpdatePassword fragment = new FragmentUpdatePassword();
                DashboardActivity dashboard = DashboardActivity.instance;
                dashboard.pushFragmentDashboard(fragment);
            }
        });
        more_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FunctionUtil.isConnected(getBaseActivity())) {
                    DashboardActivity dashboard = DashboardActivity.instance;
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

    private void logoutOn(){
        if (FunctionUtil.isConnected(getBaseActivity())) {
            final LoadingDialog dialog = new LoadingDialog();
            Bundle b = new Bundle();
            b.putString(LoadingDialog.DIALOG_TITLE, getResources().getString(R.string.confirmation));
            b.putString(LoadingDialog.DIALOG_CONTENT, getResources().getString(R.string.are_you_sure_logout));
            b.putString(LoadingDialog.LEFT_BUTTON, getResources().getString(R.string.yes));
            b.putString(LoadingDialog.RIGHT_BUTTON, getResources().getString(R.string.cancel));
            b.putBoolean(LoadingDialog.LOADING_ICON, false);
            b.putBoolean(LoadingDialog.CLOSE_BTN, true);
            dialog.setArguments(b);
            dialog.setListener(new LoadingDialog.LoadingDialogListener() {
                @Override
                public void onLeftButtonClick() {
                    APP.setPreference(getBaseActivity(), Preference.TOKEN,"");
                    APP.setPreference(getBaseActivity(), Preference.ROLE,"");
                    account.deleteAllInterval();
                    Intent intent = new Intent(getBaseActivity(), StartActivity.class);
                    startActivity(intent);
                    getBaseActivity().finish();
                }

                @Override
                public void onRightButtonClick() {

                }
            });
            dialog.show(getBaseActivity().getFragmentManager(), LoadingDialog.class.getName());
        } else {
            getBaseActivity().showAlertDialog(getResources().getString(R.string.alert), getResources().getString(R.string.error_connection_offline));
        }
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
        return "Lainnya";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_lainnya;
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
