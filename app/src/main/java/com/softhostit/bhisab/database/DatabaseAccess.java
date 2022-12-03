package com.softhostit.bhisab.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.softhostit.bhisab.Constant;

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
    public int addProductToCart(int id, String name, int sell_price, int buy_price, String openstock, String images, String barcode, String domain) {


        Cursor result = database.rawQuery("SELECT * FROM product_cart WHERE product_id='" + id + "'", null);
        if (result.getCount() > 1) {
            return 2;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("product_id", id);
            contentValues.put("name", name);
            contentValues.put("sell_price", sell_price);
            contentValues.put("buy_price", buy_price);
            contentValues.put("openstock", openstock);
            contentValues.put("images", images);
            contentValues.put("barcode", barcode);
            contentValues.put("domain", domain);


            long check = database.insert(Constant.productCart, null, contentValues);

            result.close();
            database.close();

            if (check == -1) {
                return -1;
            } else {
                return 1;
            }
        }


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
