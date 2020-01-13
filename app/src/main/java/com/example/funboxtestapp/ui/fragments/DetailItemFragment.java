package com.example.funboxtestapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.funboxtestapp.App;
import com.example.funboxtestapp.R;
import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.util.DataAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailItemFragment extends Fragment {

    private static final String TAG = DetailItemFragment.class.getSimpleName();
    private Product mProduct;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private DataAdapter dataAdapter = new DataAdapter(App.getProductDAO());


    public DetailItemFragment(Product product) {
        this.mProduct = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nameTextView = view.findViewById(R.id.fragment_detail_item_text_view_name_good);
        TextView priceTextView = view.findViewById(R.id.fragment_detail_item_text_view_price_good);
        TextView countTextView = view.findViewById(R.id.fragment_detail_item_text_view_count_good);

        nameTextView.setText(mProduct.getName());
        priceTextView.setText(String.format("%.2f руб.", mProduct.getPrice()));
        countTextView.setText(String.format("%d шт.", mProduct.getCount()));

        Button buyButton = view.findViewById(R.id.fragment_detail_item_button_buy);
        buyButton.setOnClickListener(v -> {
            mProduct.buyProduct();
            mDisposable.add(dataAdapter.update(mProduct)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> countTextView.setText(String.format("%d шт.", mProduct.getCount())),
                            throwable -> Log.e(TAG, "Unable to update products", throwable)));
        });
    }
}
