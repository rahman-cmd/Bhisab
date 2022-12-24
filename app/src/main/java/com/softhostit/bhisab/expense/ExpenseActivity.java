package com.softhostit.bhisab.expense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;
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

public class ExpenseActivity extends AppCompatActivity {

    RecyclerView expenseRecyclerView;
    List<ExpenseModel> expenseModelList;
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getSupportActionBar().setTitle("Expense");


        expenseRecyclerView = findViewById(R.id.expenseRecyclerView);
        expenseList();


    }

    private void expenseList() {
        // show list of customer here form database
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");

        expenseModelList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EXPENSE_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json array
                            JSONArray array = new JSONArray(response);
                            Log.d("response_array", array.toString());
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    //getting product object from json array
                                    JSONObject productObject = array.getJSONObject(i);
                                    ExpenseModel model = new ExpenseModel();
                                    model.setIn_cat(productObject.getInt("in_cat"));
                                    model.setAccount(productObject.getInt("account"));
                                    model.setDate(productObject.getInt("date"));
                                    model.setDes(productObject.getString("des"));
                                    model.setAmount(productObject.getInt("amount"));

                                    expenseModelList.add(model);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            expenseAdapter = new ExpenseAdapter(getApplicationContext(), expenseModelList);
                            // set adapter to recyclerview
                            expenseRecyclerView.setHasFixedSize(true);
                            expenseRecyclerView.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this));
                            expenseRecyclerView.setAdapter(expenseAdapter);

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
}