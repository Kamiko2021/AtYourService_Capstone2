package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class hiring_details extends AppCompatActivity {

    TextView firstname_hiring,lastname_hiring,status_hiring,latitude_hiring,longhitude_hiring,uid_hiring;
    TextView abovegroundpipes,underleakpipes,cloggeddrainage,cloggedtoilet;
    Button back_btn,hireMe_button;
    DatabaseReference reff;
    String uid_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiring_details);

        //====== declaring and assigning the objects===========
        //---------TextViews-------------
        firstname_hiring = (TextView) findViewById(R.id.Firstname_htv);
        lastname_hiring = (TextView) findViewById(R.id.Lastname_htv);
        status_hiring = (TextView) findViewById(R.id.status_htv);
        latitude_hiring = (TextView) findViewById(R.id.latitude_htv);
        longhitude_hiring = (TextView) findViewById(R.id.longhitude_htv);
        uid_hiring = (TextView) findViewById(R.id.uid_htv);
        abovegroundpipes = (TextView) findViewById(R.id.aboveleakpipes_price);
        underleakpipes = (TextView) findViewById(R.id.underleakpipes_price);
        cloggeddrainage = (TextView) findViewById(R.id.cloggeddrainage_price);
        cloggedtoilet = (TextView) findViewById(R.id.cloggedtoilet_price);
        //---------Buttons----------------
        back_btn=(Button) findViewById(R.id.back_button);
        hireMe_button=(Button) findViewById(R.id.hireMe_btn);
        //==========fetching data from waiting data activity====
        Intent intent=getIntent();
        String firstname=intent.getExtras().getString("firstname");
        String lastname=intent.getExtras().getString("lastname");
        String status=intent.getExtras().getString("status");
        String lat=intent.getExtras().getString("latitude");
        String lng=intent.getExtras().getString("longhitude");
        String uid=intent.getExtras().getString("uid");

        //=====displaying text to textviews======
        firstname_hiring.setText(firstname);
        lastname_hiring.setText(lastname);
        status_hiring.setText(status);
        latitude_hiring.setText (lat);
        longhitude_hiring.setText(lng);
        uid_hiring.setText(uid);

        //===========setting up event listeners==========

        hireMe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferPlumberData();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(hiring_details.this, client_searchActivity.class);
                startActivity(intent1);
            }
        });

        //assigning data from textviews and save it to global variable strings
        uid_data=uid_hiring.getText().toString();
        getServiceFees();

    }

    public void getServiceFees(){


        reff = FirebaseDatabase.getInstance().getReference().child("service").child(uid_data);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String undergroundpipesFee=snapshot.child("undergroundPipes_fee").getValue().toString();
                String abovegroundpipesFee=snapshot.child("aboveground_fee").getValue().toString();
                String cloggeddrainageFee=snapshot.child("cloggedDrainage_fee").getValue().toString();
                String cloggedtoiletFee=snapshot.child("cloggedToilet_fee").getValue().toString();

                //======Displaying data into textview====
                abovegroundpipes.setText(abovegroundpipesFee + " Php");
                underleakpipes.setText(undergroundpipesFee + " Php");
                cloggeddrainage.setText(cloggeddrainageFee + " Php");
                cloggedtoilet.setText(cloggedtoiletFee + " Php");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void transferPlumberData(){
        //----Declaring String Variables--------
        String uid,firstname,lastname,status,latitude,longhitude;
        //----initialize plumbers data into declared variables------
        uid=uid_hiring.getText().toString();
        firstname=firstname_hiring.getText().toString();
        lastname=lastname_hiring.getText().toString();
        status=status_hiring.getText().toString();
        latitude=latitude_hiring.getText().toString();
        longhitude=longhitude_hiring.getText().toString();
        //---------saving intialize to be transfered to pinLocation_client-------
        Intent intent = new Intent(hiring_details.this, pinLocation_client.class);
        intent.putExtra("uid", uid);
        intent.putExtra("firstname", firstname);
        intent.putExtra("lastname", lastname);
        intent.putExtra("status", status);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longhitude", longhitude);
        startActivity(intent);

    }

}