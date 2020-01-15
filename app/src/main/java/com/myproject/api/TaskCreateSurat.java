package com.myproject.api;

import android.content.Context;
import android.os.AsyncTask;

import com.myproject.R;
import com.myproject.aplication.APP;
import com.myproject.model.ModelResponse;
import com.myproject.model.SendRequestIsi;
import com.myproject.model.request.RequestAddSurat;
import com.myproject.model.request.RequestJenisSurat;
import com.myproject.model.response.ResponseJenisSurat;
import com.myproject.util.StringUtil;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedString;

public abstract class TaskCreateSurat extends AsyncTask<SendRequestIsi, Void, Boolean> {
    private Context context;
    private ModelResponse response;
    private RestAdapter restAdapter;
    private String errorMessage;

    public TaskCreateSurat(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        restAdapter = BaseAPI.getMTAPIV9(context);
    }

    @Override
    protected Boolean doInBackground(SendRequestIsi... requests) {
        MedicAPI methods = restAdapter.create(MedicAPI.class);
        SendRequestIsi request = requests[0];
        MultipartTypedOutput output = convertData(request);
        try{
            response = methods.createSurat(output);
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

    private MultipartTypedOutput convertData(SendRequestIsi request){
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("method", new TypedString(StringUtil.checkNullString(request.getMethod())));
        multipartTypedOutput.addPart("nik_penduduk", new TypedString(StringUtil.checkNullString(request.getNik_penduduk())));
        multipartTypedOutput.addPart("idjenissurat", new TypedString(StringUtil.checkNullString(request.getIdjenissurat())));
        if (request.getValue()!= null) {
            if (request.getValue().size() > 0) {
                for (int i = 0; i < request.getValue().size(); i++) {
                    String name = String.valueOf(request.getValue().get(i).keySet()).substring(1, String.valueOf(request.getValue().get(i).keySet()).length()-1);
                    multipartTypedOutput.addPart(name, new TypedString(StringUtil.checkNullString(String.valueOf(request.getValue().get(i).get(name)))));
                }
            }
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

    protected abstract void onSuccess(ModelResponse response);
    protected abstract void onFailed(String message);
}
