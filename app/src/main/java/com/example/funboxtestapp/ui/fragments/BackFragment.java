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
import com.example.funboxtestapp.interfaces.IFragment;
import com.example.funboxtestapp.util.DataAdapter;
import com.example.funboxtestapp.util.StoreFragmentRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BackFragment extends Fragment {

    private static final String TAG = BackFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private CompositeDisposable mProductDisposable = new CompositeDisposable();
    private DataAdapter dataAdapter = new DataAdapter(App.getProductDAO());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = view.findViewById(R.id.fragment_store_toolbar);
        toolbar.inflateMenu(R.menu.back_end_menu);
        toolbar.setTitle(getString(R.string.change));
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_item_add) {
                if(getActivity() instanceof IFragment){
                    ((IFragment) getActivity()).setFragment(new EditFragment(), "EDIT_FRAGMENT");
                }
            }
            return false;
        });

        mRecyclerView = view.findViewById(R.id.fragment_store_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        getProducts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProductDisposable.clear();
    }

    private void setAdapter(List<Product> productList){
        StoreFragmentRecyclerAdapter adapter = new StoreFragmentRecyclerAdapter(getActivity(), new ArrayList<>(productList), true);
        mRecyclerView.setAdapter(adapter);
    }

    private void getProducts(){
        mProductDisposable.add(dataAdapter.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setAdapter,
                        throwable -> Log.e(TAG, "Unable to get products", throwable)));
    }
}
