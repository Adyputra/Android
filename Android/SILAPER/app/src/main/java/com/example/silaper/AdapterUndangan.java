package com.example.silaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterUndangan extends RecyclerView.Adapter<AdapterUndangan.HolderAdapterUndangan> {
    private List<ModalUndangan> mItems;
    private Context context;

    public AdapterUndangan(Context context, List<ModalUndangan> mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @NonNull
    @Override
    public HolderAdapterUndangan onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listundangan, parent, false);
        HolderAdapterUndangan holderAdapterUndangan = new AdapterUndangan.HolderAdapterUndangan(layout);
        return holderAdapterUndangan;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderAdapterUndangan holder, int position) {
        ModalUndangan me = mItems.get(position);
        holder.Nama.setText(me.getNama());
        holder.Deskripsi.setText(me.getDeskripsi());
        holder.Harga.setText(me.getHarga());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class HolderAdapterUndangan extends RecyclerView.ViewHolder {
        TextView Nama, Deskripsi, Harga;

        public HolderAdapterUndangan(@NonNull View itemView) {
            super(itemView);
            Nama = itemView.findViewById(R.id.Namaundangan);
            Deskripsi = itemView.findViewById(R.id.Deskripsiundangan);
            Harga = itemView.findViewById(R.id.Hargaundangan);
        }
    }
}
