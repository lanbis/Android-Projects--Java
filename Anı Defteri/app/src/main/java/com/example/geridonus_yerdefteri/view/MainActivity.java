package com.example.geridonus_yerdefteri.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.geridonus_yerdefteri.R;
import com.example.geridonus_yerdefteri.adapter.ani_adaptor;
import com.example.geridonus_yerdefteri.databinding.ActivityMainBinding;
import com.example.geridonus_yerdefteri.model.ani;
import com.example.geridonus_yerdefteri.roomDB.aniDAO;
import com.example.geridonus_yerdefteri.roomDB.aniDB;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    aniDB db;
    aniDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db= Room.databaseBuilder(getApplicationContext(),aniDB.class,"aniDB").build();
        dao = db.aniDAO();

        compositeDisposable.add(dao.hepsiniGetir()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(MainActivity.this::handleResponse));
    }
    private void handleResponse(List<ani> anilar){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ani_adaptor aniAdaptor = new ani_adaptor(anilar);
        binding.recyclerView.setAdapter(aniAdaptor);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.ekle){
            Intent intent = new Intent(this,MapsActivity.class);
            intent.putExtra("durum","yeni");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}