package com.softhostit.bhisab.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.POS.PosActivity;
import com.softhostit.bhisab.POS.ProductAdapter;
import com.softhostit.bhisab.POS.ProductModel;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.coustomer.CoustomerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ProductActivity extends AppCompatActivity {

    RecyclerView productRecycler;
    List<Model> modelList;
    private Adapter adapter;
    FloatingActionButton floatingActionButton2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productRecycler = findViewById(R.id.productRecycler);

        floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show the dialog to add customer group
                final Dialog dialog = new Dialog(ProductActivity.this);
                dialog.setContentView(R.layout.add_product_dialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

                EditText addProductName = dialog.findViewById(R.id.addProductName);
                EditText addProductQuantity = dialog.findViewById(R.id.addProductQuantity);
                EditText addProductDescription = dialog.findViewById(R.id.addProductDescription);
                EditText addSellPrice = dialog.findViewById(R.id.addSellPrice);
                EditText addBuyPrice = dialog.findViewById(R.id.addBuyPrice);
                EditText addBarcode = dialog.findViewById(R.id.addBarcode);
                
                Button addProductButton = dialog.findViewById(R.id.addProductButton);
                addProductButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = addProductName.getText().toString();
                        int quantity = Integer.parseInt(addProductQuantity.getText().toString());
                        String description = addProductDescription.getText().toString();
                        int sell_price = Integer.parseInt(addSellPrice.getText().toString());
                        int buy_price = Integer.parseInt(addBuyPrice.getText().toString());
                        String barcode = addBarcode.getText().toString();

                        if (name.isEmpty()) {
                            addProductName.setError("Please enter product name");
                            addProductName.requestFocus();
                            return;
                        }

                        if (quantity == 0) {
                            addProductQuantity.setError("Please enter product quantity");
                            addProductQuantity.requestFocus();
                            return;
                        }

                        if (description.isEmpty()) {
                            addProductDescription.setError("Please enter product description");
                            addProductDescription.requestFocus();
                            return;
                        }

                        if (sell_price == 0) {
                            addSellPrice.setError("Please enter product sell price");
                            addSellPrice.requestFocus();
                            return;
                        }

                        if (buy_price == 0) {
                            addBuyPrice.setError("Please enter product buy price");
                            addBuyPrice.requestFocus();
                            return;
                        }

                        if (barcode.isEmpty()) {
                            addBarcode.setError("Please enter product barcode");
                            addBarcode.requestFocus();
                            return;
                        }

                        // add product to database
                        addProduct(name, quantity, description, sell_price, buy_price, barcode);
                        showAllProduct();
                        dialog.dismiss();
                    }
                });
            }
        });


       showAllProduct();
    }

    private void addProduct(String name, int quantity, String description, int sell_price, int buy_price, String barcode) {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        String user_id = intent.getStringExtra("user_id");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("responsedata", response);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(ProductActivity.this, "Error: " + e.toString(), Toasty.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(ProductActivity.this, "Error: " + error.toString(), Toasty.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("username", username);
                params.put("user_id", user_id);
                params.put("name", name);
                params.put("openstock", String.valueOf(quantity));
                params.put("description", description);
                params.put("sell_price", String.valueOf(sell_price));
                params.put("buy_price", String.valueOf(buy_price));
                params.put("barcode", barcode);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void showAllProduct() {
        modelList = new ArrayList<>();
        // add data to array list

        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_ALL_PRODUCT,
                // show circular progress bar

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
                                    Model model = new Model();


                                    model.setId((productObject.getInt("id")));
                                    model.setName(productObject.getString("name"));
                                    model.setSell_price((productObject.getInt("sell_price")));
                                    model.setBuy_price((productObject.getInt("buy_price")));
                                    model.setOpenstock((productObject.getInt("openstock")));
                                    model.setImages(productObject.getString("images"));
                                    model.setBarcode(productObject.getString("barcode"));
                                    model.setDomain(productObject.getString("domain"));

                                    modelList.add(model);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            adapter = new Adapter(getApplicationContext(), modelList);
                            // set adapter to recyclerview
                            productRecycler.setHasFixedSize(true);
                            productRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

                            productRecycler.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toasty.error(ProductActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();

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