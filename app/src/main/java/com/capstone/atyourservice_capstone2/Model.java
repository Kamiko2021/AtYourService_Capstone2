package com.capstone.atyourservice_capstone2;

public class Model {

    int image;
    String firstname,lastname,status,latitude,longhitude,distance;

    public Model(int image, String firstname, String lastname, String status, String latitude, String longhitude, String distance){
        this.image = image;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.latitude = latitude;
        this.longhitude = longhitude;
        this.distance = distance;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLonghitude() {
        return longhitude;
    }

    public void setLonghitude(String longhitude) {
        this.longhitude = longhitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
