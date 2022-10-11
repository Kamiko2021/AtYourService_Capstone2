package com.capstone.atyourservice_capstone2;

public class UserData {
    public String firstname,lastname, gender, birthdate, email,  userType;

    public UserData(){

    }
    public UserData(String firstname,String lastname, String gender, String birthdate, String email, String userType){
        this.firstname=firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.birthdate=birthdate;
        this.email=email;
        this.userType = userType;

        return;
    }
}