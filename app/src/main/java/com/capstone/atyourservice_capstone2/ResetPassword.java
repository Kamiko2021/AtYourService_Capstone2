package com.capstone.atyourservice_capstone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText email;
    private Button resetbtn;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //declaring components...
        email = (EditText) findViewById(R.id.Edittxt_email);
        resetbtn = (Button) findViewById(R.id.reset_btn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        //declaring firebase Authorization..
        mAuth=FirebaseAuth.getInstance();


        //Onclick events for reset button..
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }
    //declaring private method for sending password reset email..
    private void resetPassword(){
        String email_data=email.getText().toString().toString().trim();

        //validation check to make sure the edittext is not empty and make sure it have valid email address format..
        if (email_data.isEmpty()){
            email.setError("Email is Required!");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_data).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        //displays progress bar..
        progressBar.setVisibility(View.VISIBLE);

        //sends email to reset your password..
        mAuth.sendPasswordResetEmail(email_data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(ResetPassword.this, "Check your email to reset your password.", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ResetPassword.this, "Something is wrong..", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);

            }
        });

    }
}