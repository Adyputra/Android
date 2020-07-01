package com.example.silaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterKalender extends RecyclerView.Adapter<AdapterKalender.HolderAdapterKalender> {
    private List<ModalKalender> mItems;
    private Context context;

    public AdapterKalender(Context context, List<ModalKalender> mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public HolderAdapterKalender onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listkalender, parent, false);
        HolderAdapterKalender holderAdapterKalender = new AdapterKalender.HolderAdapterKalender(layout);
        return holderAdapterKalender;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderAdapterKalender holder, int position) {
        ModalKalender me = mItems.get(position);
        holder.Nama.setText(me.getNama());
        holder.Deskripsi.setText(me.getDeskripsi());
        holder.Harga.setText(me.getHarga());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class HolderAdapterKalender extends RecyclerView.ViewHolder {
        TextView Nama, Deskripsi, Harga;

        public HolderAdapterKalender(@NonNull View itemView) {
            super(itemView);
            Nama = itemView.findViewById(R.id.Namakalender);
            Deskripsi = itemView.findViewById(R.id.Deskripsikalender);
            Harga = itemView.findViewById(R.id.hargakalender);
        }
    }
}
