package com.capstone.atyourservice_capstone2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class home_plumberFragment extends Fragment {


    private TextView uid_plumber,firstname_plumber, locationtxt;
    private DatabaseReference reff;
    private CircleImageView profileImageView;
    private ImageView transactionHistory, nearbyPlumbers, profileSettings;
    private FirebaseDatabase firebaseDatabase;
    public String uid;
    private FirebaseAuth mAuth;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    //===== declare fusedclient provider====
    private FusedLocationProviderClient client;
    //=====declare global variable string to get the latitude and longhitude===
    public String latitude;
    public String longht;
    public String firstname_data,lastname_data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_homeplumber, container, false);



        // =========Set up ID for declared objects====

        uid_plumber=(TextView) view.findViewById(R.id.plumber_uid);
        firstname_plumber=(TextView) view.findViewById(R.id.plumberFirstname_txtview);
        profileImageView = (CircleImageView) view.findViewById(R.id.ProfilePicx);
        locationtxt = (TextView) view.findViewById(R.id.location_data);
        //instantiate firebase storage..
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //=========initialize location client===
        client = LocationServices.getFusedLocationProviderClient(getActivity());
//        getCurrentLocation();
        locationtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //=====check condition====
                if (ContextCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity()
                ,Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                    //When permission is granted, call method
                    getCurrentLocation();
                }else {
                    //when permission is not granted..
                    //request permission
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    ,Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });
        firstname_plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyToAcceptJob();
            }
        });


        //=================invoking fetch data method=================
        fetchData();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if (requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0]+grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            //When permission are granted
            //call method
            getCurrentLocation();
        }else {
            //When permission are denied
            //Display toast
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        //Initialize location manager
        LocationManager locationManager= (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        //check condition..
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //when location service is enabled..
            //Get last location..
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location

                    Location location = task.getResult();
                    //check condition
                    if (location!=null){
                        //when result location is not null
                        //set latitude

                        String lat= String.valueOf(location.getLatitude());
                        String lng= String.valueOf(location.getLongitude());

                        //save lat and longhitude string value to declared global variable..
                        latitude = lat;
                        longht = lng;

                        locationtxt.setText("Lat: " + lat + " Lng: " + lng);

                    }else {
//                        Toast.makeText(getActivity(), "Permission still not granted yet", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
           // when location service is not enabled..
           // Open location setting..
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
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

                    firstname_data = firstname;
                    lastname_data = lastname;
                    firstname_plumber.setText(firstname + " " + lastname);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

           //===========saving user state data into database====



        }
    }
    //=========creating and saving data into firebase database=====
    public void readyToAcceptJob(){
        //get current date...
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        WaitingData ready= new WaitingData(firstname_data,lastname_data, "2", longht, latitude, "1km");
        FirebaseDatabase.getInstance().getReference("waiting")
                .child(uid).setValue(ready).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Your now added in waiting list", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(), "something went wrong :(", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}