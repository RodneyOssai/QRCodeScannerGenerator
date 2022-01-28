package com.figurehowto.qrcodescannergenerator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class SmsActivity extends AppCompatActivity {
    TextView sms_to, sms_body;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        //Load Ad
        mAdView = findViewById(R.id.sms_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Get Intents
        final String to, body;
        to = getIntent().getStringExtra("number");
        body = getIntent().getStringExtra("sms_body");
        //Link Text view in XML to Java
        sms_to = (TextView) findViewById(R.id.sms_to);
        sms_body = (TextView) findViewById(R.id.sms_body);
        //Set Textview Programmatically
        sms_to.setText(to);
        sms_body.setText(body);

        Button sendSms = (Button) findViewById(R.id.send_sms);
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Send Message",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+ to));  // This ensures only SMS apps respond
                intent.putExtra("sms_body", body);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }
}
