package com.example.silaper.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

    String daftar_via="1";
    TextView txnama, txemail, txtgantipass;
    RelativeLayout rtedit, rtkeluar, rteditpass, rtopenwa;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);

        progressDialog = new ProgressDialog(ProfilUser.this);
        txnama = findViewById(R.id.txnama);
        txemail = findViewById(R.id.txemail);
        txtgantipass = findViewById(R.id.txt_ganti_pass);
        rtedit = findViewById(R.id.rteditakun);
        rteditpass = findViewById(R.id.rtgantipassword);
        rtopenwa = findViewById(R.id.rtopenwa);

        getdata();

        if(daftar_via.equals("2")){
            txtgantipass.setText("Ganti Password (Disable)");
        }

        rteditpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(daftar_via.equals("2")){
                    Toast.makeText(ProfilUser.this, "Anda Tidak Dapat Ganti Password Karena Akun Anda Mendaftar Via Akun Google", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(ProfilUser.this, GantiPassword.class));
                }
            }
        });
        rtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent = new Intent(ProfilUser.this, EditProfil.class);
                intent.putExtra("nama",txnama.getText().toString());
                intent.putExtra("email",txemail.getText().toString());
                intent.putExtra("daftar_via",daftar_via);
                startActivity(intent);
            }
        });
        rtopenwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = "+6289696491481", pesan ="Hello Admin, Saya "+ txnama.getText().toString()+" dari aplikasi SILAPER";
                String url = "https://api.whatsapp.com/send?phone=" + contact +"&text="+pesan;
                try {
                    PackageManager pm = getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(ProfilUser.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                }
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
                        daftar_via = data.getString("daftar_via");
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
