package com.myproject.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.integration.android.IntentIntegrator;
import com.myproject.R;
import com.myproject.activity.DashboardActivity;
import com.myproject.adapter.AdapterMenuUtama;
import com.myproject.aplication.APP;
import com.myproject.aplication.Config;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.util.CircleTransform;

import java.util.List;

public class MenuFragment extends BaseFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    private ImageView profile_home_avatar, barAvatar;
    ViewAnimator selectProfileAnimator;
    private boolean isShowSelectProfile;
    private TextView name_user;
    private TextView alamat_uset;
    private TextView ttl_user;
    private AdapterMenuUtama mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView menuUtama;

    private IntentIntegrator qrScanner;

    public MenuFragment() {}

    public static MenuFragment newInstance() {
        return newInstance("","");
    }

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
        profile_home_avatar = (ImageView) view.findViewById(R.id.profile_home_avatar);
        barAvatar = (ImageView) view.findViewById(R.id.barAvatar);
        menuUtama = (RecyclerView) view.findViewById(R.id.menuUtama);
        selectProfileAnimator = (ViewAnimator) view.findViewById(R.id.select_profile_animator);
        name_user = (TextView) view.findViewById(R.id.name_user);
        alamat_uset = (TextView) view.findViewById(R.id.alamat_uset);
        ttl_user = (TextView) view.findViewById(R.id.ttl_user);
        qrScanner = new IntentIntegrator(getBaseActivity());
        qrScanner.setBeepEnabled(false);
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
                qrScanner.setPrompt(getResources().getString(R.string.hintscanQr));
                qrScanner.initiateScan();
            }

            @Override
            public void onRight2IconClick() {

            }
        });
        String[] menu = getResources().getStringArray(R.array.main_menu_utama);
        Integer[] mThumbIds = {R.drawable.icon_home,
                R.drawable.jenis_surat, R.drawable.file_saya,
                R.drawable.icon_more
        };

        mLayoutManager = new GridLayoutManager(getActivity(),2);
        menuUtama.setLayoutManager(mLayoutManager);
        mAdapter = new AdapterMenuUtama(getBaseActivity(), menu, mThumbIds, new MyAdapterListener());
        menuUtama.setAdapter(mAdapter);
    }

    private class MyAdapterListener implements AdapterMenuUtama.ClickListener{
        @Override
        public void onCLick(int posisi) {

            DashboardActivity dashboard = DashboardActivity.instance;
            if (posisi == 0){
                FragmentBeranda fragment = new FragmentBeranda();
                dashboard.pushFragmentDashboard(fragment);
            }else if (posisi==1){
                FragmentJenisSurat fragment = new FragmentJenisSurat();
                dashboard.pushFragmentDashboard(fragment);
            }else if (posisi==2){
                FragmentFileSaya fragment = new FragmentFileSaya();
                dashboard.pushFragmentDashboard(fragment);
            }else if (posisi==3){
                FragmentLainnya fragment = new FragmentLainnya();
                dashboard.pushFragmentDashboard(fragment);
            }
        }
    }

    @Override
    public void updateUI() {
        getBaseActivity().setLeftIcon(R.drawable.no_user);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(R.drawable.icon_scan_qr_white);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(true);
        getBaseActivity().setLeftView(accountEntity.getFoto());
    }

    @Override
    public String getPageTitle() {
        return "E-Surat";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_menu;
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
}
