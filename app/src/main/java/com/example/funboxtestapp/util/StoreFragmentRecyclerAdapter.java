package com.example.funboxtestapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funboxtestapp.R;
import com.example.funboxtestapp.interfaces.IFragment;
import com.example.funboxtestapp.db.Product;
import com.example.funboxtestapp.ui.fragments.DetailFragment;
import com.example.funboxtestapp.ui.fragments.EditFragment;

import java.util.ArrayList;

public class StoreFragmentRecyclerAdapter extends RecyclerView.Adapter<StoreFragmentRecyclerAdapter.StoreItemViewHolder> {

    private Context mContext;
    private ArrayList<Product> mProducts;
    private boolean isAdmin;

    public StoreFragmentRecyclerAdapter(Context context, ArrayList<Product> products, boolean isAdmin) {
        this.mContext = context;
        this.mProducts = products;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_store_recycler_view_item, parent, false);
        return new StoreFragmentRecyclerAdapter.StoreItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemViewHolder holder, int position) {
        holder.nameTextView.setText(mProducts.get(position).getName());
        holder.countTextView.setText(String.format("%d шт.", mProducts.get(position).getCount()));
        if(isAdmin){
            holder.itemView.setOnClickListener(v -> openDetailFragment(new EditFragment(mProducts.get(position)), "EDIT_FRAGMENT"));
        }else {
            holder.itemView.setOnClickListener(v -> openDetailFragment(new DetailFragment(position), "DETAIL_FRAGMENT"));
        }
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }


    private void openDetailFragment(Fragment fragment, String tag) {
        ((IFragment) mContext).setFragment(fragment, tag);
    }

    public class StoreItemViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        TextView countTextView;
        public StoreItemViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.fragment_store_recycler_view_item_text_view_name);
            countTextView = itemView.findViewById(R.id.fragment_store_recycler_view_item_text_view_count);
        }

    }
}
