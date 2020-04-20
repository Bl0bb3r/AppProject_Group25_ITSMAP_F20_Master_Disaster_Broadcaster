package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = Point.class, name = "Point"),
        @JsonSubTypes.Type(value = Polygon.class, name = "Polygon"),
        @JsonSubTypes.Type(value = MultiPolygon.class, name = "MultiPolygon"),
        @JsonSubTypes.Type(value = LineString.class, name = "LineString"),
        @JsonSubTypes.Type(value = MultiLineString.class, name = "MultiLineString"),
        @JsonSubTypes.Type(value = GeometryCollection.class, name = "GeometryCollection")})
public abstract class Coordinates {

}
