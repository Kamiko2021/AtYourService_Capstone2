package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondPage_plumber extends AppCompatActivity {

    BottomNavigationView bottomNavi;
    DatabaseReference reff;

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
                    case R.id.signout_plumber:
                        signOut();
                        break;
                }
            }
        });
    }
    //sign out method...
    private void signOut(){
        reff = FirebaseDatabase.getInstance().getReference().child("waiting").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String currentDate = snapshot.child("currentdate").getValue().toString();
                        String firstname = snapshot.child("firstname").getValue().toString();
                        String lastname = snapshot.child("lastname").getValue().toString();
                        String latitude = snapshot.child("latitude").getValue().toString();
                        String longhitude = snapshot.child("longhitude").getValue().toString();
                        String status=snapshot.child("status").getValue().toString();
                        String uid=snapshot.child("uid").getValue().toString();

                        WaitingData ready= new WaitingData(firstname,lastname, "offline",longhitude,latitude, uid,currentDate);
                        FirebaseDatabase.getInstance().getReference("waiting")
                                .child(uid).setValue(ready).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SecondPage_plumber.this, "logged out successfully", Toast.LENGTH_LONG).show();
                                            Intent prof=new Intent(SecondPage_plumber.this, FirstPage.class);
                                            startActivity(prof);
                                        }else {
                                            Toast.makeText(SecondPage_plumber.this, "something went wrong :(", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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