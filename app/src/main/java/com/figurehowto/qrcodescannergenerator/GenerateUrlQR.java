package com.figurehowto.qrcodescannergenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class GenerateUrlQR extends AppCompatActivity {
    EditText qrName, webaddress;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_url_qr);
        //Load Ad
        mAdView = findViewById(R.id.gen_url_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Link Views XML to Java code
        qrName = (EditText) findViewById(R.id.qr_code_name_sms);
        webaddress = (EditText) findViewById(R.id.website_name);

        Button generateQR = (Button) findViewById(R.id.button_gen_url_qr);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(areFieldsValidated()){
                    String fileName = qrName.getText().toString();
                    String qrCodeString = qrCodeString(webaddress.getText().toString());
                    Intent generateQrActivity = new Intent(GenerateUrlQR.this, OutputGeneratedQR.class);
                    generateQrActivity.putExtra("qrCodeString", qrCodeString);
                    generateQrActivity.putExtra("fileName", fileName);
                    startActivity(generateQrActivity);
                    finish();
                }
            }
        });
    }

    public String qrCodeString(String webaddress) {
        int number;
        StringBuilder builder = new StringBuilder();
        if (webaddress.startsWith("http://") || webaddress.startsWith("https://") ) {
            number = 1;
        } else if (webaddress.startsWith("www.")){
            number = 2;
        }else {
            number = 3;
        }
        switch (number){
            case 1:
                builder.append(webaddress);
                break;
            case 2:
            case 3:
                //Code here
                builder.append("http://").append(webaddress);
                break;
        }

        return builder.toString();
    }

    public boolean areFieldsValidated() {
        boolean r = true;
        if (qrName.getText().toString().isEmpty()) {
            qrName.setError("Cannot Be Left Blank");
            r = false;
        }

        if ((webaddress.getText().toString().isEmpty()) || (!webaddress.getText().toString().contains("."))) {
            if (webaddress.getText().toString().isEmpty()) {
                webaddress.setError("Cannot Be Left Blank");
            }
            if (!webaddress.getText().toString().contains(".")) {
                webaddress.setError("Enter a Valid URL");
            }
            r = false;
        }

        return r;
    }
}
