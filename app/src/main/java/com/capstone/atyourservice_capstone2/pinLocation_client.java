package com.capstone.atyourservice_capstone2;

import static java.lang.Math.PI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class pinLocation_client extends AppCompatActivity {

    TextView firstname_txt,lastname_txt,status_txt,latitude_txt,longhitude_txt,uid_txt;
    TextView firstname_clientTxt,lastname_clientTxt,uid_clientTxt,longhitude_clientTxt,latitude_clientTxt,distance_txt,UserType_client,location_txt;
    WebView pinMap;
    Button pinMap_btn;
    DatabaseReference reff;
    String client_address;
    public double lat_client,lng_client;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_location_client);

        //=======initializing the textviews=======
        firstname_txt=(TextView) findViewById(R.id.firstname_plumber);
        lastname_txt=(TextView) findViewById(R.id.lastname_plumber);
        status_txt=(TextView) findViewById(R.id.status_plumber);
        latitude_txt=(TextView) findViewById(R.id.latitude_plumber);
        longhitude_txt=(TextView) findViewById(R.id.longhitude_plumber);
        uid_txt=(TextView) findViewById(R.id.uid_plumber);

        firstname_clientTxt=(TextView) findViewById(R.id.Firstname_Client);
        lastname_clientTxt=(TextView) findViewById(R.id.Lastname_Client);
        uid_clientTxt = (TextView) findViewById(R.id.uid_Client);
        latitude_clientTxt=(TextView) findViewById(R.id.latitude_Client);
        longhitude_clientTxt=(TextView) findViewById(R.id.longhitude_Client);
        distance_txt=(TextView) findViewById(R.id.dist_txt);
        UserType_client=(TextView) findViewById(R.id.ClientUserType_txt);
        location_txt=(TextView) findViewById(R.id.location_address);


        //=====pinMap buttn ==============
        pinMap_btn = (Button) findViewById(R.id.pinClientlocation_btn);


        pinMap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude_clientTxt.setText(""+lat_client);
                longhitude_clientTxt.setText(""+lng_client);

                double plumLat=Double.parseDouble(latitude_txt.getText().toString());
                double plumLng=Double.parseDouble(longhitude_txt.getText().toString());
                double dist=distance(lat_client,lng_client,plumLat,plumLng)/0.621371;
                distance_txt.setText(String.format("%.3f", dist)+" km");

                getLocationAddress();
                saveNewlocation();
            }
        });

        //===========pinMap Location==============

        pinMap = (WebView) findViewById(R.id.pinMapLocation);
        pinMap.getSettings().setJavaScriptEnabled(true);
        pinMap.addJavascriptInterface(this, "android");
        pinMap.addJavascriptInterface(this, "getLnglat");
        pinMap.loadUrl("file:///android_asset/mapboxdraggablemap.html");


//        setContentView(pinMap);

        //========fetching data from previous activity===
        Intent intent=getIntent();
        String FN_plumber=intent.getExtras().getString("firstname");
        String LN_plumber=intent.getExtras().getString("lastname");
        String stat_plumber=intent.getExtras().getString("status");
        String lat_plumber=intent.getExtras().getString("latitude");
        String lng_plumber=intent.getExtras().getString("longhitude");
        String uid_plumber=intent.getExtras().getString("uid");

        //=====displaying data into textviews=====

        firstname_txt.setText(FN_plumber);
        lastname_txt.setText(LN_plumber);
        status_txt.setText(stat_plumber);
        latitude_txt.setText(lat_plumber);
        longhitude_txt.setText(lng_plumber);
        uid_txt.setText(uid_plumber);


        fetchData();


    }
    public void fetchData(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){

            String uid=user.getUid();

            uid_clientTxt.setText(uid);

            //   =================== fetching data from firebase database==========
            reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstname = snapshot.child("firstname").getValue().toString();
                    String lastname = snapshot.child("lastname").getValue().toString();
                    String userType=snapshot.child("userType").getValue().toString();

                    firstname_clientTxt.setText(firstname);
                    lastname_clientTxt.setText(lastname);
                    UserType_client.setText(userType);
                 reff=FirebaseDatabase.getInstance().getReference("locations").child(uid);
                 reff.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String lat=snapshot.child("lat").getValue().toString();
                        String lng=snapshot.child("lng").getValue().toString();

                         latitude_clientTxt.setText(lat);
                         longhitude_clientTxt.setText(lng);

                         lat_client=Double.parseDouble(lat);
                         lng_client=Double.parseDouble(lng);
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }
    }
    public void getLocationAddress(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        String address=null;
        String city=null;
        String state=null;
        String country=null;
        String postalCode=null;
        String knownName=null;

        try {
            addresses = geocoder.getFromLocation(lat_client, lng_client, 1);

            address=addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();
        }catch (IOException e){
            e.printStackTrace();
        }
        client_address= city+", "+state+", "+country+", "+postalCode+", "+ knownName;
        location_txt.setText(client_address);
        Toast.makeText(this, ""+city+", "+state+", "+country+", "+postalCode+", "+ knownName, Toast.LENGTH_LONG).show();

    }

    public void saveNewlocation(){
        String client_uid= uid_clientTxt.getText().toString();
        locationData location_data=new locationData(""+lat_client,""+lng_client);
        FirebaseDatabase.getInstance().getReference("locations").child(client_uid)
                .setValue(location_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(pinLocation_client.this, "location saved", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(pinLocation_client.this, "location not saved", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @JavascriptInterface
    public void setLng(double lng,double lat){
        lng_client=lng;
        lat_client=lat;

    }
    @JavascriptInterface
    public double getLngLat(int a){
       double client_lat=0.0;
       double client_lng;
        if (a==1){
            client_lat=lat_client;
            return client_lat;
       }else if (a==2){
            client_lng=lng_client;
            return lng_client;
        }
       return client_lat;
    }

    //======code in getting the destance=====
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / PI);
    }

}