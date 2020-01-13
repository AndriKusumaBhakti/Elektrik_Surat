package com.myproject.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.myproject.R;
import com.myproject.api.TaskUpdatePassword;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.database.AccountEntity;
import com.myproject.model.ModelResponse;
import com.myproject.model.request.ModelPassword;

import java.util.List;

public class FragmentUpdatePassword extends BaseFragment implements View.OnClickListener{
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Account account;
    private List<AccountEntity> profiles;
    private AccountEntity accountEntity;

    private EditText input_password_lama;
    private EditText input_password_baru;
    private EditText input_ulang_password_baru;

    private TextInputLayout label_password_lama;
    private TextInputLayout label_password_baru;
    private TextInputLayout label_ulang_password_baru;

    private Button btn_simpan;
    final LoadingDialog loading = new LoadingDialog();

    public FragmentUpdatePassword() {}

    public static FragmentUpdatePassword newInstance() {
        return newInstance("","");
    }

    public static FragmentUpdatePassword newInstance(String param1, String param2) {
        FragmentUpdatePassword fragment = new FragmentUpdatePassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        input_password_lama = (EditText) view.findViewById(R.id.input_password_lama);
        input_password_baru = (EditText) view.findViewById(R.id.input_password_baru);
        input_ulang_password_baru = (EditText) view.findViewById(R.id.input_ulang_password_baru);

        label_password_lama = (TextInputLayout) view.findViewById(R.id.label_password_lama);
        label_password_baru = (TextInputLayout) view.findViewById(R.id.label_password_baru);
        label_ulang_password_baru = (TextInputLayout) view.findViewById(R.id.label_ulang_password_baru);

        btn_simpan = (Button) view.findViewById(R.id.btn_simpan);
    }

    @Override
    public void setUICallbacks() {
        btn_simpan.setOnClickListener(this);
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
        input_password_lama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    label_password_lama.setError(getResources().getString(R.string.invalid_password));
                    input_password_lama.setTextColor(getResources().getColor(R.color.material_red));
                } else {
                    label_password_lama.setError(null);
                    input_password_lama.setTextColor(getResources().getColor(R.color.black));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        input_password_baru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    label_password_baru.setError(getResources().getString(R.string.invalid_password));
                    input_password_baru.setTextColor(getResources().getColor(R.color.material_red));
                } else {
                    label_password_baru.setError(null);
                    input_password_baru.setTextColor(getResources().getColor(R.color.black));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        input_ulang_password_baru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(input_password_baru.getText().toString())) {
                    label_ulang_password_baru.setError(getResources().getString(R.string.invalid_password_confirm));
                    input_ulang_password_baru.setTextColor(getResources().getColor(R.color.material_red));
                } else {
                    label_ulang_password_baru.setError(null);
                    input_ulang_password_baru.setTextColor(getResources().getColor(R.color.black));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
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
        return "Atur password";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_update_password;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_simpan) {
            if (validateForm()) {
                loading.show(getBaseActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
                ModelPassword model = new ModelPassword();
                model.setMethod("reqUpdatePassword");
                model.setNik_penduduk(accountEntity.getNik());
                model.setPass_lama(input_password_lama.getText().toString().trim());
                model.setPass_baru(input_password_baru.getText().toString().trim());
                TaskUpdatePassword task = new TaskUpdatePassword(getBaseActivity()) {
                    @Override
                    protected void onSuccess(ModelResponse response) {
                        if (loading != null){
                            loading.dismiss();
                        }
                        getBaseActivity().showAlertDialog("Pesan", response.getMessage());
                        if (response.getStatus()) {
                            getFragmentManager().popBackStack();
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
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model);
            }
        }
    }

    private boolean validateForm() {
        boolean status = true;
        if (input_password_lama.getText().toString().isEmpty()) {
            label_password_lama.setErrorEnabled(true);
            label_password_lama.setError(getResources().getString(R.string.password_requiret));
            status = false;
        } else {
            label_password_lama.setError(null);
        }

        if (input_password_baru.getText().toString().isEmpty()) {
            label_password_baru.setErrorEnabled(true);
            label_password_baru.setError(getResources().getString(R.string.password_requiret));
            status = false;
        } else {
            label_password_baru.setError(null);
        }

        if (input_ulang_password_baru.getText().toString().isEmpty()) {
            label_ulang_password_baru.setErrorEnabled(true);
            label_ulang_password_baru.setError(getResources().getString(R.string.password_requiret));
            status = false;
        } else {
            label_ulang_password_baru.setError(null);
        }
        return status;
    }
}
