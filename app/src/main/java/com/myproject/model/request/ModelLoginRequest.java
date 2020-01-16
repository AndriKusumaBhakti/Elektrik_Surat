package com.myproject.model.request;

public class ModelLoginRequest {
    private String method;
    private String nik_penduduk;
    private String password;
    private String registrasi_id;

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

    public String getRegistrasi_id() {
        return registrasi_id;
    }

    public void setRegistrasi_id(String registrasi_id) {
        this.registrasi_id = registrasi_id;
    }
}
