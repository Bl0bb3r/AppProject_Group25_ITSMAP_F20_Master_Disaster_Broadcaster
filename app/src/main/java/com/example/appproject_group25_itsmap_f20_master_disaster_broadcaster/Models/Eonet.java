package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models;

import java.util.ArrayList;

public class Eonet {
    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String title;
    private String description;
    private ArrayList<Event> events;

}
