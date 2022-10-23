package com.capstone.atyourservice_capstone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class hiring_details extends AppCompatActivity {

    TextView firstname_hiring,lastname_hiring,status_hiring,latitude_hiring,longhitude_hiring,uid_hiring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiring_details);


        firstname_hiring = (TextView) findViewById(R.id.Firstname_htv);
        lastname_hiring = (TextView) findViewById(R.id.Lastname_htv);
        status_hiring = (TextView) findViewById(R.id.status_htv);
        latitude_hiring = (TextView) findViewById(R.id.latitude_htv);
        longhitude_hiring = (TextView) findViewById(R.id.longhitude_htv);
        uid_hiring = (TextView) findViewById(R.id.uid_htv);

        Intent intent=getIntent();
        String firstname=intent.getExtras().getString("firstname");
        String lastname=intent.getExtras().getString("lastname");
        String status=intent.getExtras().getString("status");
        String lat=intent.getExtras().getString("latitude");
        String lng=intent.getExtras().getString("longhitude");
        String uid=intent.getExtras().getString("uid");

        firstname_hiring.setText(firstname);
        lastname_hiring.setText(lastname);
        status_hiring.setText(status);
        latitude_hiring.setText (lat);
        longhitude_hiring.setText(lng);
        uid_hiring.setText(uid);
    }
}