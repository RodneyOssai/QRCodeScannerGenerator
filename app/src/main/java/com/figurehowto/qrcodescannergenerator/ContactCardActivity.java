package com.figurehowto.qrcodescannergenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactCardActivity extends AppCompatActivity {
    private TextView contactName, contactTitle, contactOrg, contactPhone, contactEmail, contactHomeEmail, contactAddr, contactUrl;
    private ImageView callContact, messageContact;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_card);

        //Load Ad
        mAdView = findViewById(R.id.contact_card_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final String name, title, org, phone, email, homeEmail, addr, url;

        //Get Intent
        name = getIntent().getStringExtra("contact_name");
        title = getIntent().getStringExtra("contact_title");
        org = getIntent().getStringExtra("contact_org");
        phone = getIntent().getStringExtra("contact_tel");
        email = getIntent().getStringExtra("contact_email");
        homeEmail = getIntent().getStringExtra("contact_email_home");
        addr = getIntent().getStringExtra("contact_addr");
        url = getIntent().getStringExtra("contact_url");

        //Link Views XML to Java code
        contactName = (TextView) findViewById(R.id.contact_name);
        contactTitle = (TextView) findViewById(R.id.contact_title);
        contactOrg = (TextView) findViewById(R.id.contact_organisation);
        contactPhone = (TextView) findViewById(R.id.contact_phone);
        contactEmail = (TextView) findViewById(R.id.contact_email);
        contactHomeEmail = (TextView) findViewById(R.id.contact_email_home);
        contactAddr = (TextView) findViewById(R.id.contact_address);
        contactUrl = (TextView) findViewById(R.id.contact_url);
        Button addContact = (Button) findViewById(R.id.add_contact);
        callContact = (ImageView) findViewById(R.id.call_contact);
        messageContact = (ImageView) findViewById(R.id.message_contact);


        if (name != null) {
            contactName.setText(name.replaceAll(";", " "));
        }
        contactTitle.setText(title);
        contactOrg.setText(org);
        contactPhone.setText(phone);
        contactEmail.setText(email);
        contactHomeEmail.setText(homeEmail);
        if(addr!= null){
            contactAddr.setText(addressCleanup(addr));
        }
        contactUrl.setText(url);

        //Set OnClickListener to Button And ImageView.
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Add Contact", Toast.LENGTH_LONG).show();
                // Creates a new Intent to insert a contact
                Intent addPersonIntent = new Intent(Intent.ACTION_INSERT);
                // Sets the MIME type to match the Contacts Provider
                addPersonIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                addPersonIntent.putExtra(ContactsContract.Intents.Insert.NAME, name.replaceAll(";", " "));
                addPersonIntent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                addPersonIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                addPersonIntent.putExtra(ContactsContract.Intents.Insert.POSTAL, addr);
                addPersonIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, org);
                addPersonIntent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, title);
                startActivity(addPersonIntent);
            }
        });
        callContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

            }
        });
        messageContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Send Message",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+ phone));  // This ensures only SMS apps respond
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    public String addressCleanup(String str) {
        // Replacing every ";" with a space(" ")
        str = str.replaceAll(";", " ");

        // Remove extra spaces from the beginning
        // and the ending of the string
        str = str.trim();

        // Replace all the consecutive white spaces with a ","
        //str = str.replaceAll(" +", ",");

        if (str.equals(""))
            return "-1";

        return str;
    }


}
