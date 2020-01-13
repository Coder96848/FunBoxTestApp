package com.example.funboxtestapp.interfaces;

import com.example.funboxtestapp.db.Product;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface IAdapter {
    Single<List<Product>> getProducts();
    Completable insert(Product product);
    Completable update(Product product);
    Completable buyProduct(Product product);
    Completable delete(Product product);
    Completable deleteAll();
}
