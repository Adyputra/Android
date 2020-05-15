package com.example.silaper.profil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LupaPassword extends AppCompatActivity {

    ProgressDialog pd;

    EditText txtkode, txtemail, txtpassword, txtconfirmpass;
    TextView txtulang, txtcekkode;
    Button btnproses;
    CountDownTimer cTimer = null;
    boolean ulang = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        LupaPassword.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        txtulang = findViewById(R.id.txt_kirim_kode);
        txtcekkode = findViewById(R.id.txt_cek_kode);
        btnproses = findViewById(R.id.btn_lanjutkanregister);
        txtkode = findViewById(R.id.txt_kode_reset);
        txtemail = findViewById(R.id.txt_email);
        txtpassword = findViewById(R.id.txt_password);
        txtconfirmpass = findViewById(R.id.txt_confirm_password);
        pd = new ProgressDialog(LupaPassword.this);

        txtulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtemail.getText().toString().isEmpty()) {
                    Toast.makeText(LupaPassword.this, "Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                    txtemail.requestFocus();
                    pd.dismiss();

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtemail.getText().toString()).matches()) {

                    Toast.makeText(LupaPassword.this, "Email Tidak Valid", Toast.LENGTH_LONG).show();
                    txtemail.requestFocus();
                    pd.dismiss();
                } else if(ulang){
                    pd.setMessage("Mengirim Kode...");
                    pd.setCancelable(false);
                    pd.show();
                    StringRequest senddata = new StringRequest(Request.Method.POST, ServerApi.IPServer + "auth/reqotp", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                pd.cancel();
                            try {
                                JSONObject res = new JSONObject(response);

                                JSONObject respon = res.getJSONObject("respon");
                                if(respon.getBoolean("status")){
                                    Toast.makeText(LupaPassword.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
                                    startTimer();
                                    txtkode.requestFocus();
                                }else{
                                    Toast.makeText(LupaPassword.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();

                                }

                                pd.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.cancel();

                            Log.e("errornyaa ", "" + error);
                            Toast.makeText(LupaPassword.this, "Terjadi Kesalahan, "+error, Toast.LENGTH_SHORT).show();


                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", txtemail.getText().toString());

                            return params;
                        }
                    };

                    AppController.getInstance().addToRequestQueue(senddata);
                }
            }
        });

        txtcekkode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtkode.getText().toString().isEmpty()) {
                    Toast.makeText(LupaPassword.this, "Kode Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                    txtkode.requestFocus();
                    pd.dismiss();

                } else{
                    pd.setMessage("Mengecek Kode...");
                    pd.setCancelable(false);
                    pd.show();
                    StringRequest senddata = new StringRequest(Request.Method.POST, ServerApi.IPServer + "auth/cekkode", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                pd.cancel();
                            try {
                                JSONObject res = new JSONObject(response);

                                JSONObject respon = res.getJSONObject("respon");
                                if(respon.getBoolean("status")){
                                    Toast.makeText(LupaPassword.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();

                                    txtpassword.setEnabled(true);
                                    txtconfirmpass.setEnabled(true);
                                }else{
                                    Toast.makeText(LupaPassword.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
                                    txtkode.requestFocus();
                                    txtpassword.setEnabled(false);
                                    txtconfirmpass.setEnabled(false);

                                }

                                pd.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.cancel();

                            Log.e("errornyaa ", "" + error);
                            Toast.makeText(LupaPassword.this, "Terjadi Kesalahan, "+error, Toast.LENGTH_SHORT).show();


                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("kode", txtkode.getText().toString());
                            params.put("email", txtemail.getText().toString());

                            return params;
                        }
                    };

                    AppController.getInstance().addToRequestQueue(senddata);
                }
            }
        });

        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtemail.getText().toString().isEmpty()) {
                    Toast.makeText(LupaPassword.this, "Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                    txtemail.requestFocus();
                    pd.dismiss();

                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtemail.getText().toString()).matches()) {

                    Toast.makeText(LupaPassword.this, "Email Tidak Valid", Toast.LENGTH_LONG).show();
                    txtemail.requestFocus();
                    pd.dismiss();
                }else if (txtkode.getText().toString().isEmpty()) {
                    Toast.makeText(LupaPassword.this, "Kode Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                    txtkode.requestFocus();
                    pd.dismiss();

                } else if (txtpassword.getText().toString().isEmpty()) {
                    Toast.makeText(LupaPassword.this, "Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                    pd.dismiss();

                }else if (txtconfirmpass.getText().toString().isEmpty()) {
                    Toast.makeText(LupaPassword.this, "Konfirmasi Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                    pd.dismiss();

                }else if (!txtconfirmpass.getText().toString().equals(txtpassword.getText().toString())) {
                    Toast.makeText(LupaPassword.this, "Password Konfirmasi Salah", Toast.LENGTH_LONG).show();
                    pd.dismiss();

                } else{
                    pd.setMessage("Memproses ...");
                    pd.setCancelable(false);
                    pd.show();
                    StringRequest senddata = new StringRequest(Request.Method.POST, ServerApi.IPServer + "auth/prosesgantipassword", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                pd.cancel();
                            try {
                                JSONObject res = new JSONObject(response);

                                JSONObject respon = res.getJSONObject("respon");
                                if(respon.getBoolean("status")){
                                    Toast.makeText(LupaPassword.this, respon.getString("pesan"), Toast.LENGTH_LONG).show();

                                    onBackPressed();
                                }else{
                                    Toast.makeText(LupaPassword.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();

                                }

                                pd.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.cancel();

                            Log.e("errornyaa ", "" + error);
                            Toast.makeText(LupaPassword.this, "Terjadi Kesalahan, "+error, Toast.LENGTH_SHORT).show();


                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", txtemail.getText().toString());
                            params.put("kode", txtkode.getText().toString());
                            params.put("pass", txtpassword.getText().toString());
                            params.put("repass", txtconfirmpass.getText().toString());

                            return params;
                        }
                    };

                    AppController.getInstance().addToRequestQueue(senddata);
                }
            }
        });
    }
    void startTimer() {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtulang.setText("(" + millisUntilFinished / 1000+"s)");
                ulang = false;
            }
            public void onFinish() {
                txtulang.setText("Kirim Kode");
                ulang = true;
            }
        };
        cTimer.start();
    }
}
