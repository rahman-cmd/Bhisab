package com.softhostit.bhisab.POS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.softhostit.bhisab.R;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Order Summary");
    }
}