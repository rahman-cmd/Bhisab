package com.softhostit.bhisab.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.softhostit.bhisab.R;

public class RegisterOtpActivity extends AppCompatActivity {
    TextView randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otp);

        Intent intent = getIntent();
        String otp = intent.getStringExtra("otp");
        String mobileNumber = intent.getStringExtra("mobileNumber");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        randomNumber = findViewById(R.id.randomNumber);
        randomNumber.setText(otp);
    }
}