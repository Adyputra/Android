package com.example.silaper.produk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.silaper.R;
import com.example.silaper.configfile.ServerApi;
import com.example.silaper.produk.model.ModelKatalog;
import com.example.silaper.produk.page.DataProduk;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterKatalog extends RecyclerView.Adapter<AdapterKatalog.HolderData> {
    private List<ModelKatalog> mItems;
    private Context context;

    public AdapterKatalog(Context context, List<ModelKatalog> mItems){
        this.mItems = mItems;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_katalog,viewGroup,false);
        AdapterKatalog.HolderData holderData = new AdapterKatalog.HolderData((layout));
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKatalog.HolderData holder, int position) {
        ModelKatalog me = mItems.get(position);
        try{
            holder.nama.setText(me.getNama());
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
        TextView nama;
        CardView cardpanel;
        String kode;
        public HolderData(@NonNull View itemView) {

            super(itemView);
            imgikon = itemView.findViewById(R.id.imgkatalog);
            nama = itemView.findViewById(R.id.namakatalog);
            cardpanel = itemView.findViewById(R.id.cardkatalog);

            cardpanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DataProduk.class);
                    intent.putExtra("putkode", kode);
                    context.startActivity(intent);
                }
            });
        }
    }
}
