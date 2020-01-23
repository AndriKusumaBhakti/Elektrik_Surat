package com.myproject.api;

import android.content.Context;
import android.os.AsyncTask;

import com.myproject.R;
import com.myproject.model.ModelResponse;
import com.myproject.model.request.RequestJenisSurat;
import com.myproject.model.request.RequestPhoto;
import com.myproject.model.response.ResponseLogin;
import com.myproject.util.StringUtil;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedString;

public abstract class TaskUpdateFoto extends AsyncTask<RequestPhoto, Void, Boolean> {
    private Context context;
    private ResponseLogin response;
    private RestAdapter restAdapter;
    private String errorMessage;

    public TaskUpdateFoto(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        restAdapter = BaseAPI.getMTAPIV9(context);
    }

    @Override
    protected Boolean doInBackground(RequestPhoto... requests) {
        MedicAPI methods = restAdapter.create(MedicAPI.class);
        RequestPhoto request = requests[0];
        MultipartTypedOutput output = convertData(request);
        try{
            response = methods.aploadFotoProfile(output);
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

    private MultipartTypedOutput convertData(RequestPhoto request){
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("method", new TypedString(StringUtil.checkNullString(request.getMethod())));
        multipartTypedOutput.addPart("nik_penduduk", new TypedString(StringUtil.checkNullString(request.getNik_penduduk())));
        if(request.getFoto()!=null) {
            multipartTypedOutput.addPart("foto", request.getFoto());
        }
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
