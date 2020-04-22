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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Deklarasi
    TextView Regis;
    EditText Email, Password;
    Button Login;
    RequestQueue requestQueue;
    String EmailHolder, PasswordHolder;
    ProgressDialog progressDialog;
    String HttpURL = "http://192.168.43.153/Project/Android/user_login.php";
    boolean CheckEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Deklarasi Id

    Email = findViewById(R.id.editText_Email);
    Password = findViewById(R.id.editText_Password);
    Login = findViewById(R.id.button_login);
    requestQueue = Volley.newRequestQueue(MainActivity.this);
    progressDialog = new ProgressDialog(MainActivity.this);
    Regis = findViewById(R.id.Register);

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
                Toast.makeText(MainActivity.this,"Salah satu Field Belum di isi",Toast.LENGTH_LONG).show();
            }
        }
    });
    //Regis button
    Regis.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(),Main2Activity.class));
        }
    });
    }
    public void UserLogin() {

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        progressDialog.dismiss();

                        if (ServerResponse.equalsIgnoreCase("Data Anda benar")) {

                            Toast.makeText(MainActivity.this, "Selamat datang di Data Covid-19", Toast.LENGTH_LONG).show();

                            finish();

                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            intent.putExtra("UserEmailTAG", EmailHolder);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("User_Email", EmailHolder);
                params.put("User_Password", PasswordHolder);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
