package com.capstone.atyourservice_capstone2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PlumbersProfileSettings extends Fragment {

    private TextView emailtxt_plumber,uid_plumber,firstname_plumber,lastname_plumber,birthdate_plumber;
    private DatabaseReference reff;
    private FirebaseDatabase firebaseDatabase;

    public String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_plumbers_profile_settings, container, false);

        // =========Set up ID for declared objects====
        emailtxt_plumber=(TextView) view.findViewById(R.id.plumberEmail_txtview);
        uid_plumber=(TextView) view.findViewById(R.id.plumber_uid);
        firstname_plumber=(TextView) view.findViewById(R.id.plumberFirstname_txtview);
        lastname_plumber=(TextView) view.findViewById(R.id.plumberLastname_txtview);
        birthdate_plumber = (TextView) view.findViewById(R.id.plumberbirthdate_txtview);

        //=================invoking fetch data method=================


        fetchData();
        return view;
    }


    public void fetchData(){
        //=========Accessing User Credentials=============
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();

            //========saving data into textviews======

            emailtxt_plumber.setText(email);
            uid_plumber.setText(uid);

            //   =================== fetching data from firebase database==========
            reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();
                    String birthdate = snapshot.child("birthdate").getValue().toString();


                    birthdate_plumber.setText(birthdate);
                    firstname_plumber.setText(firstname);
                    lastname_plumber.setText(lastname);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    }
}