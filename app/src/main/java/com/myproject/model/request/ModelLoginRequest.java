package com.myproject.model.request;

public class ModelLoginRequest {
    private String method;
    private String nik_penduduk;
    private String password;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNik_penduduk() {
        return nik_penduduk;
    }

    public void setNik_penduduk(String nik_penduduk) {
        this.nik_penduduk = nik_penduduk;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
