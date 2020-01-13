package com.myproject.model;

import java.io.Serializable;

/**
 * Created by frensky on 09/07/2015.
 */
public class ModelResponse implements Serializable {
    private Boolean status;
    private String message;

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
}
