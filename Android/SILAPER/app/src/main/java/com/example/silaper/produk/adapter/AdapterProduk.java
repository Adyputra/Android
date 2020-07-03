package com.example.silaper.produk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silaper.R;
import com.example.silaper.configfile.ServerApi;
import com.example.silaper.configfile.Util;
import com.example.silaper.produk.model.ModelKatalog;
import com.example.silaper.produk.model.ModelProduk;
import com.example.silaper.produk.page.DetailProduk;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterProduk extends RecyclerView.Adapter<AdapterProduk.HolderData> {
    private List<ModelProduk> mItems;
    private Context context;

    public AdapterProduk(Context context, List<ModelProduk> mItems){
        this.mItems = mItems;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_produk,viewGroup,false);
        AdapterProduk.HolderData holderData = new AdapterProduk.HolderData((layout));
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProduk.HolderData holder, int position) {
        ModelProduk me = mItems.get(position);
        try{
            holder.nama.setText(me.getNama());
            holder.harga.setText(Util.setformatrupiah(me.getHarga()));
            holder.kode = me.getKode();

            Picasso.get()
                    .load(ServerApi.IP + me.getIkon())
                    .into(holder.imgikon);
        }catch (Exception ea){
            ea.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        ImageView imgikon;
        TextView nama, harga;
        CardView cardpanel;
        String kode;
        public HolderData(@NonNull View itemView) {

            super(itemView);
            imgikon = itemView.findViewById(R.id.imgproduk);
            nama = itemView.findViewById(R.id.namaproduk);
            cardpanel = itemView.findViewById(R.id.cardkatalog);
            harga = itemView.findViewById(R.id.harga);

            cardpanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailProduk.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("putkode", kode);
                    context.startActivity(intent);
                }
            });
        }
    }
}
