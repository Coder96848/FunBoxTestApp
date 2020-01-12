package com.example.funboxtestapp.interfaces;

import androidx.fragment.app.Fragment;

public interface IFragment {
        void setFragment(Fragment fragment, String tag);
        void onFragmentBackStack();
}
