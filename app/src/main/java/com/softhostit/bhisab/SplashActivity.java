package com.softhostit.bhisab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.softhostit.bhisab.Login.LoginActivity;
import com.softhostit.bhisab.utils.Utils;

import es.dmoral.toasty.Toasty;


public class SplashActivity extends AppCompatActivity {


    public static int splashTimeOut = 2000;
    Utils utils;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        utils = new Utils();

        progressBar = findViewById(R.id.progressBar);

        // no internet connection dialog
        // check internet connection
        // if no internet connection
        // show dialog
        // if internet connection
        // hide dialog
        if (utils.isNetworkAvailable(SplashActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, splashTimeOut);

        } else {
            progressBar.setVisibility(View.GONE);
            // show no internet dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please check your internet connection and try again");
            builder.setCancelable(false);
            builder.setPositiveButton("Retry", (dialog, which) -> {
               //  internet connection refresh
                recreate();
            });
            builder.show();


        }


    }
}

