package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
//https://www.youtube.com/watch?v=8RPfrhzRw2s
//https://stackoverflow.com/questions/18551587/java-object-with-variable-dimensional-array
    public class ShapeDeserializer implements JsonDeserializer<Shape> {
    @Override
    public Shape deserialize(JsonElement json, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {

        if (json instanceof JsonObject) {
            JsonObject jsonObject = json.getAsJsonObject();
            Shape.ShapeType type = context.deserialize(jsonObject.get("type"), Shape.ShapeType.class);
            switch (type) {
                case Polygon:
                    return context.deserialize(json, PolygonShape.class);
                case MultiPolygon:
                    return context.deserialize(json, MultiPolygonShape.class);
                case Point:
                    return context.deserialize(json, PointShape.class);
                default:
                    throw new JsonParseException("Unrecognized shape type: " + type);
            }
        }
        else if(json instanceof JsonArray) {
            JsonArray  jarray =  json.getAsJsonArray();

            for (int i = 0; i< jarray.size(); i++)
            {

                Shape.ShapeType type = context.deserialize(jarray.get(i).getAsJsonObject().get("type"), Shape.ShapeType.class);
                switch (type) {
                    case Polygon:
                        return context.deserialize(json, PolygonShape.class);
                    case MultiPolygon:
                        return context.deserialize(json, MultiPolygonShape.class);
                    case Point:
                        return context.deserialize(json, PointShape.class);
                    default:
                        throw new JsonParseException("Unrecognized shape type: " + type);
                }

            }
        }
        return null;
        }
}
