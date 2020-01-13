package com.myproject.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.myproject.R;
import com.myproject.util.StringUtil;

public class AlertDialog extends DialogFragment {
    public interface AlertDialogListener{
        public void onDismmisClick();
    }

    private AlertDialogListener listener;

    public void setListener(AlertDialogListener listener) {
        this.listener = listener;
    }

    protected Button yesBtn;
    protected Dialog dialog;
    protected TextView cancelBtn;
    protected TextView titleText;
    protected TextView contentText;

    protected String title;
    protected String content;
    protected String btns;
    protected boolean capitalize = true;

    public void setTitleandContent(String title, String content) {
        this.title = title;
        this.content = content;
        this.btns = "OK";
    }

    public void setTitleandContent(String title, String content, String btns) {
        this.title = title;
        this.content = content;
        this.btns = btns;
    }

    public void setCapitalize(boolean capitalize){
        this.capitalize = capitalize;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // Prevent dialog close on back press button
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    if(listener != null){
                        listener.onDismmisClick();
                    }
                }

                return false;
            }
        });

        dialog.show();


        initViews();


        yesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
                if(listener!=null){
                    listener.onDismmisClick();
                }

            }


        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
                if(listener!=null){
                    listener.onDismmisClick();
                }
            }
        });

        if(StringUtil.checkNullString(title).trim().isEmpty() == false){
            titleText.setText(StringUtil.capitalizeFirstString(title));
        }
        else{
            titleText.setText(getContext().getResources().getString(R.string.alert));
        }

        if(StringUtil.checkNullString(content).trim().isEmpty() == false){
            if(capitalize) {
                contentText.setText(StringUtil.capitalizeString(content));
            } else {
                contentText.setText(content);
            }
        }
        else{
            contentText.setText("---");
        }

        if(StringUtil.checkNullString(btns).trim().isEmpty() == false){
            yesBtn.setText(StringUtil.capitalizeAllString(btns));
        }
        else{
            yesBtn.setText("---");
        }


        return dialog;
    }

    private void initViews() {
        yesBtn = (Button) dialog.findViewById(R.id.btn_yes);
        cancelBtn = (TextView) dialog.findViewById(R.id.close_x_text);
        titleText = (TextView) dialog.findViewById(R.id.txt_title);
        contentText = (TextView) dialog.findViewById(R.id.alert_content_text);
    }

    @Override
    public void dismiss() {

        super.dismiss();
    }
}
