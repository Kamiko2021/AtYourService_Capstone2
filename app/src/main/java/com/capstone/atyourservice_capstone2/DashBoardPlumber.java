package com.capstone.atyourservice_capstone2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;



public class DashBoardPlumber extends AppCompatActivity {

    private TextView emailtxt_plumber,uid_plumber,firstname_plumber,lastname_plumber,birthdate_plumber,gender_plumber;
    private DatabaseReference reff;
    private ImageView transactionHistory, nearbyPlumbers, profileSettings;
    private FirebaseDatabase firebaseDatabase;

    public String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_plumber);

        // =========Set up ID for declared objects====
        emailtxt_plumber=(TextView) findViewById(R.id.plumberEmail_txtview);
        uid_plumber=(TextView) findViewById(R.id.plumber_uid);
        firstname_plumber=(TextView) findViewById(R.id.plumberFirstname_txtview);
        lastname_plumber=(TextView) findViewById(R.id.plumberLastname_txtview);
        birthdate_plumber = (TextView) findViewById(R.id.plumberbirthdate_txtview);
        gender_plumber = (TextView) findViewById(R.id.gender_txtview);


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
                    String gender = snapshot.child("gender").getValue().toString();

                    birthdate_plumber.setText(birthdate);
                    firstname_plumber.setText(firstname);
                    lastname_plumber.setText(lastname);
                    gender_plumber.setText(gender);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}