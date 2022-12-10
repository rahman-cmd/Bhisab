package com.softhostit.bhisab.coustomer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class CoustomerActivity extends AppCompatActivity {

    private Context context;
    List<CustomerModel> customerModelList;
    private CustomerAdapter customerAdapter;
    RecyclerView customerRecyclerView;
    ProgressBar progressBarCustomer;

    FloatingActionButton mAddFab, add_customer_fab, add_customer_group_fab;

    // These are taken to make visible and invisible along with FABs
    TextView add_customer_action_text, add_customer_group_action_text;

    // to check whether sub FAB buttons are visible or not.
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coustomer);
        getSupportActionBar().setTitle("Customer");

        customerRecyclerView = findViewById(R.id.customerRecyclerView);
        progressBarCustomer = findViewById(R.id.progressBarCustomer);

        coustomerList();
        mAddFab = findViewById(R.id.add_fab);

        // FAB button
        add_customer_fab = findViewById(R.id.add_customer_fab);
        add_customer_group_fab = findViewById(R.id.add_customer_group_fab);

        // Also register the action name text, of all the FABs.
        add_customer_action_text = findViewById(R.id.add_customer_action_text);
        add_customer_group_action_text = findViewById(R.id.add_customer_group_action_text);

        // Now set all the FABs and all the action name texts as GONE
        add_customer_fab.setVisibility(View.GONE);
        add_customer_group_fab.setVisibility(View.GONE);
        add_customer_action_text.setVisibility(View.GONE);
        add_customer_group_action_text.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mAddFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                add_customer_fab.show();
                add_customer_group_fab.show();
                add_customer_action_text.setVisibility(View.VISIBLE);
                add_customer_group_action_text.setVisibility(View.VISIBLE);
                isAllFabsVisible = true;
            } else {
                add_customer_fab.hide();
                add_customer_group_fab.hide();
                add_customer_action_text.setVisibility(View.GONE);
                add_customer_group_action_text.setVisibility(View.GONE);
                isAllFabsVisible = false;
            }
        });

        add_customer_group_fab.setOnClickListener(
                view -> Toast.makeText(getApplicationContext(), "Person Added", Toast.LENGTH_SHORT
                ).show());


        add_customer_fab.setOnClickListener(
                view -> Toast.makeText(getApplicationContext(), "Alarm Added", Toast.LENGTH_SHORT
                ).show());
    }



    private void coustomerList() {
        // show list of customer here form database
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");

        // show data in recycler view
        customerModelList = new ArrayList<>();
        // add data to array list
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CLIENT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBarCustomer.setVisibility(View.GONE);
                        customerRecyclerView.setVisibility(View.VISIBLE);
                        try {
                            //converting response to json array
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    //getting product object from json array
                                    JSONObject productObject = array.getJSONObject(i);
                                    CustomerModel model = new CustomerModel();


                                    model.setId((productObject.getInt("id")));
                                    model.setFname(productObject.getString("fname"));
                                    model.setCname(productObject.getString("cname"));
                                    model.setPhone1(productObject.getString("phone1"));
                                    model.setUser_id(productObject.getInt("user_id"));
                                    model.setPhoto(productObject.getString("photo"));
                                    model.setDomain(productObject.getString("domain"));
                                    customerModelList.add(model);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            customerAdapter = new CustomerAdapter(getApplicationContext(), customerModelList);
                            // set adapter to recyclerview
                            customerRecyclerView.setHasFixedSize(true);
//                        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            customerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            customerRecyclerView.setAdapter(customerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarCustomer.setVisibility(View.GONE);
                        customerRecyclerView.setVisibility(View.VISIBLE);
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