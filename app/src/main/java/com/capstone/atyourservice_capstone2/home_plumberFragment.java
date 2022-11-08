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


    //===== declare fusedclient provider====
    private FusedLocationProviderClient client;
    //=====declare global variable string to get the latitude and longhitude===
    public String latitude;
    public String longht;
    public String firstname_data,lastname_data;
    private StorageReference mStorageRef;
    private StorageReference displayStorageRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_homeplumber, container, false);
        //======initialize loading dialog======
        final LoadingDialog dialog=new LoadingDialog(getActivity());
        dialog.startLoadingDialog();

        // =========Set up ID for declared objects====
        uid_plumber=(TextView) view.findViewById(R.id.plumber_uid);
        firstname_plumber=(TextView) view.findViewById(R.id.plumberFirstname_txtview);
        profileImageView = (CircleImageView) view.findViewById(R.id.ProfilePicx);
        locationtxt = (TextView) view.findViewById(R.id.location_data);
        //instantiate firebase storage..

        //=========initialize location client===
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        //====== it delays asking location permission into 1sec========
        final Handler handler = new Handler(Looper.getMainLooper());
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
        }, 1000);

        //=================invoking fetch data method=================
        fetchData();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchprofilepicAndDisplay();
            }
        },2000);
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               savedLocation();
           }
       }, 3000);
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               readyToAcceptJob();
           }
       },4000);
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               dialog.dismissDialog();
           }
       }, 5000);



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

    //displays profile picture from database...
    private void fetchprofilepicAndDisplay(){
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
                                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    profileImageView.setImageBitmap(bitmap);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
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
    }

    //=================if the user is ready his service and can be viewed from waiting list=============
    public void readyToAcceptJob(){
        //get current date...
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        WaitingData ready= new WaitingData(firstname_data,lastname_data, "online",longht,latitude, uid,thisDate);
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