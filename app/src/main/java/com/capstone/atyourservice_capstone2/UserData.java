package com.capstone.atyourservice_capstone2;

public class UserData {
    public String fullname, birthdate, email, userType;

    public UserData(){

    }
    public UserData(String fullname, String birthdate, String email, String userType){
        this.fullname=fullname;
        this.birthdate=birthdate;
        this.email=email;
        this.userType = userType;

        return;
    }
}