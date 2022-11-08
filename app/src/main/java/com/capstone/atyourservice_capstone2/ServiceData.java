package com.capstone.atyourservice_capstone2;

public class ServiceData {

   public String uid_plumber,RepairPipes,InstallPipes,UnclogToilet,UnclogDrainage;

    public ServiceData(){

    }
    public ServiceData(String uid_plumber,String RepairPipes,String InstallPipes,String UnclogToilet,String UnclogDrainage){
        this.uid_plumber=uid_plumber;
        this.RepairPipes = RepairPipes;
        this.InstallPipes = InstallPipes;
        this.UnclogToilet = UnclogToilet;
        this.UnclogDrainage = UnclogDrainage;

        return;
    }
}
