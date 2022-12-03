package com.capstone.atyourservice_capstone2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class home_clientFragment extends Fragment {

    private TextView uid_client,firstname_client, locationtxt;
    private DatabaseReference reff;
    private CircleImageView profileImageView;
    private ImageView transactionHistory, nearbyPlumbers, profileSettings;
    private FirebaseDatabase firebaseDatabase;
    public String uid;
    public String latitude;
    public String longht;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient client;
    private StorageReference displayStorageRef;


    //Initialize progress dialog
    LoadingDialog loadingDialog=new LoadingDialog(getActivity());



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home_client, container, false);
        //======initialize loading dialog======
        final LoadingDialog dialog=new LoadingDialog(getActivity());
        dialog.startLoadingDialog();

        //====pushnotif token==



        // =========Set up ID for declared objects====
        uid_client=(TextView) view.findViewById(R.id.client_uid);
        firstname_client=(TextView) view.findViewById(R.id.clientFirstname_txtview);
        profileImageView = (CircleImageView) view.findViewById(R.id.ProfilePicx_client);
        locationtxt = (TextView) view.findViewById(R.id.locationClient_data);

        //=========initialize location client===
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        fetchData();

        //====== it delays asking location permission into 1sec========
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
        }, 500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchprofilepicAndDisplay();
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                savedLocation();

            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismissDialog();
            }
        }, 3000);


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


            uid_client.setText(uid);

            //   =================== fetching data from firebase database==========
            reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();

                    firstname_client.setText(firstname + " " + lastname);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //===========saving user state data into database====


        }
    }

    //=======================displays profile picture from database...
    private void fetchprofilepicAndDisplay(){
        try {
            reff = FirebaseDatabase.getInstance().getReference().child("uploads").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String profile = snapshot.child("ProfilePicture").getValue().toString();

                    displayStorageRef = FirebaseStorage.getInstance().getReference().child("uploads/"+ profile);

                    try {
                        final File localFile = File.createTempFile(profile, "jpg");
                        displayStorageRef.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        profileImageView.setImageBitmap(bitmap);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
//            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        }

    }

    //=============get location latlang======
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

    private void savedLocation(){
        locationData location_data=new locationData(latitude,longht);
        FirebaseDatabase.getInstance().getReference("locations").child(uid)
                .setValue(location_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "location saved", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(), "location not saved", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}