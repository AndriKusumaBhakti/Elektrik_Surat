package com.myproject.model;

import com.myproject.model.response.ModelPengguna;

import java.io.Serializable;

public class ModelScanQr implements Serializable {
    private GenerateQrCode qrcode;
    private ModelPengguna penduduk;
    private ModelSuratQr surat;
    private ModelJenisSurat jenissurat;
    private ModelDusun dusun;

    public GenerateQrCode getQrcode() {
        return qrcode;
    }

    public void setQrcode(GenerateQrCode qrcode) {
        this.qrcode = qrcode;
    }

    public ModelPengguna getPenduduk() {
        return penduduk;
    }

    public void setPenduduk(ModelPengguna penduduk) {
        this.penduduk = penduduk;
    }

    public ModelSuratQr getSurat() {
        return surat;
    }

    public void setSurat(ModelSuratQr surat) {
        this.surat = surat;
    }

    public ModelJenisSurat getJenissurat() {
        return jenissurat;
    }

    public void setJenissurat(ModelJenisSurat jenissurat) {
        this.jenissurat = jenissurat;
    }

    public ModelDusun getDusun() {
        return dusun;
    }

    public void setDusun(ModelDusun dusun) {
        this.dusun = dusun;
    }
}
