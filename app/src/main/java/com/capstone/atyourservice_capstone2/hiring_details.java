package com.capstone.atyourservice_capstone2;

import static java.lang.Math.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

public class hiring_details extends AppCompatActivity {

    TextView firstname_hiring,lastname_hiring,status_hiring,latitude_hiring,longhitude_hiring,uid_hiring;
    TextView repairPipes,installPipes,unclogDrainage,unclogToilet,distance;
    Button hireMe_button;
    DatabaseReference reff;
    String client_uid,Client_lat,Client_lng;
    WebView showMap;
    String uid_data,plumber_fn,plumber_ln,plumber_lat,plumber_lng,plumber_stat;

    //============= Pop-up initialization=========
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Button cancel_popup,setConcern;
    Spinner concern_spinnerPopup;
    EditText concern_editext;
    DatabaseReference saveConcern;
    CircleImageView profileImageView;
    StorageReference displayStorageRef;

    //===========Initialize progress dialog==========
    LoadingDialog loadingDialog=new LoadingDialog(hiring_details.this);

    //===========Initialize buttomNavigation=======
    BottomNavigationView bottomNavi;


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
        repairPipes = (TextView) findViewById(R.id.repairPipes_txt);
        installPipes = (TextView) findViewById(R.id.installPipes_txt);
        unclogDrainage = (TextView) findViewById(R.id.unclogDrainage_txt);
        unclogToilet = (TextView) findViewById(R.id.unclogToilet_txt);
        distance=(TextView) findViewById(R.id.distance_txt);
        profileImageView = (CircleImageView) findViewById(R.id.profileImg_plumber);

        //---------Buttons----------------

        hireMe_button=(Button) findViewById(R.id.hireMe_btn);

        //----------initialize BottomNavigationView-----
        bottomNavi=(BottomNavigationView) findViewById(R.id.bottomNav_client);
        bottomNavi.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_client:
                        Intent homeIntent=new Intent(hiring_details.this, SecondPage_client.class);
                        startActivity(homeIntent);
                        break;
                }
            }
        });

        //==========fetching data from waiting data activity====
        Intent intent=getIntent();
        String firstname=intent.getExtras().getString("firstname");
        plumber_fn = firstname;
        String lastname=intent.getExtras().getString("lastname");
        plumber_ln = lastname;
        String status=intent.getExtras().getString("status");
        plumber_stat = status;
        String lat=intent.getExtras().getString("latitude");
        plumber_lat = lat;
        String lng=intent.getExtras().getString("longhitude");
        plumber_lng = lng;
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
                openPopup();
            }
        });


        //assigning data from textviews and save it to global variable strings
        uid_data=uid;
        loadingDialog.startLoadingDialog();
        Handler handler=new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getServiceFees();
            }
        }, 1000);
        getClientLngLat();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               fetchprofilepicAndDisplay(uid);
            }
        },2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               loadingDialog.dismissDialog();
            }
        }, 2500);

        //=========displaying show map to webview===
        showMap = (WebView) findViewById(R.id.showMapLocation);
        showMap.getSettings().setJavaScriptEnabled(true);
        showMap.addJavascriptInterface(this, "android");
        showMap.loadUrl("file:///android_asset/mapmarker.html");


        //====code to hide hire button if the status is not vacant===
        if (status.equals("offline")){
            hireMe_button.setEnabled(false);
            Toast.makeText(hiring_details.this,"The Plumber is Offline!",Toast.LENGTH_LONG).show();
        }else if (status.equals("online")) {
            hireMe_button.setEnabled(true);
            Toast.makeText(hiring_details.this, "The Plumber is Online!", Toast.LENGTH_LONG).show();
        }
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
                                        profileImageView.setImageBitmap(bitmap);
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

    //=======Alert Dialog Pop-up=======
    public void openPopup(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View concernPopupView = getLayoutInflater().inflate(R.layout.concern_popup, null);

        //======= Editext initialization==========
        concern_editext = (EditText) concernPopupView.findViewById(R.id.concern);

        //======= Spinner Initialization==========
        concern_spinnerPopup = (Spinner) concernPopupView.findViewById(R.id.services);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(hiring_details.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.services));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        concern_spinnerPopup.setAdapter(myAdapter1);

        //========= Button Initialization=====
        cancel_popup = (Button) concernPopupView.findViewById(R.id.cancel_button);
        setConcern = (Button) concernPopupView.findViewById(R.id.submit_concern);

        //===== setting up view=====
        dialogBuilder.setView(concernPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        setConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concernsData concerns=new concernsData(concern_spinnerPopup.getSelectedItem().toString(), concern_editext.getText().toString());
                saveConcern=FirebaseDatabase.getInstance().getReference("concerns");
                saveConcern.child(client_uid).setValue(concerns).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            transferPlumberData();
                        }else{
                            Toast.makeText(hiring_details.this, "concerns did not set", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        cancel_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    //======this code will display the service fee of the plumber=====
    public void getServiceFees(){


        reff = FirebaseDatabase.getInstance().getReference().child("service").child(uid_data);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String repairpipesFee=snapshot.child("RepairPipes").getValue().toString();
                String installpipesFee=snapshot.child("InstallPipes").getValue().toString();
                String unclogdrainageFee=snapshot.child("UnclogDrainage").getValue().toString();
                String unclogtoiletFee=snapshot.child("UnclogToilet").getValue().toString();

                //======Displaying data into textview====
                repairPipes.setText(repairpipesFee + " Php");
                installPipes.setText(installpipesFee + " Php");
                unclogDrainage.setText(unclogdrainageFee + " Php");
                unclogToilet.setText(unclogtoiletFee + " Php");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //=======this method will tranfer data from previous page====
    public void transferPlumberData(){
        //----Declaring String Variables--------
        String uid,firstname,lastname,status,latitude,longhitude;
        //----initialize plumbers data into declared variables------
        uid=uid_data;
        firstname=plumber_fn;
        lastname=plumber_ln;
        status=plumber_stat;
        latitude=plumber_lat;
        longhitude=plumber_lng;
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

    //=====this function will get the distance of plumber and client====


    //=====this function will fetch client uid and location latlng====
    public void getClientLngLat(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            client_uid=user.getUid();
            reff = FirebaseDatabase.getInstance().getReference().child("locations").child(client_uid);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String latitude=snapshot.child("lat").getValue().toString();
                    String longhitude=snapshot.child("lng").getValue().toString();
                    Client_lat=latitude;
                    Client_lng=longhitude;

                    //====== here calculation for distance======
                    double dist=distance(Double.parseDouble(latitude),Double.parseDouble(longhitude),
                            Double.parseDouble(plumber_lat),
                            Double.parseDouble(plumber_lng))/0.621371;
                    distance.setText(String.format("%.3f", dist)+" km");


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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