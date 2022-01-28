package com.figurehowto.qrcodescannergenerator;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class OutputGeneratedQR extends AppCompatActivity {
    String TAG = "GenerateQRcode";
    Button button;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    ImageView qrImg;
    TextView imageName;
    File shareQRfile;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_generated_qr);
        //Load Ad
        mAdView = findViewById(R.id.generated_output_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-1234502077623540/5852730086", adRequest, new InterstitialAdLoadCallback() {
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

        if (mInterstitialAd != null) {
            mInterstitialAd.show(OutputGeneratedQR.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

        //Get Intents
        final String qrCodeString = getIntent().getStringExtra("qrCodeString");
        final String fileName = getIntent().getStringExtra("fileName");

        //Link Views XML to Java code
        qrImg = (ImageView) findViewById(R.id.generatedQR);
        imageName = (TextView) findViewById(R.id.file_name);
        final Button saveQR = (Button) findViewById(R.id.save_qr);
        final Button shareQR = (Button) findViewById(R.id.share_qr);
        imageName.setText(String.format("%sQR Code", fileName));

        //Generate QR code if there is a Data Available.
        if (!qrCodeString.isEmpty()) {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    qrCodeString, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.getBitmap();
                qrImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.v(TAG, e.toString());
            }

            saveQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File  mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "QRscannerGenerator");
                    // Create the storage directory if it does not exist
                    if (! mediaStorageDir.exists()){
                        mediaStorageDir.mkdirs();
                    }
                    // Create a media file name
                    final File mediaFile;
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator + new StringBuilder(fileName).append(".PNG").toString());

                    Dexter.withActivity(OutputGeneratedQR.this)
                            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    boolean save;
                                    String result;
                                    try {
                                        FileOutputStream fos = new FileOutputStream(mediaFile);
                                        save = bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                                        fos.close();
                                        result = save ? "Image Saved" : "Image Not Saved";
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                    } catch (FileNotFoundException e) {
                                        Log.d(TAG, "File not found: " + e.getMessage());
                                    } catch (IOException e) {
                                        Log.d(TAG, "Error accessing file: " + e.getMessage());
                                    }


                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                    Toast.makeText(OutputGeneratedQR.this, "Unable to Save QR without Permission", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                }
                            }).check();

                }
            });

            shareQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(OutputGeneratedQR.this, "Clicked", Toast.LENGTH_LONG).show();
                    try {
                         shareQRfile = new File(getExternalCacheDir(),"temp.png");
                        FileOutputStream fOut = new FileOutputStream(shareQRfile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fOut);
                        fOut.flush();
                        fOut.close();
                        shareQRfile.setReadable(true, false);
                        Uri uri = Uri.fromFile(shareQRfile);
                        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try{
                                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                m.invoke(null);
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setType("image/png");
                        startActivity(Intent.createChooser(intent, "Share image via"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(shareQRfile.exists()){
            shareQRfile.delete();
        }
    }
}
