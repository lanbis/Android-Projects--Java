package com.eren.resimdefteri;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.eren.resimdefteri.databinding.ActivityDetaylarBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class detaylar extends AppCompatActivity {
    private ActivityDetaylarBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap secilenResim;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetaylarBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        register();

        database = this.openOrCreateDatabase("Resimler",MODE_PRIVATE,null);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")){
            //yeni resim
            binding.isimText.setText("");
            binding.tarihText.setText("");
            binding.yerText.setText("");
            binding.butonKaydet.setVisibility(View.VISIBLE);

            Bitmap buton = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.buton);
            binding.butonSec.setImageBitmap(buton);

        }else{
            int resimId = intent.getIntExtra("ArtId",1);
            binding.butonKaydet.setVisibility(View.INVISIBLE);

            try {
                Cursor cursor = database.rawQuery("SELECT * FROM resim WHERE id = ?",new String[] {String.valueOf(resimId)});

                int isimIx = cursor.getColumnIndex("isim");
                int yerIx = cursor.getColumnIndex("konum");
                int tarihIx = cursor.getColumnIndex("yil");
                int resimIx = cursor.getColumnIndex("resim");

                while (cursor.moveToNext()){

                    binding.isimText.setText(cursor.getString(isimIx));
                    binding.tarihText.setText(cursor.getString(tarihIx));
                    binding.yerText.setText(cursor.getString(yerIx));

                    byte[] bytes = cursor.getBlob(resimIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    binding.butonSec.setImageBitmap(bitmap);
                }
                cursor.close();
            }catch (Exception e){
                e.printStackTrace();

            }
        }
    }


    public void kaydet(View view){
        String isim = binding.isimText.getText().toString();
        String tarih = binding.tarihText.getText().toString();
        String konum = binding.yerText.getText().toString();

        Bitmap kucukResim = resmiKucult(secilenResim,600);
        // resimi veritabanına kaydeilebilir hale getirmek için ayarlıyoruz
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        kucukResim.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte [] byteArray = outputStream.toByteArray();

        try {
            database = this.openOrCreateDatabase("Resimler",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS resim (id INTEGER PRIMARY KEY, isim VARCHAR, konum VARCHAR, yil VARCHAR, resim BLOB)");
            String sqlString = "INSERT INTO resim(isim, konum, yil, resim) VALUES(?, ?, ?, ?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1,isim);
            sqLiteStatement.bindString(2,konum);
            sqLiteStatement.bindString(3,tarih);
            sqLiteStatement.bindBlob(4,byteArray);
            sqLiteStatement.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(detaylar.this,MainActivity.class);
        // aktiviteleri temizliyor
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public Bitmap resmiKucult(Bitmap image,int maximumSize){
        // resimlerin kapladığı boyutu azaltmak için resimleri yeniden boyutlandırıyoruz
        // genisligi alıyoruz
        int genislik = image.getWidth();
        // yüksekligi alıyoruz
        int yukseklik = image.getHeight();
        // ratioyu belirliyoruz
        float ratio = (float) (genislik) / (float) (yukseklik);
        if (ratio > 1){
            // yatay resim
            genislik = maximumSize;
            yukseklik =  (int) (genislik/ratio);
        }
        else{
            yukseklik = maximumSize;
            genislik = (int) (yukseklik*ratio);
            //dikey resim
        }
        return image.createScaledBitmap(image,genislik,yukseklik,true);
    }

    public void sec(View view){
        //İzin yok mu diye kontrol etmek için:
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {// izin varmı diye kontrol ediyor
            // İzin isteme sebebimi göstermem gerekiyormu:
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){// izin verilmediyse açıklama kutusu gösteriyor
                // gerekiyorsa ekrana bildirim yaptır
                Snackbar.make(view,"Galeriye Erişmek İçin İzin Gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //izin isteme ekranına yöneltiyor
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }
            // izin isteme sebebimi göstermem gerekmiyorsa direk izin iste
            else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        // izinim varsa galeriye gitme methodunu çalıştır
        else{
            Intent galeri = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // galeriye gidip görsel seçilecek ve görselin adresini alacak.
            activityResultLauncher.launch(galeri);
        }
    }

    public void register(){
        // galeriye gidip seçtikten sonraki yapılacakların kontrolu ve uygulanması
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            // kullanıcı bir şey seçilmiş mi kontrol ediliyor
            public void onActivityResult(ActivityResult result) {
                // geriye bir veri dönmüş mü
                if (result.getResultCode()== RESULT_OK) {
                    // kullanıcının seçtiği görselin nerede olduğunun verisini al
                Intent cevap = result.getData();
                // dönen veri null mu
                if (cevap !=null){
                    // veriyi uri olarak kaydet
                    Uri resimData = cevap.getData();
                    // olası bir hatada programın çökmesini engellemek için try-catch methodunu kullanıyoruz.
                    try {
                        // telefon sdk'si 28 ve üzerinde ise getbitmap yerine image decoder kullanılıyor.
                        if (Build.VERSION.SDK_INT >=28) {
                            //binding.butonSec.setImageURI(resimData); doğrudan resimi görünür olarak ayarlar. fakat bu programda veritabanına kaydedeceğiz.
                            // resimi veritabanına kaydedilebilir halde tutmak için bitmap olarak kullanıyoruz.
                            ImageDecoder.Source source = ImageDecoder.createSource(detaylar.this.getContentResolver(), resimData);
                            secilenResim = ImageDecoder.decodeBitmap(source);
                            binding.butonSec.setImageBitmap(secilenResim);
                        }
                        else{
                            secilenResim = MediaStore.Images.Media.getBitmap(detaylar.this.getContentResolver(),resimData);
                            binding.butonSec.setImageBitmap(secilenResim);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
                }
            }
        }
        );
        // izin isteyeceğimizi belirttik
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                // izin verildi mi diye kontrol et ve yap
                if(result){
                    // resim seçme ekranına git
                    Intent galeri = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(galeri);
                }
                // izin verilmediyse yap
                else{
                    // izin verilmediğine dair toast göster
                    Toast.makeText(getApplicationContext(),"İzin Verilmedi",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}