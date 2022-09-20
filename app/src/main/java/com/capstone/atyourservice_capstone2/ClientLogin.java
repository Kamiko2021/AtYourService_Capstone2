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


public class ClientLogin extends AppCompatActivity {

    //declarations..
    private TextView register,forgotpassword,plumberlogin;
    private EditText email,password;

    private Button signIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register_txtview);
        email = (EditText) findViewById(R.id.Edittxt_email);
        password = (EditText) findViewById(R.id.Edittxt_password);
        signIn = (Button) findViewById(R.id.LogIn_btn);
        plumberlogin=(TextView) findViewById(R.id.PlumbersLogin_txtview);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        forgotpassword=(TextView)findViewById(R.id.ForgotPassword_txtview);
        firebaseDatabase=FirebaseDatabase.getInstance();


       plumberlogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent plogin=new Intent(ClientLogin.this, PlumbersLogIn.class);
               startActivity(plogin);
           }
       });

        //Forgot password onclicklistener event..
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //redirect to forgot password in case the user forgot his/her password..
                Intent fpass=new Intent(ClientLogin.this, ResetPassword.class);
                startActivity(fpass);
            }
        });

        //Onclick Event for RegisterClient TextView
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg=new Intent(ClientLogin.this, RegisterClient.class);
                startActivity(reg);

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

                            if (userType.equals("Client")){


                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                if(user.isEmailVerified()){

                                    //if verified us success..

                                    //    Intent prof=new Intent(ClientLogin.this, ProfilePage.class); //initialize the intent for Activity Profile
                                    Toast.makeText(ClientLogin.this, "LogIn Successfully",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE); //set the Progress Bar into Invisible..
                                    //   startActivity(prof); //Redirect the Activity Profile..
                                }else {

                                    //if verification failed...
                                    user.sendEmailVerification(); //sent email verification to client email...
                                    Toast.makeText(ClientLogin.this,"Your not verified, Kindly Check your email.", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ClientLogin.this, "Failed to Log In, Kindly check your Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}