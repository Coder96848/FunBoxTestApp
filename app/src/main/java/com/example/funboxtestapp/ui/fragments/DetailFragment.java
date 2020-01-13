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
import androidx.viewpager.widget.ViewPager;

import com.example.funboxtestapp.App;
import com.example.funboxtestapp.R;
import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.interfaces.IFragment;
import com.example.funboxtestapp.util.DataAdapter;
import com.example.funboxtestapp.util.DetailFragmentViewPagerAdapter;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();
    private ArrayList<Product> mProducts = new ArrayList<>();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private int position;
    private ViewPager mViewPager;

    public DetailFragment(int position) {
        this.position = position;
        DataAdapter dataAdapter = new DataAdapter(App.getProductDAO());
        mDisposable.add(dataAdapter.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable()
                .flatMap(Flowable::fromIterable)
                .filter(products -> products.getCount() > 0)
                .toList()
                .subscribe(products -> {
                    mProducts.addAll(products);
                    setAdapter();
                    }, throwable -> Log.e(TAG, "Unable to get products", throwable)));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.fragment_detail_toolbar);
        toolbar.setTitle(getString(R.string.shop));
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(v -> onBack());

        mViewPager = view.findViewById(R.id.fragment_detail_view_pager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDisposable.clear();
    }

    private void onBack(){
        if(getActivity() instanceof IFragment){
            ((IFragment) getActivity()).onFragmentBackStack();
        }
    }

    private void setAdapter(){
        mViewPager.setAdapter(new DetailFragmentViewPagerAdapter(getChildFragmentManager(), mProducts));
        mViewPager.setCurrentItem(position);
    }
}
