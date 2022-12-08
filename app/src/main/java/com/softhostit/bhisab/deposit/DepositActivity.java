package com.softhostit.bhisab.deposit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        categoryList();


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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
                        Toasty.success(DepositActivity.this, "Deposit Category Added Successfully", Toasty.LENGTH_SHORT).show();
                        categoryList();
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
                params.put("ct", "");
                params.put("username", username);
                params.put("domain", domain);
                return params;
            }
        };
        VolleySingleton.getInstance(DepositActivity.this).addToRequestQueue(stringRequest);
    }

    private void categoryList() {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");

        // show data in recycler view
        depositModels = new ArrayList<>();
        // add data to array list
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CATEGORY_ITEM,
                // show circular progress bar
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        deposit_recycler_view.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray dataArray = obj.getJSONArray("categories");

                            if(dataArray.length() == 0){
                                noData.setVisibility(View.VISIBLE);
                            }
                            else {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    DepositModel depositModel = new DepositModel();
                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                    depositModel.setId(dataobj.getInt("id"));
                                    depositModel.setName(dataobj.getString("name"));
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
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}