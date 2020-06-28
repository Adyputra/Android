package com.example.silaper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //Deklarasi
    TextView Regis, LupaPasswordtx;
    EditText Email, Password;
    Button Login, Loginwithgoogle;
    RequestQueue requestQueue;
    String EmailHolder, PasswordHolder;
    ProgressDialog progressDialog;

    String HttpURL = "http://192.168.100.7/Android/user_login.php";
    boolean CheckEditText;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Deklarasi Id
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    Email = findViewById(R.id.editText_Email);
    Password = findViewById(R.id.editText_Password);
    Login = findViewById(R.id.button_login);
    Loginwithgoogle = findViewById(R.id.button_login_google);
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

    Loginwithgoogle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signOut();
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 123);

        }
    });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 123) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.w("hasil", "signInResult:status code=" + account.getDisplayName());
            if (account != null){
                UserLoginGoogle(account.getId(), account.getDisplayName(), account.getEmail());
            }else{

                Toast.makeText(LoginActivity.this, "Gagal Mengambil Data", Toast.LENGTH_LONG).show();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("hasil", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
        }
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    void UserLoginGoogle(final String idnya,final String namanya, final String email) {

        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerApi.IPServer + "auth/logingoogle",
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

                params.put("email", email);
                params.put("nama", namanya);
                params.put("idgoogle", idnya);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
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
