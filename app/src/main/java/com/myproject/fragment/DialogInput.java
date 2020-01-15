//package com.myproject.fragment;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.myproject.R;
//import com.myproject.api.TaskCreateSurat;
//import com.myproject.aplication.APP;
//import com.myproject.aplication.Preference;
//import com.myproject.model.ModelResponse;
//import com.myproject.model.request.RequestAddSurat;
//import com.myproject.util.TextUtil;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Locale;
//
//public class DialogInput extends DialogFragment {
//
//    protected TextView contentView;
//    protected LinearLayout buttons;
//    protected Button leftBtn;
//    protected Dialog dialog;
//    protected ArrayList<String> content;
//    protected LinearLayout layout;
//    protected Button button_simpan;
//    protected TextView closeBtn;
//    protected String title;
//    protected TextView loading_dialog_title;
//
//    protected EditText keperluan;
//    protected EditText keterangan;
//    protected TextView start;
//    protected TextView end;
//    protected FragmentJenisSurat context;
//    protected String id_jenis_surat;
//    protected String myFormat = "dd MMMM yyyy";
//    protected SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
//
//    public void setTitleAndContent(FragmentJenisSurat context, String title, ArrayList<String> content, String id_jenis_surat){
//        this.context = context;
//        this.title = title;
//        this.content = content;
//        this.id_jenis_surat = id_jenis_surat;
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        dialog = new Dialog(getActivity());
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_input);
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                // Prevent dialog close on back press button
//                return keyCode == KeyEvent.KEYCODE_BACK;
//            }
//        });
//
//        initView();
//
//        if(!dialog.isShowing())
//            dialog.show();
//
//        return dialog;
//    }
//
//    private void initView(){
//        if (this.content.size()>0){
//            loading_dialog_title = (TextView) dialog.findViewById(R.id.loading_dialog_title);
//            if (this.title!=null){
//                loading_dialog_title.setText(this.title);
//            }else{
//                loading_dialog_title.setText("Pesan");
//            }
//            layout = (LinearLayout) dialog.findViewById(R.id.linearLayoutMain);
//            closeBtn = (TextView) dialog.findViewById(R.id.loading_dialog_close);
//
//            LinearLayout v = new LinearLayout(getActivity());
//            v.setOrientation(LinearLayout.VERTICAL);
//            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//
//            final Calendar myCalendar = Calendar.getInstance();
//            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//                @Override
//                public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                      int dayOfMonth) {
//                    // TODO Auto-generated method stub
//                    myCalendar.set(Calendar.YEAR, year);
//                    myCalendar.set(Calendar.MONTH, monthOfYear);
//                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                }
//            };
//            for (int i = 0; i<this.content.size(); i++){
//                LinearLayout vDetail = new LinearLayout(getActivity());
//                vDetail.setOrientation(LinearLayout.VERTICAL);
//                vDetail.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//                TextView tvKey = (TextView) getActivity().getLayoutInflater().inflate(R.layout.text_label, null);
//                tvKey.setText(this.content.get(i).trim());
//                tvKey.setTypeface(TextUtil.loadFontFromAssets(getActivity()));
//                if (this.content.get(i).equals("keperluan")) {
//                    keperluan = (EditText) getActivity().getLayoutInflater().inflate(R.layout.form_input, null);
//                    keperluan.setHint(this.content.get(i).trim());
//                    keperluan.setTypeface(TextUtil.loadFontFromAssets(getActivity()));
//                    vDetail.addView(tvKey);
//                    vDetail.addView(keperluan);
//                }else if (this.content.get(i).equals("keterangan")) {
//                    keterangan = (EditText) getActivity().getLayoutInflater().inflate(R.layout.form_input, null);
//                    keterangan.setHint(this.content.get(i).trim());
//                    keterangan.setTypeface(TextUtil.loadFontFromAssets(getActivity()));
//                    vDetail.addView(tvKey);
//                    vDetail.addView(keterangan);
//                }else if (this.content.get(i).equals("Berlaku")) {
//                    LinearLayout berlaku = new LinearLayout(getActivity());
//                    berlaku.setOrientation(LinearLayout.HORIZONTAL);
//                    berlaku.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//                    start = (TextView) getActivity().getLayoutInflater().inflate(R.layout.text_label_date, null);
//                    start.setText("Dari tanggal");
//
//                    start.setTypeface(TextUtil.loadFontFromAssets(getActivity()));
//
//                    end = (TextView) getActivity().getLayoutInflater().inflate(R.layout.text_label_date, null);
//                    end.setText("Sampai tanggal");
//                    end.setTypeface(TextUtil.loadFontFromAssets(getActivity()));
//
//                    start.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            new DatePickerDialog(getActivity(), date, myCalendar
//                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                            start.setText(sdf.format(myCalendar.getTime()));
//                        }
//                    });
//
//                    end.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            new DatePickerDialog(getActivity(), date, myCalendar
//                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                            end.setText(sdf.format(myCalendar.getTime()));
//                        }
//                    });
//
//                    berlaku.addView(start);
//                    berlaku.addView(end);
//
//                    vDetail.addView(tvKey);
//                    vDetail.addView(berlaku);
//                }
//
//                v.addView(vDetail);
//            }
//            layout.addView(v);
//            button_simpan = (Button) dialog.findViewById(R.id.button_simpan);
//            button_simpan.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getSendData();
//                }
//            });
//            closeBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//        }
//
//    }
//
//    private void getSendData(){
//        LoadingDialog loading = new LoadingDialog();
//        loading.show(context.getBaseActivity().getFragmentManager(), context.DIALOG_FRAGMENT_FLAG);
//        RequestAddSurat surat = new RequestAddSurat();
//        surat.setMethod("createSurat");
//        surat.setId_jenis_surat(id_jenis_surat);
//        surat.setNik_penduduk(APP.getStringPref(getActivity(), Preference.NIK));
//        if (keperluan != null){
//            surat.setKeperluan(keperluan.getText().toString());
//        }else{
//            surat.setKeperluan(null);
//        }
//        if (keterangan != null){
//            surat.setKeterangan(keterangan.getText().toString().trim());
//        }else{
//            surat.setKeterangan(null);
//        }
//        String berlaku = null;
//        if (start != null){
//            berlaku = String.valueOf(start.getText().toString().trim());
//        }
//        if (end != null){
//            berlaku = String.valueOf(berlaku+" - "+end.getText().toString().trim());
//        }
//        if (berlaku != null){
//            surat.setBerlaku(berlaku);
//        }else{
//            surat.setBerlaku(null);
//        }
//        TaskCreateSurat task = new TaskCreateSurat(context.getBaseActivity()) {
//            @Override
//            protected void onSuccess(ModelResponse response) {
//                context.removeTask(this);
//                if (loading != null){
//                    loading.dismiss();
//                }
//                if (response.getStatus()){
//                    dismiss();
//                    /*context.getBaseActivity().showAlertDialog("Pesan", "Permohonan sedang di proses");*/
//                }else{
//                    /*context.getBaseActivity().showAlertDialog("Pesan", response.getMessage());*/
//                }
//            }
//
//            @Override
//            protected void onFailed(String message) {
//                context.removeTask(this);
//                if (loading != null){
//                    loading.dismiss();
//                }
//                /*context.getBaseActivity().showAlertDialog("Pesan", message);*/
//            }
//        };
//        context.registerTask(task);
//        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, surat);
//    }
//
//    @Override
//    public void show(FragmentManager manager, String tag) {
//        if(manager.findFragmentByTag(tag) == null) {
//            super.show(manager, tag);
//        }
//    }
//
//    @Override
//    public void dismiss() {
//        dialog.dismiss();
//        super.dismiss();
//    }
//
//}
//
