package com.example.funboxtestapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.funboxtestapp.App;
import com.example.funboxtestapp.R;
import com.example.funboxtestapp.interfaces.IFragment;
import com.example.funboxtestapp.ui.fragments.BackFragment;
import com.example.funboxtestapp.ui.fragments.StoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements IFragment {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_fragment_main);
        if (currentFragment == null) {
            setFragment(new StoreFragment(), StoreFragment.class.getSimpleName());
            bottomNavigationView.setSelectedItemId(R.id.front_bottom_navigation_item);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.front_bottom_navigation_item:
                            setFragment(new StoreFragment(), StoreFragment.class.getSimpleName());
                            return true;
                        case R.id.back_bottom_navigation_item:
                            setFragment(new BackFragment(), BackFragment.class.getSimpleName());
                            return true;
                    }
                    return false;
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            App.closeDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            this.finish();
        }
    }

    @Override
    public void setFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_fragment_main, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentBackStack() {
        getSupportFragmentManager().popBackStack();
    }

}
