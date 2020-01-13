package com.myproject.model.response;

import com.myproject.model.ModelScanQr;

import java.io.Serializable;

public class ResponseScanQr implements Serializable {
    private Boolean status;
    private String message;
    private ModelScanQr result;

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

    public ModelScanQr getResult() {
        return result;
    }

    public void setResult(ModelScanQr result) {
        this.result = result;
    }
}
