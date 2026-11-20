package com.example.androidmedical.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidmedical.fragments.RegistrationTabFragment;

public class RegistrationTabAdapter extends FragmentStateAdapter {

    public RegistrationTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return RegistrationTabFragment.newInstance("future");
        } else {
            return RegistrationTabFragment.newInstance("history");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
