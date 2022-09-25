package com.capstone.atyourservice_capstone2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ClientProfileSettings extends Fragment {

   private TextView emailtxt,uid_data,fullname_data,birthdate_client;
   private DatabaseReference reff;
   private FirebaseDatabase firebaseDatabase;
   public String uid;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_client_profile_settings, container, false);

       // =========Set up ID for declared objects====
        emailtxt=(TextView) view.findViewById(R.id.email_txt);
        uid_data=(TextView) view.findViewById(R.id.uid_lbl);
        fullname_data=(TextView) view.findViewById(R.id.fullname_txt);
        birthdate_client = (TextView) view.findViewById(R.id.birthdate_txt);

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

            emailtxt.setText(email);
            uid_data.setText(uid);

            //   =================== fetching data from firebase database==========
            reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String fullname = snapshot.child("fullname").getValue().toString();
                    String birthdate = snapshot.child("birthdate").getValue().toString();

                    birthdate_client.setText(birthdate);
                    fullname_data.setText(fullname);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




   }


}