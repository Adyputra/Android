package com.example.silaper.produk.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.silaper.produk.adapter.AdapterKatalog;
import com.example.silaper.produk.adapter.AdapterProduk;
import com.example.silaper.produk.model.ModelKatalog;
import com.example.silaper.produk.model.ModelProduk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProduk extends AppCompatActivity {
    String kode="";
    RecyclerView.LayoutManager mManager;
    List<ModelProduk> mItems;
    RecyclerView tempatdatakatalog;
    AdapterProduk mAdapter;
    TextView txjudul;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_produk_from_katalog);

        Intent data = getIntent();
        kode = data.getStringExtra("putkode");
        Log.e("kode", kode);
        tempatdatakatalog = findViewById(R.id.tmpdata);
        txjudul = findViewById(R.id.txjudul);
        progressDialog = new ProgressDialog(DataProduk.this);
        mItems = new ArrayList<>();

        loaddata();
        mAdapter = new AdapterProduk(getApplicationContext(), mItems);
        mManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        tempatdatakatalog.setLayoutManager(mManager);
        tempatdatakatalog.setHasFixedSize(true);
        tempatdatakatalog.setAdapter(mAdapter);


    }

    void loaddata(){
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        StringRequest senddata = new StringRequest(Request.Method.GET, ServerApi.IPServer + "produk/detailkatalog/"+kode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if (respon.getBoolean("status")) {
                        JSONObject reskatalog = res.getJSONObject("katalog");
                        txjudul.setText("Data Produk Katalog "+reskatalog.getString("nama"));

                        JSONArray arr = res.getJSONArray("produk");
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject datakom = arr.getJSONObject(i);
                                ModelProduk md = new ModelProduk();
                                md.setNama(datakom.getString("nama_produk"));
                                md.setKode(datakom.getString("id_produk"));
                                md.setIkon(datakom.getString("image"));
                                md.setHarga(datakom.getString("harga"));
                                mItems.add(md);
                            } catch (Exception ea) {
                                Log.e("erronya atas", "" + ea);
                                ea.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), respon.getString("pesan"), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan " + error, Toast.LENGTH_SHORT).show();
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