package com.figurehowto.qrcodescannergenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class WifiSettingActivity extends AppCompatActivity {
    TextView network_name, encryption, wifi_pass, network_visibility;
   ImageView copy_pass;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_setting);

        //Load Ad
        mAdView = findViewById(R.id.wifi_setting_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Get Intents
        final String encryption_type, ssid, pswd, visibility;
        encryption_type = getIntent().getStringExtra("encryption_type");
        ssid = getIntent().getStringExtra("ssid");
        pswd = getIntent().getStringExtra("password");
        visibility = getIntent().getStringExtra("visibility");

        //Link Text view in XML to Java
        network_name = (TextView) findViewById(R.id.ssid);
        encryption = (TextView) findViewById(R.id.encryption);
        wifi_pass = (TextView) findViewById(R.id.wifi_pass);
        network_visibility = (TextView) findViewById(R.id.visibility);
        copy_pass = (ImageView) findViewById(R.id.copy_wifi_pswd);

        //Set Textview Programmatically
        encryption.setText(encryption_type);
        network_name.setText(ssid);
        wifi_pass.setText(pswd);
        network_visibility.setText(visibility);

        Button show_networks = (Button) findViewById(R.id.show_networks);
        show_networks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Network Password Copied",Toast.LENGTH_LONG).show();
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));

            }
        });
        copy_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Wi-Fi Password Copied",Toast.LENGTH_LONG).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", pswd);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                }

            }
        });
    }
}
