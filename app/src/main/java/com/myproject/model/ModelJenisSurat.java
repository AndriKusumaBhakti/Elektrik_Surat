package com.myproject.model;

import java.io.Serializable;

public class ModelJenisSurat implements Serializable {
    private String id_jenis_surat;
    private String jenis_surat;
    private String status;
    private String status_surat;

    public String getId_jenis_surat() {
        return id_jenis_surat;
    }

    public void setId_jenis_surat(String id_jenis_surat) {
        this.id_jenis_surat = id_jenis_surat;
    }

    public String getJenis_surat() {
        return jenis_surat;
    }

    public void setJenis_surat(String jenis_surat) {
        this.jenis_surat = jenis_surat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_surat() {
        return status_surat;
    }

    public void setStatus_surat(String status_surat) {
        this.status_surat = status_surat;
    }
}
