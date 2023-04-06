package com.eren.resimdefteri;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eren.resimdefteri.databinding.ActivityMainBinding;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    ArrayList<resimler> resimlerDizi;
    resimAdaptor ResimAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.eren.resimdefteri.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        resimlerDizi = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ResimAdaptor = new resimAdaptor(resimlerDizi);
        binding.recyclerView.setAdapter(ResimAdaptor);
        veriAl();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void veriAl(){
        try{
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Resimler",MODE_PRIVATE,null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM resim",null);
            int isimIx = cursor.getColumnIndex("isim");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()){
                String isim = cursor.getString(isimIx);
                int id = cursor.getInt(idIx);
                resimler Resimlerr = new resimler(isim,id);
                resimlerDizi.add(Resimlerr);
            }
            ResimAdaptor.notifyDataSetChanged();
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         if (item.getItemId() == R.id.ekle){
             Intent intent = new Intent(this,detaylar.class);
             intent.putExtra("info","new");
             startActivity(intent);
         }

        return super.onOptionsItemSelected(item);
    }
}