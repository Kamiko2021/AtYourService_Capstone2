package com.capstone.atyourservice_capstone2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class profile_plumbers extends Fragment {

    public EditText email_txt, firstname_txt,lastname_txt,birthdate_txt,gender_txt;
    public TextView usertype_txt, uid_data;
    public DatabaseReference reff;
    public FirebaseDatabase firebaseDatabase;

    public String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_plumbers, container, false);

        //----------declaration of editexts---------------
        email_txt = (EditText) view.findViewById(R.id.email_plumberData);
        firstname_txt = (EditText) view.findViewById(R.id.firstname_plumberData);
        lastname_txt = (EditText) view.findViewById(R.id.lastname_plumberData);
        birthdate_txt = (EditText) view.findViewById(R.id.birthdate_plumberData);
        gender_txt = (EditText) view.findViewById(R.id.gender_plumberData);
        //-----------declaration of textview------------
        uid_data = (TextView)view.findViewById(R.id.uid_lbl);
        usertype_txt=(TextView) view.findViewById(R.id.usertype_Data);
        fetchData();
        return view;
    }


    //----------methods in retrieving data into database---------
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

            uid_data.setText(uid);

            //   =================== fetching data from firebase database==========
            reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //-----------saving data from firebasedatabase----------
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();
                    String gender = snapshot.child("gender").getValue().toString();
                    String birthdate = snapshot.child("birthdate").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String usertype = snapshot.child("userType").getValue().toString();

                    //------------displaying data into edittxt fields----------
                    email_txt.setText(email);
                    firstname_txt.setText(firstname);
                    lastname_txt.setText(lastname);
                    gender_txt.setText(gender);
                    birthdate_txt.setText(birthdate);
                    //-------------displaying data into textview------------
                    usertype_txt.setText(usertype);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}