package com.capstone.atyourservice_capstone2;

public class WaitingData {
    public String firstname,lastname,status,longhitude,latitude,uid,currentdate;

    public WaitingData(){

    }
    public WaitingData(String firstname, String lastname, String status, String longhitude, String latitude, String uid,String currentdate){
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.longhitude = longhitude;
        this.latitude = latitude;
        this.uid = uid;
        this.currentdate = currentdate;

        return;
    }
}
