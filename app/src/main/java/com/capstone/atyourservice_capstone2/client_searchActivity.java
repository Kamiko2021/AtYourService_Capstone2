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
import com.google.firebase.auth.FirebaseAuth;

public class client_searchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_search);
        bottomNavi = (BottomNavigationView) findViewById(R.id.client_buttomNav);

        replaceFragment(new client_searchPlumber());

        bottomNavi.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_client:
                        replaceFragment(new home_clientFragment());
                        break;
                    case R.id.profile_client:
                        replaceFragment(new profile_client());
                        break;
                    case R.id.search_client:
                        replaceFragment(new client_searchPlumber());
                        break;
                    case R.id.signout_client:
                        FirebaseAuth.getInstance().signOut();
                        Intent prof=new Intent(client_searchActivity.this, ClientLogin.class);
                        startActivity(prof);
                        break;

                }

            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.client_framelayout, fragment);
        fragmentTransaction.commit();
    }
}