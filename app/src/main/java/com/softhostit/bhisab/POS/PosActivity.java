package com.softhostit.bhisab.POS;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.softhostit.bhisab.R;

import java.util.ArrayList;

public class PosActivity extends AppCompatActivity {

    private ArrayList<ProductModel> productModelArrayList;
    private ProductAdapter productAdapter;
    private RecyclerView product_list_recycler_view;


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
                for (int i = 0; i < productModelArrayList.size(); i++) {

                    // search by product id or barcode or name
                    final String text = productModelArrayList.get(i).getId().toLowerCase();
                    final String text1 = productModelArrayList.get(i).getBarcode().toLowerCase();
                    final String text2 = productModelArrayList.get(i).getName().toLowerCase();
                    if (text.contains(s) || text1.contains(s) || text2.contains(s)) {
                        filteredList.add(productModelArrayList.get(i));
                    } else {
                        Log.d("TAG", "onTextChanged: " + text);
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
        productModelArrayList = new ArrayList<>();
        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        product_list_recycler_view.setHasFixedSize(true);

        // add data to array list
        productModelArrayList.add(new ProductModel("1", "আলু", "9", "100", "5", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77027"));
        productModelArrayList.add(new ProductModel("2", "বেগুন", "130", "90", "80", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77028"));
        productModelArrayList.add(new ProductModel("3", "কলা", "150", "120", "120", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77029"));
        productModelArrayList.add(new ProductModel("4", "কুমড়া", "180", "160", "200", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77020"));
        productModelArrayList.add(new ProductModel("5", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("6", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("7", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("8", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("9", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("10", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("11", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("12", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("13", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("14", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("15", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("16", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("17", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("18", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("19", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("20", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("21", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("22", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("23", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("24", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));
        productModelArrayList.add(new ProductModel("25", "মুরগি", "200", "180", "100", "https://upload.wikimedia.org/wikipedia/commons/a/ab/Patates.jpg", "77021"));


        productAdapter = new ProductAdapter(this, productModelArrayList);
        product_list_recycler_view.setAdapter(productAdapter);


    }


}