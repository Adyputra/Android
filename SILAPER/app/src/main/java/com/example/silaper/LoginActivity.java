package com.example.silaper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.silaper.configfile.ServerApi;
import com.example.silaper.configfile.authdata;
import com.example.silaper.pemesanan.DataProduk;
import com.example.silaper.profil.LupaPassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //Deklarasi
    TextView Regis, LupaPasswordtx;
    EditText Email, Password;
    Button Login;
    RequestQueue requestQueue;
    String EmailHolder, PasswordHolder;
    ProgressDialog progressDialog;
    
    String HttpURL = "http://192.168.43.153/Android/user_login.php";
    boolean CheckEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Deklarasi Id

    Email = findViewById(R.id.editText_Email);
    Password = findViewById(R.id.editText_Password);
    Login = findViewById(R.id.button_login);
    requestQueue = Volley.newRequestQueue(LoginActivity.this);
    progressDialog = new ProgressDialog(LoginActivity.this);
    Regis = findViewById(R.id.Register);
        LupaPasswordtx = findViewById(R.id.lupapassword);

        LupaPasswordtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LupaPassword.class));
            }
        });
    //Membuat Show Password

    //login button
    Login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckEditTextIsEmptyOrNot();
            if (CheckEditText){
                UserLogin();
            }
            else {
                Toast.makeText(LoginActivity.this,"Salah satu Field Belum di isi",Toast.LENGTH_LONG).show();
            }
        }
    });
    //Regis button
    Regis.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), RegisterActivity.class));
        }
    });
    }
    public void UserLogin() {

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.IPServer + "auth/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        progressDialog.dismiss();

                        try {
                            JSONObject res = new JSONObject(ServerResponse);
                            JSONObject respon = res.getJSONObject("respon");
                            if (respon.getBoolean("status")) {

                                Toast.makeText(LoginActivity.this, respon.getString("pesan"), Toast.LENGTH_LONG).show();

                                JSONObject data = respon.getJSONObject("data");
                                authdata.getInstance(getApplicationContext()).setIdcosturmer(data.getString("id"));
                                finish();

                                Intent intent = new Intent(LoginActivity.this, DataProduk.class);
                                intent.putExtra("UserEmailTAG", EmailHolder);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, respon.getString("pesan"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", EmailHolder);
                params.put("password", PasswordHolder);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    public void CheckEditTextIsEmptyOrNot() {
        EmailHolder = Email.getText().toString().trim();
        PasswordHolder = Password.getText().toString().trim();

        if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {
            CheckEditText = false;

        } else {
            CheckEditText = true;
        }
    }
}
