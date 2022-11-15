package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SecondPage_plumber extends AppCompatActivity {

    BottomNavigationView bottomNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        bottomNavi = (BottomNavigationView) findViewById(R.id.bottomNav_plumber);
        replaceFragment(new home_plumberFragment());
        bottomNavi.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_plumber:
                        replaceFragment(new home_plumberFragment());
                        break;
                    case R.id.profile_plumber:
                        replaceFragment(new profile_plumbers());
                        break;
                    case R.id.servicefee_plumber:
                        replaceFragment(new servicefee_transaction());
                        break;
                    case R.id.notification_plumber:
                        replaceFragment(new plumberNotification());
                        break;
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.plumber_framelayout, fragment);
        fragmentTransaction.commit();
    }
}