package com.myproject.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myproject.R;

public class LoadingDialog extends DialogFragment {

    protected TextView titleView;
    protected TextView contentView;
    protected LinearLayout buttons;
    protected Button leftBtn;
    protected Button rightBtn;
    protected TextView closeBtn;
    protected ProgressBar progress;
    protected Dialog dialog;

    public static final String DIALOG_TITLE = "DIALOG_TITLE";
    public static final String DIALOG_CONTENT = "DIALOG_CONTENT";
    public static final String LEFT_BUTTON = "LEFT_BUTTON";
    public static final String RIGHT_BUTTON = "RIGHT_BUTTON";
    public static final String LOADING_ICON = "LOADING_ICON";
    public static final String CLOSE_BTN = "CLOSE_BTN";
    protected boolean cancelLable = false;

    public interface LoadingDialogListener{
        public void onLeftButtonClick();
        public void onRightButtonClick();
    }

    public void setCancelLable(boolean cancelLable){
        this.cancelLable = cancelLable;
    }

    private LoadingDialogListener listener;

    public void setListener(LoadingDialogListener listener) {
        this.listener = listener;
    }

    public void setTitleAndContent(String title, String content){
        titleView.setText(title);
        contentView.setText(content);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setContent(String content){
        contentView.setText(content);
    }

    public void showProgressBar(){
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        progress.setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(cancelLable);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // Prevent dialog close on back press button
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        initView();

        loadArgument();

        if(!dialog.isShowing())
            dialog.show();

        return dialog;
    }

    public void loadArgument(){
        Bundle bundle = getArguments();
        if(bundle!=null){
            if(bundle.containsKey(DIALOG_TITLE)){
                titleView.setText(bundle.getString(DIALOG_TITLE));
            }
            if(bundle.containsKey(DIALOG_CONTENT)){
                contentView.setText(bundle.getString(DIALOG_CONTENT));
            }
            if(bundle.containsKey(LEFT_BUTTON)){
                leftBtn.setText(bundle.getString(LEFT_BUTTON));
                buttons.setVisibility(View.VISIBLE);
                leftBtn.setVisibility(View.VISIBLE);
            }
            if(bundle.containsKey(RIGHT_BUTTON)){
                rightBtn.setText(bundle.getString(RIGHT_BUTTON));
                buttons.setVisibility(View.VISIBLE);
                rightBtn.setVisibility(View.VISIBLE);
            }
            if(bundle.containsKey(LOADING_ICON)){
                if(getArguments().getBoolean(LOADING_ICON)){
                    progress.setVisibility(View.VISIBLE);
                } else {
                    progress.setVisibility(View.GONE);
                }

            }
            if(bundle.containsKey(CLOSE_BTN)){
                if(getArguments().getBoolean(CLOSE_BTN)){
                    closeBtn.setVisibility(View.VISIBLE);
                } else {
                    closeBtn.setVisibility(View.GONE);
                }

            }
        }
    }

    private void initView(){
        titleView = (TextView) dialog.findViewById(R.id.loading_dialog_title);
        contentView = (TextView) dialog.findViewById(R.id.loading_dialog_content);
        progress = (ProgressBar) dialog.findViewById(R.id.loading_dialog_progress);
        buttons = (LinearLayout) dialog.findViewById(R.id.loading_buttons);
        leftBtn = (Button) dialog.findViewById(R.id.loading_button_left);
        rightBtn = (Button) dialog.findViewById(R.id.loading_button_right);
        closeBtn = (TextView) dialog.findViewById(R.id.loading_dialog_close);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onLeftButtonClick();
                    dismiss();
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onRightButtonClick();
                    dismiss();
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag);
        }
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
        super.dismiss();
    }

}

