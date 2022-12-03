package com.softhostit.bhisab.database;
import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
public class DatabaseOpenHelper extends SQLiteAssetHelper {
    public static final String DATABASE_NAME = "super_pos.db";
    private static final int DATABASE_VERSION = 2;
    private Context mContext;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

}