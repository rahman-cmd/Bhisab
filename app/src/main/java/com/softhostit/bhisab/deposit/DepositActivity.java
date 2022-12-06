package com.softhostit.bhisab.deposit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softhostit.bhisab.Constant;
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

    RecyclerView deposit_recycler_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_activity);

        deposit_recycler_view = findViewById(R.id.deposit_recycler_view);
        progressBar = findViewById(R.id.progressBar);

        categoryList();


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
                                Toasty.error(DepositActivity.this, "No data found", Toasty.LENGTH_SHORT).show();
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