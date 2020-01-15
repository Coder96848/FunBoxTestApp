package com.example.funboxtestapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.funboxtestapp.R;
import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.interfaces.IFragment;
import com.example.funboxtestapp.util.DetailFragmentViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DetailFragment extends Fragment {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private ArrayList<Product> mProducts;
    private int mPosition;
    private ViewPager mViewPager;

    public DetailFragment(int mPosition, ArrayList<Product> mProducts) {
        this.mPosition = mPosition;
        this.mProducts = mProducts;
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

        setAdapter(mProducts);
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

    private void setAdapter(List<Product> products){
        mViewPager.setAdapter(new DetailFragmentViewPagerAdapter(getChildFragmentManager(), new ArrayList<>(products)));
        mViewPager.setCurrentItem(mPosition);
    }
}
