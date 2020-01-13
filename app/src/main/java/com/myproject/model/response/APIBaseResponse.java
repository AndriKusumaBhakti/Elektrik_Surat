package com.myproject.model.response;

import java.io.Serializable;

/**
 * Created by frensky on 09/07/2015.
 */
public class APIBaseResponse implements Serializable {
    public boolean success;
    public String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
