package com.myproject.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.myproject.R;
import com.myproject.StartActivity;
import com.myproject.api.TaskLoginResponse;
import com.myproject.api.TaskRequestPassword;
import com.myproject.aplication.APP;
import com.myproject.aplication.Preference;
import com.myproject.base.OnActionbarListener;
import com.myproject.database.Account;
import com.myproject.model.request.ModelLoginRequest;
import com.myproject.model.request.ModelRequestPassword;
import com.myproject.model.response.ModelResponsePassword;
import com.myproject.model.response.ResponseLogin;

public class LoginFragment extends BaseFragment implements View.OnClickListener{
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView menu_right_btn;
    private TextInputLayout login_label_username;
    private TextInputLayout login_label_password;
    private EditText login_input_username;
    private EditText login_input_password;
    private ImageView show_password;
//    private TextView lupa_password;
    private Button login_btn;
    private boolean isEmail;
    private boolean isChecked = true;

    final static String DIALOG_FRAGMENT_FLAG = "DF_FLAG";


    public LoginFragment() {}

    public static LoginFragment newInstance() {
        return newInstance("","");
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView(View view) {
        menu_right_btn = (ImageView) view.findViewById(R.id.menu_right_btn);
        login_label_username = (TextInputLayout) view.findViewById(R.id.login_label_username);
        login_label_password = (TextInputLayout) view.findViewById(R.id.login_label_password);
        login_input_username = (EditText) view.findViewById(R.id.login_input_username);
        login_input_password = (EditText) view.findViewById(R.id.login_input_password);
        show_password = (ImageView) view.findViewById(R.id.show_password);
//        lupa_password = (TextView) view.findViewById(R.id.lupa_password);
        login_btn = (Button) view.findViewById(R.id.login_btn);
    }

    @Override
    public void setUICallbacks() {
        login_btn.setOnClickListener(this);
        menu_right_btn.setOnClickListener(this);
        show_password.setOnClickListener(this);
//        lupa_password.setOnClickListener(this);
        login_input_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 16) {
                    login_label_username.setError(getResources().getString(R.string.invalid_nik));
                    login_input_username.setTextColor(getResources().getColor(R.color.material_red));
                } else {
                    login_label_username.setError(null);
                    login_input_username.setTextColor(getResources().getColor(R.color.black));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        login_input_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    login_label_password.setError(getResources().getString(R.string.invalid_password));
                    login_input_password.setTextColor(getResources().getColor(R.color.material_red));
                } else {
                    login_label_password.setError(null);
                    login_input_password.setTextColor(getResources().getColor(R.color.black));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    @Override
    public void updateUI() {
        /*getBaseActivity().setLeftIcon(0);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(0);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(false);*/
    }

    @Override
    public String getPageTitle() {
        return "Login";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fregment_login;
    }

    @Override
    public void onClick(View view) {
        if (view==login_btn){
            APP.log("klik masuk");
            if (validateForm()){
                LoadingDialog loadingRequest = new LoadingDialog();
                loadingRequest.show(getActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
                ModelLoginRequest request = new ModelLoginRequest();
                request.setMethod("login");
                request.setNik_penduduk(String.valueOf(login_input_username.getText().toString()));
                request.setPassword(String.valueOf(login_input_password.getText().toString()));
                request.setRegistrasi_id(APP.getStringPref(getActivity(), Preference.ONE_SIGNAL_USER_ID));

                TaskLoginResponse task = new TaskLoginResponse(getActivity()) {
                    @Override
                    protected void onSuccess(ResponseLogin response) {
                        removeTask(this);
                        loadingRequest.dismiss();
                        if (response.getResult()!=null){
                            APP.setPreference(getBaseActivity(), Preference.TOKEN, String.valueOf(response.getResult().getToken()));
                            APP.setPreference(getBaseActivity(), Preference.ROLE, String.valueOf(response.getResult().getId_user()));
                            APP.setPreference(getBaseActivity(), Preference.NIK, String.valueOf(response.getResult().getNik_penduduk()));
                            Account akun = new Account(getBaseActivity());
                            akun.parseAccount(response.getResult());
                            Intent intent = new Intent(getBaseActivity(), StartActivity.class);
                            startActivity(intent);
                            getBaseActivity().finish();
                        }else{
                            getBaseActivity().showAlertDialog("Pesan", response.getMessage());
                        }
                    }

                    @Override
                    protected void onFailed(String message) {
                        removeTask(this);
                        loadingRequest.dismiss();
                        getBaseActivity().showAlertDialog("Pesan", message);
                    }
                };
                registerTask(task);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
            }
        }else if (view==menu_right_btn){
            if (validateFormRequestPassword()){
                LoadingDialog loadingRequest = new LoadingDialog();
                loadingRequest.show(getActivity().getFragmentManager(), DIALOG_FRAGMENT_FLAG);
                ModelRequestPassword request = new ModelRequestPassword();
                request.setMethod("reqPassword");
                request.setNik_penduduk(String.valueOf(login_input_username.getText().toString()));
                request.setRegistrasi_id(APP.getStringPref(getActivity(), Preference.ONE_SIGNAL_USER_ID));

                TaskRequestPassword task= new TaskRequestPassword(getActivity()) {
                    @Override
                    protected void onSuccess(ModelResponsePassword response) {
                        removeTask(this);
                        loadingRequest.dismiss();
                        getBaseActivity().showAlertDialog("Pesan", response.getMessage());
                    }

                    @Override
                    protected void onFailed(String message) {
                        removeTask(this);
                        loadingRequest.dismiss();
                        getBaseActivity().showAlertDialog("Pesan", message);
                    }
                };
                registerTask(task);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);

            }
        }else if (view == show_password){
            if (isChecked){
                isChecked = false;
                login_input_password.setInputType(InputType.TYPE_CLASS_TEXT);
                show_password.setImageDrawable(getResources().getDrawable(R.drawable.icon_hidd_pass));
            }else{
                isChecked = true;
                login_input_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                show_password.setImageDrawable(getResources().getDrawable(R.drawable.icon_show_pass));
            }
        }
    }

    private boolean validateForm() {
        boolean status = true;
        if (login_input_username.getText().toString().isEmpty()) {
            login_label_username.setErrorEnabled(true);
            login_label_username.setError(getResources().getString(R.string.nik_requiret));
            status = false;
        } else {
            login_label_username.setError(null);
        }
        if (login_input_password.getText().toString().isEmpty()) {
            login_label_password.setErrorEnabled(true);
            login_label_password.setError(getResources().getString(R.string.password_requiret));
            status = false;
        } else {
            login_label_password.setError(null);
        }
        return status;
    }

    private boolean validateFormRequestPassword() {
        boolean status = true;
        if (login_input_username.getText().toString().isEmpty()) {
            login_label_username.setErrorEnabled(true);
            login_label_username.setError(getResources().getString(R.string.nik_requiret));
            status = false;
        } else {
            login_label_username.setError(null);
        }
        return status;
    }
}
