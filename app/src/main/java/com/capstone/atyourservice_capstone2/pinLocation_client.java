package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class pinLocation_client extends AppCompatActivity {

    TextView firstname_txt,lastname_txt,status_txt,latitude_txt,longhitude_txt,uid_txt;
    TextView firstname_clientTxt,lastname_clientTxt,uid_clientTxt,longhitude_clientTxt,latitude_clientTxt;
    WebView pinMap;
    Button pinMap_btn;
    DatabaseReference reff;
    public double lat_client,lng_client;


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

        //=====pinMap buttn ==============
        pinMap_btn = (Button) findViewById(R.id.pinClientlocation_btn);


        pinMap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude_clientTxt.setText(""+lat_client);
                longhitude_clientTxt.setText(""+lng_client);
            }
        });

        //===========pinMap Location==============

        pinMap = (WebView) findViewById(R.id.pinMapLocation);
        pinMap.getSettings().setJavaScriptEnabled(true);
        pinMap.addJavascriptInterface(this, "android");
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

                    firstname_clientTxt.setText("FN: "+firstname);
                    lastname_clientTxt.setText("LN: "+ lastname);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @JavascriptInterface
    public void setLng(double lng,double lat){
        lng_client=lng;
        lat_client=lat;
    }

}