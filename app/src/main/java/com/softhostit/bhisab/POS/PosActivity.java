package com.softhostit.bhisab.POS;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Dashboard.DashboardModel;
import com.softhostit.bhisab.HomeActivity;
import com.softhostit.bhisab.Login.LoginActivity;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class PosActivity extends AppCompatActivity {

    List<ProductModel> productModelList;
    private ProductAdapter productAdapter;
    private RecyclerView product_list_recycler_view;

    private static String JSON_URL = "https://mocki.io/v1/1fd35679-bddf-4fc2-8621-a666f88e35c0";


    ImageView imgScanner;
    public static EditText etxtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);


        etxtSearch = findViewById(R.id.etxt_search);
        imgScanner = findViewById(R.id.img_scanner);
        product_list_recycler_view = findViewById(R.id.product_list_recycler_view);

        imgScanner.setOnClickListener(v -> {
            Intent intent = new Intent(PosActivity.this, ScannerActivity.class);
            startActivity(intent);
        });

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
                    } else {
                        Toasty.error(PosActivity.this, "No Product Found", Toast.LENGTH_SHORT).show();
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

//        loadData();

        // show data in recycler view
        productModelList = new ArrayList<>();
        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        product_list_recycler_view.setAdapter(productAdapter);


        // add data to array list
//        productModelList.add(new ProductModel("1", "আলু", "9", "100", "5", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77027"));
//        productModelList.add(new ProductModel("2", "বেগুন", "130", "90", "80", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77028"));
//        productModelList.add(new ProductModel("3", "কলা", "150", "120", "120", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77029"));
//        productModelList.add(new ProductModel("4", "কুমড়া", "180", "160", "200", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77020"));
//        productModelList.add(new ProductModel("5", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("6", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("7", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("8", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("9", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("10", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("11", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("12", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("13", "মুরগি", "200", "180", "8", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("14", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("15", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("16", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("17", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("18", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("19", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("20", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("21", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("22", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("23", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("24", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
//        productModelList.add(new ProductModel("25", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));




        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_ALL_PRODUCT,
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
                                    ProductModel model = new ProductModel();


                                    model.setId(Integer.parseInt(productObject.getString("id")));
                                    model.setName(productObject.getString("name"));
                                    model.setSell_price(Integer.parseInt(productObject.getString("sell_price")));
                                    model.setBuy_price(Integer.parseInt(productObject.getString("buy_price")));
                                    model.setOpenstock((productObject.getString("openstock")));
                                    model.setImages(productObject.getString("images"));
                                    model.setBarcode(productObject.getString("barcode"));

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
                        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        product_list_recycler_view.setAdapter(productAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toasty.error(PosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String domain = sharedPreferences.getString(Constant.KEY_DOMAIN, "");
                String username = sharedPreferences.getString(Constant.SP_USERNAME, "");
                params.put("domain", "demo.bhisab.com");
                params.put("username", "admin");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

//    private void loadData() {
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        // jeson array request for getting data from server with post method
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d("response", response.toString());
//                // if response is not null
//
//                    // looping through all the response
//                    for (int i = 0; i < response.length(); i++) {
//                        try {
//                            // getting product object from json array
//                            JSONObject productObject = response.getJSONObject(i);
//
//                            // adding the product to product list
//                           ProductModel product = new ProductModel();
//
//
//                            product.setId(Integer.parseInt(productObject.getString("id")));
//                            product.setName(productObject.getString("name"));
//                            product.setSell_price(Integer.parseInt(productObject.getString("sell_price")));
//                            product.setBuy_price(Integer.parseInt(productObject.getString("buy_price")));
//                            product.setOpenstock(Integer.parseInt(productObject.getString("openstock")));
//                            product.setImages(productObject.getString("images"));
//                            product.setBarcode(productObject.getString("barcode"));
//
//                            productModelList.add(product);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        //setup adapter
//                        productAdapter = new ProductAdapter(getApplicationContext(), productModelList);
//                        // set adapter to recyclerview
//                        product_list_recycler_view.setHasFixedSize(true);
//                        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                        product_list_recycler_view.setAdapter(productAdapter);
//
//
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error in getting response
//                Toast.makeText(PosActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        // adding the string request to request queue
//        requestQueue.add(jsonArrayRequest);
//
//        // adding the string request to request queue
//
//
//
//
//    }


    /*StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_ALL_PRODUCT,
            new Response.Listener<String>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(String response) {

                    Log.d("response", response);


                }


            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(PosActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            // get domain name and user id from shared preference
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String domain = sharedPreferences.getString(Constant.KEY_DOMAIN, "");
            String username = sharedPreferences.getString(Constant.SP_USERNAME, "");
            params.put("domain", "demo.bhisab.com");
            params.put("username", "admin");

            return params;
        }
    };

       VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);*/


}