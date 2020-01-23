package com.myproject.model.request;

import retrofit.mime.TypedFile;

public class RequestPhoto {
    private String method;
    private String nik_penduduk;
    private TypedFile foto;

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

    public TypedFile getFoto() {
        return foto;
    }

    public void setFoto(TypedFile foto) {
        this.foto = foto;
    }
}
