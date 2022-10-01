package com.capstone.atyourservice_capstone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity {

    Button plumbers,client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        plumbers=(Button) findViewById(R.id.plumber_firstpage);
        client=(Button) findViewById(R.id.Client_firstpage);

        plumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent plumberlogin=new Intent(FirstPage.this, PlumbersLogIn.class);
                startActivity(plumberlogin);
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userslogin=new Intent(FirstPage.this, ClientLogin.class);
                startActivity(userslogin);
            }
        });
    }
}