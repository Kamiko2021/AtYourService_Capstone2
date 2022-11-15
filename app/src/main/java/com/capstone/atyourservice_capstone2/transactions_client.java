package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class transactions_client extends AppCompatActivity {
    //=== Plumber Strings data initialization ========
    String plumber_uid,plumber_firstname,plumber_lastname,plumber_lng,plumber_lat,plumber_stat,plumber_distance,plumber_location;
    String service_data,concernmsg_data,servicefee_data,transactionfee_data,total_data;
    CircleImageView plumberProfilePix;
    //==== Client Strings data initialization ========
    String Client_firstname,Client_lastname,Client_uid,Client_lng,Client_lat;
    //===== Plumber Textviews initialization ======
    TextView plumberUid_txt,plumberFullname_txt,plumberLng_txt,plumberLat_txt,plumberStat_txt,plumberDist_txt,plumberLocation_txt;
    //===== Client Textviews initialization =======
    TextView clientFirstname_txt,clientLastname_txt,clientUid_txt,clientLat_txt,clientLng_txt,addressTxt;
    //====== Transaction TexViews initialization ====
    TextView service_txt,concernmsg_txt,servicefee_txt,transactionfee_txt,total_txt;
    WebView transaction_map;
    DatabaseReference reff;
    StorageReference displayStorageRef;
    LoadingDialog loading=new LoadingDialog(transactions_client.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_client);
        //==== Plumber TextViews initialization ========
        plumberUid_txt = (TextView) findViewById(R.id.plumber_uid);
        plumberFullname_txt = (TextView) findViewById(R.id.plumber_fullname);
        plumberLng_txt = (TextView) findViewById(R.id.plumber_longhitude);
        plumberLat_txt = (TextView) findViewById(R.id.plumber_latitude);
        plumberStat_txt = (TextView) findViewById(R.id.plumber_status);
        plumberDist_txt = (TextView) findViewById(R.id.plumber_distance);
        plumberLocation_txt = (TextView) findViewById(R.id.plumber_location);
        loading.startLoadingDialog();

        //====== Client TextViews Initialization ====
        clientFirstname_txt = (TextView) findViewById(R.id.firstname_clienttxt);
        clientLastname_txt = (TextView) findViewById(R.id.lastname_clienttxt);
        clientUid_txt = (TextView) findViewById(R.id.uid_clienttxt);
        clientLat_txt = (TextView) findViewById(R.id.lat_clienttxt);
        clientLng_txt = (TextView) findViewById(R.id.lng_clienttxt);
        addressTxt = (TextView) findViewById(R.id.address_txt);

        //==== transaction details textviews initialization =====
        service_txt = (TextView) findViewById(R.id.servicedata);
        concernmsg_txt = (TextView) findViewById(R.id.concernmsgdata);
        servicefee_txt = (TextView) findViewById(R.id.servicefeeData);
        transactionfee_txt = (TextView) findViewById(R.id.transactionFeedata);
        total_txt = (TextView) findViewById(R.id.totaldata);


        Intent intent=getIntent();
        //==== fetching and saving transaction data from pinlocation activity
        //---- plumbers -----
        plumberProfilePix = (CircleImageView) findViewById(R.id.profileImg_plumber);

        plumber_uid = intent.getExtras().getString("plumber_uid");
        plumberUid_txt.setText(plumber_uid);


        plumber_firstname = intent.getExtras().getString("plumber_firstname");
        plumber_lastname = intent.getExtras().getString("plumber_lastname");
        plumberFullname_txt.setText(plumber_firstname + " " + plumber_lastname);

        plumber_lng=intent.getExtras().getString("plumber_longhitude");
        plumberLng_txt.setText(plumber_lng);

        plumber_lat=intent.getExtras().getString("plumber_latitude");
        plumberLat_txt.setText(plumber_lat);
        plumber_stat=intent.getExtras().getString("plumber_status");
        plumberStat_txt.setText(plumber_stat);

        plumber_distance=intent.getExtras().getString("plumber_distance");
        plumberDist_txt.setText(plumber_distance);

//        plumber_location = intent.getExtras().getString("plumber_location");
        plumberLocation_txt.setText(getLocationAddress(plumber_lat, plumber_lng));

        //----- client ----
        Client_lng = intent.getExtras().getString("client_longhitude");
        clientLng_txt.setText(Client_lng);
        Client_lat = intent.getExtras().getString("client_latitude");
        clientLat_txt.setText(Client_lat);
        Client_firstname = intent.getExtras().getString("client_firstname");
        clientFirstname_txt.setText(Client_firstname);
        Client_lastname = intent.getExtras().getString("client_lastname");
        clientLastname_txt.setText(Client_lastname);
        Client_uid = intent.getExtras().getString("client_uid");
        clientUid_txt.setText(Client_uid);
        addressTxt.setText(getLocationAddress(Client_lat,Client_lng));

        //------ services ------
        service_data = intent.getExtras().getString("service_servicetrans");
        service_txt.setText(service_data);
        concernmsg_data = intent.getExtras().getString("service_concernmsgs");
        concernmsg_txt.setText(concernmsg_data);
        servicefee_data = intent.getExtras().getString("service_fee");
        servicefee_txt.setText(servicefee_data);
        double transFee=Double.parseDouble(servicefee_data) * 0.10;
        double totalFee=transFee + Double.parseDouble(servicefee_data);
        transactionfee_data = ""+ transFee;
        transactionfee_txt.setText(transactionfee_data);
        total_data = ""+totalFee;
        total_txt.setText(total_data);



        //------ initializing map ----
        transaction_map = (WebView) findViewById(R.id.transaction_map);
        transaction_map.getSettings().setJavaScriptEnabled(true);
        transaction_map.addJavascriptInterface(this, "android");
        transaction_map.loadUrl("file:///android_asset/mapmarker.html");

        //------ displaying client's address

        //--- saving data transaction data into database---
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchprofilepicAndDisplay(plumber_uid);
                savedTransaction();
            }
        },3000);


    }
    //==== generating address
    public String getLocationAddress(String lat, String lng){
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
            double lat_add=Double.parseDouble(lat);
            double lng_add=Double.parseDouble(lng);
            addresses = geocoder.getFromLocation(lat_add, lng_add, 1);

            address=addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();
        }catch (IOException e){
            e.printStackTrace();
        }
        String client_address= city+", "+state+", "+country+", "+postalCode+", "+ knownName;
//        addressTxt.setText(client_address);
        return client_address;


    }
    //==== saving all data ====
    public void savedTransaction(){


        saveTransaction savetrans=new saveTransaction(plumber_uid,
                plumber_firstname,
                plumber_lastname,
                plumber_lng,
                plumber_lat,
                plumber_stat,
                plumber_distance,
                plumber_location,
                service_data,
                concernmsg_data,
                servicefee_data,
                transactionfee_data,
                total_data,
                Client_firstname,
                Client_lastname,
                Client_uid,
                Client_lng,
                Client_lat);

        FirebaseDatabase.getInstance().getReference("transaction_client").child(Client_uid).setValue(savetrans)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(transactions_client.this, "transaction saved", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(transactions_client.this, "transaction not saved!", Toast.LENGTH_LONG).show();

                        }
                    }
                });

        FirebaseDatabase.getInstance().getReference("transaction_plumber").child(plumber_uid).setValue(savetrans)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(transactions_client.this, "client transaction saved", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(transactions_client.this, "client transaction not saved!", Toast.LENGTH_LONG).show();

                        }
                    }
                });

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date myDate = new Date();
        String dateNow = timeStampFormat.format(myDate);

        Random random=new Random();
        int randomNumber=random.nextInt(1000-100)+ 100;

        notificationPlumberData notifPlum=new notificationPlumberData(Client_uid,service_data,plumber_distance,addressTxt.getText().toString(),
                dateNow, Client_firstname, Client_lastname);

        FirebaseDatabase.getInstance().getReference("notification_plumber").child(plumber_uid).child(""+randomNumber).setValue(notifPlum)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(transactions_client.this, "notification save", Toast.LENGTH_LONG).show();
                            loading.dismissDialog();
                        } else {
                            Toast.makeText(transactions_client.this, "notification not saved!", Toast.LENGTH_LONG).show();
                            loading.dismissDialog();
                        }
                    }
                });
    }

    //=======================displays profile picture from database...
    private void fetchprofilepicAndDisplay(String uid){
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
                                            plumberProfilePix.setImageBitmap(bitmap);

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
            lng=Double.parseDouble(Client_lng);
            return lng;
        }else if (a==3){
            lat=Double.parseDouble(Client_lat);
            return lat;
        }
        return lat;
    }
}