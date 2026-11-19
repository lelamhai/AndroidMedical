package com.example.androidmedical.screens;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.androidmedical.R;
import com.example.androidmedical.adapters.MainPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreen extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.nav_campaign);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.nav_register);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.nav_notification);
                        break;
                    case 3:
                    default:
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_campaign) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_register) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.nav_notification) {
                viewPager.setCurrentItem(2, true);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(3, true);
                return true;
            }
            return false;
        });
    }
}
