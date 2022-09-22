package com.capstone.atyourservice_capstone2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ClientProfileSettings extends Fragment {

    TextView emailtxt,fullnametxt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_client_profile_settings, container, false);



       // =========Set up ID for declared objects====
        emailtxt=(TextView) view.findViewById(R.id.email_lbl);
        fullnametxt=(TextView) view.findViewById(R.id.fullname_lbl);
        return view;
    }

    public void fetchData(){

//        String uid=task.getResult().getUser().getUid().toString();
//        reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String userType = snapshot.child("userType").getValue().toString();
//                signIn.setText(userType);
//
//                if (userType.equals("Plumber")){
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                    if(user.isEmailVerified()){
//
//                        //if verified us success..
//
//                        //    Intent prof=new Intent(ClientLogin.this, ProfilePage.class); //initialize the intent for Activity DashBoard
//                        Toast.makeText(PlumbersLogIn.this, "LogIn Successfully",Toast.LENGTH_LONG).show();
//                        progressBar.setVisibility(View.GONE); //set the Progress Bar into Invisible..
//                        //   startActivity(prof); //Redirect the Activity DashBoard..
//                    }else {
//
//                        //if verification failed...
//                        user.sendEmailVerification(); //sent email verification to client email...
//                        Toast.makeText(PlumbersLogIn.this,"Your not verified, Kindly Check your email.", Toast.LENGTH_LONG).show();
//                        progressBar.setVisibility(View.GONE);
//                    }
//
//                }else {
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }


}