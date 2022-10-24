package com.capstone.atyourservice_capstone2;

public class ServiceData {

   public String uid_plumber,undergroundPipes_fee,aboveground_fee,cloggedToilet_fee,cloggedDrainage_fee;

    public ServiceData(){

    }
    public ServiceData(String uid_plumber,String undergroundPipes_fee,String aboveground_fee,String cloggedToilet_fee,String cloggedDrainage_fee){
        this.uid_plumber=uid_plumber;
        this.undergroundPipes_fee = undergroundPipes_fee;
        this.aboveground_fee = aboveground_fee;
        this.cloggedToilet_fee = cloggedToilet_fee;
        this.cloggedDrainage_fee = cloggedDrainage_fee;

        return;
    }
}
