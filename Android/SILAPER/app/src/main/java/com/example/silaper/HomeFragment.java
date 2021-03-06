package com.example.silaper;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.silaper.configfile.AppController;
import com.example.silaper.configfile.ServerApi;
import com.example.silaper.produk.adapter.AdapterKatalog;
import com.example.silaper.produk.model.ModelKatalog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    RecyclerView.LayoutManager mManager;
    List<ModelKatalog> mItems;
    RecyclerView tempatdatakatalog;
    AdapterKatalog mAdapter;

    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        tempatdatakatalog = v.findViewById(R.id.tmpdata);
        progressDialog = new ProgressDialog(getContext());
        mItems = new ArrayList<>();

        loaddata();
        mAdapter = new AdapterKatalog(getContext(), mItems);
        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        tempatdatakatalog.setLayoutManager(mManager);
        tempatdatakatalog.setHasFixedSize(true);
        tempatdatakatalog.setAdapter(mAdapter);



        return v;
    }


    void loaddata(){
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        StringRequest senddata = new StringRequest(Request.Method.GET, ServerApi.IPServer + "produk/datakatalog", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if (respon.getBoolean("status")) {


                        JSONArray arr = res.getJSONArray("data");
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject datakom = arr.getJSONObject(i);
                                ModelKatalog md = new ModelKatalog();
                                md.setNama(datakom.getString("nama"));
                                md.setKode(datakom.getString("id_katalog"));
                                md.setIkon(datakom.getString("ikon"));
                                mItems.add(md);
                            } catch (Exception ea) {
                                Log.e("erronya atas", "" + ea);
                                ea.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), respon.getString("pesan"), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e("errorgan", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("errornyaa ", "" + error);
                Toast.makeText(getContext(), "Terjadi Kesalahan " + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(senddata);
    }
}
