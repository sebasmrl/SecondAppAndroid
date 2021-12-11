package com.example.secondapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.secondapp.R;
import com.example.secondapp.fragments.ChatsFragment;
import com.example.secondapp.fragments.FiltersFragment;
import com.example.secondapp.fragments.HomeFragment;
import com.example.secondapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_Activity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation_menu);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());

    }//fin onCreate
        public void openFragment(Fragment fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_home, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if(item.getItemId() == R.id.item_home) {
                            openFragment(new HomeFragment());
                            return true;
                        }else if(item.getItemId() == R.id.item_filtros){
                            openFragment(new FiltersFragment());
                            return true;
                        }else if(item.getItemId() == R.id.item_chat){
                            openFragment(new ChatsFragment());
                            return true;
                        }else if(item.getItemId() == R.id.item_perfil){
                            openFragment(new ProfileFragment());
                            return true;
                        }
                        return false;
                    }
                };

}