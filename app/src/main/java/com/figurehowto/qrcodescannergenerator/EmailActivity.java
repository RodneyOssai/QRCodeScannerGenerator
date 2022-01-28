package com.figurehowto.qrcodescannergenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class EmailActivity extends AppCompatActivity {
TextView recipent,subject,email_body;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //Load Ad
        mAdView = findViewById(R.id.email_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final String to,sub,body;
         to = getIntent().getStringExtra("to");
        sub = getIntent().getStringExtra("subject");
        body = getIntent().getStringExtra("body");

        recipent = (TextView) findViewById(R.id.to_email);
        subject = (TextView) findViewById(R.id.email_subject);
        email_body = (TextView) findViewById(R.id.email_body);

        recipent.setText(to);
        subject.setText(sub);
        email_body.setText(body);

        Button sendEmail = (Button) findViewById(R.id.send_email);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Send Email",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+ to)); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, sub);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
