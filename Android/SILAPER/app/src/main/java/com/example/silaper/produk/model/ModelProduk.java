package com.example.silaper.produk.model;

public class ModelProduk {
    String kode;
    String nama;
    String ikon;

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    String harga;

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

    public ModelProduk() {
        this.kode = kode;
        this.nama = nama;
        this.ikon = ikon;
        this.harga = harga;
    }
}
