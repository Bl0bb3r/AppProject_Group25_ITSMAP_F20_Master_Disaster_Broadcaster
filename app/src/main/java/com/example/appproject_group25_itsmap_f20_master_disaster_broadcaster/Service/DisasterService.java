package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Eonet;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.Shape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.ShapeDeserializer;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Global;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisasterService extends Service {

    private RequestQueue requestQueue;
    private Global global;
    private IBinder binder = new DisasterServiceBinder();
    public ArrayList<Event> events = new ArrayList<>();
    private Gson gson;

    @Override
    public void onCreate() {

    global = new Global();
    //gson = new Gson();

        //First time install broadcast this or on open
        Intent intent = new Intent("FIRST_START");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.wtf("DisasterService","Service started");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.wtf("DisasterService", "Service has stopped");
    }

    //the request
    public void sendRequest(final Context ctx){
        //Log.wtf("Web", "Request: ");
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(ctx);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET,global.NASAEONET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //log to the the content before parseing it word object.
                        Log.wtf("StringRequest ", response);
                        ParseEvents(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            //https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
            public void onErrorResponse(VolleyError error) {
                Log.wtf("Web", "Request failed",error);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
                Toast.makeText(ctx,"No events nearby", Toast.LENGTH_LONG).show();

            }

        })
        {
            //Header that contain token
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json;");
                params.put("Authorization", global.NASAAPIKEY);
                return params;
            }};


        requestQueue.add(stringRequest);

    }

    public class DisasterServiceBinder extends Binder {
        public DisasterService getService() {
            return DisasterService.this;
        }
    }

   private List<Event> ParseEvents(String json)
   {
       Eonet eonet = new Eonet();

       //CoordinatesDeserializer deserializer = new CoordinatesDeserializer("type");
       //deserializer.registerCoordinateType("Point", Point.class);
       //deserializer.registerCoordinateType("Polygon", Polygon.class);

       GsonBuilder builder = new GsonBuilder();
       builder.registerTypeAdapter(Shape.class, new ShapeDeserializer());
       gson = builder.create();
       //gson = new GsonBuilder().registerTypeAdapter(Shape.class, new ShapeDeserializer()).create();


       //Type EventListType = new TypeToken<ArrayList<Event>>(){}.getType();
       //eonet = gson.fromJson(json, Eonet.class);

       eonet = gson.fromJson(json, new TypeToken<Eonet>(){}.getType());

       events = eonet.getEvents();
       Intent intent = new Intent("NewEvent");
       intent.putExtra("event", gson.toJson(eonet));
       LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
       return events;
   }
}

