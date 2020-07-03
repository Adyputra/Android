package com.example.silaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.silaper.profil.ProfilUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView  mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
//    private ShopFragment shopFragment;
//    private HelpFragment helpFragment;
    private ProfilUser profilUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bott_navigation);

        homeFragment = new HomeFragment();
//        shopFragment = new ShopFragment();
//        helpFragment = new HelpFragment();
        profilUser = new ProfilUser();
        loadFragment(homeFragment);
        BottomNavigationView navView = findViewById(R.id.main_nav);
        navView.setOnNavigationItemSelectedListener(this);



    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.nav_home :
                fragment = homeFragment;
                break;
            case R.id.nav_akun :
                fragment = profilUser;
                break;
//            case R.id.nav_shop:
//                fragment = shopFragment;
//                break;
//            case R.id.nav_help:
//                fragment = helpFragment;
//                break;
        }
        return loadFragment(fragment);
    }
}
