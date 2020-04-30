package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson;

import java.util.Date;

public class PolygonShape implements Shape {

    private final ShapeType type = ShapeType.Polygon;
    private double[][][] coordinates;

    public ShapeType getType() {
        return type;
    }

    public double[][][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][][] coordinates) {
        this.coordinates = coordinates;
    }

    private double magnitudeValue;
    private String magnitudeUnit;
    private Date date;

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
}
