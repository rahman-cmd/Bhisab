package com.softhostit.bhisab.POS;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class PosActivity extends AppCompatActivity {

    List<ProductModel> productModelList;
    private ProductAdapter productAdapter;
    private RecyclerView product_list_recycler_view;
    ProgressBar progressBar;

    private static String JSON_URL = "https://mocki.io/v1/1fd35679-bddf-4fc2-8621-a666f88e35c0";


    ImageView imgScanner, img_back, img_cart;
    public static EditText etxtSearch;
    public static TextView txtCount;

    DatabaseHelper cartDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        // hide action bar
        getSupportActionBar().hide();

        cartDB = new DatabaseHelper(this);


        progressBar = findViewById(R.id.progressBar);
        etxtSearch = findViewById(R.id.etxt_search);
        imgScanner = findViewById(R.id.img_scanner);
        product_list_recycler_view = findViewById(R.id.product_list_recycler_view);
        img_back = findViewById(R.id.img_back);
        img_cart = findViewById(R.id.img_cart);
        txtCount = findViewById(R.id.txt_count);

//        txtCount.setText(cartDB.getCountCard() + "");

        imgScanner.setOnClickListener(v -> {
            Intent intent = new Intent(PosActivity.this, ScannerActivity.class);
            startActivity(intent);
        });

        img_back.setOnClickListener(v -> {
            finish();
        });


        // show add to cart count from database


        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");


        etxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                s = s.toString().toLowerCase();
                ArrayList<ProductModel> filteredList = new ArrayList<>();

                //search data from server
                for (int i = 0; i < productModelList.size(); i++) {
                    // search by product id or barcode or name
                    final String text = productModelList.get(i).getId() + productModelList.get(i).getBarcode() + productModelList.get(i).getName().toLowerCase();
                    if (text.contains(s)) {
                        filteredList.add(productModelList.get(i));
                    }
                }

                // set adapter with filtered list
                productAdapter = new ProductAdapter(PosActivity.this, filteredList);
                product_list_recycler_view.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        // show data in recycler view
        productModelList = new ArrayList<>();
        // add data to array list
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_ALL_PRODUCT,
                // show circular progress bar


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        product_list_recycler_view.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                        try {
                            //converting response to json array
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                try {
                                    //getting product object from json array
                                    JSONObject productObject = array.getJSONObject(i);
                                    ProductModel model = new ProductModel();


                                    model.setId((productObject.getInt("id")));
                                    model.setName(productObject.getString("name"));
                                    model.setSell_price((productObject.getInt("sell_price")));
                                    model.setBuy_price((productObject.getInt("buy_price")));
                                    model.setOpenstock((productObject.getInt("openstock")));
                                    model.setImages(productObject.getString("images"));
                                    model.setBarcode(productObject.getString("barcode"));
                                    model.setDomain(productObject.getString("domain"));

                                    productModelList.add(model);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            productAdapter = new ProductAdapter(getApplicationContext(), productModelList);
                            // set adapter to recyclerview
                            product_list_recycler_view.setHasFixedSize(true);
//                        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            product_list_recycler_view.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

                            product_list_recycler_view.setAdapter(productAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        product_list_recycler_view.setVisibility(View.VISIBLE);

                        Toasty.error(PosActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();

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

        viewAll();


    }

    public void showMsg(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    private void viewAll() {
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = cartDB.getAllData();
                if (res.getCount() == 0) {
                    showMsg("Alert", "No Product in Cart");
                    return;
                }

                Intent intent = new Intent(PosActivity.this, CartActivity.class);
                startActivity(intent);

            }
        });
    }


}