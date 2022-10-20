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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterClient extends AppCompatActivity {

    //firebase Authentication declaration..
    private FirebaseAuth mAuth;

    //Object Declarations..
    private EditText Firstname,Lastname, gender, Birthdate,Email,Password;
    private Button register;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        //firebase database reference initialization
        mAuth = FirebaseAuth.getInstance();


        //Objects Initialization..
        Firstname = (EditText)findViewById(R.id.firstname_client);
        Lastname = (EditText) findViewById(R.id.lastname_client);
        gender = (EditText) findViewById(R.id.gender_client);
        Birthdate = (EditText)findViewById(R.id.birthdate_client);
        Email = (EditText)findViewById(R.id.Email_client);
        Password = (EditText)findViewById(R.id.Password_client);
        register = (Button)findViewById(R.id.submit_client);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_clientReg);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();
            }
        });

    }


    public void SignUp(){

        //fetching data from EditText..
        String firstname_data = Firstname.getText().toString().trim();
        String lastname_data = Lastname.getText().toString().trim();
        String gender_data = gender.getText().toString().trim();
        String birthdate_data= Birthdate.getText().toString().trim();
        String email_data= Email.getText().toString().trim();
        String password_data= Password.getText().toString().trim();

        //validation check..
        if (firstname_data.isEmpty()){
            Firstname.setError("Firstname is Required!");
            Firstname.requestFocus();
            return;
        }
        if (lastname_data.isEmpty()){
            Lastname.setError("Lastname is Required!");
            Lastname.requestFocus();
            return;
        }
        if (gender_data.isEmpty()){
            gender.setError("Please type Male or Female");
            gender.requestFocus();
            return;
        }
        if (birthdate_data.isEmpty()){
            Birthdate.setError("Age is Required!");
            Birthdate.requestFocus();
            return;
        }
        if (email_data.isEmpty()){
            Email.setError("Email is Required!");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_data).matches()){
            Email.setError("Please provide valid email!");
            Email.requestFocus();
            return;
        }
        if (password_data.isEmpty()){
            Password.setError("Password is Required!");
            Password.requestFocus();
            return;
        }
        if (Password.length() < 6){
            Password.setError("Minimum of 6 characters");
            Password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //firebase saving client data...
        mAuth.createUserWithEmailAndPassword(email_data, password_data).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    UserData client= new UserData(firstname_data, lastname_data, gender_data, birthdate_data, email_data, "Client");
                    FirebaseDatabase.getInstance().getReference("USERS")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Toast.makeText(RegisterClient.this, "Client Registered", Toast.LENGTH_LONG).show();

                                        progressBar.setVisibility(View.GONE);

                                    //---------------------------send email verification

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                        if(user.isEmailVerified()){

                                            //if verified us success..


                                            Toast.makeText(RegisterClient.this, "Verified",Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE); //set the Progress Bar into Invisible..

                                        }else {

                                            //if verification failed...
                                            user.sendEmailVerification(); //sent email verification to client email...
                                            Toast.makeText(RegisterClient.this,"Verification Link sent.", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterClient.this, ClientLogin.class));
                                            progressBar.setVisibility(View.GONE);
                                        }


                                    } else {
                                        Toast.makeText(RegisterClient.this, "Registration Fail, please try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                }





            }
        });

    }
}