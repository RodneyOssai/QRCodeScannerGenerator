package com.figurehowto.qrcodescannergenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class GenerateEmailQR extends AppCompatActivity {
    EditText qrName, emailTo, emailSubject, emailBody;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_email_qr);
        //Load Ad
        mAdView = findViewById(R.id.gen_email_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Link Views XML to Java code
        qrName = (EditText) findViewById(R.id.qr_code_name_mail);
        emailTo = (EditText) findViewById(R.id.mail_to);
        emailSubject = (EditText) findViewById(R.id.mail_subject);
        emailBody = (EditText) findViewById(R.id.e_mail_body);
        Button generateQR = (Button) findViewById(R.id.button_gen_email_qr);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areFieldsValidated()) {
                    String fileName = qrName.getText().toString();
                    String qrCodeString = qrCodeString(emailTo.getText().toString(), emailSubject.getText().toString(), emailBody.getText().toString());
                    Intent generateQrActivity = new Intent(GenerateEmailQR.this, OutputGeneratedQR.class);
                    generateQrActivity.putExtra("qrCodeString", qrCodeString);
                    generateQrActivity.putExtra("fileName", fileName);
                    startActivity(generateQrActivity);
                    finish();
                }
            }
        });
    }

    public String qrCodeString(String emailTo, String emailSubject, String emailBody) {
        StringBuilder builder = new StringBuilder();
        builder.append("MATMSG:TO:").append(emailTo).append(";");
        builder.append("SUB:").append(emailSubject).append(";");
        builder.append("BODY:").append(emailBody).append(";").append(";");

        return builder.toString();
    }

    public boolean areFieldsValidated() {
        boolean r = true;
        if (qrName.getText().toString().isEmpty()) {
            qrName.setError("Cannot Be Blank");
            r = false;
        }

        if ((emailTo.getText().toString().isEmpty()) || (!emailTo.getText().toString().contains("@") && !emailTo.getText().toString().contains("."))) {
            if (emailTo.getText().toString().isEmpty()) {
                emailTo.setError("Cannot Be Left Blank");
            }
            if (((!emailTo.getText().toString().contains("@")) && (!emailTo.getText().toString().contains(".")))) {
                emailTo.setError("Enter a Valid Email Address");
            }
            r = false;
        }

        return r;
    }
}
