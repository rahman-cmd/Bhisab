package com.softhostit.bhisab.register;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softhostit.bhisab.R;

import es.dmoral.toasty.Toasty;

public class RegisterOtpActivity extends AppCompatActivity {
    TextView randomNumber, otpNumber;
    Button otptn;
    EditText otpverification;

    @SuppressLint("SetTextI18n")
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
        otpverification = findViewById(R.id.otpverification);
        otpNumber = findViewById(R.id.otpNumber);



        otptn = findViewById(R.id.otptn);
        randomNumber.setText(otp);
        otpNumber.setText(mobileNumber);



        // button click match otp
        otptn.setOnClickListener(v -> {
            String otpNumber = otpverification.getText().toString();
            if (otpNumber.equals(otp)) {
                Toasty.success(RegisterOtpActivity.this, "OTP Matched", Toasty.LENGTH_SHORT).show();
            } else {
                Toasty.error(RegisterOtpActivity.this, "OTP Not Matched", Toasty.LENGTH_SHORT).show();
            }
        });
    }
}