package com.example.funboxtestapp.util;

import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.db.ProductDAO;
import com.example.funboxtestapp.interfaces.IAdapter;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class DataAdapter implements IAdapter {

    private ProductDAO productDAO;

    public DataAdapter(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Flowable<List<Product>> getProducts() {
        return this.productDAO.getProducts();
    }

    @Override
    public Completable insert(Product product) {
        return this.productDAO.insert(product);
    }

    @Override
    public Completable update(Product product) {
        return this.productDAO.update(product);
    }

    @Override
    public Completable delete(Product product) {
        return this.productDAO.delete(product);
    }

    @Override
    public Completable deleteAll() {
        return this.productDAO.deleteAll();
    }
}
