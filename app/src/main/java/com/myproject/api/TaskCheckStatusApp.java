package com.myproject.api;

import android.content.Context;
import android.os.AsyncTask;

import com.myproject.R;
import com.myproject.model.request.StatusAppRequest;
import com.myproject.model.response.CheckStatusAppResponse;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

public abstract class TaskCheckStatusApp extends AsyncTask<StatusAppRequest, Void, Boolean> {
    private Context context;
    private CheckStatusAppResponse response;
    private RestAdapter restAdapter;
    private String errorMessage;

    public TaskCheckStatusApp(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        restAdapter = BaseAPI.getMTAPIV9(context);
    }

    @Override
    protected Boolean doInBackground(StatusAppRequest... requests) {
        MedicAPI methods = restAdapter.create(MedicAPI.class);
        StatusAppRequest request = requests[0];
        try{
            response = methods.checkStatusApp(request);
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

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if(aBoolean){
            onSuccessStatus(response);
        } else {
            if(errorMessage!=null){
                onFailedStatus(errorMessage);
            } else {
                onFailedStatus(context.getResources().getString(R.string.error_fetching_data));
            }
        }
    }

    protected abstract void onSuccessStatus(CheckStatusAppResponse response);
    protected abstract void onFailedStatus(String message);
}
