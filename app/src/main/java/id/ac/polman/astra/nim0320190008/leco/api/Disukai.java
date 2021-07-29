package id.ac.polman.astra.nim0320190008.leco.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Disukai {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("id_user")
    @Expose
    private Integer id_user;

    @SerializedName("id_resep")
    @Expose
    private Integer id_resep;

    @SerializedName("tanggal")
    @Expose
    private Integer tanggal;

    public Disukai() {
    }

    public Disukai(Integer id, Integer id_user, Integer id_resep, Integer tanggal) {
        this.id = id;
        this.id_user = id_user;
        this.id_resep = id_resep;
        this.tanggal = tanggal;
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

    public Integer getId_resep() {
        return id_resep;
    }

    public void setId_resep(Integer id_resep) {
        this.id_resep = id_resep;
    }

    public Integer getTanggal() {
        return tanggal;
    }

    public void setTanggal(Integer tanggal) {
        this.tanggal = tanggal;
    }
}
