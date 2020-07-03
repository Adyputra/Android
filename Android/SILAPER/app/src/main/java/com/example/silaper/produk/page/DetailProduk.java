package com.example.silaper.produk.page;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.silaper.R;
import com.example.silaper.configfile.AppController;
import com.example.silaper.configfile.ServerApi;
import com.example.silaper.configfile.Util;
import com.example.silaper.pemesanan.FormPemesanan;
import com.example.silaper.produk.model.ModelProduk;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailProduk extends AppCompatActivity {
    String kode ="";
    TextView txnama, txharga, txdeskripsi, txnamakatalog;
    ImageView imgcover;
    Button btngopesan;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        txnama = findViewById(R.id.namaproduk);
        txharga = findViewById(R.id.txharga);
        txdeskripsi = findViewById(R.id.txdeskripsi);
        imgcover = findViewById(R.id.imgcover);
        btngopesan = findViewById(R.id.btnpesan);

        progressDialog = new ProgressDialog(DetailProduk.this);
        Intent data = getIntent();
        kode = data.getStringExtra("putkode");
        Log.e("kode", kode);

        loaddata();

        btngopesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProduk.this, FormPemesanan.class);
                intent.putExtra("id_produk", kode);
                startActivity(intent);
            }
        });
    }


    void loaddata(){
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        StringRequest senddata = new StringRequest(Request.Method.GET, ServerApi.IPServer + "produk/detailproduk/"+kode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if (respon.getBoolean("status")) {
                        JSONObject datanya = res.getJSONObject("data");
                        txnama.setText(datanya.getString("nama_produk"));
                        txharga.setText(Util.setformatrupiah(datanya.getString("harga")));
                        txdeskripsi.setText(datanya.getString("deskripsi"));
                        Picasso.get()
                                .load(ServerApi.IP + datanya.getString("image"))
                                .into(imgcover);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(DetailProduk.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e("errorgan", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("errornyaa ", "" + error);
                Toast.makeText(DetailProduk.this, "Terjadi Kesalahan " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(senddata);
    }
}