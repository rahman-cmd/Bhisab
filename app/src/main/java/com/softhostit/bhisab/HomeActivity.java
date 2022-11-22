package com.softhostit.bhisab;

import static android.content.ContentValues.TAG;
import static org.apache.poi.sl.usermodel.PresetColor.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView nav_view;
    CardView posPrint, coustomer;
    TextView dailySales;

    User user = SharedPrefManager.getInstance(this).getUser();

    final String domain = user.getDomain();
    final String username = user.getUsername();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();

        dailySales = findViewById(R.id.dailySales);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_DASHBOARD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dailySales.setText(response);

                        try {
                            JSONObject object = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                      /*  try {
                            //converting response to json object
                           JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("dashboard");

                                //creating a new dashboard object
                                DashboardModel dashboardModel = new DashboardModel(
                                        userJson.getString("domain"),
                                        userJson.getString("username"),
                                        userJson.getString("today_sales"),
                                        userJson.getString("today_expense"),
                                        userJson.getString("today_receive"),
                                        userJson.getString("today_balance")
                                );


                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).dashboardData(dashboardModel);
//                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            } else {
                                Toasty.error(HomeActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
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


//        nav_view = findViewById(R.id.nav_view);
//        nav_view.setItemIconTintList(null);


        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            User user = SharedPrefManager.getInstance(this).getUser();

            actionBar.setTitle("Welcome " + user.getUsername());
            actionBar.setSubtitle(user.getDomain());


        } else {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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


}