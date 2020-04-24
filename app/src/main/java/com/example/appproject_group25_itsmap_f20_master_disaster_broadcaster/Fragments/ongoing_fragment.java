package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.event_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.mydisasters_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.DisasterType;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Eonet;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.MultiPolygonShape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.PointShape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.PolygonShape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.Shape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.ShapeDeserializer;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ongoing_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ongoing_fragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap googleMap;
    MapView mapView;
    ListView listViewEvents;
    Button btn_back;
    event_adapter eventAdapter;

    Gson gson;
    private ArrayList<Event> events = new ArrayList<>();
    MainActivity mainContext;
    //DisasterService disasterService;

    //User Location
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public ongoing_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ongoing_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ongoing_fragment newInstance(String param1, String param2) {
        ongoing_fragment fragment = new ongoing_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //mainContext.disasterService.sendRequest(getActivity().getApplicationContext());
        //get user location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ongoing_fragment, container, false);
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

        listViewEvents = (ListView) rootView.findViewById(R.id.ongoing_listview);

            if (mainContext.disasterService.events.size() > 0) {
                events = mainContext.disasterService.events;
            }
        eventAdapter = new event_adapter(getContext(), events);
        eventAdapter.notifyDataSetChanged();

        listViewEvents.setAdapter(eventAdapter);

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               if (googleMap != null) {
                   if (events.size() > position) {
                       googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GetCords(events.get(position)), 10));
                   }
               }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //get so that i can access DisasterService that is bound to main Activity.
        mainContext = (MainActivity) context;

    }
    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();
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
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        Disaster disaster = (Disaster) marker.getTag();
        gson = new Gson();

        FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentmanager.beginTransaction();

        String jsonDisaster = gson.toJson(disaster);


        submitDisaster_fragment mFragment = submitDisaster_fragment.newInstance(jsonDisaster);
        transaction.replace(R.id.mainactivity_framelayout, mFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        return false;
    }

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

                                googleMap.addPolygon(new PolygonOptions().addAll(points).fillColor(Color.BLUE).strokeColor(Color.YELLOW).visible(true));
                                Log.wtf("PolygonShape", "Polygon contains: " + points.size());

                                break;
                            case MultiPolygon:
                                break;
                            case Point:
                                PointShape point = (PointShape) shape;
                                //Cords from nasa is Lon/lat
                                Log.wtf("PointShape", "Point contains: " + " Lat: " +point.getCoordinates()[1]+" Lon: "+point.getCoordinates()[0]);
                                if (googleMap != null)
                                {
                                    //Cords from nasa is Lon/lat
                                    LatLng mapPoint = new LatLng(((PointShape) shape).getCoordinates()[1], point.getCoordinates()[0]);
                                    Marker markerPoint = googleMap.addMarker(new MarkerOptions().position(mapPoint).title(event.getTitle()).alpha(0.7f));
                                    Disaster disaster = new Disaster();
                                    disaster.setDisasterType(DisasterType.Wildfire);
                                    disaster.setLatDisaster(mapPoint.latitude);
                                    disaster.setLonDisaster(mapPoint.longitude);
                                    markerPoint.setTag(disaster);

                                }

                                break;

                            default:
                                throw new JsonParseException("Unrecognized shape type: " + type);

                        }
                    }
                }
            }
        }
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
                                throw new JsonParseException("Unrecognized shape type: " + type);

                        }
                    }
                }
                return null;
    }
}
