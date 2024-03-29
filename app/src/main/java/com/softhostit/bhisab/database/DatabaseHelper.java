package com.softhostit.bhisab.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.softhostit.bhisab.POS.PosActivity;


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

    public int insertData(Integer id, String name, Integer sell_price, Integer buy_price, Integer openstock, String images, String barcode, String domain) {
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
        if (result == -1)
            return 0;
        else
            return 1;

    }

    // get all Data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    // delete data by id
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[]{id});
    }

    public int getCountCard() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // get total sell_price
    public int getTotalPrice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(sell_price) FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }

    // delete all data when user logout
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
}