package com.myproject.model;

import java.io.Serializable;

public class ModelSuratSaya implements Serializable {
    private String id_surat;
    private String jenis_surat;
    private String link_surat;
    private String approve;

    public String getId_surat() {
        return id_surat;
    }

    public void setId_surat(String id_surat) {
        this.id_surat = id_surat;
    }

    public String getJenis_surat() {
        return jenis_surat;
    }

    public void setJenis_surat(String jenis_surat) {
        this.jenis_surat = jenis_surat;
    }

    public String getLink_surat() {
        return link_surat;
    }

    public void setLink_surat(String link_surat) {
        this.link_surat = link_surat;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }
}
