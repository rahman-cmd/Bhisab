package com.softhostit.bhisab.POS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.components.XAxis;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CartActivity extends AppCompatActivity {

    RecyclerView cart_recyclerview;
    private ArrayList<CartModel> cartModelArrayList;
    private CartAdapter cartAdapter;
    DatabaseHelper cartDB;
    static TextView txt_total_price;

    Button btn_submit_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Product Cart");

        cart_recyclerview = findViewById(R.id.cart_recyclerview);
        txt_total_price = findViewById(R.id.txt_total_price);

        cartDB = new DatabaseHelper(this);
        cartModelArrayList = new ArrayList<>();
        viewData();

        btn_submit_order = findViewById(R.id.btn_submit_order);
        btn_submit_order.setOnClickListener(v -> {
            Toasty.success(this, "Order Submitted", Toasty.LENGTH_SHORT).show();
        });

    }

    private void viewData() {

        // get data from sqlite database getAllData() method

        Cursor cursor = cartDB.getAllData();
        // set for loop for get all data from database
        cart_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        while (cursor.moveToNext()) {
            CartModel cartModel = new CartModel();
            cartModel.setName(cursor.getString(2));
            cartModel.setSell_price(cursor.getString(3));
            cartModel.setId(cursor.getString(1));
            cartModelArrayList.add(cartModel);
        }

        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        cart_recyclerview.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        cart_recyclerview.setHasFixedSize(true);
        cartAdapter = new CartAdapter(CartActivity.this, cartModelArrayList);
        cart_recyclerview.setAdapter(cartAdapter);
    }
}