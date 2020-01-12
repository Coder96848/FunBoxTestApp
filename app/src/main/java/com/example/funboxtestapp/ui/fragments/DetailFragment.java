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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.funboxtestapp.App;
import com.example.funboxtestapp.R;
import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.interfaces.IFragment;
import com.example.funboxtestapp.util.DataAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();
    private Product mProduct;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private DataAdapter dataAdapter = new DataAdapter(App.getProductDAO());

    public DetailFragment(Product mProduct) {
        this.mProduct = mProduct;
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

        TextView nameTextView = view.findViewById(R.id.fragment_detail_text_view_name_good);
        TextView priceTextView = view.findViewById(R.id.fragment_detail_text_view_price_good);
        TextView countTextView = view.findViewById(R.id.fragment_detail_text_view_count_good);

        nameTextView.setText(mProduct.getName());
        priceTextView.setText(String.format("%.2f руб.", mProduct.getPrice()));
        countTextView.setText(String.format("%d шт.", mProduct.getCount()));

        Button buyButton = view.findViewById(R.id.fragment_detail_button_buy);
        buyButton.setOnClickListener(v -> {
            mProduct.buyProduct();
            mDisposable.add(dataAdapter.update(mProduct)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() ->{},
                            throwable -> Log.e(TAG, "Unable to update products", throwable)));
            onBack();
        });
    }

    private void onBack(){
        if(getActivity() instanceof IFragment){
            ((IFragment) getActivity()).onFragmentBackStack();
        }
    }
}
