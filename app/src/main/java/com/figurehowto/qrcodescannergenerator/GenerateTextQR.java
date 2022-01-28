package com.figurehowto.qrcodescannergenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class GenerateTextQR extends AppCompatActivity {
    EditText qrName,text;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_text_qr);
        //Load Ad
        mAdView = findViewById(R.id.gen_text_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Link Views XML to Java code
        qrName = (EditText) findViewById(R.id.qr_code_name_text);
        text = (EditText) findViewById(R.id.plain_text);

        Button generateQR = (Button) findViewById(R.id.button_gen_text_qr);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(areFieldsValidated()){
                   String fileName = qrName.getText().toString();
                   String qrCodeString = qrCodeString(text.getText().toString());
                   Intent generateQrActivity = new Intent(GenerateTextQR.this, OutputGeneratedQR.class);
                   generateQrActivity.putExtra("qrCodeString", qrCodeString);
                   generateQrActivity.putExtra("fileName", fileName);
                   startActivity(generateQrActivity);
                   finish();
               }
            }
        });
    }
    public String qrCodeString(String text ) {
        return text;
    }

    public boolean areFieldsValidated() {
        boolean r = true;
        if (qrName.getText().toString().isEmpty()) {
            qrName.setError("Cannot Be Left Blank");
            r = false;
        }
        if (text.getText().toString().isEmpty()) {
            text.setError("You have not entered any text");
            r = false;
        }

        return r;
    }
}
