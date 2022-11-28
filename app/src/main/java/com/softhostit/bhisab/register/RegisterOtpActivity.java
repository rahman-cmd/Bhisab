package com.softhostit.bhisab.register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.HomeActivity;
import com.softhostit.bhisab.Login.LoginActivity;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.SmsBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class RegisterOtpActivity extends AppCompatActivity {
    TextView otpNumber, resendCode;
    Button otptn;

    ProgressDialog loading;

    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
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
        String mobileNumber = intent.getStringExtra("mobile");
        String username = intent.getStringExtra("username");
        String company_name = intent.getStringExtra("company_name");
        String password = intent.getStringExtra("password");

        // random otp generate
        int randomPIN = (int) (Math.random() * 1000);
        String otps = String.format("%04d", randomPIN);

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

        // resend code from api url
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendEnabled) {
                    resendCode.setEnabled(false);
                    resendCode.setText("Resend code in " + resendTime + " seconds");
                    startCountDownTimer();

                    // send otp to server
                    RequestQueue queue = Volley.newRequestQueue(RegisterOtpActivity.this);
                    String url = "https://smsfrom.net/api/sent/compose?api_key=17|K7rNA5OMa8HOO8b556SoxIQvxZSf5MUWiD1cQIQt&from_type=sender_id&from_number=&sender_id=8&to_numbers=" + mobileNumber + "&body= বি-হিসাব মোবাইল এপ্লিকেশনে আপনাকে অভিন্দন " + otps + " www.bhisab.com" + " হিসাব হোক সহজ স্বচ্ছ ও ঝামেলা বিহীন";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toasty.error(RegisterOtpActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                } else {
                    Toasty.error(RegisterOtpActivity.this, "Please wait for 10 minutes", Toasty.LENGTH_SHORT).show();
                }
            }
        });

        otpNumber = findViewById(R.id.otpNumber);
        otptn = findViewById(R.id.otptn);
        otpNumber.setText(mobileNumber);

        startSmartUserConsent();



        // button click match otp and go to home activity
        otptn.setOnClickListener(v -> {

            loading = new ProgressDialog(this);
            loading.setCancelable(false);
            loading.setMessage(getString(R.string.please_wait));
            loading.show();

            final String allOtptext = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();

            if (allOtptext.length() == 4) {
                if (allOtptext.equals(otp) || allOtptext.equals(otps)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_REGISTER, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // show dialog
                            Log.d("log_data", response);
                            loading.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    loading.dismiss();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                } else {
                                    Toasty.error(RegisterOtpActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toasty.error(RegisterOtpActivity.this, "Register Error", Toasty.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("mobile", mobileNumber);
                            params.put("username", username);
                            params.put("company_name", company_name);
                            params.put("password", password);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(stringRequest);

//                    Toasty.success(RegisterOtpActivity.this, "OTP Matched", Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.error(RegisterOtpActivity.this, "Invalid OTP", Toasty.LENGTH_SHORT).show();
                }
            } else {
                Toasty.error(RegisterOtpActivity.this, "Please enter valid OTP", Toasty.LENGTH_SHORT).show();
            }
        });
    }


    // otp set text start

    private void showKeyboard(EditText editText) {
        editText.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer() {
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
                if (selectedPosition == 0) {
                    selectedPosition = 1;
                    showKeyboard(otp2);
                } else if (selectedPosition == 1) {
                    selectedPosition = 2;
                    showKeyboard(otp3);
                } else if (selectedPosition == 2) {
                    selectedPosition = 3;
                    showKeyboard(otp4);
                } else if (selectedPosition == 3) {
                    selectedPosition = 3;
                    showKeyboard(otp4);
                }
            }

        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (selectedPosition == 3) {
                selectedPosition = 2;
                showKeyboard(otp3);
            } else if (selectedPosition == 2) {
                selectedPosition = 1;
                showKeyboard(otp2);
            } else if (selectedPosition == 1) {
                selectedPosition = 0;
                showKeyboard(otp1);
            } else if (selectedPosition == 0) {
                selectedPosition = 0;
                showKeyboard(otp1);
            }
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    // otp set text end

    // use for otp read start
    private void startSmartUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT) {

            if ((resultCode == RESULT_OK) && (data != null)) {

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);


            }


        }

    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()) {

            String otp = matcher.group(0);
            otp1.setText(String.valueOf(otp.charAt(0)));
            otp2.setText(String.valueOf(otp.charAt(1)));
            otp3.setText(String.valueOf(otp.charAt(2)));
            otp4.setText(String.valueOf(otp.charAt(3)));

        }


    }

    private void registerBroadcastReceiver() {

        smsBroadcastReceiver = new SmsBroadcastReceiver();

        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent, REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }
    // use for otp read end
}