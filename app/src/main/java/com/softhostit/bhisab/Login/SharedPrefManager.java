package com.softhostit.bhisab.Login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.softhostit.bhisab.Dashboard.DashboardModel;
import com.softhostit.bhisab.register.RegisterModel;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "loginuser";
    private static final String KEY_DOMAIN = "domain";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "id";

    public static SharedPrefManager mInstance;
    public static Context ctx;

    public SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_DOMAIN, user.getDomain());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_NAME, user.getName());
        editor.apply();
    }

    // this method will store dashboard data in shared preferences
    public void dashboardData(DashboardModel dashboardModel) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("domain", dashboardModel.getDomain());
        editor.putString("username", dashboardModel.getUsername());
        editor.putString("today_sales", dashboardModel.getToday_sales());
        editor.putString("today_expense", dashboardModel.getToday_expense());
        editor.putString("today_receive", dashboardModel.getToday_receive());
        editor.putString("today_balance", dashboardModel.getToday_balance());
        editor.apply();
    }

    // this method will get dashboard data from shared preferences
    public DashboardModel getDashboardData() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new DashboardModel(
                sharedPreferences.getString("domain", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("today_sales", null),
                sharedPreferences.getString("today_expense", null),
                sharedPreferences.getString("today_receive", null),
                sharedPreferences.getString("today_balance", null)
        );
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_DOMAIN, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_NAME, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }

}

