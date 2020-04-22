package com.example.silaper;

public class ModelData {

    private String FID,kode_provinsi, provinsi, kasus_positif, kasus_meninggal, kasus_sembuh;

    public String getKode_provinsi(){

        return kode_provinsi;
    }
    public void setKode_provinsi(String kode_provinsi) {

        this.kode_provinsi = kode_provinsi;
    }
    public  String getProvinsi() {

        return provinsi;
    }
    public void setProvinsi(String provinsi) {

        this.provinsi = provinsi;
    }
    public String  getKasus_positif(){

        return kasus_positif;
    }
    public void setKasus_positif(String kasus_positif) {

        this.kasus_positif = kasus_positif;
    }
    public String getKasus_meninggal() {

        return kasus_meninggal;
    }
    public void setKasus_meninggal(String kasus_meninggal){
        this.kasus_meninggal = kasus_meninggal;
    }
    public String getKasus_sembuh() {

        return kasus_sembuh;
    }
    public void setKasus_sembuh(String kasus_sembuh) {

        this.kasus_sembuh = kasus_sembuh;
    }
    public String getFID() {

        return FID;
    }
    public void setFID(String FID) {

        this.FID = FID;
    }
}
