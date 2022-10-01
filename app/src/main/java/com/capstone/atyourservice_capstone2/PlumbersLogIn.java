package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlumbersLogIn extends AppCompatActivity {
    //declarations..
    private TextView ApplyAsPlumbers,forgotpassword,SignInClient;
    private EditText email,password;

    private Button signIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plumbers_log_in);

        //declarations..
        signIn = (Button) findViewById(R.id.LogIn_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        email = (EditText) findViewById(R.id.Edittxt_email);
        password = (EditText) findViewById(R.id.Edittxt_password);
        ApplyAsPlumbers = (TextView) findViewById(R.id.PlumbersApplication_txtview);
        forgotpassword = (TextView) findViewById(R.id.ForgotPassword_txtview);
       SignInClient = (TextView) findViewById(R.id.AsClient_txtview);

        //firebase Authentication instance..
        mAuth = FirebaseAuth.getInstance();

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resetpassword=new Intent(PlumbersLogIn.this, ResetPassword.class);
                startActivity(resetpassword);
            }
        });

        ApplyAsPlumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent applyplumber=new Intent(PlumbersLogIn.this, ApplyPlumber.class);
                startActivity(applyplumber);
            }
        });

       SignInClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClientSignIn=new Intent(PlumbersLogIn.this, ClientLogin.class);
                startActivity(ClientSignIn);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlumberSignIn();
            }
        });

    }

    public void PlumberSignIn(){
        String email_data = email.getText().toString().trim();
        String password_data = password.getText().toString().trim();


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

        //firebase signIn validation...
        mAuth.signInWithEmailAndPassword(email_data, password_data).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    //code for email validation...
                    String uid=task.getResult().getUser().getUid().toString();
                    reff = FirebaseDatabase.getInstance().getReference().child("USERS").child(uid);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userType = snapshot.child("userType").getValue().toString();
                            signIn.setText(userType);

                            if (userType.equals("Plumber")){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if(user.isEmailVerified()){

                                    //if verified us success..


                                    Toast.makeText(PlumbersLogIn.this, "LogIn Successfully",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE); //set the Progress Bar into Invisible..
                                    Intent prof=new Intent(PlumbersLogIn.this, DashBoardPlumber.class);
                                    startActivity(prof);
                                }else {

                                    //if verification failed...
                                    user.sendEmailVerification(); //sent email verification to client email...
                                    Toast.makeText(PlumbersLogIn.this,"Your not verified, Kindly Check your email.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }

                            }else {

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }else {
                    Toast.makeText(PlumbersLogIn.this, "Failed to Log In, Kindly check your Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}