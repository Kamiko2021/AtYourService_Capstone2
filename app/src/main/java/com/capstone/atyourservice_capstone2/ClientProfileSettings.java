package com.capstone.atyourservice_capstone2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientProfileSettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientProfileSettings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientProfileSettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientProfileSettings.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientProfileSettings newInstance(String param1, String param2) {
        ClientProfileSettings fragment = new ClientProfileSettings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //==========Declarations========
//    TextView emailtxt,fullnametxt,txtbirthdate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_profile_settings, container, false);



        //=========Set up ID for declared objects====
//        emailtxt=(TextView) getView().findViewById(R.id.email_lbl);
//        fullnametxt=(TextView) getView().findViewById(R.id.fullname_lbl);

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