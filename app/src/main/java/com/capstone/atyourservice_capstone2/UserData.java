package com.capstone.atyourservice_capstone2;

public class UserData {
    public String firstname,lastname, gender, birthdate, email,  userType;
    public String email_access,status, state, latitude, longhitude, distance,location;

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

    public void AccessData(String email_access, String status, String latitude, String longhitude, String distance, String location){
        this.email_access = email_access;
        this.status = status;
        this.state = state;
        this.latitude = latitude;
        this.longhitude = longhitude;
        this.distance = distance;
        this.location = location;

        return;
    }
}