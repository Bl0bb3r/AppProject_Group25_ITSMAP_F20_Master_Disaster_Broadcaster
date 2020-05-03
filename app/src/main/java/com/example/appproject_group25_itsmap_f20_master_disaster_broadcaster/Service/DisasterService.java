package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Utility.Repository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.core.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisasterService extends Service {

    private RequestQueue requestQueue;
    private Global global;
    private IBinder binder = new DisasterServiceBinder();
    public ArrayList<Event> events = new ArrayList<>();
    public ArrayList<Disaster> UsersDisasters = new ArrayList<>();
    private Gson gson;
    //firebase
    FirebaseFirestore db;
    FirebaseStorage storage;
    public StorageReference storageRef;
    //Firebase authentication variable
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;

//database methods
    Repository repository;


    @Override
    public void onCreate() {


        // Access a Cloud Firestore instance from your Activity
        FirebaseApp.initializeApp(getApplicationContext());
         db = FirebaseFirestore.getInstance();
         storage = FirebaseStorage.getInstance("gs://disastermasterbroadcaster.appspot.com/");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Create a storage reference from our app
        storageRef = storage.getReference();

        repository = new Repository(db, storage, storageRef, getApplicationContext());



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
        UsersDisasters = (ArrayList<Disaster>) GetAllDisasters();
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
                        //Log.wtf("StringRequest ", response);
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

                Intent intent = new Intent("NoEvents");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
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

   //Firebase
    public List<Disaster> GetAllDisasters()
    {
        return repository.GetAllDisasters(currentUser.getUid());
    }

    public Disaster GetDisaster(String disasterId)
    {
        return repository.GetDisaster(currentUser.getUid(), disasterId);
    }

    public void InsertDisaster(Disaster disaster)
    {
        repository.InsertDisaster(disaster, currentUser.getUid());
    }

    public String UploadImage(String filepath)
    {
        return repository.UploadImage(filepath);
    }

    public List<User> GetAllUsers()
    {
        return repository.GetAllUsers();
    }

    public User GetUser() {
        return repository.GetUser(currentUser.getUid());
    }

    public void UpdateUser(User user) {
        repository.UpdateUser(user, currentUser.getUid());
    }

}

