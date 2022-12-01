package com.softhostit.bhisab.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.softhostit.bhisab.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;


    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {


        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    // instart of this method we can use addProductToCart() method
    public void addProductToCart(int id, String name, int sell_price, int buy_price, String openstock, String images, String domain) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("sell_price", sell_price);
        contentValues.put("buy_price", buy_price);
        contentValues.put("openstock", openstock);
        contentValues.put("images", images);
        contentValues.put("domain", domain);
        database.insert("product_cart", null, contentValues);
    }


    //Get all product from cart
    public ArrayList<HashMap<String, String>> getAllProductFromCart() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM product_cart";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("sell_price", cursor.getString(2));
                map.put("buy_price", cursor.getString(3));
                map.put("openstock", cursor.getString(4));
                map.put("images", cursor.getString(5));
                map.put("domain", cursor.getString(6));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }



    //get cart item count
    public int getCartItemCount() {

        Cursor cursor = database.rawQuery("SELECT * FROM product_cart", null);
        int itemCount = cursor.getCount();


        cursor.close();
        database.close();
        return itemCount;
    }



}
