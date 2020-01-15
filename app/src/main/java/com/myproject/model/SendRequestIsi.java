package com.myproject.model;

import java.util.ArrayList;
import java.util.HashMap;

public class SendRequestIsi {
    private String isiSurat;
    private String method;
    private String nik_penduduk;
    private String idjenissurat;
    private ArrayList<HashMap<String, String>> value;

    public String getIsiSurat() {
        return isiSurat;
    }

    public void setIsiSurat(String isiSurat) {
        this.isiSurat = isiSurat;
    }

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

    public String getIdjenissurat() {
        return idjenissurat;
    }

    public void setIdjenissurat(String idjenissurat) {
        this.idjenissurat = idjenissurat;
    }

    public ArrayList<HashMap<String, String>> getValue() {
        return value;
    }

    public void setValue(ArrayList<HashMap<String, String>> value) {
        this.value = value;
    }
}
