package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson;

import java.util.Date;

public interface Shape {
    ShapeType getType();
    enum ShapeType{Point, MultiPoint, Polygon, MultiPolygon, LineString, MultiLineString}


}
