package com.example.silaper.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.silaper.R;
import com.example.silaper.configfile.AppController;
import com.example.silaper.configfile.ServerApi;
import com.example.silaper.configfile.authdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GantiPassword extends AppCompatActivity {

    EditText edpasslama, edpassbaru, edpasskonfir;
    ProgressDialog progressDialog;
    Button btnproses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        GantiPassword.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new ProgressDialog(GantiPassword.this);

        edpasslama = findViewById(R.id.edt_pass_lama);
        edpassbaru = findViewById(R.id.edt_pass_baru);
        edpasskonfir = findViewById(R.id.edt_repass_baru);
        btnproses = findViewById(R.id.buttonSimpanPassword);

        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edpasslama.getText().toString().isEmpty()){
                    Toast.makeText(GantiPassword.this, "Password Lama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if(edpassbaru.getText().toString().isEmpty()){
                    Toast.makeText(GantiPassword.this, "Password Baru Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if(edpasskonfir.getText().toString().isEmpty()){
                    Toast.makeText(GantiPassword.this, "Konfirmasi Password Baru Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if(!edpasskonfir.getText().toString().equals(edpassbaru.getText().toString())){
                    Toast.makeText(GantiPassword.this, "Konfirmasi Password Salah", Toast.LENGTH_SHORT).show();
                }else{
                    prosesdata();
                }
            }
        });
    }

    void prosesdata(){

        progressDialog.setMessage("Memproses Data. .");
        progressDialog.show();
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerApi.IPServer+"auth/gantipassword/"+ authdata.getInstance(getApplicationContext()).getIdcosturmer(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if(respon.getBoolean("status")){

                        Toast.makeText(GantiPassword.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();

                        edpassbaru.setText("");
                        edpasslama.setText("");
                        edpasskonfir.setText("");
                    }else {
                        Toast.makeText(GantiPassword.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GantiPassword.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("passlama", edpasslama.getText().toString());
                params.put("passbaru", edpassbaru.getText().toString());
                params.put("passkonfir", edpasskonfir.getText().toString());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(senddata);
    }
}
