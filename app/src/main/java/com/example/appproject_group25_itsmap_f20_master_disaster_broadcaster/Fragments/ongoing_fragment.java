package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.EventAdapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.DisasterType;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.PointShape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.PolygonShape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.Shape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Utility.LoadImageTask;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ongoing_fragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, EventAdapter.OnEventListener {


    GoogleMap googleMap;
    MapView mapView;
    RecyclerView recyclerViewEvents;
    Button btn_back;
    EventAdapter eventAdapter;

    Gson gson;
     private static  ArrayList<Event> events;

    //User Location
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //view
    View rootView;
    public ongoing_fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ongoing_fragment newInstance(ArrayList<Event> _events) {
        ongoing_fragment fragment = new ongoing_fragment();
        events = _events;
        return fragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        rootView = inflater.inflate(R.layout.fragment_ongoing_fragment, container, false);
        //Maps
        //https://developers.google.com/maps/documentation/android-sdk/start
        mapView = rootView.findViewById(R.id.mapView_ongoing);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);



        btn_back = (Button) rootView.findViewById(R.id.ongoing_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();
            }
        });

        recyclerViewEvents = (RecyclerView) rootView.findViewById(R.id.ongoing_recyclerview);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recyclerview_space));
        recyclerViewEvents.addItemDecoration(dividerItemDecoration);

        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getActivity()));


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.wtf("OnGoing", "DisasterService is -- isBound: " + ((MainActivity) getActivity()).isBound);
       // if (((MainActivity) getActivity()).isBound) {

        //    if (((MainActivity) getActivity()).disasterService.events.size() > 0) {
         //       events = ((MainActivity) getActivity()).disasterService.events;

                //      for(int i = 0; i < events.size()-1; i++)
                //      {
                //          for (int j = 0; j < ((MainActivity)getActivity()).disasterService.UsersDisasters.size()-1;j++)
                //          {
                //              if (events.get(i).getTitle().equals(((MainActivity)getActivity()).disasterService.UsersDisasters.get(j).getTitle()))
                //              {
                //                 events.remove(i);
                //            }
                //        }

                Log.wtf("OnGoing", "Events -- Size: " + events.size());

                eventAdapter = new EventAdapter(events, this);
                eventAdapter.notifyDataSetChanged();
                recyclerViewEvents.setAdapter(eventAdapter);
        //    }
       // }
    }
    @Override
    public void onStart() {

        super.onStart();

    }
    @Override
    public void onStop() {
        super.onStop();
    }




    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        setUpMap();
        if (events.size() > 0) {
            SetMarkersOnMap(events);
        }
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //data here
                        LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
                        //move camera to user
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user,10));
                        // Zoom in, animating the camera.
                        //googleMap.animateCamera(CameraUpdateFactory.zoomIn());

                    }
                }
            }
        };

       //https://stackoverflow.com/questions/48529963/using-mfusedlocationclient-to-get-current-location-within-a-firebase-service
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            //data here
                            LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
                            //move camera to user
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user,10));
                            // Zoom in, animating the camera.
                            //googleMap.animateCamera(CameraUpdateFactory.zoomIn());

                        }
                    }
                });


        googleMap.setOnMarkerClickListener(this);

    }

    public void setUpMap(){

        googleMap.setMyLocationEnabled(true);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        Disaster disaster = (Disaster) marker.getTag();
        gson = new Gson();

        String jsonDisaster = gson.toJson(disaster);

        //go to submit disaster
        Intent intent = new Intent("GoToSubmit");
        intent.putExtra("Disaster", jsonDisaster);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

        return false;
    }


    private LatLng GetCords(Event event)
    {
                if (event.getGeometry() != null) {
                    for (Shape shape : event.getGeometry()) {
                        Shape.ShapeType type = shape.getType();

                        switch (type) {
                            case Polygon:
                                PolygonShape polygon = (PolygonShape) shape;
                                List<LatLng> points = new ArrayList<>();
                                for(int i = 0; i< polygon.getCoordinates().length; i++)
                                {
                                    int arayArrayLength = polygon.getCoordinates()[i].length;
                                    for(int j = 0; j< polygon.getCoordinates()[i].length; j++)
                                    {
                                        //Cords from nasa is Lon/lat
                                        double lat = polygon.getCoordinates()[i][j][1];
                                        double lng = polygon.getCoordinates()[i][j][0];
                                        LatLng latLng = new LatLng(lat, lng);
                                        points.add(latLng);
                                    }
                                }
                                return points.get(0);

                            case MultiPolygon:
                                return null;

                            case Point:
                                PointShape point = (PointShape) shape;
                                    //Cords from nasa is Lon/lat
                                    LatLng latLng = new LatLng(((PointShape) shape).getCoordinates()[1], point.getCoordinates()[0]);
                                    return latLng;
                            default:
                                throw new JsonParseException(""+getText(R.string.unrecognized_shape) + type);

                        }
                    }
                }
                return null;
    }
//TODO: support other types of GeoJson data
    private void SetMarkersOnMap(List<Event> events)
    {

        if (events != null) {
            for (Event event : events) {
                if (event.getGeometry() != null) {
                    for (Shape shape : event.getGeometry()) {
                        Shape.ShapeType type = shape.getType();

                        switch (type) {
                            case Polygon:
                                PolygonShape polygon = (PolygonShape) shape;
                                List<LatLng> points = new ArrayList<>();
                                for(int i = 0; i< polygon.getCoordinates().length; i++)
                                {
                                    int arayArrayLength = polygon.getCoordinates()[i].length;
                                    for(int j = 0; j< polygon.getCoordinates()[i].length; j++)
                                    {
                                        //Cords from nasa is Lon/lat
                                        double lat = polygon.getCoordinates()[i][j][1];
                                        double lng = polygon.getCoordinates()[i][j][0];
                                        LatLng latLng = new LatLng(lat, lng);
                                        points.add(latLng);
                                    }
                                }

                                //googleMap.addPolygon(new PolygonOptions().addAll(points).fillColor(Color.BLUE).strokeColor(Color.YELLOW).visible(true));

                                break;
                            case MultiPolygon:
                                break;
                            case Point:
                                PointShape point = (PointShape) shape;
                                //Cords from nasa is Lon/lat
                                //Log.wtf("PointShape", "Point contains: " + " Lat: " +point.getCoordinates()[1]+" Lon: "+point.getCoordinates()[0]);
                                if (googleMap != null)
                                {
                                    //Cords from nasa is Lon/lat
                                    LatLng mapPoint = new LatLng(((PointShape) shape).getCoordinates()[1], point.getCoordinates()[0]);

                                    String resName = MapIconPicker(event);

                                    InputStream is = ((MainActivity)getActivity()).getResources().openRawResource(Integer.parseInt(resName));
                                    Bitmap bit = BitmapFactory.decodeStream(new BufferedInputStream(is));

                                    Bitmap bitmap = null;
                                    Disaster disaster = new Disaster();
                                    disaster.setDisasterType(DisasterTypeFromTitle(event));
                                    disaster.setTitle(event.getTitle());
                                    disaster.setLatDisaster(mapPoint.latitude);
                                    disaster.setLonDisaster(mapPoint.longitude);
                                    disaster.setEmblemImage(resName);

                                    try {
                                        bitmap = new LoadImageTask().execute(bit).get();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Marker markerPoint = googleMap.addMarker(new MarkerOptions().position(mapPoint).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title(event.getTitle()).alpha(0.7f));
                                    markerPoint.setTag(disaster);

                                }

                                break;

                            default:
                                throw new JsonParseException(""+getText(R.string.unrecognized_shape) + type);

                        }
                    }
                }
            }
        }
    }
    //TODO: Refactor these 2
    public String MapIconPicker(Event event)
    {
        if (event.getTitle() != null) {

            String title = event.getTitle().toLowerCase();

            if (title.contains("fire"))
            {
                return ""+ R.drawable.fire;
                //return "fire";
            }
            else if (title.contains("cyclone"))
            {
                return ""+R.drawable.tornado;
                //return "tornado";
            }
            else if (title.contains("tornado"))
            {
                return ""+R.drawable.tornado;
                //return "tornado";
            }
            else if (title.contains("volcano"))
            {
                return ""+R.drawable.volcano;
                //return "volcano";
            }
            else if (title.contains("iceberg"))
            {
                return ""+R.drawable.iceberg;
                //return "iceberg";
            }
            else if (title.contains("flood"))
            {
                return ""+R.drawable.flood;
                //return "flood";
            }
            else if (title.contains("blizzard"))
            {
                return ""+R.drawable.blizzard;
                //return "blizzard";
            }
            else if (title.contains("hail"))
            {
                return ""+R.drawable.hail;
                //return "hail";
            }
            else if (title.contains("drought"))
            {
                return ""+R.drawable.drought;
                //return "drought";
            }
            else if (title.contains("dust"))
            {
                return ""+R.drawable.dust;
                //return "dust";
            }
            else if (title.contains("meteor"))
            {
                return ""+R.drawable.meteor;
                //return "meteor";
            }
            else if (title.contains("earthquake"))
            {
                return ""+R.drawable.ground;
                //return "ground";
            }
            else if (title.contains("landslide"))
            {
                return ""+R.drawable.danger;
                //return "danger";
            }
            else if (title.contains("avalance"))
            {
                return ""+R.drawable.season;
                //return "Avalance";
            }
            else if (title.contains("thunder"))
            {
                return ""+R.drawable.storm;
                //return "storm";
            }
            else if (title.contains("tsunamien"))
            {
                return ""+R.drawable.wave;
                //return "wave";
            }
            else if (title.contains("heat"))
            {
                return ""+R.drawable.sun;
                //return "sun";
            }
            else{
                return ""+R.drawable.cancel;
                //return "cancel";
            }
        } else {

            return ""+R.drawable.cancel;
            //return "cancel";
        }
    }

    public DisasterType DisasterTypeFromTitle(Event event)
    {
        if (event.getTitle() != null) {

            String title = event.getTitle();

            if (title.contains("fire")) {
                return DisasterType.Wildfire;
            } else if (title.contains("Cyclone")) {
                return DisasterType.CyclonicStorm;
            } else if (title.contains("Tornado")) {
                return DisasterType.Tornado;
            } else if (title.contains("Volcano")) {
                return DisasterType.Volcano;
            } else if (title.contains("Iceberg")) {
                return DisasterType.Iceberg;
            } else if (title.contains("Flood")) {
                return DisasterType.Flood;
            } else if (title.contains("Blizzard")) {
                return DisasterType.Blizzard;
            } else if (title.contains("Hail")) {
                return DisasterType.HailStorm;
            } else if (title.contains("Drought")) {
                return DisasterType.Drought;
            } else if (title.contains("Dust")) {
                return DisasterType.DustStorm;
            } else if (title.contains("Meteor")) {
                return DisasterType.Meteor;
            } else if (title.contains("Earthquake")) {
                return DisasterType.Earthquake;
            } else if (title.contains("Landslide")) {
                return DisasterType.Landslide;
            } else if (title.contains("Avalance")) {
                return DisasterType.Landslide;
            } else if (title.contains("Thunder")) {
                return DisasterType.Thunderstorm;
            } else if (title.contains("Tsunami")) {
                return DisasterType.Tsunami;
            } else if (title.contains("Heat")) {
                return DisasterType.HeatWeave;
            }
            else{
                return DisasterType.Unknown;
            }
        }

        return DisasterType.Unknown;
    }

    @Override
    public void onEventClick(int position) {
        Log.wtf("EventAdapter", "Click called position: "+ position);
        if (googleMap != null) {
            if (events.size() > position) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GetCords(events.get(position)), 10));
            }
        }
    }


}
