package com.myproject.model.request;

public class RequestAddSurat {
    private String method;
    private String nik_penduduk;
    private String id_jenis_surat;
    private String keperluan;
    private String keterangan;
    private String berlaku;

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

    public String getId_jenis_surat() {
        return id_jenis_surat;
    }

    public void setId_jenis_surat(String id_jenis_surat) {
        this.id_jenis_surat = id_jenis_surat;
    }

    public String getKeperluan() {
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
    }
}
