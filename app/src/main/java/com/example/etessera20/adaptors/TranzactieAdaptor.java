package com.example.etessera20.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.etessera20.R;
import com.example.etessera20.models.Tranzactie;

import java.util.List;

public class TranzactieAdaptor extends RecyclerView.Adapter<TranzactieAdaptor.MyViewHolder> {
    Context context;
    List<Tranzactie> mTranzactii;

    public TranzactieAdaptor(Context context, List<Tranzactie> mTranzactii) {
        this.context = context;
        this.mTranzactii = mTranzactii;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tranzactie,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.data.setText(mTranzactii.get(position).getData());
        holder.nume.setText(mTranzactii.get(position).getNume());
        if(mTranzactii.get(position).getTip().equals("extragere")){
            holder.valoare.setText("-" + Double.toString(mTranzactii.get(position).getValoare()) + " lei");
        }else{
            holder.valoare.setText("+" + Double.toString(mTranzactii.get(position).getValoare()) + " lei");
        }

    }

    @Override
    public int getItemCount() {

        if(mTranzactii!=null){
            return mTranzactii.size();
        }else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView data;
        TextView nume;
        TextView valoare;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.tv_data_tranzactie);
            nume = itemView.findViewById(R.id.tv_titlu_tranzactie);
            valoare = itemView.findViewById(R.id.tv_suma);
        }
    }
}
