package com.softhostit.bhisab.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "addCart.db";
    public static final String TABLE_NAME = "product_table";
    public static final String COL_1 = "CART_ID";
    public static final String COL_2 = "id";
    public static final String COL_3 = "name";
    public static final String COL_4 = "sell_price";
    public static final String COL_5 = "buy_price";
    public static final String COL_6 = "openstock";
    public static final String COL_7 = "images";
    public static final String COL_8 = "barcode";
    public static final String COL_9 = "domain";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(CART_ID INTEGER PRIMARY KEY AUTOINCREMENT, id INTEGER, name TEXT, sell_price INTEGER, buy_price INTEGER, openstock INTEGER, images TEXT, barcode TEXT, domain TEXT)");

    }

    public boolean insertData(Integer id, String name, Integer sell_price, Integer buy_price, Integer openstock, String images, String barcode, String domain) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, id);
        contentValues.put(COL_3, name);
        contentValues.put(COL_4, sell_price);
        contentValues.put(COL_5, buy_price);
        contentValues.put(COL_6, openstock);
        contentValues.put(COL_7, images);
        contentValues.put(COL_8, barcode);
        contentValues.put(COL_9, domain);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    // get all Data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}