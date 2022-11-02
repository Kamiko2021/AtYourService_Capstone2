package com.capstone.atyourservice_capstone2;

public class updateClient {
    public String firstname,lastname,gender,birthdate,userType,email;

    public updateClient(){

    }
    public updateClient(String firstname,String lastname,String gender,String birthdate,String userType,String email){
        this.firstname=firstname;
        this.lastname=lastname;
        this.gender = gender;
        this.birthdate=birthdate;
        this.userType=userType;
        this.email=email;

        return;
    }
}
