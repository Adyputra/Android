package com.example.silaper.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class EditProfil extends AppCompatActivity {

    String nama, email;
    Button btnsave;
    EditText txnama, txemail;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        Intent data = getIntent();
        progressDialog = new ProgressDialog(EditProfil.this);
        nama = data.getStringExtra("nama");
        email = data.getStringExtra("email");

        btnsave = findViewById(R.id.buttonSimpan);
        txnama = findViewById(R.id.edt_nama);
        txemail = findViewById(R.id.edt_email);

        txnama.setText(nama);
        txemail.setText(email);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txnama.getText().toString().isEmpty()){
                    Toast.makeText(EditProfil.this, "Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else if(txemail.getText().toString().isEmpty()){
                    Toast.makeText(EditProfil.this, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txemail.getText().toString()).matches()) {
                    Toast.makeText(EditProfil.this, "Email Tidak Valid", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else{
                    prosesdata();
                }
            }
        });
    }


//    @Override
//    public void onBackPressed() {
//        finish();
//        Intent intent = new Intent(EditProfil.this, ProfilUser.class);
//        startActivity(intent);
//    }
    void prosesdata(){

        progressDialog.setMessage("Memproses Data. .");
        progressDialog.show();
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerApi.IPServer+"auth/editprofil/"+ authdata.getInstance(getApplicationContext()).getIdcosturmer(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if(respon.getBoolean("status")){

                        Toast.makeText(EditProfil.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
//                        onBackPressed();
                        finish();
                        Intent intent = new Intent(EditProfil.this, ProfilUser.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(EditProfil.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditProfil.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", txemail.getText().toString());
                params.put("nama", txnama.getText().toString());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(senddata);
    }
}
