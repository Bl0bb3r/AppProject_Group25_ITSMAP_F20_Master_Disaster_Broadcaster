package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.DateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisasterDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisasterDetails extends Fragment implements OnMapReadyCallback {

    private static final String ARG_EVENT = "disasterParams";
    //maps
    private GoogleMap googleMap;
    private MapView mapView;
    private StorageReference storageRef;
    private Gson gson;
    Disaster disaster;

    //views
    TextView points;
    TextView title;
    TextView date;
    ImageView userImage;
    Button btn_back;

    public DisasterDetails() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DisasterDetails newInstance(String disObject) {
        DisasterDetails fragment = new DisasterDetails();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT, disObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://disastermasterbroadcaster.appspot.com/");
        storageRef = storage.getReference();
        if (getArguments() != null) {
            gson = new Gson();
            disaster = gson.fromJson(getArguments().getString(ARG_EVENT), Disaster.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_disaster_details, container, false);

        //get
         points = rootView.findViewById(R.id.textview_points);
         title = rootView.findViewById(R.id.textView_label_title);
         date = rootView.findViewById(R.id.disaster_details_date);
         userImage = rootView.findViewById(R.id.disaster_details_imageView);
         btn_back = rootView.findViewById(R.id.disaster_details_btn_back);
         //set
        points.setText(""+disaster.getPoints());
        title.setText(disaster.getTitle());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        date.setText(dateFormat.format(disaster.getDate()));

        if (disaster.getUserImage() != null) {
            storageRef.child(disaster.getUserImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(getActivity()).load(uri).into(userImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        else{
            userImage.setImageResource(R.drawable.disasterdude);
        }
        mapView = rootView.findViewById(R.id.disaster_details_mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        //back button
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();


            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.setMyLocationEnabled(true);

        final LatLng disasterCords = new LatLng(disaster.getLatDisaster(), disaster.getLonDisaster());
        final LatLng userCords = new LatLng(disaster.getLatUser(), disaster.getLonUser());
        googleMap.addMarker(new MarkerOptions().position(disasterCords).title(disaster.getDisasterType().toString()).alpha(0.7f));
        googleMap.addMarker(new MarkerOptions().position(userCords).title("You"));

        //not needed but maybe we wanna add this TODO: add the distance from the disaster to the view
        float[] results = new float[1];
        Location.distanceBetween(userCords.latitude, userCords.longitude,
                disasterCords.latitude, disasterCords.longitude, results);


        Log.wtf("DisasterDetails","distance: "+results[0]/1000+" KM");
        // https://developers.google.com/maps/documentation/android-sdk/polygon-tutorial
        googleMap.addPolyline(new PolylineOptions().add(userCords,disasterCords).color(Color.BLUE));

        LatLngBounds disaster_user;
        try {
            disaster_user = new LatLngBounds(disasterCords,userCords);
        }catch (Exception e)
        {
            disaster_user = new LatLngBounds(userCords,disasterCords);
            Log.wtf("LatLngBounds", "swapping user and disaster location");
        }
        //set camera posistion and zoomto 0.1f

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(disaster_user.getCenter(),0.1f));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(0.1f), 2000, null);

    }
}
