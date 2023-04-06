package com.example.geridonus_yerdefteri.view;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.example.geridonus_yerdefteri.R;
import com.example.geridonus_yerdefteri.model.ani;
import com.example.geridonus_yerdefteri.roomDB.aniDAO;
import com.example.geridonus_yerdefteri.roomDB.aniDB;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.geridonus_yerdefteri.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher;
    LocationManager locationManager;
    aniDB db;
    aniDAO dao;
    LocationListener locationListener;
    Double secilenEnlem;
    Double secilenBoylam;
    private CompositeDisposable disposable = new CompositeDisposable();
    ani secilenAni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        register();
        db= Room.databaseBuilder(getApplicationContext(),aniDB.class,"aniDB").build();
        dao = db.aniDAO();
        binding.button.setEnabled(false);
    }
    public void register(){
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result){
                // izin verildi
                if (ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,locationListener);
                    Location sonKonum = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (sonKonum!=null){
                        LatLng sonLat = new LatLng(sonKonum.getLatitude(),sonKonum.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sonLat,15));
                    }
                }
            }
            else {
                // izin verilmedi
                Toast.makeText(MapsActivity.this,"İzin Verilmedi !",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        Intent intent = getIntent();
        String durum = intent.getStringExtra("durum");

        if (durum.equals("yeni")){
            binding.button.setVisibility(View.VISIBLE);
            binding.button2.setVisibility(View.GONE);
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() { // bazı özellikleri implemente etmezsen uygulama çökebiliyor. Konum isteme aralığına ve mesafesine de dikkat et
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
                }
                @Override
                public void onProviderEnabled(@NonNull String provider) {
                    System.out.println("DEBUG 2");
                    //Toast.makeText(MapsActivity.this,"onProviderEnabled",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onProviderDisabled(@NonNull String provider) {
                    System.out.println("DEBUG 3");
                    //Toast.makeText(MapsActivity.this,"onProviderDisabled",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    System.out.println("DEBUG 4");
                    //Toast.makeText(MapsActivity.this,"onStatusChanged",Toast.LENGTH_LONG).show();
                }
            };

            // İZİN KONTROL - İSTEME EKRANI
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){// izin var mı bak
                // izin yoksa izin iste
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){ //izin isteme sebebimi göstermem gerekiyor mu
                // izin isteme sebebimi göstermeme gerek var mı
                Snackbar.make(binding.getRoot(),"Konum İçin İzin Gerekli",Snackbar.LENGTH_LONG).setAction("İzin Ver", view -> {
                    // izin iste
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }).show();
            }
            else{
                // izinim varsa konumunu al:
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,locationListener);
                Location sonKonum = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.setMyLocationEnabled(true);
                if (sonKonum!=null){
                    LatLng sonLat = new LatLng(sonKonum.getLatitude(),sonKonum.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sonLat,15));
                }
            }
            LatLng istanbul = new LatLng(41.015137, 28.979530);
            mMap.addMarker(new MarkerOptions().position(istanbul).title("Welkom Tu İstanbul"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(istanbul,15));
        }
        else{
            mMap.clear();
            secilenAni = (ani) intent.getSerializableExtra("ani");
            LatLng latLng = new LatLng(secilenAni.enlem,secilenAni.boylam);
            mMap.addMarker(new MarkerOptions().position(latLng).title(secilenAni.isim));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            binding.textView.setText(secilenAni.isim);

            // Butonların Görünürlüğü
            binding.button.setVisibility(View.GONE);
            binding.button2.setVisibility(View.VISIBLE);

            // DÜZENLEMEYİ KAPATABİLMEK İÇİN
            binding.textView.setTextIsSelectable(false);
            binding.textView.setFocusable(false);
            binding.textView.setEnabled(false);
            binding.textView.setActivated(false);
            binding.textView.setInputType(InputType.TYPE_NULL);
        }
    }
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Seçim"));
        secilenEnlem = latLng.latitude;
        secilenBoylam = latLng.longitude;
        binding.button.setEnabled(true);
    }
    public void kaydet(View view){
        ani ani = new ani(binding.textView.getText().toString(),secilenEnlem,secilenBoylam);

        //dao.insert(ani).subscribeOn(Schedulers.io()).subscribe();

        //disposable - kullan at çöp torbası
        //anroidschedulers i implemente ederken dikkat et. sürüm farklılığı olabiliyor.
        disposable.add(dao.insert(ani) // bu işlemi gerçekleştir
                .subscribeOn(Schedulers.io()) // bunu iothread ile yap
                .observeOn(AndroidSchedulers.mainThread()) // mainthread da gözlemlicem ama yazmasam da olur
                .subscribe(MapsActivity.this::handleResponse)); // işlem bittiğinde bu işlemi gerçekleştir
    }
    public void sil(View view){
        disposable.add(dao.delete(secilenAni)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MapsActivity.this::handleResponse));
    }

    private void handleResponse(){
        Intent intent = new Intent(MapsActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear(); // kullanılan rxjava işlemleri hafızadan temizlenir. verimlilik için gerekli
    }
}