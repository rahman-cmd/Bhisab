package com.softhostit.bhisab.deposit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.coustomer.CustomerAdapter;
import com.softhostit.bhisab.coustomer.CustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DepositActivity extends AppCompatActivity {

    private Context context;
    List<DepositModel> depositModels;
    private DepositAdapter depositAdapter;
    ProgressBar progressBar;
    ImageView noData;

    RecyclerView deposit_recycler_view;
    FloatingActionButton addDeposit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_activity);

        getSupportActionBar().setTitle("Deposit");

        deposit_recycler_view = findViewById(R.id.deposit_recycler_view);
        progressBar = findViewById(R.id.progressBar);
        noData = findViewById(R.id.noData);
        addDeposit = findViewById(R.id.addDeposit);

        addDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        depositList();
        coustomerList();


    }

    private void showAlertDialog() {
        // show dialog
        final Dialog dialog = new Dialog(DepositActivity.this);
        dialog.setContentView(R.layout.add_category_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        EditText addCategory = dialog.findViewById(R.id.addCategory);
        Button addCategoryBtn = dialog.findViewById(R.id.addCategoryBtn);

        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = addCategory.getText().toString().trim();
                if (category.isEmpty()) {
                    addCategory.setError("Enter Deposit Category");
                    addCategory.requestFocus();
                    return;
                }
                addCategory(category);
                dialog.dismiss();
            }
        });
    }

    private void addCategory(String category) {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CATEGORY_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
                        Toasty.success(DepositActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                        depositList();
                    } else {
                        Toasty.error(DepositActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(DepositActivity.this, "Error: " + e.toString(), Toasty.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(DepositActivity.this, "Error: " + error.toString(), Toasty.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", category);
                params.put("auto_select", "");
                params.put("no_delete", "");
                params.put("user_id", user_id + "");
                params.put("ct", "");
                params.put("username", username);
                params.put("domain", domain);
                return params;
            }
        };
        VolleySingleton.getInstance(DepositActivity.this).addToRequestQueue(stringRequest);
    }

    private void coustomerList() {
        // show list of customer here form database
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        // add data to array list
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CLIENT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json array
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    //getting product object from json array
                                    JSONObject productObject = array.getJSONObject(i);

                                    int id_client = productObject.getInt("id");
                                    String fname = productObject.getString("fname");
                                    Toasty.info(DepositActivity.this, id_client + "" + fname, Toasty.LENGTH_SHORT).show();



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasty.error(getApplicationContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("username", username);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    private void depositList() {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);

        // show data in recycler view
        depositModels = new ArrayList<>();
        // add data to array list
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.DEPOSIT_LIST,
                // show circular progress bar
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        deposit_recycler_view.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if(jsonArray.length() == 0){
                                noData.setVisibility(View.VISIBLE);
                            }
                            else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DepositModel depositModel = new DepositModel();
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    depositModel.setId(dataobj.getInt("id"));
                                    depositModel.setTime(dataobj.getInt("time"));
                                    depositModel.setAccount(dataobj.getString("account"));
                                    depositModel.setDate(dataobj.getInt("date"));
                                    depositModel.setAmount(dataobj.getInt("amount"));
                                    depositModel.setUser_id(dataobj.getInt("user_id"));
                                    depositModel.setPayer(dataobj.getInt("payer"));

//                                    if (payer == client_id) {
//                                        depositModel.setFname(dataobj.getString("payer"));
//                                    } else {
//                                       Toasty.error(DepositActivity.this, "Error: " + "No data found", Toasty.LENGTH_SHORT).show();
//                                    }

                                    depositModel.setIn_cat(dataobj.getString("in_cat"));
                                    depositModel.setDes(dataobj.getString("des"));
                                    depositModel.setDomain(dataobj.getString("domain"));
                                    depositModels.add(depositModel);
                                }
                            }


                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            depositAdapter = new DepositAdapter(DepositActivity.this, depositModels);
                            // set adapter to recyclerview
                            deposit_recycler_view.setHasFixedSize(true);

                            // set LayoutManager to RecyclerView
                            deposit_recycler_view.setLayoutManager(new LinearLayoutManager(DepositActivity.this));

                            deposit_recycler_view.setAdapter(depositAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(DepositActivity.this, "Error 1: " + e.toString(), Toasty.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        deposit_recycler_view.setVisibility(View.VISIBLE);

                        Toasty.error(DepositActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("username", username);
                params.put("user_id", user_id + "");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}