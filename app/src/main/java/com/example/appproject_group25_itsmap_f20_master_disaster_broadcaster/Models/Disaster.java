package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.UUID;

public class Disaster {

@Exclude
    private String id;
    private String title;
    private Date date;
    private int emblemImage;

    private DisasterType disasterType;

    private String userImage;
    private int points;
    private double distance;

    //coords for disaster
    private double latDisaster;
    private double lonDisaster;

    //cords for user
    private double latUser;
    private double lonUser;

    //private LatLng disasterCords;
    //private LatLng userCords;

    public Disaster()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEmblemImage() {
        return emblemImage;
    }

    public void setEmblemImage(int emblemImage) {
        this.emblemImage = emblemImage;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints() {
        double THREE_HUNDRED = 300;
        double HUNDRED_FIFTY = 150;
        double ONE_HUNDRED = 100;
        double FIFTY = 50;
        double TWENTY_FIVE = 25;
        double FIVE = 5;
        if (distance > 0.0) {
            if (distance <= THREE_HUNDRED && distance >= HUNDRED_FIFTY) {
                points = 5;
            } else if (distance <= HUNDRED_FIFTY && distance >= ONE_HUNDRED) {
                points = 10;
            } else if (distance <= ONE_HUNDRED && distance >= FIFTY) {
                points = 15;
            } else if (distance <= FIFTY && distance >= TWENTY_FIVE) {
                points = 30;
            } else if (distance <= TWENTY_FIVE && distance >= FIVE) {
                points = 100;
            } else if (distance <= FIVE) {
                points = 1000;
            }
            else{
                points = 1;
            }
        }
    }

    public DisasterType getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(DisasterType disasterType) {
        this.disasterType = disasterType;
    }
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


    public double getLatDisaster() {
        return latDisaster;
    }

    public void setLatDisaster(double latDisaster) {
        this.latDisaster = latDisaster;
    }

    public double getLonDisaster() {
        return lonDisaster;
    }

    public void setLonDisaster(double lonDisaster) {
        this.lonDisaster = lonDisaster;
    }

    public double getLatUser() {
        return latUser;
    }

    public void setLatUser(double latUser) {
        this.latUser = latUser;
    }

    public double getLonUser() {
        return lonUser;
    }

    public void setLonUser(double lonUser) {
        this.lonUser = lonUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
