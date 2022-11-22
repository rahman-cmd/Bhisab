package com.softhostit.bhisab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.softhostit.bhisab.Login.LoginActivity;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.POS.PosActivity;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    BottomNavigationView nav_view;
    CardView posPrint, coustomer;
    LinearLayout btnLogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        nav_view = findViewById(R.id.nav_view);
//        nav_view.setItemIconTintList(null);

        btnLogout = findViewById(R.id.btnLogout);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            User user = SharedPrefManager.getInstance(this).getUser();
//
//            id.setText(String.valueOf(user.getId()));
//            userEmail.setText(user.getEmail());
//            gender.setText(user.getGender());
//            userName.setText(user.getName());

            btnLogout.setOnClickListener(this);
        }
        else{
            Intent  intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }




        posPrint = findViewById(R.id.posPrint);
        posPrint.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), PosActivity.class));
            /*Toasty.success(HomeActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();*/
        });

        coustomer = findViewById(R.id.coustomer);
        coustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(HomeActivity.this, "Success!", Toasty.LENGTH_SHORT, true).show();
            }
        });




    }

    @Override
    public void onClick(View v) {
        if(v.equals(btnLogout)){
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }
    }
}