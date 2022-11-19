package com.softhostit.bhisab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.softhostit.bhisab.Login.LoginActivity;
import com.softhostit.bhisab.POS.PosActivity;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView nav_view;
    CardView posPrint, coustomer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nav_view = findViewById(R.id.nav_view);
        nav_view.setItemIconTintList(null);



        posPrint = findViewById(R.id.posPrint);
        posPrint.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), PosActivity.class));
            /*Toasty.success(HomeActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();*/
        });

        coustomer = findViewById(R.id.coustomer);
        coustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });




    }
}