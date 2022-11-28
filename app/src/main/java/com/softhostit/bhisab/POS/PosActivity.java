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

public class PosActivity extends AppCompatActivity {


    RecyclerView product_list_recycler_view;
    ImageView imgScanner;
    public static EditText etxtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);


        etxtSearch = findViewById(R.id.etxt_search);
        imgScanner = findViewById(R.id.img_scanner);

        product_list_recycler_view = findViewById(R.id.product_list_recycler_view);
        product_list_recycler_view.setHasFixedSize(true);
        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(this));



        imgScanner.setOnClickListener(v -> {
            Intent intent = new Intent(PosActivity.this, ScannerActivity.class);
            startActivity(intent);
        });

        etxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("data", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 1) {

                    //search data from server
                    Log.d("data", s.toString());

                    // data will be shown in recycler view, when data is received from server

                } else {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("data", s.toString());

            }
        });




    }


}