package com.myproject.model.response;

import com.myproject.model.ModelRequestSurat;
import com.myproject.model.ModelSuratSaya;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseAllSurat implements Serializable {
    private Boolean status;
    private String message;
    private ArrayList<ModelRequestSurat> result;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ModelRequestSurat> getResult() {
        return result;
    }

    public void setResult(ArrayList<ModelRequestSurat> result) {
        this.result = result;
    }
}
