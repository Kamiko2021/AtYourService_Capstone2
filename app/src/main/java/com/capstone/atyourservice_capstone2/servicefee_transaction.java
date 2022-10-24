package com.capstone.atyourservice_capstone2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class servicefee_transaction extends Fragment {

    public TextView uid_plumber,firstname_plumber, locationtxt;
    public EditText undergroundPipes,abovegroundPipes,cloggedToilet,cloggedDrainage;
    private Button readyBtn,setServiceBtn;
    private CircleImageView profileImageView;
    private DatabaseReference reff;
    public String uid,firstname_data,lastname_data;
    //==========latlng string declaration====================
    public String latitude;
    public String longht;

    private FusedLocationProviderClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_servicefee_transaction, container, false);
        //=========declaration for objects==========
        //------TextViews---------
        uid_plumber=(TextView) view.findViewById(R.id.plumber_uid);
        firstname_plumber=(TextView) view.findViewById(R.id.plumberFirstname_txtview);
        profileImageView = (CircleImageView) view.findViewById(R.id.ProfilePicx);
        locationtxt = (TextView) view.findViewById(R.id.locationPlumber_data);
        //---------Button-----------
        readyBtn = (Button) view.findViewById(R.id.startWaitingClient_btn);
        setServiceBtn = (Button) view.findViewById(R.id.setServiceFee_btn);
        //----------EditTexts--------------
        undergroundPipes = (EditText) view.findViewById(R.id.undergroundPipes_txt);
        abovegroundPipes = (EditText) view.findViewById(R.id.abovegroundPipes_txt);
        cloggedToilet = (EditText) view.findViewById(R.id.cloggedToilet_txt);
        cloggedDrainage = (EditText) view.findViewById(R.id.cloggedDrainage_txt);
        //=========initialize location client===
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        //========================onclick events================
        readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyToAcceptJob();
            }
        });
        setServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setServiceFee();
            }
        });

        fetchData();
        getCurrentLocation();
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

    //===========method in getting the user location======================

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

    //==========Retrieving data from firebase database===========
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

    //=================if the user is ready his service and can be viewed from waiting list=============
    public void readyToAcceptJob(){
        //get current date...
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        WaitingData ready= new WaitingData(firstname_data,lastname_data, "2", longht, latitude, uid,thisDate);
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

    //=======setting service fee transaction==========
    public void setServiceFee(){
        String aboveground_Pipes,underground_Pipes,clogged_Toilet,clogged_Drainage,uid_data;
        //========fetching data into EditTexts====
       underground_Pipes = undergroundPipes.getText().toString();
       aboveground_Pipes = abovegroundPipes.getText().toString();
       clogged_Drainage = cloggedDrainage.getText().toString();
       clogged_Toilet = cloggedToilet.getText().toString();
       uid_data = uid_plumber.getText().toString();

        ServiceData service = new ServiceData(uid,underground_Pipes,aboveground_Pipes,clogged_Toilet,clogged_Drainage);
        FirebaseDatabase.getInstance().getReference("service")
                .child(uid).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "service fee set properly", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(), "service fee set failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

}