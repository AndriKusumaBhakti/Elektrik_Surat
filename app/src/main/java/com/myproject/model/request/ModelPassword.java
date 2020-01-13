package com.myproject.model.request;

public class ModelPassword {
    private String method;
    private String nik_penduduk;
    private String pass_lama;
    private String pass_baru;

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

    public String getPass_lama() {
        return pass_lama;
    }

    public void setPass_lama(String pass_lama) {
        this.pass_lama = pass_lama;
    }

    public String getPass_baru() {
        return pass_baru;
    }

    public void setPass_baru(String pass_baru) {
        this.pass_baru = pass_baru;
    }
}
