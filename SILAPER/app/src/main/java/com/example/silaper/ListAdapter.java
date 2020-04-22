package com.example.silaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.HolderData> {
    private Context context;
    private ArrayList<ModelData> dataModelArrayList;

    public ListAdapter(Context context, ArrayList<ModelData> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        HolderData holderData = new HolderData((layout));
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        ModelData md = dataModelArrayList.get(position);
        try {
            holder.lokasi.setText(md.getProvinsi());
            holder.positif.setText(md.getKasus_positif());
            holder.meninggal.setText(md.getKasus_meninggal());
            holder.sembuh.setText(md.getKasus_sembuh());
            holder.kodeprov = md.getKode_provinsi();
            holder.FID = md.getFID();
        } catch (Exception ea) {
            ea.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView lokasi, positif, meninggal, sembuh;
        String kodeprov, FID;

        @SuppressLint("CutPasteId")
        public HolderData(View view) {
            super(view);
            lokasi = view.findViewById(R.id.lokasi);
            positif = view.findViewById(R.id.positif);
            meninggal = view.findViewById(R.id.meninggal);
            sembuh = view.findViewById(R.id.sembuh);
        }
    }
}