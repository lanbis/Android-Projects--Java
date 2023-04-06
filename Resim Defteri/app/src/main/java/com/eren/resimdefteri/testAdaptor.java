package com.eren.resimdefteri;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eren.resimdefteri.databinding.ActivityRecyclerRowBinding;

import java.util.ArrayList;

public class testAdaptor extends RecyclerView.Adapter<testAdaptor.testTutucu> {
    // kullanacağımız diziyi tanımladık
    private ArrayList<resimler> resimlerDizi;

    //dizinin consturactorunu oluşturduk
    public testAdaptor(ArrayList<resimler> resimlerDizi) {
        this.resimlerDizi = resimlerDizi;
    }

    @NonNull
    @Override
    // Viewholderın hangi layoutta tutacağını belirtiyor. recycler row un birbirleri ile ilişkilendirilmesi
    public testTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   //infale ayarlamalarını yaptık ve tutucuya yolladık
    ActivityRecyclerRowBinding binding = ActivityRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
    return new testTutucu(binding);


    }

    @Override
    //verilecek dizinin boyutu
    public int getItemCount() {
        return resimlerDizi.size();
    }

    @Override
    //viewlere verilecek özellikler
    public void onBindViewHolder(testTutucu holder, int position) {
    holder.binding.recyclerViewTextView.setText(resimlerDizi.get(position).isim);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(holder.itemView.getContext(),detaylar.class);
            intent.putExtra("info","old");
            intent.putExtra("ArtId",resimlerDizi.get(position).id);
            holder.itemView.getContext().startActivity(intent);
        }
    });
    }

    public static class testTutucu extends RecyclerView.ViewHolder{
        private ActivityRecyclerRowBinding binding;
        public testTutucu(ActivityRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
