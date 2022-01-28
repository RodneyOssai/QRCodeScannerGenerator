package com.figurehowto.qrcodescannergenerator;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import com.figurehowto.qrcodescannergenerator.Model.EMAILmodel;
import com.figurehowto.qrcodescannergenerator.Model.SMSmodel;
import com.figurehowto.qrcodescannergenerator.Model.URLmodel;
import com.figurehowto.qrcodescannergenerator.Model.VCARDmodel;
import com.figurehowto.qrcodescannergenerator.Model.WIFImodel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private TextView txt_result;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Load Ad
        mAdView = findViewById(R.id.result_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String qrCodeResult = getIntent().getStringExtra("qr_result");
        txt_result = (TextView) findViewById(R.id.txt_result);
        txt_result.setText(qrCodeResult);

    }


}
