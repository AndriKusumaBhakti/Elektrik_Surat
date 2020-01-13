package com.myproject.api;

import android.content.Context;
import android.os.AsyncTask;

import com.myproject.R;
import com.myproject.model.ModelResponse;
import com.myproject.model.request.ModelPassword;
import com.myproject.model.request.RequestJenisSurat;
import com.myproject.model.response.ResponseFileSurat;
import com.myproject.util.StringUtil;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedString;

public abstract class TaskUpdatePassword extends AsyncTask<ModelPassword, Void, Boolean> {
    private Context context;
    private ModelResponse response;
    private RestAdapter restAdapter;
    private String errorMessage;

    public TaskUpdatePassword(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        restAdapter = BaseAPI.getMTAPIV9(context);
    }

    @Override
    protected Boolean doInBackground(ModelPassword... requests) {
        MedicAPI methods = restAdapter.create(MedicAPI.class);
        ModelPassword request = requests[0];
        MultipartTypedOutput output = convertData(request);
        try{
            response = methods.updatePass(output);
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

    private MultipartTypedOutput convertData(ModelPassword request){
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("method", new TypedString(StringUtil.checkNullString(request.getMethod())));
        multipartTypedOutput.addPart("nik_penduduk", new TypedString(StringUtil.checkNullString(request.getNik_penduduk())));
        multipartTypedOutput.addPart("pass_lama", new TypedString(StringUtil.checkNullString(request.getPass_lama())));
        multipartTypedOutput.addPart("pass_baru", new TypedString(StringUtil.checkNullString(request.getPass_baru())));
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

    protected abstract void onSuccess(ModelResponse response);
    protected abstract void onFailed(String message);
}
