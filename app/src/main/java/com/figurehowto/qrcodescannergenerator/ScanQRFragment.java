package com.figurehowto.qrcodescannergenerator;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.figurehowto.qrcodescannergenerator.Model.EMAILmodel;
import com.figurehowto.qrcodescannergenerator.Model.SMSmodel;
import com.figurehowto.qrcodescannergenerator.Model.URLmodel;
import com.figurehowto.qrcodescannergenerator.Model.VCARDmodel;
import com.figurehowto.qrcodescannergenerator.Model.WIFImodel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.figurehowto.qrcodescannergenerator.ScanHistoryAdapter.SharedPreference.PREFS_NAME;
import static com.figurehowto.qrcodescannergenerator.ScanHistoryAdapter.SharedPreference.SCAN_RESULT;

public class ScanQRFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private InterstitialAd mInterstitialAd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(
                R.layout.fragment_scan_qr, container, false);
        InterstitialAd.load(getActivity().getApplicationContext(),"ca-app-pub-1234502077623540/5852730086", new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });


        //Initialize
        scannerView = (ZXingScannerView) view.findViewById(R.id.camera_view);
        //Request Permissions
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        scannerView.setResultHandler(ScanQRFragment.this);
                        scannerView.startCamera();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "Impossible to use this app without Camera Permission", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        // Intent To Open Scan History Activity
        ImageView scan_history = (ImageView)view.findViewById(R.id.scan_history);
        scan_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Scan History", Toast.LENGTH_LONG).show();
                Intent generateTextActivity = new Intent(getActivity(), ScanHistoryActivity.class);
                startActivity(generateTextActivity);
            }
        });

        // Switch On Torch
        final ImageView torch = (ImageView) view.findViewById(R.id.torch);
        torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!torch.isActivated()){
                    scannerView.setFlash(true);
                    torch.setActivated(true);
                }else {
                    torch.isActivated();
                    torch.setActivated(false);
                    scannerView.setFlash(false);
                }

            }
        });

        //Generate QR Bottom Navigation.
        ImageView generateQR = (ImageView) view.findViewById(R.id.generate_qr);
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewPager2 viewPager2 = getActivity().findViewById(R.id.pager);
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() +1 ,true);

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(ScanQRFragment.this); // Register ourselves as a handler for scan results.
                        scannerView.startCamera();          // Start camera on resume

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "U", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


    @Override
    public void handleResult(Result rawResult) {
        // Show Loaded Ad
        if (mInterstitialAd != null) {
            mInterstitialAd.show(getActivity());
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

        Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        assert vibrator != null;
        vibrator.vibrate(300);
        saveToSharedPrefData(rawResult.getText());
        processRawResult(rawResult.getText());
        scannerView.stopCamera();

    }
    public String extractInt(String str) {
        // Replacing every non-digit number
        // with a space(" ")
        str = str.replaceAll("[^\\d]", " ");

        // Remove extra spaces from the beginning
        // and the ending of the string
        str = str.trim();

        // Replace all the consecutive white
        // spaces with a single space
        str = str.replaceAll(" +", " ");

        if (str.equals(""))
            return "-1";

        return str;
    }

    public void processRawResult(String string) {

        if (string.startsWith("BEGIN:")) {
            // Runs if the QR code is a Contact Card
            String[] tokens = string.split("\n");
            VCARDmodel contactCard = new VCARDmodel();
            for (String token : tokens) {
                if (token.startsWith("TITLE:")) {
                    contactCard.setTitle(token.substring("TITLE:".length())); //Remove string Title
                }
                if (token.startsWith("N:")) {
                    contactCard.setName(token.substring("N:".length())); //Remove string Begin
                }
                if (token.startsWith("ORG:")) {
                    contactCard.setOrg(token.substring("ORG:".length())); //Remove string Begin
                }
                if (token.startsWith("ADR")) {
                    contactCard.setAddress(token.substring(token.lastIndexOf(":") + 1)); //Remove string Begin
                }
                if ((token.startsWith("TEL")) && (token.contains("CELL") || token.contains("cell") )) {
                    contactCard.setTelephone(extractInt(token));
                }
                if ((token.startsWith("TEL")) && (token.contains("TYPE=WORK,VOICE") || token.contains("work,voice")) ) {
                    if(contactCard.getTelephone() == null){
                        String str = token.substring(token.lastIndexOf(":")+1);
                        contactCard.setTelephone(extractInt(str));
                    }

                }
                if (token.startsWith("EMAIL")) {
                    contactCard.setEmail(token.substring(token.lastIndexOf(":") + 1)); //
                }
                if ((token.startsWith("EMAIL")) && (token.contains("HOME"))) {
                    contactCard.setHomeEmail(token.substring(token.lastIndexOf(":") + 1)); //
                }
                if (token.startsWith("URL:")) {
                    contactCard.setUrl(token.substring("URL:".length())); //Remove string URL
                }


            }
            Intent contactCardActivity = new Intent(getActivity(), ContactCardActivity.class);
            contactCardActivity.putExtra("contact_name", contactCard.getName());
            contactCardActivity.putExtra("contact_title", contactCard.getTitle());
            contactCardActivity.putExtra("contact_org", contactCard.getOrg());
            contactCardActivity.putExtra("contact_tel", contactCard.getTelephone());
            contactCardActivity.putExtra("contact_email", contactCard.getEmail());
            contactCardActivity.putExtra("contact_email_home", contactCard.getHomeEmail());
            contactCardActivity.putExtra("contact_addr", contactCard.getAddress());
            contactCardActivity.putExtra("contact_url", contactCard.getUrl());
            startActivity(contactCardActivity);
        } else if ((string.startsWith("http://")) || (string.startsWith("https://"))  ) {
            // Runs if the QR code is a Link.
            URLmodel weblink = new URLmodel();
            weblink.setUrl(string);
            //Intent to open a new ativity where weblink is handled
            Intent webLinkActivity = new Intent(getActivity(), WebLinkActivity.class);
            webLinkActivity.putExtra("weblink", weblink.getUrl());
            startActivity(webLinkActivity);

        } else if (string.startsWith("MATMSG:")) {
            // Runs if the QR code is an email
            String[] tokens = string.split(";");
            EMAILmodel email = new EMAILmodel();
            for (String token : tokens) {
                if (token.startsWith("MATMSG:TO:")) {
                    email.setTo(token.substring("MATMSG:TO:".length())); //Remove string Begin
                }
                if (token.startsWith("SUB:")) {
                    email.setSub(token.substring("SUB:".length())); //Remove string Begin
                }
                if (token.startsWith("BODY:")) {
                    email.setBody(token.substring("BODY:".length())); //Remove string Begin
                }
            }
            Intent emailActivity = new Intent(getActivity(), EmailActivity.class);
            emailActivity.putExtra("to", email.getTo());
            emailActivity.putExtra("subject", email.getSub());
            emailActivity.putExtra("body", email.getBody());
            startActivity(emailActivity);

        } else if (string.startsWith("SMSTO:")) {
            // Runs if the QR code is an SMS
            String[] tokens = string.split(":");
            SMSmodel sms = new SMSmodel();
            sms.setPhoneNumber(tokens[1]);
            sms.setSmsBody(tokens[2]);

            Intent smsActivity = new Intent(getActivity(), SmsActivity.class);
            smsActivity.putExtra("number", sms.getPhoneNumber());
            smsActivity.putExtra("sms_body", sms.getSmsBody());
            startActivity(smsActivity);

        } else if (string.startsWith("WIFI:")) {
            //Runs if the QR code is a wifi setting.
            String substring = string.substring("WIFI:".length());
            String[] tokens = substring.split(";");
            WIFImodel wifi_setting = new WIFImodel();
            for (String token : tokens) {
                Log.e("Token",token);
                if (token.startsWith("T:")) {
                    if (token.substring("T:".length()).contentEquals("nopass")) {
                        wifi_setting.setEncryption("No Encryption");
                    } else {
                        wifi_setting.setEncryption(token.substring("T:".length()));
                    }
                }
                if (token.startsWith("S:")) {
                    wifi_setting.setSsid(token.substring("S:".length())); //Remove string Begin
                }
                if (token.startsWith("P:")) {
                    wifi_setting.setPassword(token.substring("P:".length())); //Remove string Begin
                }
                if (token.startsWith("H:")) {
                    if ( (token.substring("H:".length()).isEmpty()) || (token.substring("H:".length()).contentEquals("false")) ) {
                        wifi_setting.setHidden("Not Hidden");
                    } else {
                        if (token.substring("H:".length()).contentEquals("true")) {
                            wifi_setting.setHidden("Hidden Network");
                        }

                    }

                }
            }
            Intent wifiActivity = new Intent(getActivity(), WifiSettingActivity.class);
            wifiActivity.putExtra("encryption_type", wifi_setting.getEncryption());
            wifiActivity.putExtra("ssid", wifi_setting.getSsid());
            wifiActivity.putExtra("password", wifi_setting.getPassword());
            wifiActivity.putExtra("visibility", wifi_setting.getHidden());
            startActivity(wifiActivity);


        }else {
            //Finally just show TEXT if QR code cannot be understood
            Intent resultActivity = new Intent(getActivity(), ResultActivity.class);
            resultActivity.putExtra("qr_result", string);
            startActivity(resultActivity);

        }


    }

    private void saveToSharedPrefData(String rawResult) {
        //Add ScanResult to Arraylist.
        ArrayList<String> arrayListScanResults = getSavedScanResult(getContext());
        if (arrayListScanResults == null)
            arrayListScanResults = new ArrayList<String>();
        arrayListScanResults.add(rawResult);

        //Save Scan Result To Shared Pref
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = getContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonResults = gson.toJson(arrayListScanResults);
        editor.putString(SCAN_RESULT, jsonResults);
        editor.commit();
    }

    public ArrayList<String> getSavedScanResult(Context context) {
        SharedPreferences settings;
        List<String> savedScan;
        settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if (settings.contains(SCAN_RESULT)) {
            String jsonFavorites = settings.getString(SCAN_RESULT, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites,String[].class);
            savedScan = Arrays.asList(favoriteItems);
            savedScan = new ArrayList<String>(savedScan);
        } else
            return null;

        return (ArrayList<String>) savedScan;
    }
}
