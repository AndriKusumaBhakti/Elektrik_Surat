package com.myproject.model;

import java.io.Serializable;

public class GenerateQrCode implements Serializable {
    private String id_qrcode;
    private String nama_kades;
    /*private String keperluan;
    private String keterangan;
    private String berlaku;*/

    public String getId_qrcode() {
        return id_qrcode;
    }

    public void setId_qrcode(String id_qrcode) {
        this.id_qrcode = id_qrcode;
    }

    public String getNama_kades() {
        return nama_kades;
    }

    public void setNama_kades(String nama_kades) {
        this.nama_kades = nama_kades;
    }

    /*public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getBerlaku() {
        return berlaku;
    }

    public void setBerlaku(String berlaku) {
        this.berlaku = berlaku;
    }*/
}
