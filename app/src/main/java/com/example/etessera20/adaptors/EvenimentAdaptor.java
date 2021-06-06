package com.example.etessera20.adaptors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etessera20.R;
import com.example.etessera20.models.Eveniment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EvenimentAdaptor extends RecyclerView.Adapter<EvenimentAdaptor.MyViewHolder> {

    Context context;
    List<Eveniment> mData;
    EventItemClickListener eventItemClickListener;


    public EvenimentAdaptor(Context context, List<Eveniment> mData, EventItemClickListener eventItemClickListener) {
        this.context = context;
        this.mData = mData;
        this.eventItemClickListener = eventItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_eveniment, parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvEveniment.setText(mData.get(position).getTitlu());
        Picasso.get().load(mData.get(position).getCover_img()).into(holder.ivEveniment);

    }

    @Override
    public int getItemCount(){

        if(mData!=null){
            return mData.size();
        }else return 0;
    }

    public void filterList(List<Eveniment> filtredList){
        mData = filtredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvEveniment;
        private ImageView ivEveniment;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvEveniment = (TextView) itemView.findViewById(R.id.item_eveniment_title);
            ivEveniment = (ImageView) itemView.findViewById(R.id.item_eveniment_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventItemClickListener.onEventClick(mData.get(getAdapterPosition()), ivEveniment);
                }
            });
        }
    }



}
