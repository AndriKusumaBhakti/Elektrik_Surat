package com.myproject.model.response;

import com.myproject.model.ModelSuratSaya;

import java.util.ArrayList;

public class ResponseFileSurat {
    private Boolean status;
    private String message;
    private ArrayList<ModelSuratSaya> result;

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

    public ArrayList<ModelSuratSaya> getResult() {
        return result;
    }

    public void setResult(ArrayList<ModelSuratSaya> result) {
        this.result = result;
    }
}
