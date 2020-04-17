package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models;

import java.util.List;

public class User {

    private String name;
    private int rank;
    private String country;
    private int totalPoints;
    private List<Disaster> disasters;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public List<Disaster> getDisasters() {
        return disasters;
    }

    public void setDisasters(List<Disaster> disasters) {
        this.disasters = disasters;
    }


}
