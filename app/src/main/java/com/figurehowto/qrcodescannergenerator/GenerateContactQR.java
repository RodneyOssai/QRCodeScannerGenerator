package com.figurehowto.qrcodescannergenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class GenerateContactQR extends AppCompatActivity {
    EditText qrName, firstName, lastName, phoneNo, emailAddr, jobTitle, companyName, streetAddr, cityName, stateName, postCode, country;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_contact);
        //Load Ad
        mAdView = findViewById(R.id.gen_contact_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Link Views XML to Java code
        qrName = (EditText) findViewById(R.id.qr_code_name_contact);
        firstName = (EditText) findViewById(R.id.f_name);
        lastName = (EditText) findViewById(R.id.l_name);
        phoneNo = (EditText) findViewById(R.id.p_number);
        emailAddr = (EditText) findViewById(R.id.e_mail);
        jobTitle = (EditText) findViewById(R.id.job_title);
        companyName = (EditText) findViewById(R.id.p_organisation);
        streetAddr = (EditText) findViewById(R.id.c_street);
        cityName = (EditText) findViewById(R.id.c_city);
        stateName = (EditText) findViewById(R.id.c_state);
        postCode = (EditText) findViewById(R.id.c_postcode);
        country = (EditText) findViewById(R.id.c_country);


        Button generateQR = (Button) findViewById(R.id.button_gen_contact_qr);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(areFieldsValidated()){
                    String fileName = qrName.getText().toString();
                    String qrCodeString = qrCodeString(firstName.getText().toString(), lastName.getText().toString(), phoneNo.getText().toString(), emailAddr.getText().toString(), jobTitle.getText().toString(), companyName.getText().toString(), streetAddr.getText().toString(), cityName.getText().toString(), stateName.getText().toString(), postCode.getText().toString(), country.getText().toString());
                    Intent generateQrActivity = new Intent(GenerateContactQR.this, OutputGeneratedQR.class);
                    generateQrActivity.putExtra("qrCodeString", qrCodeString);
                    generateQrActivity.putExtra("fileName", fileName);
                    startActivity(generateQrActivity);
                    finish();
                }
            }
        });

    }

    public String qrCodeString(String firstName, String lastName, String phoneNo, String emailAddr, String jobTitle, String companyName, String streetAddr, String cityName, String stateName, String postCode, String country) {
        StringBuilder builder = new StringBuilder();
        builder.append("BEGIN:VCARD\nVERSION:3.0\n");
        builder.append("N:").append(lastName).append(";").append(firstName).append("\n");
        builder.append("FN:").append(firstName).append(" ").append(lastName).append("\n");
        builder.append("ORG:").append(companyName).append("\n");
        builder.append("TITLE:").append(jobTitle).append("\n");
        builder.append("ADR:").append(streetAddr).append(";").append(cityName).append(";").append(stateName).append(";").append(postCode).append(";").append(country).append("\n");
        builder.append("TEL;TYPE=work,voice:").append("\n");
        builder.append("TEL;TYPE=cell,voice:").append(phoneNo).append("\n");
        builder.append("TEL;TYPE=work,fax:").append("\n");
        builder.append("EMAIL;WORK;INTERNET:").append(emailAddr).append("\n");
        builder.append("URL:").append("\n");
        builder.append("END:VCARD");

        return builder.toString();
    }

    public boolean areFieldsValidated() {
        boolean r = true;
        if (qrName.getText().toString().isEmpty()) {
            qrName.setError("Cannot Be Blank");
            r = false;
        }
        if ((firstName.getText().toString().isEmpty()) || (lastName.getText().toString().isEmpty())) {
            if (firstName.getText().toString().isEmpty()) {
                firstName.setError("Field Must be filled to continue");
            }
            if (lastName.getText().toString().isEmpty()) {
                lastName.setError("Field Must be filled to continue");
            }
            r = false;
        }

        if (phoneNo.getText().toString().isEmpty()) {
            phoneNo.setError("Cannot Be Blank");
            r = false;
        }

        if ((emailAddr.getText().toString().isEmpty()) || (!emailAddr.getText().toString().contains("@") && !emailAddr.getText().toString().contains(".") )) {
            if (emailAddr.getText().toString().isEmpty()) {
                emailAddr.setError("Cannot Be Left Blank");
            }
            if (((!emailAddr.getText().toString().contains("@")) && (!emailAddr.getText().toString().contains(".")))) {
                emailAddr.setError("Enter a Valid Email Address");
            }
            r = false;
        }

        return r;
    }

}



