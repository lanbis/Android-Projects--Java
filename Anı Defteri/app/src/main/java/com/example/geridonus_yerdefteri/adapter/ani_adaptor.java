package com.example.geridonus_yerdefteri.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geridonus_yerdefteri.databinding.RecyclerRowBinding;
import com.example.geridonus_yerdefteri.model.ani;
import com.example.geridonus_yerdefteri.view.MapsActivity;

import java.io.Serializable;
import java.util.List;

public class ani_adaptor extends RecyclerView.Adapter<ani_adaptor.yerTutucu>  {
    List<ani> anilar;
    public ani_adaptor(List<ani> anilar) {
        this.anilar = anilar;
    }

    @NonNull
    @Override
    public yerTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new yerTutucu(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull yerTutucu holder, int position) {
        holder.binding.textView2.setText(anilar.get(position).isim);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), MapsActivity.class);
                intent.putExtra("ani",anilar.get(position));
                intent.putExtra("durum","eski");
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return anilar.size();
    }

    public class yerTutucu extends RecyclerView.ViewHolder{
        RecyclerRowBinding binding;
        public yerTutucu(@NonNull RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}