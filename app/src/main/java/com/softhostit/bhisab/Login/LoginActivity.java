package com.softhostit.bhisab.Login;


import static com.softhostit.bhisab.R.id;
import static com.softhostit.bhisab.R.layout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.HomeActivity;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.model.Login;
import com.softhostit.bhisab.networking.ApiClient;
import com.softhostit.bhisab.networking.ApiInterface;
import com.softhostit.bhisab.register.RegisterActivity;
import com.softhostit.bhisab.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    EditText domainName, etxDomain, etxUsername, etxPassword;
    SharedPreferences sp;
    ProgressDialog loading;
    Utils utils;

    MaterialButton loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);






        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        loginBtn = findViewById(id.loginBtn);
        registerBtn = findViewById(id.registerBtn);

        etxDomain = findViewById(id.domainName);
        etxUsername = findViewById(id.username);
        etxPassword = findViewById(id.password);
        utils = new Utils();

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String domain = sp.getString(Constant.KEY_DOMAIN, "");
        String username = sp.getString(Constant.KEY_USERNAME, "");
        String password = sp.getString(Constant.SP_PASSWORD, "");


        etxDomain.setText(domain);
        etxUsername.setText(username);
        etxPassword.setText(password);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        loginBtn.setOnClickListener(v -> {
            String domain1 = etxDomain.getText().toString();
            String username1 = etxUsername.getText().toString();
            String password1 = etxPassword.getText().toString();



            if (domain1.isEmpty()) {
                etxDomain.setError(getString(R.string.enter_valid_domain));
                etxDomain.requestFocus();
            } else if (username1.isEmpty()) {
                etxUsername.setError(getString(R.string.enter_valid_username));
                etxUsername.requestFocus();
            } else if (password1.isEmpty()) {
                etxPassword.setError(getString(R.string.please_enter_password));
                etxPassword.requestFocus();
            } else {

                if (utils.isNetworkAvailable(LoginActivity.this)) {
                    userLogin();

                } else {
                    Toasty.error(this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void userLogin() {
        //first getting the values
        final String domain = etxDomain.getText().toString();
        final String username = etxUsername.getText().toString();
        final String password = etxPassword.getText().toString();
        final String apiKey = "83GnEHvvM60C";

        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.d("response", response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                loading.dismiss();
                                Toasty.success(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                JSONObject companyJson = obj.getJSONObject("company");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("domain"),
                                        userJson.getString("username"),
                                        userJson.getString("password"),
                                        companyJson.getString("name")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                loading.dismiss();
                                Toasty.error(LoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toasty.error(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}