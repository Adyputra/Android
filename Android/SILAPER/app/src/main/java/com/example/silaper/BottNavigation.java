package com.example.silaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottNavigation extends AppCompatActivity {

    private BottomNavigationView  mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private ShopFragment shopFragment;
    private HelpFragment helpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bott_navigation);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        homeFragment = new HomeFragment();
        shopFragment = new ShopFragment();
        helpFragment = new HelpFragment();

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home :
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_shop:
                        setFragment(shopFragment);
                        return true;
                    case R.id.nav_help:
                        setFragment(helpFragment);
                        return true;
                    default:
                        return false;
                }
            }

            private void setFragment(Fragment fragment) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_frame, fragment);
                fragmentTransaction.commit();

            }
        });
    }
}
