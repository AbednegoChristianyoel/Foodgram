package com.example.foodgram;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNav extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ExploreFragment exploreFragment = new ExploreFragment();
    ProfileFragment settingsFragment = new ProfileFragment();
    LikeFragment likeFragment = new LikeFragment();
    PostFragment postFragment = new PostFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav);


        bottomNavigationView  = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                       return true;
                    case R.id.explore:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ExploreFragment()).commit();
                        return true;
                    case R.id.post:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new PostFragment()).commit();
                        return true;
                    case R.id.like:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new LikeFragment()).commit();
                        return true;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileFragment()).commit();
                        return true;
                }

            }
        });

    }
}