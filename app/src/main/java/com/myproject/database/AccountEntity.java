package com.myproject.database;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "account")
public class AccountEntity extends BaseEntity{
    public static final String NIK_PENDUDUK = "nik_penduduk";
    public static final String NAMA_PENDUDUK = "nama_penduduk";
    public static final String ALAMAT_PENDUDUK = "alamat_penduduk";
    public static final String TEMPAT_LAHIR = "tempat_lahir";
    public static final String TANGGAL_LAHIR = "tanggal_lahir";
    public static final String STATUS = "status";
    public static final String PEKERJAAN = "pekerjaan";

    public static final String PENDIDIKAN = "pendidikan";
    public static final String JK = "jenis_kelamin";
    public static final String KEWARGANEGARAAN = "kewarganegaraan";
    public static final String ID_USER = "id_user";

    @DatabaseField(columnName = NIK_PENDUDUK)
    private String nik;
    @DatabaseField(columnName = NAMA_PENDUDUK)
    private String nama;
    @DatabaseField(columnName = ALAMAT_PENDUDUK)
    private String alamat;
    @DatabaseField(columnName = TEMPAT_LAHIR)
    private String tempat;
    @DatabaseField(columnName = TANGGAL_LAHIR)
    private String tanggal;
    @DatabaseField(columnName = STATUS)
    private String status;
    @DatabaseField(columnName = PEKERJAAN)
    private String pekerjaan;
    @DatabaseField(columnName = PENDIDIKAN)
    private String pendidikan;
    @DatabaseField(columnName = JK)
    private String jk;
    @DatabaseField(columnName = KEWARGANEGARAAN)
    private String kewarganegaraan;
    @DatabaseField(columnName = ID_USER)
    private String id_user;

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getKewarganegaraan() {
        return kewarganegaraan;
    }

    public void setKewarganegaraan(String kewarganegaraan) {
        this.kewarganegaraan = kewarganegaraan;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
