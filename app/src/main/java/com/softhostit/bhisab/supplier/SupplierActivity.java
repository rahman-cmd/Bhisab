package com.softhostit.bhisab.supplier;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.coustomer.CoustomerActivity;
import com.softhostit.bhisab.coustomer.CustomerAdapter;
import com.softhostit.bhisab.coustomer.CustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SupplierActivity extends AppCompatActivity {
    private Context context;
    List<SupplierModel> supplierModelList;
    private SupplierAdapter supplierAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    FloatingActionButton add_fab, add_supplier_fab, add_supplier_group_fab;

    // These are taken to make visible and invisible along with FABs
    TextView add_supplier_action_text, add_supplier_group_action_text;

    // to check whether sub FAB buttons are visible or not.
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        getSupportActionBar().setTitle("Supplier");

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        add_fab = findViewById(R.id.add_fab);
        add_supplier_fab = findViewById(R.id.add_supplier_fab);
        add_supplier_group_fab = findViewById(R.id.add_supplier_group_fab);
        add_supplier_action_text = findViewById(R.id.add_supplier_action_text);
        add_supplier_group_action_text = findViewById(R.id.add_supplier_group_action_text);


        // Now set all the FABs and all the action name texts as GONE
        add_supplier_fab.setVisibility(View.GONE);
        add_supplier_group_fab.setVisibility(View.GONE);
        add_supplier_action_text.setVisibility(View.GONE);
        add_supplier_group_action_text.setVisibility(View.GONE);

        isAllFabsVisible = false;

        add_fab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                add_supplier_fab.show();
                add_supplier_group_fab.show();
                add_supplier_action_text.setVisibility(View.VISIBLE);
                add_supplier_group_action_text.setVisibility(View.VISIBLE);
                isAllFabsVisible = true;
            } else {
                add_supplier_fab.hide();
                add_supplier_group_fab.hide();
                add_supplier_action_text.setVisibility(View.GONE);
                add_supplier_group_action_text.setVisibility(View.GONE);
                isAllFabsVisible = false;
            }
        });

        add_supplier_fab.setOnClickListener(view -> {


        });

        add_supplier_group_fab.setOnClickListener(view -> {

        });

        supplierList();
    }

    private void supplierList() {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");

        // show data in recyclerview
        supplierModelList  = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SUPPLIER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        try {
                            //converting response to json array
                            JSONArray array = new JSONArray(response);
                            Log.d("supplier", response);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    //getting product object from json array
                                    JSONObject productObject = array.getJSONObject(i);
                                    SupplierModel model = new SupplierModel();


                                    model.setId((productObject.getInt("id")));
                                    model.setName(productObject.getString("fname"));
                                    model.setPhone(productObject.getString("phone1"));
                                    model.setCname(productObject.getString("cname"));
                                    model.setAddress(productObject.getString("address"));
                                    supplierModelList.add(model);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            supplierAdapter = new SupplierAdapter(getApplicationContext(), supplierModelList);
                            // set adapter to recyclerview
                            recyclerView.setHasFixedSize(true);
//                        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            recyclerView.setAdapter(supplierAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
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
}