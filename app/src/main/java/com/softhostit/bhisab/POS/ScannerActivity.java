package com.softhostit.bhisab.POS;


import android.Manifest;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.utils.BaseActivity;

import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    int currentApiVersion = Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        if (currentApiVersion >= Build.VERSION_CODES.M) {
            requestCameraPermission();
        }

        scannerView = new ZXingScannerView(ScannerActivity.this);
        setContentView(scannerView);
        scannerView.startCamera();
        scannerView.setResultHandler(ScannerActivity.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();

        try {
            ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 150);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            //set result in main activity or previous activity
            PosActivity.etxtSearch.setText(myResult);
            Log.d("QRCodeScanner", result.getText());
            Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        } catch (Exception e){

        }

        //set result in main activity or previous activity
        PosActivity.etxtSearch.setText(myResult);
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

        onBackPressed();


    }

    //Runtime permission
    private void requestCameraPermission() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        scannerView = new ZXingScannerView(ScannerActivity.this);
                        setContentView(scannerView);
                        scannerView.startCamera();
                        scannerView.setResultHandler(ScannerActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            Toasty.info(ScannerActivity.this, R.string.camera_permission, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();
                    }
                }).check();
    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}