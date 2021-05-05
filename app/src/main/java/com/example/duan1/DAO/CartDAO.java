package com.example.duan1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duan1.Database.SQLite;
import com.example.duan1.Model.CartItem;

import java.util.ArrayList;

public class CartDAO {
    private SQLiteDatabase DB;

    public CartDAO(Context context) {
        SQLite sqLite = new SQLite(context);
        DB = sqLite.getReadableDatabase();
        DB = sqLite.getWritableDatabase();
    }

    public long insert(CartItem item) {
        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        values.put("price", item.getPrice());
        values.put("quantity", item.getQuantity());
        return DB.insert("Cart", null, values);
    }

    public ArrayList<CartItem> get(String sql, String... selectionArgs) {
        ArrayList<CartItem> list = new ArrayList<>();
        Cursor c = DB.rawQuery(sql, selectionArgs);
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex("id"));
            long price = c.getLong(c.getColumnIndex("price"));
            int quantity = c.getInt(c.getColumnIndex("quantity"));
            CartItem item = new CartItem(id, price, quantity);
            list.add(item);
        }
        return list;
    }

    public long update(CartItem item) {
        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        values.put("price", item.getPrice());
        values.put("quantity", item.getQuantity());
        return DB.update("Cart", values, "id=?", new String[]{item.getId()});
    }

    public ArrayList<CartItem> getAll() {
        String sqlGetAll = "SELECT * FROM Cart";
        return get(sqlGetAll);
    }

    public boolean checkExist(String id) {
        ArrayList<CartItem> list = get("SELECT * FROM CART WHERE id=?", id);
        return list.size() > 0;
    }

    public long delete(CartItem item) {
        String id = item.getId();
        return DB.delete("Cart", "id=?", new String[]{id});
    }

    public long totalPrice() {
        long total = 0;
        Cursor cursor = DB.rawQuery("SELECT SUM(price * quantity) FROM Cart", null);
        if (cursor.moveToFirst()) {
            do {
                total = cursor.getLong(0);
            } while (cursor.moveToNext());
        }
        return total;
    }

    public long deleteAll() {
        return DB.delete("Cart", null, null);
    }
}
