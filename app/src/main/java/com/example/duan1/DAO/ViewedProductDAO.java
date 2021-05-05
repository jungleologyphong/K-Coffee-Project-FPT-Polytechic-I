package com.example.duan1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.duan1.Database.SQLite;
import com.example.duan1.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewedProductDAO {
    private SQLiteDatabase DB;

    public ViewedProductDAO(Context context) {
        SQLite sqLite = new SQLite(context);
        DB = sqLite.getReadableDatabase();
        DB = sqLite.getWritableDatabase();
    }

    public long insert(Product item) {
        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        return DB.insert("ViewedProduct", null, values);
    }

    public ArrayList<Product> get(String sql, String... selectArgs) {
        ArrayList<Product> list = new ArrayList<>();
        Cursor c = DB.rawQuery(sql, selectArgs);
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex("id"));
            Product product = new Product();
            product.setId(id);
            list.add(0, product);
        }
        return list;
    }

    public long update(Product item) {
        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        return DB.update("ViewedProduct", values, "id=?", new String[]{item.getId()});
    }

    public ArrayList<Product> getAll() {
        String sql = "SELECT * FROM ViewedProduct";
        return get(sql);
    }

    public boolean checkExist(String id) {
        List<Product> list = get("SELECT * FROM ViewedProduct WHERE id=?", id);
        for (Product item : list) {
            Log.e("Cc", item.toString());
        }
        return list.size() > 0;
    }

    public long delete(String id) {
        return DB.delete("ViewedProduct", "id=?", new String[]{id});
    }
}
