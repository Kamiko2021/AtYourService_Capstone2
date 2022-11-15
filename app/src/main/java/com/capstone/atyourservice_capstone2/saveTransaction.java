package com.capstone.atyourservice_capstone2;

public class saveTransaction {
    //=== Plumber Strings data initialization ========
   public String plumber_uid,plumber_firstname,plumber_lastname,plumber_lng,plumber_lat,plumber_stat,plumber_distance,plumber_location;
   public String service_data,concernmsg_data,servicefee_data,transactionfee_data,total_data;
    //==== Client Strings data initialization ========
   public String Client_firstname,Client_lastname,Client_uid,Client_lng,Client_lat;

    public saveTransaction(){

    }
    public saveTransaction(String plumber_uid,String plumber_firstname,String plumber_lastname,String plumber_lng,String plumber_lat,String plumber_stat,String plumber_distance,String plumber_location,
                           String service_data,String concernmsg_data,String servicefee_data,String transactionfee_data,String total_data,
                           String Client_firstname,String Client_lastname,String Client_uid,String Client_lng,String Client_lat){

        this.plumber_firstname = plumber_firstname;
        this.plumber_lastname = plumber_lastname;
        this.plumber_uid = plumber_uid;
        this.plumber_lat = plumber_lat;
        this.plumber_lng = plumber_lng;
        this.plumber_distance = plumber_distance;
        this.plumber_location = plumber_location;
        this.plumber_stat = plumber_stat;

        this.Client_firstname = Client_firstname;
        this.Client_lastname = Client_lastname;
        this.Client_uid = Client_uid;
        this.Client_lat = Client_lat;
        this.Client_lng = Client_lng;

        this.service_data = service_data;
        this.servicefee_data = servicefee_data;
        this.concernmsg_data = concernmsg_data;
        this.transactionfee_data = transactionfee_data;
        this.total_data = total_data;

        return;
    }
}
