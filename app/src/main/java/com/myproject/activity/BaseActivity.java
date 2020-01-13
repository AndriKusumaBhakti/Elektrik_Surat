package com.myproject.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.myproject.R;
import com.myproject.aplication.APP;
import com.myproject.base.ActivityInterface;
import com.myproject.base.OnActionbarListener;
import com.myproject.fragment.AlertDialog;
import com.myproject.fragment.BaseFragment;
import com.myproject.util.FragmentHelper;
import com.myproject.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity implements ActivityInterface {
    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;
    protected Context context;
    protected List<AsyncTask> threadList;
    private SparseIntArray mErrorString;
    private final int TRANSITION_REQUEST_CODE = 391;
    public static final int TRANSITION_OUT_IN = R.anim.slide_in_right;
    public static final int TRANSITION_OUT_OUT = R.anim.slide_out_left;
    private FragmentHelper fragmentHelper;
    public ViewAnimator menuAnimator;
    private View actionBarView;
    ActionBar actionBar;
    private OnActionbarListener actionbarListener;

    public TextView tvActionBarTitle;
    public ImageView leftIcon, rightIcon,rightIcon2;
    private ImageView logoTitle;
    Toolbar toolbar;
    protected LinearLayout layoutBar;
    protected RelativeLayout toolbarBackground;
    final static String DIALOG_FRAGMENT_FLAG = "DF_FLAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentHelper = FragmentHelper.getInstance(getSupportFragmentManager());
        context = this;
        mErrorString = new SparseIntArray();

        initSharedPreference();
        setContentView(getLayout());
        initView();
        setUICallbacks();
        showCustomActionBar();
        setFont();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.google_blue));

        }
    }

    private void initSharedPreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    private void setFont() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        TextUtil.getInstance(this).setFont(rootView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onBackPressed() {
        int stackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (stackCount > 0) {
            try{
                getSupportFragmentManager().popBackStack();
            }
            catch(IllegalStateException ignored){

            }
        } else {
            try{
                super.onBackPressed();
            }
            catch(IllegalStateException ignored){

            }
        }
        APP.log("count : "+stackCount);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void replaceFragment(int container, String tagFragment, BaseFragment fragment, boolean addBackToStack) {
        if(!isFinishing()){
            try{
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (addBackToStack) {
                    ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, R.anim.fragment_slide_out_left, R.anim.fragment_slide_in_right);
                    ft.addToBackStack(tagFragment);
                }
                ft.replace(container, fragment);
                ft.commit();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void replaceFragmentwithTag(int container, BaseFragment fragment, boolean addBackToStack,String tag) {
        if(!isFinishing()){
            try{
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (addBackToStack) {
                    ft.addToBackStack(null);
                }
                ft.replace(container, fragment,tag);
                ft.commit();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TRANSITION_REQUEST_CODE) {
            overridePendingTransition(TRANSITION_OUT_IN, TRANSITION_OUT_OUT);
        }
    }

    public String checkNullString(String string) {
        return (string == null) ? "" : string;
    }
    public void registerTask(AsyncTask task){
        if(threadList == null){
            threadList = new ArrayList<>();
        }
        threadList.add(task);
    }

    public void removeTask(AsyncTask task){
        if(threadList != null){
            threadList.remove(task);
        }
    }

    public void setActionBarTitle(String title) {
        APP.log("SET WITH : " + title);
        if (tvActionBarTitle != null) {
            tvActionBarTitle.setText(title);
            tvActionBarTitle.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void setDefaultActionbarIcon() {
        if (menuAnimator != null){
            menuAnimator.setDisplayedChild(0);
        }
        setLeftIcon(R.mipmap.ic_launcher);
        setRightIcon(0);
        setRightIcon2(0);
    }

    public void setLeftIcon(int drawableRes){
        if (leftIcon != null) {
            if (drawableRes == 0){
                leftIcon.setVisibility(View.GONE);
            }
            else{
                leftIcon.setVisibility(View.VISIBLE);
                leftIcon.setImageResource(drawableRes);
            }
        }
    }

    public void setRightIcon(int drawableRes) {
        if (rightIcon != null) {
            if (drawableRes == 0)
                rightIcon.setVisibility(View.GONE);
            else{
                rightIcon.setVisibility(View.VISIBLE);
                rightIcon.setImageResource(drawableRes);
            }
        }
    }

    public void setRightIcon2(int drawableRes) {
        if (rightIcon2 != null) {
            if (drawableRes == 0){
                rightIcon2.setVisibility(View.GONE);
            }
            else{
                rightIcon2.setVisibility(View.VISIBLE);
                rightIcon2.setImageResource(drawableRes);
            }
        }
    }

    public void showDisplayLogoTitle(boolean shows) {
        if (logoTitle != null) {
            if (shows) {
                logoTitle.setVisibility(View.VISIBLE);
                tvActionBarTitle.setVisibility(View.GONE);
            }
            else {
                logoTitle.setVisibility(View.GONE);
                tvActionBarTitle.setVisibility(View.VISIBLE);
            }
        }
    }

    public void changeHomeToolbarBackground(boolean change) {
        if (toolbarBackground != null) {
            if (change) {
                layoutBar.setBackgroundColor(getResources().getColor(R.color.google_green));
            }
            else {
                layoutBar.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions, final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(BaseActivity.this, requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {
            APP.log("[DONE] PERMISSIONS RECEIVED");
        }
    }

    public void setActionbarListener(OnActionbarListener actionbarListener) {
        this.actionbarListener = actionbarListener;
    }

    protected void showCustomActionBar() {

        actionBar = getSupportActionBar();
        APP.log("ACTIOn BAR " + actionBar);
        if (actionBar != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            actionBarView = inflater.inflate(R.layout.view_custom_base, null, false);
            actionbarClickListener();
            menuAnimator = (ViewAnimator) actionBarView.findViewById(R.id.menu_animator);

            tvActionBarTitle = (TextView) actionBarView.findViewById(R.id.menu_title);
            leftIcon = (ImageView) actionBarView.findViewById(R.id.menu_icons);
            toolbarBackground = (RelativeLayout) actionBarView.findViewById(R.id.toolbar_background);
            layoutBar  = (LinearLayout) actionBarView.findViewById(R.id.layoutBar);
            rightIcon = (ImageView) actionBarView.findViewById(R.id.menu_right_btn);
            rightIcon2 = (ImageView) actionBarView.findViewById(R.id.menu_right_btn2);
            logoTitle = (ImageView) actionBarView.findViewById(R.id.logoTitle);
            final Animation inAnim = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
            final Animation outAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(actionBarView);

            toolbar=(Toolbar)actionBarView.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);

            actionBar.show();
        }
    }

    private void actionbarClickListener() {
        View actionLeft = actionBarView.findViewById(R.id.menu_left_btn);
        View actionRight = actionBarView.findViewById(R.id.menu_right_btn);
        View actionRight2 = actionBarView.findViewById(R.id.menu_right_btn2);
        actionLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (actionbarListener != null) {
                    actionbarListener.onLeftIconClick();
                }
            }
        });

        actionRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (actionbarListener != null) {
                    actionbarListener.onRightIconClick();
                }
            }
        });

        actionRight2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (actionbarListener != null) {
                    actionbarListener.onRight2IconClick();
                }
            }
        });
    }

    public void showAlertDialog(String title, String content){
        showAlertDialog(title,content,"OK");
    }

    public void showAlertDialog(String title, String content, String btn){
        if(!isFinishing()){
            AlertDialog dialogs = new AlertDialog();
            dialogs.setTitleandContent(title, content, btn);
            dialogs.show(getFragmentManager(), DIALOG_FRAGMENT_FLAG);
            /*FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.add(dialogs, null);
            ft.commitAllowingStateLoss();*/
        }
    }
}
