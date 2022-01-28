package com.figurehowto.qrcodescannergenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class WebLinkActivity extends AppCompatActivity {
    private TextView weblink;
    ImageView copy_link;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String qrCodeResult;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_link);

        //Load Ad
        mAdView = findViewById(R.id.weblink_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Get Intent
         qrCodeResult = getIntent().getStringExtra("weblink");
        //Link Views XML to Java code
        weblink = (TextView) findViewById(R.id.weblink);
        copy_link = (ImageView) findViewById(R.id.copy_weblink);
        Button visitPage = (Button) findViewById(R.id.visit_page);

        weblink.setText(qrCodeResult);

        //Set OnClickListener to Button And ImageView.
        visitPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Visit Website",Toast.LENGTH_LONG).show();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(qrCodeResult));
                startActivity(i);
            }
        });

        copy_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("url", qrCodeResult);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Copied",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}

