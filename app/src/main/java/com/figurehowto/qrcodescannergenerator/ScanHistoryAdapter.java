package com.figurehowto.qrcodescannergenerator;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.figurehowto.qrcodescannergenerator.Model.EMAILmodel;
import com.figurehowto.qrcodescannergenerator.Model.SMSmodel;
import com.figurehowto.qrcodescannergenerator.Model.URLmodel;
import com.figurehowto.qrcodescannergenerator.Model.VCARDmodel;
import com.figurehowto.qrcodescannergenerator.Model.WIFImodel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder> {
    Context context;
    Activity activity;
    SharedPreference sharedPreference = new SharedPreference();
    private ArrayList<String> arrayListScanResults;
    private View view;

    public ScanHistoryAdapter(Context context, Activity activity, ArrayList<String> arrayListScanResults) {
        this.context = context;
        this.activity = activity;
        this.arrayListScanResults = arrayListScanResults;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_history_format, parent, false);
        return new ScanHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        int resultNumber =(arrayListScanResults.size()) - (holder.getLayoutPosition());
        holder.scanResultNumber.setText(String.format("%s. ", String.valueOf(resultNumber)));

        holder.scan_result.setText(String.valueOf(arrayListScanResults.get(position)));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultToDelete = arrayListScanResults.get(position);
                arrayListScanResults.remove(arrayListScanResults.indexOf(resultToDelete));
                sharedPreference.removeScanResult(activity, resultToDelete);
                notifyDataSetChanged();

                Toast.makeText(context, "Deleted ", Toast.LENGTH_LONG).show();
            }
        });


        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", String.valueOf(arrayListScanResults.get(position)));
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied", Toast.LENGTH_LONG).show();
            }
        });

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        processRawResult(String.valueOf(arrayListScanResults.get(position)));
    }
});
    }

    @Override
    public int getItemCount() {
        return arrayListScanResults.size();
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
            Intent contactCardActivity = new Intent(context, ContactCardActivity.class);
            contactCardActivity.putExtra("contact_name", contactCard.getName());
            contactCardActivity.putExtra("contact_title", contactCard.getTitle());
            contactCardActivity.putExtra("contact_org", contactCard.getOrg());
            contactCardActivity.putExtra("contact_tel", contactCard.getTelephone());
            contactCardActivity.putExtra("contact_email", contactCard.getEmail());
            contactCardActivity.putExtra("contact_email_home", contactCard.getHomeEmail());
            contactCardActivity.putExtra("contact_addr", contactCard.getAddress());
            contactCardActivity.putExtra("contact_url", contactCard.getUrl());
            context.startActivity(contactCardActivity);
        } else if ((string.startsWith("http://")) || (string.startsWith("https://"))  ) {
            // Runs if the QR code is a Link.
            URLmodel weblink = new URLmodel();
            weblink.setUrl(string);
            //Intent to open a new ativity where weblink is handled
            Intent webLinkActivity = new Intent(context, WebLinkActivity.class);
            webLinkActivity.putExtra("weblink", weblink.getUrl());
            context.startActivity(webLinkActivity);

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
            Intent emailActivity = new Intent(context, EmailActivity.class);
            emailActivity.putExtra("to", email.getTo());
            emailActivity.putExtra("subject", email.getSub());
            emailActivity.putExtra("body", email.getBody());
            context.startActivity(emailActivity);

        } else if (string.startsWith("SMSTO:")) {
            // Runs if the QR code is an SMS
            String[] tokens = string.split(":");
            SMSmodel sms = new SMSmodel();
            sms.setPhoneNumber(tokens[1]);
            sms.setSmsBody(tokens[2]);

            Intent smsActivity = new Intent(context, SmsActivity.class);
            smsActivity.putExtra("number", sms.getPhoneNumber());
            smsActivity.putExtra("sms_body", sms.getSmsBody());
            context.startActivity(smsActivity);

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
            Intent wifiActivity = new Intent(context, WifiSettingActivity.class);
            wifiActivity.putExtra("encryption_type", wifi_setting.getEncryption());
            wifiActivity.putExtra("ssid", wifi_setting.getSsid());
            wifiActivity.putExtra("password", wifi_setting.getPassword());
            wifiActivity.putExtra("visibility", wifi_setting.getHidden());
            context.startActivity(wifiActivity);


        }else {
            //Finally just show TEXT if QR code cannot be understood
            Intent resultActivity = new Intent(context, ResultActivity.class);
            resultActivity.putExtra("qr_result", string);
            context.startActivity(resultActivity);

        }


    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView delete, copy;
        TextView scanResultNumber, scan_result;

        public ViewHolder(View itemView) {
            super(itemView);


            delete = (ImageView) itemView.findViewById(R.id.delete);
            copy = (ImageView) itemView.findViewById(R.id.copy);
            scanResultNumber = (TextView) itemView.findViewById(R.id.scanResultNumber);
            scan_result = (TextView) itemView.findViewById(R.id.scan_res);

        }

    }

    public class SharedPreference {

        public static final String PREFS_NAME = "QRCODE_SCANNER_GEN_APP";
        public static final String SCAN_RESULT = "Scan_Result";

        public SharedPreference() {
            super();
        }

        // This four methods are used for maintaining Saved Results.
        public void saveScanResult(Context context, ArrayList<String> scanResults) {
            SharedPreferences settings;
            SharedPreferences.Editor editor;
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();
            Gson gson = new Gson();
            String jsonResults = gson.toJson(scanResults);
            editor.putString(SCAN_RESULT, jsonResults);
            editor.commit();
        }

        public void addScanResult(Context context, String rawResult) {
            ArrayList<String> favorites = getSavedScanResult(context);
            if (favorites == null)
                favorites = new ArrayList<String>();
            favorites.add(rawResult);
            saveScanResult(context, favorites);
        }

        public void removeScanResult(Context context, String result) {
            ArrayList<String> scanResults = getSavedScanResult(context);
            if (scanResults != null) {
                scanResults.remove(result);
                saveScanResult(context, scanResults);
            }
        }

        public ArrayList<String> getSavedScanResult(Context context) {
            SharedPreferences settings;
            List<String> savedScan;
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (settings.contains(SCAN_RESULT)) {
                String jsonFavorites = settings.getString(SCAN_RESULT, null);
                Gson gson = new Gson();
                String[] favoriteItems = gson.fromJson(jsonFavorites, String[].class);
                savedScan = Arrays.asList(favoriteItems);
                savedScan = new ArrayList<String>(savedScan);
            } else
                return null;

            return (ArrayList<String>) savedScan;
        }
    }

}
