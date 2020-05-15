package com.example.silaper.configfile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

public class Util {

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static String settanggal(String tanggal){

        String waktunya;
        String[] splittanggal = tanggal.split("-");
        String[] bulan = {"bln","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        waktunya = splittanggal[2] +" "+bulan[Integer.parseInt(splittanggal[1])]+" "+splittanggal[0];
        return waktunya;
    }

    public static String setwaktu(String tanggal){
        String waktunya;
        String[] splitwaktu = tanggal.split(" ");
        String[] splittanggal =splitwaktu[0].split("-");
        String[] bulan = {"bln","Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        waktunya = splittanggal[2] +" "+bulan[Integer.parseInt(splittanggal[1])]+" "+splittanggal[0] +" "+ splitwaktu[1];
        return waktunya;
    }

    public static String setformatrupiah(String bil){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String waktunya;
        waktunya = formatRupiah.format((double) Integer.parseInt(bil));
        return "Rp "+waktunya.split("p")[1];
    }
}
