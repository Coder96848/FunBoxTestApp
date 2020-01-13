package com.example.funboxtestapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funboxtestapp.App;
import com.example.funboxtestapp.R;
import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.util.DataAdapter;
import com.example.funboxtestapp.util.StoreFragmentRecyclerAdapter;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class StoreFragment extends Fragment {

    private static final String TAG = StoreFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private DataAdapter dataAdapter = new DataAdapter(App.getProductDAO());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = view.findViewById(R.id.fragment_store_toolbar);
        toolbar.setTitle(getString(R.string.shop));
        recyclerView = view.findViewById(R.id.fragment_store_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        getProducts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDisposable.clear();
    }

    private void setAdapter(List<Product> productList){
        StoreFragmentRecyclerAdapter adapter = new StoreFragmentRecyclerAdapter(this.getContext(), new ArrayList<>(productList), false);
        recyclerView.setAdapter(adapter);
    }

    private void getProducts(){
        mDisposable.add(dataAdapter.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable()
                .flatMap(Flowable::fromIterable)
                .filter(products -> products.getCount() > 0)
                .toList()
                .subscribe(this::setAdapter, throwable -> Log.e(TAG, "Unable to get products", throwable)));
    }
}
