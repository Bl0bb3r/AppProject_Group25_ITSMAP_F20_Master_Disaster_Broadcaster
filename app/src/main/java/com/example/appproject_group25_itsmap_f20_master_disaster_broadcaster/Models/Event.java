package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class Event {

    private String id;
    private String title;
    private String description;
    private String link;
    private String closed;
    @SerializedName("categories")
    private List<Categories> categories;
    private List<Sources> sources;
    private List<Geometry> geometry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }


    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public List<Sources> getSources() {
        return sources;
    }

    public void setSources(List<Sources> sources) {
        this.sources = sources;
    }

    public List<Geometry> getGeometry() {
        return geometry;
    }

    public void setGeometry(List<Geometry> geometry) {
        this.geometry = geometry;
    }


}
