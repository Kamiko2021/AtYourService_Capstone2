package com.capstone.atyourservice_capstone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class transaction_plumber extends AppCompatActivity {
    //==== String initialization====
    String firstname,lastname,uid_client,distance,address,dateNow,serviceRequest;
    String plumber_uid;
    //===== Client TextViews initialization====
    TextView client_fullname,client_uidTxt,client_distance,client_serviceRequest,client_address,client_dateNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_plumber);

        //textview assigning id
        client_fullname=(TextView) findViewById(R.id.client_fullname);
        client_uidTxt=(TextView) findViewById(R.id.uid_clienttxt);
        client_distance= (TextView) findViewById(R.id.distanceData);
        client_dateNow = (TextView) findViewById(R.id.transactionDatedata);
        client_serviceRequest = (TextView) findViewById(R.id.servicedata);
        client_address = (TextView) findViewById(R.id.address_txt);

        //fetching data from notification
        Intent intent=getIntent();
        firstname=intent.getExtras().getString("firstname");
        lastname=intent.getExtras().getString("lastname");
        uid_client=intent.getExtras().getString("client_uid");
        dateNow=intent.getExtras().getString("dateNow");
        distance=intent.getExtras().getString("distance");
        address=intent.getExtras().getString("address");
        serviceRequest=intent.getExtras().getString("serviceRequest");

        //displaying text to textviews
        client_fullname.setText(firstname +" "+ lastname);
        client_uidTxt.setText(uid_client);
        client_distance.setText(distance);
        client_dateNow.setText(dateNow);
        client_serviceRequest.setText(serviceRequest);
        client_address.setText(serviceRequest);
    }

}