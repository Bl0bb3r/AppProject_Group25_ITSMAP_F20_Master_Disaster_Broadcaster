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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Eonet;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.Shape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.ShapeDeserializer;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    public ArrayList<Disaster> UsersDisasters = new ArrayList<>();
    private Gson gson;
    //firebase
    FirebaseFirestore db;
    @Override
    public void onCreate() {

        // Access a Cloud Firestore instance from your Activity
        FirebaseApp.initializeApp(getApplicationContext());
         db = FirebaseFirestore.getInstance();

        global = new Global();

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

       GsonBuilder builder = new GsonBuilder();
       builder.registerTypeAdapter(Shape.class, new ShapeDeserializer());
       gson = builder.create();

       eonet = gson.fromJson(json, new TypeToken<Eonet>(){}.getType());

       events = eonet.getEvents();
       Intent intent = new Intent("NewEvent");
       intent.putExtra("event", gson.toJson(eonet));
       LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
       return events;
   }

   //FIREBASE
    public void InsertDisaster(Disaster disaster)
    {
        // Add a new document with a generated ID
        db.collection("disasters")
                .add(disaster)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FIREBASE", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE", "Error adding document", e);
                    }
                });

    }

    public void GetAllDisasters(){
         db.collection("disasters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.wtf("FIREBASE", document.getId() + " => " + document.getData());

                                UsersDisasters.add(document.toObject(Disaster.class));
                            }
                            Intent intent = new Intent("GetALLDB");
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        } else {
                            Log.wtf("FIREBASE", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}

