package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;


public class DashBoardClient extends AppCompatActivity {

    private TextView emailtxt,uid_client,firstname_client,lastname_client,birthdate_client;
    private DatabaseReference reff;
    private FirebaseDatabase firebaseDatabase;

    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // =========Set up ID for declared objects====
        emailtxt=(TextView) findViewById(R.id.clientemail_profiledata);
        uid_client=(TextView) findViewById(R.id.uid_lbl);
        firstname_client=(TextView) findViewById(R.id.clientfirstname_profiledata);
        lastname_client=(TextView) findViewById(R.id.clientlastname_profiledata);
        birthdate_client = (TextView) findViewById(R.id.clientbirthdate_profile);

        //=================invoking fetch data method=================

        fetchData();

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
            uid_client.setText(uid);

            //   =================== fetching data from firebase database==========
            reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();
                    String birthdate = snapshot.child("birthdate").getValue().toString();


                    birthdate_client.setText(birthdate);
                    firstname_client.setText(firstname);
                    lastname_client.setText(lastname);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }
}