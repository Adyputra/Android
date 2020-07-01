package com.example.silaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.silaper.configfile.authdata;
import com.example.silaper.pemesanan.DataProduk;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // langsung pindah ke LoginActivity atau activity lain
        // begitu memasuki splash screen ini
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (authdata.getInstance(getApplicationContext()).ceklogin()) {

                    startActivity(new Intent(getApplicationContext(), BottNavigation.class));
//                    startActivity(new Intent(getApplicationContext(), DataProduk.class));
                    finish();

                }else{

                    startActivity(new Intent(getApplicationContext(), BottNavigation.class));
                    finish();
                }
    }
},1000L); //3000 L = 3 detik
    }
}