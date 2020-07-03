package com.example.silaper.produk.model;

public class ModelKatalog {
    String kode, nama, ikon;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getIkon() {
        return ikon;
    }

    public void setIkon(String ikon) {
        this.ikon = ikon;
    }

    public ModelKatalog() {
        this.kode = kode;
        this.nama = nama;
        this.ikon = ikon;
    }
}
