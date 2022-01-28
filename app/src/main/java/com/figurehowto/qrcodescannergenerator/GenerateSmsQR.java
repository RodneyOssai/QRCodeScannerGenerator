package com.figurehowto.qrcodescannergenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class GenerateSmsQR extends AppCompatActivity {
    EditText qrName, smsReciplent, messageBody;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_sms_qr);
        //Load Ad
        mAdView = findViewById(R.id.gen_sms_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Link Views XML to Java code
        qrName = (EditText) findViewById(R.id.qr_code_name_sms);
        smsReciplent = (EditText) findViewById(R.id.sms_recipent);
        messageBody = (EditText) findViewById(R.id.message_body);

        Button generateQR = (Button) findViewById(R.id.button_gen_sms_qr);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFieldsValidated()) {
                    String fileName = qrName.getText().toString();
                    String qrCodeString = qrCodeString(smsReciplent.getText().toString(), messageBody.getText().toString());
                    Intent generateQrActivity = new Intent(GenerateSmsQR.this, OutputGeneratedQR.class);
                    generateQrActivity.putExtra("qrCodeString", qrCodeString);
                    generateQrActivity.putExtra("fileName", fileName);
                    startActivity(generateQrActivity);
                    finish();
                }
            }
        });

    }

    public String qrCodeString(String smsReciplent, String messageBody) {
        StringBuilder builder = new StringBuilder();
        builder.append("SMSTO:").append(smsReciplent).append(":");
        builder.append(messageBody);

        return builder.toString();
    }

    public boolean areFieldsValidated() {
        boolean r = true;
        if (qrName.getText().toString().isEmpty()) {
            qrName.setError("Cannot Be Left Blank");
            r = false;
        }

        if ((smsReciplent.getText().toString().isEmpty()) || ((smsReciplent.getText().toString().length() < 2))) {
            if (smsReciplent.getText().toString().isEmpty()) {
                smsReciplent.setError("Cannot Be Left Blank");
            }
            if ((smsReciplent.getText().toString().length() < 2)) {
                smsReciplent.setError("Enter a Valid Phone Number");
            }
            r = false;
        }

        return r;
    }


}
