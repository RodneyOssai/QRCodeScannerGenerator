package com.figurehowto.qrcodescannergenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class GenerateWiFiQr extends AppCompatActivity {
    EditText qrName, networkName, networkPassword;
    Switch broadcastStatus;
    RadioGroup encryptyonType;
    boolean isNetworkHidden;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_wi_fi_qr);
        //Load Ad
        mAdView = findViewById(R.id.gen_wifi_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //Link Views XML to Java code
        qrName = (EditText) findViewById(R.id.qr_code_name_wifi);
        networkName = (EditText) findViewById(R.id.network_name);
        networkPassword = (EditText) findViewById(R.id.netw_pass);
        broadcastStatus = (Switch) findViewById(R.id.hidden);
        encryptyonType = (RadioGroup) findViewById(R.id.encryption_type);

        //Check if the switch is Toggled On or Of
        if (broadcastStatus != null) {
            broadcastStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        //do stuff when Switch is ON
                        isNetworkHidden = true;
                    } else {
                        //do stuff when Switch if OFF
                        isNetworkHidden = false;
                    }
                }
            });
        }


        Button generateQR = (Button) findViewById(R.id.button_gen_wifi_qr);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (areFieldsValidated()) {
                    //Find which radiobutton is selected and get the value
                    int radioButtonID = encryptyonType.getCheckedRadioButtonId();
                    final RadioButton selectedRadioButton = (RadioButton) findViewById(radioButtonID);
                    String selectedtext = (String) selectedRadioButton.getText().toString();

                    String fileName = qrName.getText().toString();
                    String qrCodeString = qrCodeString(networkName.getText().toString(), networkPassword.getText().toString(), selectedtext, isNetworkHidden);

                    //Intent to send date we need for QR code to OutputGeneratedQR.class
                    Intent generateQrActivity = new Intent(GenerateWiFiQr.this, OutputGeneratedQR.class);
                    generateQrActivity.putExtra("qrCodeString", qrCodeString);
                    generateQrActivity.putExtra("fileName", fileName);
                    startActivity(generateQrActivity);
                    finish();
                }

            }
        });
    }

    public String qrCodeString(String networkName, String networkPass, String encryptionType, boolean isNetworkHidden) {
        StringBuilder builder = new StringBuilder();
        builder.append("WIFI:");
        builder.append("T:");
        switch (encryptionType) {
            case "No Encryption":
                builder.append("nopass;");
                break;
            case "WPA/WPA2":
                builder.append("WPA;");
                break;
            case "WEP":
                //Code here
                builder.append("WEP;");
                break;
        }
        builder.append("S:").append(networkName).append(";");
        builder.append("P:").append(networkPass).append(";");
        builder.append("H:").append(isNetworkHidden).append(";");

        return builder.toString();
    }

    public boolean areFieldsValidated() {
        boolean r = true;
        if (qrName.getText().toString().isEmpty()) {
            qrName.setError("Cannot Be Left Blank");
            r = false;
        }

        return r;
    }
}
