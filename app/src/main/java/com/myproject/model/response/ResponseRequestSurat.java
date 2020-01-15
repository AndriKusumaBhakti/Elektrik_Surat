package com.myproject.model.response;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseRequestSurat implements Serializable {
    private Boolean status;
    private String message;
    private ArrayList<String> result;
    private String key;

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

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
