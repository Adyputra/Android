package com.example.silaper.pemesanan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.silaper.R;

public class DataProduk extends AppCompatActivity {

    Button btnpesan, btndetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_produk);

        btnpesan = findViewById(R.id.button_pesan);
        btndetail = findViewById(R.id.button_detail);
        btnpesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataProduk.this, FormPemesanan.class);
                intent.putExtra("id_kalender","2");
                startActivity(intent);
            }
        });
        btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataProduk.this, DetailPesanan.class);
                intent.putExtra("kode_penjualan","mC8kI4SDcgB8OhbI");
                startActivity(intent);
            }
        });
    }
}
