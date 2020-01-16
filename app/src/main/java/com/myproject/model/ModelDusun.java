package com.myproject.model;

import java.io.Serializable;

public class ModelDusun implements Serializable {
    private String id_dusun;
    private String nama_dusun;

    public String getId_dusun() {
        return id_dusun;
    }

    public void setId_dusun(String id_dusun) {
        this.id_dusun = id_dusun;
    }

    public String getNama_dusun() {
        return nama_dusun;
    }

    public void setNama_dusun(String nama_dusun) {
        this.nama_dusun = nama_dusun;
    }
}
