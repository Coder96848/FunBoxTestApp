package com.example.funboxtestapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.funboxtestapp.App;
import com.example.funboxtestapp.R;
import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.interfaces.IFragment;
import com.jakewharton.rxbinding3.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class EditFragment extends Fragment {

    private static final String TAG = EditFragment.class.getSimpleName();
    private Product mProduct;
    private boolean isNewProduct;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mCountEditText;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public EditFragment() {
        mProduct = new Product("", 0,0);
        isNewProduct = true;
    }

    public EditFragment(Product mProduct) {
        this.mProduct = mProduct;
        isNewProduct = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.fragment_edit_toolbar);
        toolbar.inflateMenu(R.menu.edit_menu);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(v -> onBack());
        if (isNewProduct) {
            toolbar.setTitle(getString(R.string.add));
        }else {
            toolbar.setTitle(getString(R.string.change));
        }

        Button buttonSave = view.findViewById(R.id.fragment_edit_button_save);
        mNameEditText = view.findViewById(R.id.fragment_edit_edit_text_name);
        mPriceEditText = view.findViewById(R.id.fragment_edit_edit_text_price);
        mCountEditText = view.findViewById(R.id.fragment_edit_edit_text_count);

        mNameEditText.setText(mProduct.getName());
        mPriceEditText.setText(String.format("%.2f", mProduct.getPrice()));
        mCountEditText.setText(String.format("%d", mProduct.getCount()));

        if (isNewProduct) {
            mDisposable.add(RxView.clicks(buttonSave)
                    .subscribe(isClick -> {
                        mDisposable.add(App.getDataAdapter().insert(newProduct())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        );
                        onBack();
                        }, throwable -> Log.e(TAG, "Unable to get products", throwable)));
        }else {
            mDisposable.add(RxView.clicks(buttonSave)
                    .subscribe(isClick -> {
                        mProduct.setName(getName());
                        mProduct.setPrice(getPrice());
                        mProduct.setCount(getCount());
                        mDisposable.add(App.getDataAdapter().update(mProduct)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        );
                        onBack();
                        },throwable -> Log.e(TAG, "Unable to get products", throwable)
                    ));
        }
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

    private Product newProduct(){
        return new Product(getName(),
                getPrice(),
                getCount());
    }

    private String getName(){
        String name = mNameEditText.getText().toString();
        return !name.equals("") ? mNameEditText.getText().toString() : "";
    }

    private Double getPrice(){
        String price = mPriceEditText.getText().toString().replace(",", ".");
        return !price.equals("") ? Double.parseDouble(price) : 0;
    }

    private int getCount(){
        String count = mCountEditText.getText().toString();
        return !count.equals("") ? Integer.parseInt(count) : 0;
    }

}
