package com.example.silaper.pemesanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.silaper.R;
import com.example.silaper.configfile.ServerApi;
import com.example.silaper.configfile.VolleyMultipartRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.aprilapps.easyphotopicker.EasyImage;

public class KonfirmasiBayar extends AppCompatActivity {
    private ImageView setImage;
    Bitmap matbib;
    private Button OpenImage , send;
    ProgressDialog pd;
    String kode;
    ProgressDialog progressDoalog;
    public static final int REQUEST_CODE_CAMERA = 001;
    public static final int REQUEST_CODE_GALLERY = 002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_bayar);
        requestMultiplePermissions();
//      Bundle bundle = getIntent().getExtras();
//      kode = bundle.getString("kode");
        Intent data = getIntent();
        kode = data.getStringExtra("kode");
        setImage = findViewById(R.id.showImg);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progress();
                send.setText("Mengirim Data Ke Server");
                send.setEnabled(false);
                kirimregistrasi();

            }
        });
        OpenImage = findViewById(R.id.open_image);
        OpenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestImage();
            }
        });
    }
    private void setRequestImage(){
        CharSequence[] item = {"Kamera", "Galeri"};
        AlertDialog.Builder request = new AlertDialog.Builder(this)
                .setTitle("Add Image")
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //Membuka Kamera Untuk Mengambil Gambar
                                EasyImage.openCamera(KonfirmasiBayar.this, REQUEST_CODE_CAMERA);
                                break;
                            case 1:
                                //Membuaka Galeri Untuk Mengambil Gambar
                                EasyImage.openGallery(KonfirmasiBayar.this, REQUEST_CODE_GALLERY);
                                break;
                        }
                    }
                });
        request.create();
        request.show();
    }
    //Method Ini Digunakan Untuk Menapatkan Hasil pada Activity, dari Proses Yang kita buat sebelumnya
    //Dan Mendapatkan Hasil File Photo dari Galeri atau Kamera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Error pada Image
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Image
                switch (type){
                    case REQUEST_CODE_CAMERA:
                        Picasso.get()
                                .load(imageFile)
                                .into(setImage);
                        matbib = BitmapFactory.decodeFile(imageFile.getPath());
                        break;

                    case REQUEST_CODE_GALLERY:
                        Picasso.get()
                                .load(imageFile)
                                .into(setImage);
                        matbib = BitmapFactory.decodeFile(imageFile.getPath());
                        break;
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Batalkan penanganan, Anda mungkin ingin menghapus foto yang diambil jika dibatalkan
            }
        });
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void kirimregistrasi() {
//        pd.setMessage("Memproses Registrasi...");
//        pd.setCancelable(false);
//       pd.show();

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ServerApi.IPServer + "pemesanan/konfirbayar/" + kode,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try{

                            Toast.makeText(KonfirmasiBayar.this, "Upload Bukti Berhasil ", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(KonfirmasiBayar.this, DetailPesanan.class);
                            in.putExtra("kode_penjualan", kode);
                            startActivity(in);


                        } catch (Exception e) {
                            Toast.makeText(KonfirmasiBayar.this, "Upload Bukti Gagal ", Toast.LENGTH_SHORT).show();
                            Log.e("volsatu", "errornya : " + e.getMessage());
                            e.printStackTrace();
                        }
                        //   pd.cancel();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("voldua", "errornya : " + error.getMessage());
                        Toast.makeText(KonfirmasiBayar.this, "Upload Bukti Berhasil ", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(KonfirmasiBayar.this, DetailPesanan.class);
                        in.putExtra("kode_penjualan", kode);
                        startActivity(in);
                        //    pd.cancel();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("foto_bukti", new DataPart(imagename + ".png", getFileDataFromDrawable(matbib)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public void onBackPressed() {
        Intent dompet = new Intent(KonfirmasiBayar.this , DetailPesanan.class);
        dompet.putExtra("kode_penjualan", kode);
        startActivity(dompet);
        super.onBackPressed();
    }
}
