package com.example.funboxtestapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.funboxtestapp.App;
import com.example.funboxtestapp.R;
import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.interfaces.IFragment;
import com.example.funboxtestapp.util.DataAdapter;

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
    private DataAdapter dataAdapter = new DataAdapter(App.getProductDAO());

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

        if (isNewProduct) {
            toolbar.setTitle(getString(R.string.add));
        }else {
            toolbar.setTitle(getString(R.string.change));
        }

        mNameEditText = view.findViewById(R.id.fragment_edit_edit_text_name);
        mPriceEditText = view.findViewById(R.id.fragment_edit_edit_text_price);
        mCountEditText = view.findViewById(R.id.fragment_edit_edit_text_count);

        mNameEditText.setText(mProduct.getName());
        mPriceEditText.setText(String.format("%.2f", mProduct.getPrice()));
        mCountEditText.setText(String.format("%d", mProduct.getCount()));

        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.menu_item_edit) {
                if (isNewProduct) {
                    mDisposable.add(dataAdapter.insert(newProduct())
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {},
                                    throwable -> Log.e(TAG, "Unable to update products", throwable)));
                }else {
                    mProduct.setName(mNameEditText.getText().toString());
                    mProduct.setPrice(Double.valueOf(mPriceEditText.getText().toString().replace(",", ".")));
                    mProduct.setCount(Integer.valueOf(mCountEditText.getText().toString()));
                    mDisposable.add(dataAdapter.update(mProduct)
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {},
                                    throwable -> Log.e(TAG, "Unable to update products", throwable)));
                }
                onBack();
            }
            return false;

        });
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(v -> onBack());
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
        return new Product(mNameEditText.getText().toString(),
                Double.valueOf(mPriceEditText.getText().toString().replace(",", ".")),
                Integer.valueOf(mCountEditText.getText().toString()));
    }
}
