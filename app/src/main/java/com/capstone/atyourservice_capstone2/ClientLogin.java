package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ClientLogin extends AppCompatActivity {

    //declarations..
    private TextView forgotpassword,logging,clientReg;
    private EditText email,password;

    private Button signIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference reff;

    //=== displaying notifications
    pushNotification pushnotif=new pushNotification();

    //===== pop-up dialog
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    ImageView clientRegIcon,plumberRegIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);


        email = (EditText) findViewById(R.id.Edittxt_email);
        password = (EditText) findViewById(R.id.Edittxt_password);
        signIn = (Button) findViewById(R.id.LogIn_btn);
        clientReg = (TextView) findViewById(R.id.clientRegister_btn);


        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        logging = (TextView) findViewById(R.id.LoggingIn_client);
        forgotpassword=(TextView)findViewById(R.id.ForgotPassword_txtview);
        firebaseDatabase=FirebaseDatabase.getInstance();


        //Forgot password onclicklistener event..
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //redirect to forgot password in case the user forgot his/her password..
                Intent fpass=new Intent(ClientLogin.this, ResetPassword.class);
                startActivity(fpass);
            }
        });

        clientReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateAccount();
            }
        });



        //Onclick Event for SignIn Button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLogin();
            }
        });
    }

    //==== displaying pop-up application
    public void openCreateAccount(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp=getLayoutInflater().inflate(R.layout.create_account_dialog, null);

        //==== imageView Initialization
          clientRegIcon=(ImageView) viewPopUp.findViewById(R.id.clientIcon);
          plumberRegIcon=(ImageView) viewPopUp.findViewById(R.id.plumberIcon);

        //===setting up view
        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        //=== implementing create onClickEvent
        clientRegIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fpass=new Intent(ClientLogin.this, RegisterClient.class);
                startActivity(fpass);
            }
        });
        plumberRegIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fpass=new Intent(ClientLogin.this, ApplyPlumber.class);
                startActivity(fpass);
            }
        });
    }


    //methods...
    public void UserLogin(){
        String email_data=email.getText().toString().trim();
        String password_data=password.getText().toString().trim();


        // validation check..
        if (email_data.isEmpty()){
            email.setError("Please provide your email.");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_data).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }
        if(password_data.isEmpty()){
            password.setError("Please provide your password.");
            password.requestFocus();
            return;
        }
        if (password_data.length()<6){
            password.setError("Minimum of 6 characters.");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE); // set the Progress Bar into Visible..
        logging.setVisibility(View.VISIBLE);

        //firebase signIn validation...
        mAuth.signInWithEmailAndPassword(email_data, password_data).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    //code for email validation...

                    String uid=task.getResult().getUser().getUid().toString();

                    //=================Retrieving Data from Firebase Database======

                    reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userType = snapshot.child("userType").getValue().toString();

                            if (userType.equals("Client")){


                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                if(user.isEmailVerified()){

                                    //if verified us success..

                                    Intent prof=new Intent(ClientLogin.this, SecondPage_client.class); //initialize the intent for Activity DashBoardClient
                                    Toast.makeText(ClientLogin.this, "LogIn Successfully",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE); //set the Progress Bar into Invisible..
                                    logging.setVisibility(View.GONE); //set tge Progress Bar into Invisible..

                                    pushnotif.displayNotification(ClientLogin.this, "Log-in","Success");

                                    startActivity(prof); //Redirect the Activity DashBoardClient..
                                }else {

                                    //if verification failed...
                                    user.sendEmailVerification(); //sent email verification to client email...
                                    pushnotif.displayNotification(ClientLogin.this, "Not Verified","Your not verified, Kindly Check your email SPAM folder.");
                                    progressBar.setVisibility(View.GONE);
                                    logging.setVisibility(View.GONE);
                                }

                            }else if (userType.equals("Plumber")){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if(user.isEmailVerified()){

                                    //if verified us success..


                                    Toast.makeText(ClientLogin.this, "LogIn Successfully",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE); //set the Progress Bar into Invisible..
                                    Intent prof=new Intent(ClientLogin.this, SecondPage_plumber.class);
                                    startActivity(prof);
                                    progressBar.setVisibility(View.GONE);

                                }else {

                                    //if verification failed...
                                    user.sendEmailVerification(); //sent email verification to client email...
                                    Toast.makeText(ClientLogin.this,"Your not verified, Kindly Check your email.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);

                                }
                            }
                            else
                            {

                                pushnotif.displayNotification(ClientLogin.this, "Not Registered","User is not registered or does not exist.");
                                progressBar.setVisibility(View.GONE);
                                logging.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {


                            pushnotif.displayNotification(ClientLogin.this, "Canceled","connection canceled.");
                            progressBar.setVisibility(View.GONE);
                            logging.setVisibility(View.GONE);
                        }
                    });


                }else {
                    Toast.makeText(ClientLogin.this, "", Toast.LENGTH_LONG).show();
                    pushnotif.displayNotification(ClientLogin.this, "Login Failed","Failed to Log In, Kindly check your Credentials.");
                    progressBar.setVisibility(View.GONE);
                    logging.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ClientLogin.this, "Log-In fail, please check your internet or log-in credentials", Toast.LENGTH_LONG).show();
                pushnotif.displayNotification(ClientLogin.this, "Internet Connection Fail","Internet Connection Fail!");
                progressBar.setVisibility(View.GONE);
                logging.setVisibility(View.GONE);
            }
        });

    }

}