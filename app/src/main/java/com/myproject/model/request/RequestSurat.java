package com.myproject.model.request;

public class RequestSurat {
    private String method;
    private String id_jenis;
    private String nik_kependudukan;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getId_jenis() {
        return id_jenis;
    }

    public void setId_jenis(String id_jenis) {
        this.id_jenis = id_jenis;
    }

    public String getNik_kependudukan() {
        return nik_kependudukan;
    }

    public void setNik_kependudukan(String nik_kependudukan) {
        this.nik_kependudukan = nik_kependudukan;
    }
}
