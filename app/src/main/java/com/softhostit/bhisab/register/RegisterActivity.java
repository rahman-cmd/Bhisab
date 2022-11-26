package com.softhostit.bhisab.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.HomeActivity;
import com.softhostit.bhisab.Login.LoginActivity;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    Button loginBtn, registerBtn;
    EditText registerMobileNumber, registerUsername, registerPassword;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        registerMobileNumber = findViewById(R.id.registerMobileNumber);
        registerUsername = findViewById(R.id.registerUsername);
        registerPassword = findViewById(R.id.registerPassword);
        utils = new Utils();

        // button click random number generate


        registerBtn.setOnClickListener(v -> {
            String mobileNumber = registerMobileNumber.getText().toString();
            String username = registerUsername.getText().toString();
            String password = registerPassword.getText().toString();

            if (mobileNumber.isEmpty()) {
                registerMobileNumber.setError("Mobile Number is required");
                registerMobileNumber.requestFocus();
                return;
            } else if (username.isEmpty()) {
                registerUsername.setError("Username is required");
                registerUsername.requestFocus();
                return;
            } else if (password.isEmpty()) {
                registerPassword.setError("Password is required");
                registerPassword.requestFocus();
                return;
            } else if (password.length() < 6) {
                registerPassword.setError("Password must be at least 6 characters");
                registerPassword.requestFocus();
                return;
            } else {
                if (utils.isNetworkAvailable(RegisterActivity.this)) {
                    userRegister();

                } else {
                    Toasty.error(this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                }
            }


        });


        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void userRegister() {
        String mobileNumber = registerMobileNumber.getText().toString();
        String username = registerUsername.getText().toString();
        String password = registerPassword.getText().toString();


        // button click 6 digit random number generate
        int randomPIN = (int) (Math.random() * 1000);
        String otp = String.format("%04d", randomPIN);

        // send otp to server
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://smsfrom.net/api/sent/compose?api_key=17|K7rNA5OMa8HOO8b556SoxIQvxZSf5MUWiD1cQIQt&from_type=sender_id&from_number=&sender_id=8&to_numbers=" + mobileNumber + "&body= বিহিসাব মোবাইল এপ্লিকেশনে আপনাকে অভিন্দন " + otp + "www.bhisab.com" + " হিসাব হোক সহজ স্বচ্ছ ও ঝামেলা বিহীন";

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
                Toasty.error(RegisterActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


//        user registration otp verification

        if (utils.isNetworkAvailable(RegisterActivity.this)) {
            Intent intent = new Intent(RegisterActivity.this, RegisterOtpActivity.class);
            intent.putExtra("mobileNumber", mobileNumber);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            intent.putExtra("otp", otp);
            startActivity(intent);
        } else {
            Toasty.error(this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
        }


    }
}