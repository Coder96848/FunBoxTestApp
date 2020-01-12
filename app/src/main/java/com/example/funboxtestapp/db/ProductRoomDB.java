package com.example.funboxtestapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductRoomDB extends RoomDatabase {

    public abstract ProductDAO productDAO();

    private static volatile ProductRoomDB INSTANCE;

    public static ProductRoomDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProductRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProductRoomDB.class, "product_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
