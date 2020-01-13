package com.example.funboxtestapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(Product product);

    @Update
    Completable update(Product Product);

    @Update
    Completable buyProduct(Product product);

    @Delete
    Completable delete(Product product);

    @Query("DELETE FROM product_table")
    Completable deleteAll();

    @Query("SELECT * FROM product_table")
    Single<List<Product>> getProducts();
}
