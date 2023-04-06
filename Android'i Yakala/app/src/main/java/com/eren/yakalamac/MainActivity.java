package com.eren.yakalamac;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
public class MainActivity extends AppCompatActivity {
    TextView scoreText;
    TextView timeText;
    TextView maxText;
    ImageView res1;
    Handler handler;
    Runnable runnable;
    int score;
    Random randomX = new Random();
    Random randomY = new Random();
    SharedPreferences maxs;
    Integer tMax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreText = findViewById(R.id.scoreText);
        timeText = findViewById(R.id.timeText);
        res1 = findViewById(R.id.imageView1);
        maxText = findViewById(R.id.maxText);
        hide();
        maxs = this.getSharedPreferences("com.eren.yakalamac",Context.MODE_PRIVATE);
        Integer maksi = maxs.getInt("score",0);
        if (maksi>0) {
            String yazi = maksi.toString();
           maxText.setText("Max Skor:"+yazi);
        }
        score =0;
        new CountDownTimer(10000,1000){
            @Override
            public void onTick(long l) {
                timeText.setText("Zaman: "+ l/1000);
            }
            @Override
            public void onFinish() {
                timeText.setText("Zaman Bitti !");
                handler.removeCallbacks(runnable);
                res1.setVisibility(View.INVISIBLE);
                tMax = maxs.getInt("score",0);
                if (score > tMax ) {
                   maxs.edit().putInt("score",score).apply();
                }
                AlertDialog.Builder bitti = new AlertDialog.Builder(MainActivity.this);
                bitti.setTitle("Yeniden Başlatılsın mı");
                bitti.setMessage("Tekrar Oynamak İster Misiniz?");
                bitti.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //restart
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                bitti.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"Oynadığın İçin Teşekkürler",Toast.LENGTH_SHORT).show();
                    }
                });
                bitti.show();
            }
        }.start();

    }

    public void hide(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                res1.setVisibility(View.INVISIBLE);
                res1.setX(randomX.nextInt(1000));
                res1.setY(randomY.nextInt(1000));
                res1.setVisibility(View.VISIBLE);
                handler.postDelayed(this,600);
            }
        };
        handler.post(runnable);
    }

    public void SkorArt(View view){
        score++;
        scoreText.setText("Skor: "+score);
    }
}