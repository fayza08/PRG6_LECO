package id.ac.polman.astra.nim0320190008.leco.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Resep implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("id_user")
    @Expose
    private Integer id_user;


    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("alat_bahan")
    @Expose
    private String alat_bahan;

    @SerializedName("tahap")
    @Expose
    private String tahap;

    @SerializedName("nilai")
    @Expose
    private Integer nilai;

    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    @SerializedName("foto")
    @Expose
    private String foto;

    @SerializedName("status")
    @Expose
    private Integer status;

    public Resep() {
    }

    public Resep(Integer id, Integer id_user, String nama, String alat_bahan, String tahap, Integer nilai, String keterangan, String foto, Integer status) {
        this.id = id;
        this.id_user = id_user;
        this.nama = nama;
        this.alat_bahan = alat_bahan;
        this.tahap = tahap;
        this.nilai = nilai;
        this.keterangan = keterangan;
        this.foto = foto;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlat_bahan() {
        return alat_bahan;
    }

    public void setAlat_bahan(String alat_bahan) {
        this.alat_bahan = alat_bahan;
    }

    public String getTahap() {
        return tahap;
    }

    public void setTahap(String tahap) {
        this.tahap = tahap;
    }

    public Integer getNilai() {
        return nilai;
    }

    public void setNilai(Integer nilai) {
        this.nilai = nilai;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
