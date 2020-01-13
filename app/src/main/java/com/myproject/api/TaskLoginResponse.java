package com.myproject.api;

import android.content.Context;
import android.os.AsyncTask;

import com.myproject.R;
import com.myproject.model.request.ModelLoginRequest;
import com.myproject.model.request.ModelRequestPassword;
import com.myproject.model.response.ModelResponsePassword;
import com.myproject.model.response.ResponseLogin;
import com.myproject.util.StringUtil;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedString;

public abstract class TaskLoginResponse extends AsyncTask<ModelLoginRequest, Void, Boolean> {
    private Context context;
    private ResponseLogin response;
    private RestAdapter restAdapter;
    private String errorMessage;

    public TaskLoginResponse(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        restAdapter = BaseAPI.getMTAPIV9(context);
    }

    @Override
    protected Boolean doInBackground(ModelLoginRequest... requests) {
        MedicAPI methods = restAdapter.create(MedicAPI.class);
        ModelLoginRequest request = requests[0];
        MultipartTypedOutput output = convertData(request);
        try{
            response = methods.cekLogin(output);
        } catch (RetrofitError error){
            errorMessage = error.getLocalizedMessage();
            return false;
        } catch (Exception error){
            if(error.getCause()!=null){
                errorMessage =  error.getCause().getMessage();
            }
            return false;
        }

        return true;
    }

    private MultipartTypedOutput convertData(ModelLoginRequest request){
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("method", new TypedString(StringUtil.checkNullString(request.getMethod())));
        multipartTypedOutput.addPart("nik_penduduk", new TypedString(StringUtil.checkNullString(request.getNik_penduduk())));
        multipartTypedOutput.addPart("password", new TypedString(StringUtil.checkNullString(request.getPassword())));
        return multipartTypedOutput;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(aBoolean){
            onSuccess(response);
        } else {
            if(errorMessage!=null){
                onFailed(errorMessage);
            } else {
                onFailed(context.getResources().getString(R.string.error_fetching_data));
            }
        }
    }

    protected abstract void onSuccess(ResponseLogin response);
    protected abstract void onFailed(String message);
}
