package com.example.funboxtestapp.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.ui.fragments.DetailItemFragment;

import java.util.ArrayList;

public class DetailFragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Product> products;

    public DetailFragmentViewPagerAdapter(@NonNull FragmentManager fm, ArrayList<Product> products) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.products = products;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new DetailItemFragment(products.get(position));
    }

    @Override
    public int getCount() {
        return products.size();
    }
}
