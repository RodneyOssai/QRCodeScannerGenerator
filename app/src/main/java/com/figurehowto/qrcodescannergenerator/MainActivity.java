package com.figurehowto.qrcodescannergenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent homeActivity = new Intent(MainActivity.this,ScreenSlidePagerActivity.class);
               startActivity(homeActivity);
               finish();
           }
       },SPLASH_TIME_OUT);
   }
}
