package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Geometry {

    private double magnitudeValue;
    private String magnitudeUnit;
    private Date date;
    private String type;
    @SerializedName("coordinates")
    private ArrayList<Coordinates> coordinates;

    public double getMagnitudeValue() {
        return magnitudeValue;
    }

    public void setMagnitudeValue(double magnitudeValue) {
        this.magnitudeValue = magnitudeValue;
    }

    public String getMagnitudeUnit() {
        return magnitudeUnit;
    }

    public void setMagnitudeUnit(String magnitudeUnit) {
        this.magnitudeUnit = magnitudeUnit;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Coordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }
}
