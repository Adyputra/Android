package com.example.silaper.pemesanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.silaper.R;
import com.example.silaper.configfile.AppController;
import com.example.silaper.configfile.ServerApi;
import com.example.silaper.configfile.Util;
import com.example.silaper.configfile.authdata;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailPesanan extends AppCompatActivity {

    String kodejual="", statuspeesanan="";
    TextView txqtybeli, txnama, txnamabarang, txharga, txbank, txtotalbeli, txalamat, txcatatan, txstatus, txcatatanadmin, txnohp;
    Button btnkonfir, btnbatalkan;
    ProgressDialog progressDialog;
    LinearLayout lnpanel, lngaris;
    ImageView imgbukti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);
        progressDialog = new ProgressDialog(DetailPesanan.this);
        Intent data = getIntent();
        kodejual = data.getStringExtra("kode_penjualan");
        txalamat = findViewById(R.id.txalamatview);
        txcatatan = findViewById(R.id.txcatatanview);
        txqtybeli = findViewById(R.id.txqtybeli);
        txnama = findViewById(R.id.txnamapemesan);
        txnamabarang = findViewById(R.id.txnamabarang);
        txtotalbeli = findViewById(R.id.txtotal);
        txharga = findViewById(R.id.txhargabarang);
        txbank = findViewById(R.id.txdatabank);
        txstatus = findViewById(R.id.txtstatus);
        txcatatanadmin = findViewById(R.id.txcatatanadmin);
        btnbatalkan = findViewById(R.id.btnbatalkan);
        btnkonfir = findViewById(R.id.btnkonfirbayar);
        lnpanel = findViewById(R.id.panelcatatanadmin);
        lngaris = findViewById(R.id.garispanelcatatanadmin);
        imgbukti = findViewById(R.id.imgbukti);
        txnohp = findViewById(R.id.txnohpview);
        getdata();

        btnbatalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailPesanan.this)
                        .setIcon(R.drawable.icapps)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda Yakin Ingin Membatalkan Pesanan Ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prosesbatalkan();
                            }

                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

        btnkonfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailPesanan.this, KonfirmasiBayar.class);
                intent.putExtra("kode",kodejual);
                startActivity(intent);
            }
        });
    }

    private void prosesbatalkan()
    {

        progressDialog.setMessage("Proses Membatalkan Pesanan");
        progressDialog.show();
        StringRequest senddata = new StringRequest(Request.Method.DELETE, ServerApi.IPServer+"pemesanan/batalkan/"+kodejual, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if(respon.getBoolean("status")){
                        Toast.makeText(DetailPesanan.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();

                        getdata();
                    }else {
                        Toast.makeText(DetailPesanan.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                }) {

        };

        AppController.getInstance().addToRequestQueue(senddata);
    }

    private void getdata()
    {

        progressDialog.setMessage("Mengambil data . . .");
        progressDialog.show();
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerApi.IPServer+"pemesanan/detailpesanan/"+kodejual, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if(respon.getBoolean("status")){

                        JSONObject data = res.getJSONObject("data");
                        txnama.setText(data.getString("nama"));
                        txnamabarang.setText(data.getString("nama_kalender"));
                        txharga.setText(Util.setformatrupiah(data.getString("harga")));
                        txqtybeli.setText(data.getString("qty"));
                        int tmptotal = Integer.parseInt(data.getString("qty")) * Integer.parseInt(data.getString("harga"));
                        txtotalbeli.setText(Util.setformatrupiah(String.valueOf(tmptotal)));
                        txalamat.setText(data.getString("alamat_kirim"));
                        txcatatan.setText(data.getString("catatan_member"));
                        txcatatanadmin.setText(data.getString("catatan_status"));
                        txnohp.setText(data.getString("no_hp"));
                        txbank.setText(data.getString("nama_bank") + " ("+data.getString("no_rek")+") a/n "+data.getString("nama_rek"));

                        String[] sta = {"Di Batalkan","Menunggu Pembayaran","Menunggu Konfirmasi Admin","Di Proses","Di Tolak","Selesai"};
                        statuspeesanan = data.getString("status");
                        txstatus.setText(sta[Integer.parseInt(data.getString("status"))]);
                        Log.e("linkfoto", ServerApi.IP + data.getString("bukti_tf"));
//                        if(!data.getString("bukti_tf").equals("")){

                            Picasso.get()
                                    .load(ServerApi.IP + data.getString("bukti_tf"))
                                    .into(imgbukti);
//                        }
                        setpanelbystatus();
                    }else {
                        Toast.makeText(DetailPesanan.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
                    }


//                    Log.e("resss",""+ datakavling);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                }) {

        };

        AppController.getInstance().addToRequestQueue(senddata);
    }

    void setpanelbystatus(){
        lnpanel.setVisibility(View.GONE);
        lngaris.setVisibility(View.GONE);
        if(statuspeesanan.equals("0") || statuspeesanan.equals("3") || statuspeesanan.equals("5")){
            btnkonfir.setVisibility(View.GONE);
            btnbatalkan.setVisibility(View.GONE);
        }else if(statuspeesanan.equals("1")){
            btnkonfir.setVisibility(View.VISIBLE);
            btnbatalkan.setVisibility(View.VISIBLE);
        }else if(statuspeesanan.equals("2")){
            btnkonfir.setVisibility(View.GONE);
            btnbatalkan.setVisibility(View.VISIBLE);
        }else if(statuspeesanan.equals("4")){
            btnkonfir.setVisibility(View.VISIBLE);
            btnbatalkan.setVisibility(View.VISIBLE);
            lnpanel.setVisibility(View.VISIBLE);
            lngaris.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DetailPesanan.this.finish();
        startActivity(new Intent(DetailPesanan.this, DataProduk.class));
    }

}
