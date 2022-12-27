package com.softhostit.bhisab.POS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.components.XAxis;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<CartModel> cartModelArrayList;
    private CartAdapter cartAdapter;
    DatabaseHelper cartDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Order Summary");

        recyclerView = findViewById(R.id.recycler);
        cartDB = new DatabaseHelper(this);
        cartModelArrayList = new ArrayList<>();
        viewData();


    }

    private void viewData() {

        // get data from sqlite database getAllData() method

        Cursor cursor = cartDB.getAllData();
        // set for loop for get all data from database
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        while (cursor.moveToNext()) {
            CartModel cartModel = new CartModel();
            cartModel.setName(cursor.getString(2));
            cartModel.setSell_price(cursor.getString(3));
            cartModelArrayList.add(cartModel);
        }

        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.setHasFixedSize(true);
        cartAdapter = new CartAdapter(CartActivity.this, cartModelArrayList);
        recyclerView.setAdapter(cartAdapter);
    }
}