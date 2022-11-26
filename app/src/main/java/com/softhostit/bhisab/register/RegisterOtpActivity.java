package com.softhostit.bhisab.register;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softhostit.bhisab.R;

import es.dmoral.toasty.Toasty;

public class RegisterOtpActivity extends AppCompatActivity {
    TextView randomNumber, otpNumber, resendCode;
    Button otptn;
    EditText otp1, otp2, otp3, otp4;

    // true after every 10 minutes
    private boolean resendEnabled = false;

    // resend time in seconds
    private int resendTime = 60;
    private int selectedPosition = 0;

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

        // random otp generate
        int randomPIN = (int) (Math.random() * 1000);
        String otps = String.format("%04d", randomPIN);

        randomNumber = findViewById(R.id.randomNumber);
        resendCode = findViewById(R.id.resendCode);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);

        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);

        // default open keyboard otp1
        showKeyboard(otp1);
        startCountDownTimer();

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendEnabled) {
                    resendCode.setEnabled(false);
                    resendCode.setText("Resend code in " + resendTime + " seconds");
                    startCountDownTimer();
                    randomNumber.setText(otps);

                    Toasty.success(RegisterOtpActivity.this, otps, Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.error(RegisterOtpActivity.this, "Please wait for 10 minutes", Toasty.LENGTH_SHORT).show();
                }
            }
        });


        randomNumber.setText(otp);

        otpNumber = findViewById(R.id.otpNumber);



        otptn = findViewById(R.id.otptn);
        otpNumber.setText(mobileNumber);



        // button click match otp
        otptn.setOnClickListener(v -> {
            final  String allOtptext = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();

            if (allOtptext.length() == 4) {
                if (allOtptext.equals(otp) || allOtptext.equals(otps)) {
                   Toasty.success(RegisterOtpActivity.this, "OTP Matched", Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.error(RegisterOtpActivity.this, "Invalid OTP", Toasty.LENGTH_SHORT).show();
                }
            } else {
                Toasty.error(RegisterOtpActivity.this, "Please enter valid OTP", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void showKeyboard(EditText editText) {
        editText.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer(){
        resendEnabled = false;
        resendCode.setTextColor(getResources().getColor(R.color.grey));

        new CountDownTimer(resendTime * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                resendCode.setText("Resend Code in " + millisUntilFinished / 1000 + "s");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                resendCode.setText("Resend Code");
                resendCode.setTextColor(getResources().getColor(R.color.white));
                resendEnabled = true;
            }
        }.start();
    }



    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if (selectedPosition == 0){
                    selectedPosition = 1;
                    showKeyboard(otp2);
                }
                else if (selectedPosition == 1){
                    selectedPosition = 2;
                    showKeyboard(otp3);
                }
                else if (selectedPosition == 2){
                    selectedPosition = 3;
                    showKeyboard(otp4);
                }
                else if (selectedPosition == 3){
                    selectedPosition = 3;
                    showKeyboard(otp4);
                }
            }

        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
       if (keyCode == KeyEvent.KEYCODE_DEL){
           if (selectedPosition == 3) {
               selectedPosition = 2;
               showKeyboard(otp3);
           }

           else if (selectedPosition == 2) {
               selectedPosition = 1;
               showKeyboard(otp2);
           }

           else if (selectedPosition == 1) {
               selectedPosition = 0;
               showKeyboard(otp1);
           }

           else if (selectedPosition == 0) {
               selectedPosition = 0;
               showKeyboard(otp1);
           }
           return true;
       } else{
              return super.onKeyUp(keyCode, event);
       }
    }
}