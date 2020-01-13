package com.myproject.model.response;

import com.myproject.model.ModelJenisSurat;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseJenisSurat implements Serializable {
    private Boolean status;
    private String message;
    private ArrayList<ModelJenisSurat> result;

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

    public ArrayList<ModelJenisSurat> getResult() {
        return result;
    }

    public void setResult(ArrayList<ModelJenisSurat> result) {
        this.result = result;
    }
}
