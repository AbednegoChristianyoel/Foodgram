package com.example.foodgram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodgram.Fragment.ExploreFragment;
import com.example.foodgram.Fragment.HomeFragment;
import com.example.foodgram.Fragment.LikeFragment;
import com.example.foodgram.Fragment.PostFragment;
import com.example.foodgram.Fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class BottomNav extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    HomeFragment homeFragment = new HomeFragment();
    ExploreFragment exploreFragment = new ExploreFragment();
    ProfileFragment settingsFragment = new ProfileFragment();
    LikeFragment likeFragment = new LikeFragment();
    PostFragment postFragment = new PostFragment();

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BottomNav.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav);


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
    }

    private NavigationBarView.OnItemSelectedListener navigationItemSelectedListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                            return true;
                        case R.id.explore:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ExploreFragment()).commit();
                            return true;
                        case R.id.post:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new PostFragment()).commit();
                            return true;
                        case R.id.like:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new LikeFragment()).commit();
                            return true;
                        default:
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                    }
                    return true;
                }
            };
}