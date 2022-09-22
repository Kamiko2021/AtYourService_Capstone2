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
    private EditText Fullname,birthdate,Email,Password;
    private Button register;
    private ProgressBar progressBar;
    private TextView ClientLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_plumber);

        //firebase database reference initialization
        mAuth = FirebaseAuth.getInstance();


        //Objects Initialization..
        Fullname = (EditText)findViewById(R.id.Fullname_edittxt);
        birthdate = (EditText)findViewById(R.id.birthdate_edittxt);
        Email = (EditText)findViewById(R.id.Email_edittxt);
        Password = (EditText)findViewById(R.id.Password_edittxt);
        register = (Button)findViewById(R.id.register_btn);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        ClientLogin = (TextView) findViewById(R.id.SignUpAsClient_edittxt);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();
            }
        });

        ClientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClientsignIn = new Intent(ApplyPlumber.this, ClientLogin.class);
                startActivity(ClientsignIn);
            }
        });

    }


    public void SignUp(){

        //fetching data from EditText..
        String fullname_data = Fullname.getText().toString().trim();
        String birthdate_data= birthdate.getText().toString().trim();
        String email_data= Email.getText().toString().trim();
        String password_data= Password.getText().toString().trim();

        //validation check..
        if (fullname_data.isEmpty()){
            Fullname.setError("Fullname is Required!");
            Fullname.requestFocus();
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
                    UserData client= new UserData(fullname_data, birthdate_data, email_data, "Plumber");
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