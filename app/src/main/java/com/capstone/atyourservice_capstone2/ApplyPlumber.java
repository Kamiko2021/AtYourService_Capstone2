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
import com.google.firebase.database.FirebaseDatabase;

public class ApplyPlumber extends AppCompatActivity {

    //firebase Authentication declaration..
    private FirebaseAuth mAuth;

    //Object Declarations..
    private EditText Firstname,Lastname,birthdate,Email,Password;
    private Button register;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_plumber);

        //firebase database reference initialization
        mAuth = FirebaseAuth.getInstance();


        //Objects Initialization..
        Firstname = (EditText)findViewById(R.id.plumberFirstname_edittxt);
        Lastname = (EditText) findViewById(R.id.plumberLastname_edittxt);
        birthdate = (EditText)findViewById(R.id.plumberbirthdate_edittxt);
        Email = (EditText)findViewById(R.id.plumberEmail_edittxt);
        Password = (EditText)findViewById(R.id.plumberPassword_edittxt);
        register = (Button)findViewById(R.id.register_btn);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);


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
        String birthdate_data= birthdate.getText().toString().trim();
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
        if (birthdate_data.isEmpty()){
            birthdate.setError("Age is Required!");
            birthdate.requestFocus();
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
                    UserData client= new UserData(firstname_data,lastname_data, birthdate_data, email_data, "Plumber");
                    FirebaseDatabase.getInstance().getReference("USERS")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Toast.makeText(ApplyPlumber.this, "Plumber Registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ApplyPlumber.this, ClientLogin.class));
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else {
                                        Toast.makeText(ApplyPlumber.this, "Registration Fail, please try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                }





            }
        });

    }
}