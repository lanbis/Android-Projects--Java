package com.eren.resimdefteri;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eren.resimdefteri.databinding.ActivityRecyclerRowBinding;

import java.util.ArrayList;

public class resimAdaptor extends RecyclerView.Adapter<resimAdaptor.resimTutucu> {

    ArrayList<resimler> resimlerDizi;

    public resimAdaptor(ArrayList<resimler> resimlerDizi) {
        this.resimlerDizi = resimlerDizi;
    }

    public resimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityRecyclerRowBinding recyclerRowBinding = ActivityRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new resimTutucu(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull resimAdaptor.resimTutucu holder, int position) {
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

    @Override
    public int getItemCount() {
        return resimlerDizi.size();
    }

    public class resimTutucu extends RecyclerView.ViewHolder{
        private ActivityRecyclerRowBinding binding;
        public resimTutucu(ActivityRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

}
