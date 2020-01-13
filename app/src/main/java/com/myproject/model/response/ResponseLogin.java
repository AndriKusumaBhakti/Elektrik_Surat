package com.myproject.model.response;

public class ResponseLogin {
    private boolean sukses;
    private String message;
    private ModelPengguna result;

    public boolean isSukses() {
        return sukses;
    }

    public void setSukses(boolean sukses) {
        this.sukses = sukses;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelPengguna getResult() {
        return result;
    }

    public void setResult(ModelPengguna result) {
        this.result = result;
    }
}
