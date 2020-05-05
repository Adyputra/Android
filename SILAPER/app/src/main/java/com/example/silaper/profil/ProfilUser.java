package com.example.silaper.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.example.silaper.pemesanan.DetailPesanan;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfilUser extends AppCompatActivity {

    TextView txnama, txemail;
    RelativeLayout rtedit, rtkeluar;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);

        progressDialog = new ProgressDialog(ProfilUser.this);
        txnama = findViewById(R.id.txnama);
        txemail = findViewById(R.id.txemail);
        rtedit = findViewById(R.id.rteditakun);

        getdata();

        rtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent = new Intent(ProfilUser.this, EditProfil.class);
                intent.putExtra("nama",txnama.getText().toString());
                intent.putExtra("email",txemail.getText().toString());
                startActivity(intent);
            }
        });
    }


    private void getdata()
    {

        progressDialog.setMessage("Mengambil data . . .");
        progressDialog.show();
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerApi.IPServer+"auth/profil?id="+ authdata.getInstance(getApplicationContext()).getIdcosturmer(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if(respon.getBoolean("status")){

                        JSONObject data = respon.getJSONObject("data");
                        txnama.setText(data.getString("nama"));
                        txemail.setText(data.getString("email"));

                    }else {
                        Toast.makeText(ProfilUser.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
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
}
