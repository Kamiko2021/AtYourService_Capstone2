package com.capstone.atyourservice_capstone2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class home_plumberFragment extends Fragment {


    private TextView uid_plumber,firstname_plumber;
    private DatabaseReference reff;
    private CircleImageView profileImageView;
    private ImageView transactionHistory, nearbyPlumbers, profileSettings;
    private FirebaseDatabase firebaseDatabase;
    public String uid;
    private FirebaseAuth mAuth;

    private FirebaseStorage storage;
    private StorageReference storageReference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_homeplumber, container, false);

        // =========Set up ID for declared objects====

        uid_plumber=(TextView) view.findViewById(R.id.plumber_uid);
        firstname_plumber=(TextView) view.findViewById(R.id.plumberFirstname_txtview);
        profileImageView = (CircleImageView) view.findViewById(R.id.ProfilePicx);
        //instantiate firebase storage..
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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


            uid_plumber.setText(uid);

            //   =================== fetching data from firebase database==========
            reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();

                    firstname_plumber.setText(firstname + " " + lastname);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



}