package com.capstone.atyourservice_capstone2;

public class UserData {
    public String fullname, age, email, userType;

    public UserData(){

    }
    public UserData(String fullname, String age, String email, String userType){
        this.fullname=fullname;
        this.age=age;
        this.email=email;
        this.userType = userType;

        return;
    }
}