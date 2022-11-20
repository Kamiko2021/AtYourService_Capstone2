package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class transaction_plumber extends AppCompatActivity {
    //==== String initialization====
    String firstname,lastname,uid_client,distance,address,dateNow,serviceRequest,client_latdata,client_lngdata;
    String plumber_firstname,plumber_lastname,plumber_lat,plumber_lng,plumber_stat,plumber_uid;
    String concernmsg,servicefee,total,transaction_fee;
    CircleImageView profileIMG_client,profileIMG_plumber;


    //===== Client TextViews initialization====
    TextView client_fullname,client_uidTxt,client_distance,client_serviceRequest,client_address,client_dateNow,client_lattxt,client_lngtxt;
    TextView plumber_fullname,plumber_uidtxt,plumber_stattxt,plumber_lattxt,plumber_lngtxt;
    TextView concernMessage,servicefeetxt,totaltxt,transactionFee;
    DatabaseReference reff;
    StorageReference displayStorageRef;
    Button paymentBtn;
    WebView transaction_map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_plumber);
        //===== fetching the plumbers uid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        plumber_uid = user.getUid();


        //textview assigning id for plumber
        plumber_fullname = (TextView) findViewById(R.id.plumber_fullname);
        plumber_uidtxt=(TextView) findViewById(R.id.plumber_uid);
        plumber_stattxt=(TextView) findViewById(R.id.plumber_status);
        plumber_lattxt=(TextView) findViewById(R.id.plumber_latitude);
        plumber_lngtxt=(TextView) findViewById(R.id.plumber_longhitude);
        concernMessage = (TextView) findViewById(R.id.concernmsgdata);
        servicefeetxt = (TextView) findViewById(R.id.servicefeeData);
        profileIMG_plumber = (CircleImageView) findViewById(R.id.profileImg_plumber);
        totaltxt=(TextView) findViewById(R.id.totaldata);
        transactionFee=(TextView) findViewById(R.id.transactionFeedata);
        paymentBtn = (Button) findViewById(R.id.pendingRequest);

        //textview assigning id
        client_fullname=(TextView) findViewById(R.id.client_fullname);
        client_uidTxt=(TextView) findViewById(R.id.uid_clienttxt);
        client_distance= (TextView) findViewById(R.id.distanceData);
        client_dateNow = (TextView) findViewById(R.id.transactionDatedata);
        client_serviceRequest = (TextView) findViewById(R.id.servicedata);
        client_address = (TextView) findViewById(R.id.address_txt);
        client_lattxt=(TextView) findViewById(R.id.lat_clienttxt);
        client_lngtxt=(TextView) findViewById(R.id.lng_clienttxt);
        profileIMG_client=(CircleImageView) findViewById(R.id.profileImg_client);


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



        fetchTransaction();
        fetchprofilepicAndDisplay(plumber_uid, "Plumber");
        fetchprofilepicAndDisplay(uid_client, "Client");
        //------ initializing map ---
        transaction_map = (WebView) findViewById(R.id.transaction_map);
        transaction_map.getSettings().setJavaScriptEnabled(true);
        transaction_map.addJavascriptInterface(this, "android");
        transaction_map.loadUrl("file:///android_asset/mapmarker.html");

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    //=======================displays profile picture from database...
    private void fetchprofilepicAndDisplay(String uid, String userType){
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

                                     if (userType.equals("Plumber")){
                                         Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                         profileIMG_plumber.setImageBitmap(bitmap);
                                     } else if (userType.equals("Client")){
                                         Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                         profileIMG_client.setImageBitmap(bitmap);
                                     }


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

    public void fetchTransaction(){
        reff = FirebaseDatabase.getInstance().getReference("transaction_plumber").child(plumber_uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                plumber_firstname=snapshot.child("plumber_firstname").getValue().toString();
                plumber_lastname=snapshot.child("plumber_lastname").getValue().toString();
                plumber_lat=snapshot.child("plumber_lat").getValue().toString();
                plumber_lng=snapshot.child("plumber_lng").getValue().toString();
                plumber_stat=snapshot.child("plumber_stat").getValue().toString();
                client_latdata=snapshot.child("Client_lat").getValue().toString();
                client_lngdata=snapshot.child("Client_lng").getValue().toString();
                servicefee=snapshot.child("servicefee_data").getValue().toString();
                total=snapshot.child("total_data").getValue().toString();
                transaction_fee = snapshot.child("transactionfee_data").getValue().toString();

                plumber_fullname.setText(plumber_firstname +" "+ plumber_lastname);
                plumber_lattxt.setText(plumber_lat);
                plumber_lngtxt.setText(plumber_lng);
                plumber_stattxt.setText(plumber_stat);
                plumber_uidtxt.setText(plumber_uid);
                client_lattxt.setText(client_latdata);
                client_lngtxt.setText(client_lngdata);
                servicefeetxt.setText(servicefee);
                concernMessage.setText(concernmsg);
                totaltxt.setText(total);
                transactionFee.setText(transaction_fee);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @JavascriptInterface
    public double getLnglat(int a){
        double lat=0.0;
        double lng;
        if (a==0){
            lng=Double.parseDouble(plumber_lng);
            return lng;
        }else if (a==1){
            lat= Double.parseDouble(plumber_lat);
            return lat;
        }else if (a==2){
            lng=Double.parseDouble(client_lngdata);
            return lng;
        }else if (a==3){
            lat=Double.parseDouble(client_latdata);
            return lat;
        }
        return lat;
    }

}