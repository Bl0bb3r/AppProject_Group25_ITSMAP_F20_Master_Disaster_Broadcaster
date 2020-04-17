package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models;

public class Disaster {

    private String id;
    private String DisasterImage;



    private DisasterType disasterType;
    private double disaster_longitude;
    private double disaster_latitude;

    private String UserImage;
    private double user_longitude;
    private double user_latitude;
    private int points;


    public Disaster()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        id = id;
    }

    public String getDisasterImage() {
        return DisasterImage;
    }

    public void setDisasterImage(String disasterImage) {
        DisasterImage = disasterImage;
    }

    public double getDisaster_longitude() {
        return disaster_longitude;
    }

    public void setDisaster_longitude(double disaster_longitude) {
        this.disaster_longitude = disaster_longitude;
    }

    public double getDisaster_latitude() {
        return disaster_latitude;
    }

    public void setDisaster_latitude(double disaster_latitude) {
        this.disaster_latitude = disaster_latitude;
    }

    public double getUser_longitude() {
        return user_longitude;
    }

    public void setUser_longitude(double user_longitude) {
        this.user_longitude = user_longitude;
    }

    public double getUser_latitude() {
        return user_latitude;
    }

    public void setUser_latitude(double user_latitude) {
        this.user_latitude = user_latitude;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public DisasterType getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(DisasterType disasterType) {
        this.disasterType = disasterType;
    }
    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }


}
