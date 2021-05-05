package com.example.duan1.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

    static final String DBName = "ASMMM";
    static final int Version =2;

    public SQLite(Context context) {
        super(context, DBName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        String sqlCart = "CREATE TABLE Cart(" +
                "id TEXT NOT NULL," +
                "price LONG NOT NULL," +
                "quantity INTEGER NOT NULL);";
        DB.execSQL(sqlCart);

        String sqlViewedProduct = "CREATE TABLE ViewedProduct(" +
                "id TEXT NOT NULL);";
        DB.execSQL(sqlViewedProduct);

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        String sqlDropCart = "DROP TABLE Cart ;";
        DB.execSQL(sqlDropCart);
        String sqlDropViewedProduct = "DROP TABLE ViewedProduct;";
        DB.execSQL(sqlDropViewedProduct);
        onCreate(DB);
    }
}
