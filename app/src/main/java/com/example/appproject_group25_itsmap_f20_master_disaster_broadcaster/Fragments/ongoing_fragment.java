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

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.event_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters.mydisasters_adapter;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Eonet;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.MultiPolygonShape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.PointShape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.PolygonShape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.Shape;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.GeoJson.ShapeDeserializer;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
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
    Intent serviceIntent;
    ServiceConnection disasterServiceConnection;
    DisasterService disasterService;
    private boolean isBound;
    private LocalBroadcastManager localBroadcastManager;


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

        //setup broadcast filters and register it.
        IntentFilter filter = new IntentFilter();
        filter.addAction("NewEvent");


        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(DisasterReceiver, filter);

        Event testEvent1 = new Event();
        testEvent1.setId("1");
        testEvent1.setTitle("Wildfire");
        events.add(testEvent1);

        serviceIntent = new Intent(getActivity(), DisasterService.class);
        //bind service
        DisasterServiceConnection();
        getActivity().bindService(serviceIntent, disasterServiceConnection, Context.BIND_AUTO_CREATE);


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
        if (isBound) {
            if (disasterService.events.size() > 1) {
                events = disasterService.events;
            }
        }
        eventAdapter = new event_adapter(getContext(), events);
        eventAdapter.notifyDataSetChanged();

        listViewEvents.setAdapter(eventAdapter);

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO: go to SubmitDisasterFragment

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void DisasterServiceConnection()
    {
        disasterServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DisasterService.DisasterServiceBinder binder = (DisasterService.DisasterServiceBinder) service;
                isBound = true;
                disasterService = binder.getService();


                disasterService.sendRequest(getActivity().getApplicationContext());
                Log.wtf("Binder", "OngoingFragment bound to service");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
                Log.wtf("Binder", "OngoingFragment unbound to service");
            }
        };
    }


    private BroadcastReceiver DisasterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("NewEvent"))
            {
                events.clear();
                events = disasterService.events;
                eventAdapter.updateList(events);

                // Add a marker in Sydney, Australia, and move the camera.
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
                                              double lat = polygon.getCoordinates()[i][j][0];
                                              double lng = polygon.getCoordinates()[i][j][1];
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
                                       Log.wtf("PointShape", "Point contains: " + " Lat: " +point.getCoordinates()[0]+" Lon: "+point.getCoordinates()[1]);
                                       if (googleMap != null)
                                       {
                                           LatLng mapPoint = new LatLng(((PointShape) shape).getCoordinates()[0], point.getCoordinates()[1]);
                                           Marker markerPoint = googleMap.addMarker(new MarkerOptions().position(mapPoint).title(event.getTitle()).alpha(0.7f));
                                           markerPoint.setTag(event);

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

            //this is broadcast when the service is started for the first time.
            else if (intent.getAction().equals("FIRST_START"))
            {
                //BAD DONT do this in a real app
                //TODO figure out how to update adapter on first open
                //try{

                    //Thread.sleep(1500);

                //}
               // catch(InterruptedException e){

               // }
                if (isBound) {

                }
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        googleMap.setOnMarkerClickListener(this);
        setUpMap();
    }

    public void setUpMap(){

        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //googleMap.setMyLocationEnabled(true);
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        Event event = (Event) marker.getTag();
        Log.wtf("MapClick", "event: "+event.getTitle()+" Type: "+event.getGeometry().get(0).getType());

        FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentmanager.beginTransaction();

        transaction.replace(R.id.mainactivity_framelayout, new submitDisaster_fragment());
        transaction.addToBackStack(null);
        transaction.commit();
        return false;
    }
}
