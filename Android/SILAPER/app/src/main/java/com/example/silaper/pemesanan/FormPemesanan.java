package com.example.silaper.pemesanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.silaper.configfile.authdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormPemesanan extends AppCompatActivity {

    String kodebank="", kodebarang="", kodeuser="";
    int hargabarang=0;
    ArrayList<String> databank=new ArrayList<String>();
    ArrayList<String> indexdatabank=new ArrayList<String>();
    EditText txalamat, txcatatan;
    TextView txqtybeli, txnama, txnamabarang, txharga, txbank, txtotalbeli;
    Button btnmin, btnplus, btnproses, btnpilihbank;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pemesanan);
        FormPemesanan.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new ProgressDialog(FormPemesanan.this);
        Intent data = getIntent();
        kodebarang = data.getStringExtra("id_kalender");
        kodeuser = authdata.getInstance(getApplicationContext()).getIdcosturmer();
        txalamat = findViewById(R.id.etxalamat);
        txcatatan = findViewById(R.id.etcatatan);
        txqtybeli = findViewById(R.id.txqtybeli);
        txnama = findViewById(R.id.txnamapemesan);
        txnamabarang = findViewById(R.id.txnamabarang);
        txtotalbeli = findViewById(R.id.txtotal);
        txharga = findViewById(R.id.txhargabarang);
        txbank = findViewById(R.id.txakunbank);
        btnmin = findViewById(R.id.btnminqty);
        btnplus = findViewById(R.id.btnplusqty);
        btnproses = findViewById(R.id.btnprosespesanan);
        btnpilihbank = findViewById(R.id.btnpilihbank);
        getdata();

        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmptotal = Integer.parseInt(txqtybeli.getText().toString()) + 1;
                txqtybeli.setText(String.valueOf(tmptotal));
                hitungtotal();
            }
        });

        btnmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmptotal = Integer.parseInt(txqtybeli.getText().toString()) - 1;
                if(tmptotal >= 1){
                    txqtybeli.setText(String.valueOf(tmptotal));
                    hitungtotal();

                }

            }
        });
        btnpilihbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(FormPemesanan.this);
                pictureDialog.setTitle("Pilih Akun Bank Admin");
                pictureDialog.setItems(databank.toArray(new String[0]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                kodebank = indexdatabank.get(which);
                                txbank.setText(databank.get(which));

                            }
                        });
                pictureDialog.show();
            }
        });

        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kodebank.equals("")){
                    Toast.makeText(FormPemesanan.this, "Pilih Pembayaran Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else if(txalamat.getText().toString().isEmpty()){
                    Toast.makeText(FormPemesanan.this, "Alamat Pengiriman Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else{
                    prosesdata();
                }
            }
        });
    }

    void prosesdata(){

        progressDialog.setMessage("Memproses Pemesanan Anda. .");
        progressDialog.show();
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerApi.IPServer+"pemesanan/buatpesanan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if(respon.getBoolean("status")){

                        Toast.makeText(FormPemesanan.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
                        String koderes = respon.getString("kode");
                        finish();
                        Intent intent = new Intent(FormPemesanan.this, DetailPesanan.class);
                        intent.putExtra("kode_penjualan",koderes);
                        startActivity(intent);
                    }else {
                        Toast.makeText(FormPemesanan.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FormPemesanan.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_customer", kodeuser);
                params.put("id_kalender", kodebarang);
                params.put("kode_akunbank", kodebank);
                params.put("qty", txqtybeli.getText().toString());
                params.put("alamat_kirim", txalamat.getText().toString());
                params.put("catatan_member", txcatatan.getText().toString());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(senddata);
    }
    private void getdata()
    {

        progressDialog.setMessage("Mengambil data . . .");
        progressDialog.show();
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerApi.IPServer+"pemesanan/dataform?id="+kodeuser+"&kodebarang="+kodebarang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if(respon.getBoolean("status")){

                        JSONObject data = res.getJSONObject("data");
                        txnama.setText(data.getString("namaakun"));
                        txnamabarang.setText(data.getString("nama_kalender"));
                        txharga.setText(Util.setformatrupiah(data.getString("harga")));
                        hargabarang = Integer.parseInt(data.getString("harga"));
                        txqtybeli.setText("1");
                        hitungtotal();
                        JSONArray arr = res.getJSONArray("databank");
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject datakom = arr.getJSONObject(i);
                                String ku = datakom.getString("nama_bank") ;
                                String koded = datakom.getString("kode_akunbank");
                                databank.add(ku);
                                indexdatabank.add(koded);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }

                    }else {
                        Toast.makeText(FormPemesanan.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
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

    void hitungtotal(){
        try {
            int tmptotal = Integer.parseInt(txqtybeli.getText().toString()) * hargabarang;
            txtotalbeli.setText(Util.setformatrupiah(String.valueOf(tmptotal)));
        }catch (Exception e){

        }
    }
}
