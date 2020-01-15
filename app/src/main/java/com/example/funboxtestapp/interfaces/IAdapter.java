package com.example.funboxtestapp.interfaces;

import com.example.funboxtestapp.db.Product;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


public interface IAdapter {
    Flowable<List<Product>> getProducts();
    Completable insert(Product product);
    Completable update(Product product);
    Completable delete(Product product);
    Completable deleteAll();
}
