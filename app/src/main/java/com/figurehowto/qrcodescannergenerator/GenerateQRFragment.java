package com.figurehowto.qrcodescannergenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class GenerateQRFragment extends Fragment {
    private AdView mAdView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_generate_qr, container, false);
        //Load An  Ad
        mAdView = view.findViewById(R.id.generate_qr_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //When Generate Contact View is clicked
        LinearLayout generateContact = (LinearLayout) view.findViewById(R.id.gen_contact_qr);
        generateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generateContactActivity = new Intent(getActivity(), GenerateContactQR.class);
                startActivity(generateContactActivity);
            }
        });
        //When Generate Email View is clicked
        LinearLayout generateEmail = (LinearLayout) view.findViewById(R.id.gen_email_qr);
        generateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generateEmailActivity = new Intent(getActivity(), GenerateEmailQR.class);
                startActivity(generateEmailActivity);
            }
        });
        //When Generate SMS View is clicked
        LinearLayout generateMessage = (LinearLayout) view.findViewById(R.id.gen_sms_qr);
        generateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generateSmsActivity = new Intent(getActivity(), GenerateSmsQR.class);
                startActivity(generateSmsActivity);
            }
        });
        //When Generate URL View is clicked
        LinearLayout generateUrl = (LinearLayout) view.findViewById(R.id.gen_url_qr);
        generateUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generateUrlActivity = new Intent(getActivity(), GenerateUrlQR.class);
                startActivity(generateUrlActivity);
            }
        });
        //When Generate Wi-Fi View is clicked
        LinearLayout generateWiFi = (LinearLayout) view.findViewById(R.id.gen_wifi_qr);
        generateWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generateWiFiActivity = new Intent(getActivity(), GenerateWiFiQr.class);
                startActivity(generateWiFiActivity);
            }
        });

        //When Generate Text View is clicked
        LinearLayout generateText = (LinearLayout) view.findViewById(R.id.generate_text_qr);
        generateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generateTextActivity = new Intent(getActivity(), GenerateTextQR.class);
                startActivity(generateTextActivity);
            }
        });




        //When Scan QR Code ImageView View is clicked
        ImageView scanQR = (ImageView) view.findViewById(R.id.scan_qr1);
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager2 viewPager2 = getActivity().findViewById(R.id.pager);
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1 ,true);

            }
        });
        return view;
    }
}