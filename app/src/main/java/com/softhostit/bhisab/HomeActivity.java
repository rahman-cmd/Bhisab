package com.softhostit.bhisab;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.softhostit.bhisab.Dashboard.DashboardModel;
import com.softhostit.bhisab.Login.LoginActivity;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.POS.PosActivity;
import com.softhostit.bhisab.coustomer.CoustomerActivity;
import com.softhostit.bhisab.deposit.DepositActivity;
import com.softhostit.bhisab.expense.ExpenseActivity;
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

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView nav_view;
    CardView posPrint, coustomer, deposit, expense, report, setting, supplierBtn, addProduct;
    TextView dailySales, today_expense, today_receive, today_balance;

    // DrawerLayout
    DrawerLayout drawerLayout;
    NavigationView nav_View_drowaer;
    ActionBarDrawerToggle toggle;

    ImageView imageMenu;

    User user = SharedPrefManager.getInstance(this).getUser();

    final String domain = user.getDomain();
    final String username = user.getUsername();
    final String store_name = user.getName();
    final int id = user.getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        dailySales = findViewById(R.id.dailySales);
        today_expense = findViewById(R.id.today_expense);
        today_receive = findViewById(R.id.today_receive);
        today_balance = findViewById(R.id.today_balance);




        // Navagation Drawar------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        nav_View_drowaer = findViewById(R.id.nav_View_drowaer);
        imageMenu = findViewById(R.id.imageMenu);

        nav_View_drowaer.setItemIconTintList(null);

        toggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Drawer item Click event ------
        nav_View_drowaer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Job:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.contact:
                        // click event for contact us dialog
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.fiveStar:
                        // click and got to play store
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.softhostit.bhisab")));
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.About:
                        // click event for about us dialog
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.share:
                        // click to share app
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Probash Jatra");
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.probashjatraltd.probashjatra" + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                        drawerLayout.closeDrawers();
                        break;


                    case R.id.privacy:
                        // click event for privacy policy dialog

                        drawerLayout.closeDrawers();
                        break;

                    case R.id.logoutBtn:
                        // click event for logout
                        if (SharedPrefManager.getInstance(HomeActivity.this).isLoggedIn()) {
                            SharedPrefManager.getInstance(HomeActivity.this).logout();
                            finish();
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        }
                        drawerLayout.closeDrawers();
                        break;

                }

                return false;
            }
        });


        // App Bar Click Event
        imageMenu = findViewById(R.id.imageMenu);
        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code Here
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

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

            View headerView = nav_View_drowaer.getHeaderView(0);
            TextView userRol = (TextView) headerView.findViewById(R.id.userRole);
            TextView userName = (TextView) headerView.findViewById(R.id.userName);
            userRol.setText("Welcome " + user.getUsername());
            userName.setText(store_name);


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
                intent.putExtra("user_id", id);
                startActivity(intent);
            }
        });

        expense = findViewById(R.id.expense);
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ExpenseActivity.class);
                intent.putExtra("domain", domain);
                intent.putExtra("username", username);
                intent.putExtra("user_id", id);
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