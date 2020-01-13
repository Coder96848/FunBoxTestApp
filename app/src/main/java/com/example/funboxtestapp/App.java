package com.example.funboxtestapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.db.ProductDAO;
import com.example.funboxtestapp.db.ProductRoomDB;
import com.example.funboxtestapp.util.DataAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class App extends Application {

    private static final String APP_PREFERENCES = "appSettings";
    private static final String TAG = App.class.getSimpleName();
    private static SharedPreferences sharedPreferences;
    private static ProductRoomDB productRoomDB;
    private static ProductDAO productDAO;
    private static DataAdapter dataAdapter;
    private static boolean isFirstStart = true;

    public static ProductDAO getProductDAO() {
        return productDAO;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        isFirstStart = sharedPreferences.getBoolean("isFirstStart", true);
        try {
            productRoomDB = ProductRoomDB.getInstance(getApplicationContext());
            productDAO = productRoomDB.productDAO();
            dataAdapter = new DataAdapter(productDAO);
            if(setFirstStartFlag()) initialData();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void closeDb(){
        productRoomDB.close();
    }

    private void initialData(){
        CompositeDisposable disposable = new CompositeDisposable();

        Product product = new Product("Apple iPod touch 5 32Gb", 8888, 5);
        insert(product, disposable);
        product = new Product("Samsung Galaxy S Duos S7562", 7230, 2);
        insert(product, disposable);
        product = new Product("Canon EOS 600D Kit", 15659, 4);
        insert(product, disposable);
        product = new Product("Samsung Galaxy Tab 2 10.1 P5100 16Gb", 13290, 9);
        insert(product, disposable);
        product = new Product("PocketBook Touch", 5197, 2);
        insert(product, disposable);
        product = new Product("Samsung Galaxy Note II 16Gb", 17049.50, 2);
        insert(product, disposable);
        product = new Product("Nikon D3100 Kit", 12190, 4);
        insert(product, disposable);
        product = new Product("Canon EOS 1100D Kit", 10985, 2);
        insert(product, disposable);
        product = new Product("Sony Xperia acro S", 11800.99, 1);
        insert(product, disposable);
        product = new Product("Lenovo G580", 8922, 1);
        insert(product, disposable);

        disposable.clear();
    }

    private void insert(Product product, CompositeDisposable disposable){
        disposable.add(dataAdapter.insert(product)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {},
                        throwable -> Log.e(TAG, "Unable to update products", throwable)));
    }

    public static boolean setFirstStartFlag() {
        if (isFirstStart) {
            isFirstStart = false;
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putBoolean("isFirstStart", isFirstStart);
            e.apply();
            return true;
        }
        return false;
    }
}
