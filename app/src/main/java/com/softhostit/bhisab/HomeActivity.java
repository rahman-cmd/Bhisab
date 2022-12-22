package com.softhostit.bhisab;



import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.softhostit.bhisab.Dashboard.DashboardModel;
import com.softhostit.bhisab.Login.LoginActivity;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.POS.PosActivity;
import com.softhostit.bhisab.coustomer.CoustomerActivity;
import com.softhostit.bhisab.deposit.DepositActivity;
import com.softhostit.bhisab.product.ProductActivity;
import com.softhostit.bhisab.supplier.SupplierActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity  {

    BottomNavigationView nav_view;
    CardView posPrint, coustomer, deposit, expense, report, setting, supplierBtn, addProduct;
    TextView dailySales, today_expense, today_receive, today_balance, c_name;

    User user = SharedPrefManager.getInstance(this).getUser();

    final String domain = user.getDomain();
    final String username = user.getUsername();
    final String store_name = user.getName();
    final int id = user.getId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();

        dailySales = findViewById(R.id.dailySales);
        today_expense = findViewById(R.id.today_expense);
        today_receive = findViewById(R.id.today_receive);
        today_balance = findViewById(R.id.today_balance);
        c_name = findViewById(R.id.c_name);


        // show the dashboard data
//        api call every 3 second
/*        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDashboardData();
                handler.postDelayed(this, 1000);
            }
        }, 1000);*/

        getDashboardData();

        // show the dashboard data

//        nav_view = findViewById(R.id.nav_view);
//        nav_view.setItemIconTintList(null);


        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            User user = SharedPrefManager.getInstance(this).getUser();

            actionBar.setTitle("Welcome " + user.getUsername());
            actionBar.setSubtitle(user.getDomain());
            c_name.setText(store_name);


        } else {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        addProduct = findViewById(R.id.addProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                intent.putExtra("domain", domain);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });


        posPrint = findViewById(R.id.posPrint);
        posPrint.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PosActivity.class);
            intent.putExtra("domain", domain);
            intent.putExtra("username", username);
            startActivity(intent);

        });

        coustomer = findViewById(R.id.coustomer);
        coustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CoustomerActivity.class);
                intent.putExtra("domain", domain);
                intent.putExtra("username", username);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

        supplierBtn = findViewById(R.id.supplierBtn);
        supplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SupplierActivity.class);
                intent.putExtra("domain", domain);
                intent.putExtra("username", username);
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

        deposit = findViewById(R.id.deposit);
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DepositActivity.class);
                intent.putExtra("domain", domain);
                intent.putExtra("username", username);
                intent.putExtra("user_id", id);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

            }
        });


    }

    private void getDashboardData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_DASHBOARD,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            JSONObject userJson = obj.getJSONObject("dashboard");
                            DashboardModel dashboardModel = new DashboardModel(
                                    userJson.getString("domain"),
                                    userJson.getString("username"),
                                    userJson.getString("today_sales"),
                                    userJson.getString("today_expense"),
                                    userJson.getString("today_receive"),
                                    userJson.getString("today_balance")
                            );


                            dailySales.setText("৳ " + dashboardModel.getToday_sales());
                            today_expense.setText("৳ " + dashboardModel.getToday_expense());
                            today_receive.setText("৳ " + dashboardModel.getToday_receive());
                            today_balance.setText("৳ " + dashboardModel.getToday_balance());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toasty.error(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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

        // get dashboard data
    }

    // log out option menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logoout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logOutBtn:
                if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                    SharedPrefManager.getInstance(this).logout();
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // on back press show exit dialog


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
}