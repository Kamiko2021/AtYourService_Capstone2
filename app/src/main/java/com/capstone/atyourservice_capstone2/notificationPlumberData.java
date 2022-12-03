package com.capstone.atyourservice_capstone2;

public class notificationPlumberData {
    public String client_uid,serviceRequest,distance,address,dateNow,firstname,lastname,requestStatus;

    public notificationPlumberData(){

    }
    public notificationPlumberData(String client_uid, String serviceRequest, String distance, String address,String dateNow,
                                   String firstname,String lastname,String requestStatus){
        this.client_uid = client_uid;
        this.serviceRequest = serviceRequest;
        this.distance = distance;
        this.address = address;
        this.dateNow = dateNow;
        this.firstname = firstname;
        this.lastname = lastname;
        this.requestStatus=requestStatus;

        return;
    }
}
