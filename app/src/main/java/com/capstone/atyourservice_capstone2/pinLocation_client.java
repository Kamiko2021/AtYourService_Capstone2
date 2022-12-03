package com.capstone.atyourservice_capstone2;

import static java.lang.Math.PI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class pinLocation_client extends AppCompatActivity {

    TextView firstname_txt,lastname_txt,status_txt,latitude_txt,longhitude_txt,uid_txt;
    TextView firstname_clientTxt,lastname_clientTxt,uid_clientTxt,longhitude_clientTxt,latitude_clientTxt,distance_txt,UserType_client,location_txt;
    WebView pinMap;
    Button pinMap_btn,reqDetailsbtn;
    CircleImageView clientProfile,plumberProfile;
    StorageReference displayStorageRef;
    DatabaseReference reff;
    String client_address;
    //===== variable to save service Details ======
    String services,concernmsgs,serviceCharge;
    public double lat_client,lng_client;

    //============= Pop-up initialization=========
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    TextView plumberFname_popup,plumberLname_popup,plumberuid_popup,plumberLng_popup,
    plumberLat_popup,plumberStat_popup,plumberRating_popup,plumberDistance_popup,plumberLocation_popup;
    TextView clientFname,clientLname,clientuid,clientlat,clientlng;
    TextView serviceTxt,concernmsgTxt,servicefeeTxt;
    Button submitReqBtn,closeBtn;


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


        //===== initializing circle image view==========
        clientProfile = (CircleImageView) findViewById(R.id.profileImg_client);
        plumberProfile = (CircleImageView) findViewById(R.id.profileImg_plumber);

        //=====pinMap buttn ==============
        pinMap_btn = (Button) findViewById(R.id.pinClientlocation_btn);
        reqDetailsbtn = (Button) findViewById(R.id.RequestDetails);


        reqDetailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDetailsPopup();
            }
        });

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
        fetchprofilepicAndDisplay(uid_plumber,"Plumber");
        fetchprofilepicAndDisplay(uid_clientTxt.getText().toString().trim(), "Client");

        if (status_txt.getText().toString().equals("offline")){
            status_txt.setTextColor(Color.parseColor("#ff0000"));
        }else if (status_txt.getText().toString().equals("online")){
            status_txt.setTextColor(Color.parseColor("#09ff00"));
        }

    }
    public void requestDetailsPopup(){
        dialogBuilder = new AlertDialog.Builder(this);
        View requestDetailsView= getLayoutInflater().inflate(R.layout.submission_details, null);

        //====== Plumber TextView Initialization=====
        plumberuid_popup=(TextView) requestDetailsView.findViewById(R.id.plumber_uid);
        plumberuid_popup.setText(uid_txt.getText().toString().trim());
        String Puid=uid_txt.getText().toString().trim();

        plumberFname_popup = (TextView) requestDetailsView.findViewById(R.id.plumber_fname);
        plumberFname_popup.setText(firstname_txt.getText().toString());
        String Pfirstname=firstname_txt.getText().toString();

        plumberLname_popup = (TextView) requestDetailsView.findViewById(R.id.plumber_lname);
        plumberLname_popup.setText(lastname_txt.getText().toString());
        String Plastname=lastname_txt.getText().toString();

        plumberLng_popup = (TextView) requestDetailsView.findViewById(R.id.plumber_lng);
        plumberLng_popup.setText(longhitude_txt.getText().toString());
        String Plng=longhitude_txt.getText().toString();

        plumberLat_popup = (TextView) requestDetailsView.findViewById(R.id.plumber_lat);
        plumberLat_popup.setText(latitude_txt.getText().toString());
        String Plat=latitude_txt.getText().toString();

        plumberStat_popup = (TextView) requestDetailsView.findViewById(R.id.plumber_stat);
        plumberStat_popup.setText(status_txt.getText().toString());
        String Pstat=status_txt.getText().toString();

        plumberRating_popup = (TextView) requestDetailsView.findViewById(R.id.plumber_rating);
        plumberDistance_popup = (TextView) requestDetailsView.findViewById(R.id.plumber_distance);
        plumberDistance_popup.setText(distance_txt.getText().toString());
        String Pdist=distance_txt.getText().toString();

        plumberLocation_popup = (TextView) requestDetailsView.findViewById(R.id.plumber_location);
        plumberLocation_popup.setText(location_txt.getText().toString());
        String Llocation=location_txt.getText().toString();

        //======== Client TextView Initialization =========
        clientuid = (TextView) requestDetailsView.findViewById(R.id.client_uid);
        clientuid.setText(uid_clientTxt.getText().toString().trim());
        String Cuid=uid_clientTxt.getText().toString().trim();

        clientFname = (TextView) requestDetailsView.findViewById(R.id.client_fname);
        clientFname.setText(firstname_clientTxt.getText().toString());
        String Cfirstname=firstname_clientTxt.getText().toString();

        clientLname = (TextView) requestDetailsView.findViewById(R.id.client_lname);
        clientLname.setText(lastname_clientTxt.getText().toString());
        String Clastname =lastname_clientTxt.getText().toString();

        clientlng = (TextView) requestDetailsView.findViewById(R.id.client_lng);
        clientlng.setText(longhitude_clientTxt.getText().toString());
        String Clng=longhitude_clientTxt.getText().toString();

        clientlat = (TextView) requestDetailsView.findViewById(R.id.client_lat);
        clientlat.setText(latitude_clientTxt.getText().toString());
        String Clat=latitude_clientTxt.getText().toString();

        //===== initializing submit Request button======
        submitReqBtn = (Button) requestDetailsView.findViewById(R.id.submitRequest);
        closeBtn = (Button) requestDetailsView.findViewById(R.id.closebtn);

        //====== initializing and displaying concerns =============
        serviceTxt = (TextView) requestDetailsView.findViewById(R.id.servicedata);
        concernmsgTxt = (TextView) requestDetailsView.findViewById(R.id.concernmsgdata);
        servicefeeTxt = (TextView) requestDetailsView.findViewById(R.id.servicefeedata);



        reff = FirebaseDatabase.getInstance().getReference().child("concerns").child(uid_clientTxt.getText().toString().trim());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String concern=snapshot.child("concerns").getValue().toString();
                String service=snapshot.child("service").getValue().toString();
                serviceTxt.setText(service);
                services = service;
                concernmsgTxt.setText(concern);
                concernmsgs=concern;

             reff = FirebaseDatabase.getInstance().getReference().child("service")
                     .child(uid_txt.getText().toString().trim());
             reff.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String fee;
                     if (service.equals("UnclogDrainage")){
                         fee= snapshot.child("UnclogDrainage").getValue().toString();
                         servicefeeTxt.setText(fee);
                         serviceCharge = fee;
                     } else if (service.equals("UnclogToilet")){
                         fee = snapshot.child("UnclogToilet").getValue().toString();
                         servicefeeTxt.setText(fee);
                         serviceCharge = fee;
                     } else if (service.equals("InstallPipes")){
                         fee = snapshot.child("InstallPipes").getValue().toString();
                         servicefeeTxt.setText(fee);
                         serviceCharge = fee;
                     }else if (service.equals("RepairPipes")){
                         fee = snapshot.child("RepairPipes").getValue().toString();
                         servicefeeTxt.setText(fee);
                         serviceCharge = fee;
                     }

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

        submitReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(pinLocation_client.this, transactions_client.class);
                //======= Client data transfer=======
                intent.putExtra("client_uid", Cuid);
                intent.putExtra("client_firstname", Cfirstname);
                intent.putExtra("client_lastname", Clastname);
                intent.putExtra("client_longhitude", Clng);
                intent.putExtra("client_latitude", Clat);

                //======== Plumber data transfer ====
                intent.putExtra("plumber_uid", Puid);
                intent.putExtra("plumber_firstname", Pfirstname);
                intent.putExtra("plumber_lastname", Plastname);
                intent.putExtra("plumber_longhitude", Plng);
                intent.putExtra("plumber_latitude", Plat);
                intent.putExtra("plumber_status", Pstat);
                intent.putExtra("plumber_distance", Pdist);
                intent.putExtra("plumber_location", Llocation);

                //======= Service Transaction =====
                intent.putExtra("service_servicetrans", services);
                intent.putExtra("service_concernmsgs", concernmsgs);
                intent.putExtra("service_fee", serviceCharge);
                startActivity(intent);


            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //===== setting up view=====
        dialogBuilder.setView(requestDetailsView);
        dialog = dialogBuilder.create();
        dialog.show();
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

                         //===== distance from of client and plumber===
                         double plumLat=Double.parseDouble(latitude_txt.getText().toString());
                         double plumLng=Double.parseDouble(longhitude_txt.getText().toString());
                         double dist=distance(lat_client,lng_client,plumLat,plumLng)/0.621371;
                         distance_txt.setText(String.format("%.3f", dist)+" km");

//                         getLocationAddress();
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
                                            plumberProfile.setImageBitmap(bitmap);
                                        } else if (userType.equals("Client")){
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            clientProfile.setImageBitmap(bitmap);
                                        }
//                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                                        profileImageView.setImageBitmap(bitmap);
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