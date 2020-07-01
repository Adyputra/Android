package com.example.silaper;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class listundangan extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ModalUndangan> modelDataList;
    AdapterUndangan adapterUndangan;
    public static final String IPServer="http://192.168.100.10/Project/Bersama/api/ListUndangan";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listundangan);
        recyclerView = findViewById(R.id.recyclerundangan);

        loaddetail();
    }

    public void loaddetail()
    {
        StringRequest senddata = new StringRequest(Request.Method.GET, IPServer , new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    JSONObject obj  = new JSONObject(response);

                    modelDataList = new ArrayList<>();
                    JSONArray data = obj.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++)
                    {
                        ModalUndangan playerModel = new ModalUndangan();
                        JSONObject dataobj = data.getJSONObject(i);
                        playerModel.setNama(dataobj.getString("nama"));
                        playerModel.setDeskripsi(dataobj.getString("deskripsi"));
                        playerModel.setHarga(dataobj.getString("harga"));


                        modelDataList.add(playerModel);
                    }
                    setupListView();

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(senddata);
    }

    private void setupListView()
    {
        adapterUndangan = new AdapterUndangan(this, modelDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterUndangan);

    }
}
