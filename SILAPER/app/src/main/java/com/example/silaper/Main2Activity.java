package com.example.silaper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {


    EditText FullName, Email, Password, Repass ;
    TextView daftar;
    Button Register;
    RequestQueue requestQueue;
    String NameHolder, EmailHolder, PasswordHolder, RepassHolder;
    ProgressDialog progressDialog;
    String HttpUrl = "http://192.168.43.153/Project/Android/User-Registration.php";
    Boolean CheckEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FullName = findViewById(R.id.EditTextFullName);
        Email =  findViewById(R.id.editText_Email);
        Password = findViewById(R.id.editText_Password);
        Repass =  findViewById(R.id.editText_RePassword);
        Register = findViewById(R.id.button_signup);
        requestQueue = Volley.newRequestQueue(Main2Activity.this);
        progressDialog = new ProgressDialog(Main2Activity.this);
        daftar = findViewById(R.id.textViewL);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),MainActivity.class));
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();
                if (CheckEditText){
                    UserRegistration();
                    finish();
                }
                else{
                    Toast.makeText(Main2Activity.this, "salah satu field belum diisi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void UserRegistration(){
        progressDialog.setMessage("Mohon tunggu, kami sedang memasukkan data kamu ke server");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String ServerResponse) {
                progressDialog.dismiss();
                Toast.makeText(Main2Activity.this, ServerResponse, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(Main2Activity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("User_Email", EmailHolder);
                params.put("User_Password", PasswordHolder);
                params.put("User_RePassword", RepassHolder);
                params.put("User_Full_Name", NameHolder);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        requestQueue.add(stringRequest);
    }

    public void CheckEditTextIsEmptyOrNot(){
        NameHolder = FullName.getText().toString().trim();
        EmailHolder = Email.getText().toString().trim();
        PasswordHolder = Password.getText().toString().trim();
        RepassHolder = Repass.getText().toString().trim();
        String repass = Repass.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder) || TextUtils.isEmpty(RepassHolder)){
            if (!cekPassword(password, repass)){
                Toast.makeText(Main2Activity.this,"Password tidak sama", Toast.LENGTH_LONG).show();
            }
            else {CheckEditText = false;}

        }
        else {
            CheckEditText = true;
        }
    }
    private boolean cekPassword(String password, String repassword){
        return password.equals(repassword);
    }

}



