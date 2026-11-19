package com.example.androidmedical.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidmedical.fragments.CompaignFragment;
import com.example.androidmedical.fragments.NotificationFragment;
import com.example.androidmedical.fragments.ProfileFragment;
import com.example.androidmedical.fragments.RegisterFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CompaignFragment();
            case 1:
                return new RegisterFragment();
            case 2:
                return new NotificationFragment();
            case 3:
            default:
                return new ProfileFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
