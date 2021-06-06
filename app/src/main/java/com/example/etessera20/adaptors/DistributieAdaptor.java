package com.example.etessera20.adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.etessera20.R;
import com.example.etessera20.models.Artist;
import com.example.etessera20.models.Distributie;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class DistributieAdaptor extends RecyclerView.Adapter<DistributieAdaptor.DistributieViewHolder> {

    Context mContext;
    List<Artist> mData;

    public DistributieAdaptor(Context mContext, List<Artist> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public DistributieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_distributie, parent , false);
        return new DistributieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributieViewHolder holder, int position) {

        holder.nume.setText(mData.get(position).getNume());
        Picasso.get().load(mData.get(position).getImagine()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class DistributieViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView nume;

        public DistributieViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_distributie_img);
            nume = itemView.findViewById(R.id.item_distributie_nume);
        }
    }
}
