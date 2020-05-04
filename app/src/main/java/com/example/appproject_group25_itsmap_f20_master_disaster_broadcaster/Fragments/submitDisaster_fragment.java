package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.DisasterType;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Date;


public class submitDisaster_fragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {
    private static final String ARG_EVENT = "eventParam";

    public String DISASTER_IMAGE = "DisasterImage";
    private submitDisasterListener listener;
    //maps
    private GoogleMap googleMap;
    private MapView mapView;
    //User Location
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    //elements in view
    private Button btn_back;
    private Button btn_submit;
    private Button btn_cam;
    private ImageView disasterImage;
    // rest
    private Gson gson;
    private Disaster disaster;

    //File
    File currentFile;

    //View
    View rootView;

    public interface submitDisasterListener{
        void onInputSubmitSent(String input);
    }

    public submitDisaster_fragment() {
        // Required empty public constructor
    }

    public void updateImage(String filename)
    {
        BitmapFactory.Options options;
        currentFile = new File(filename);


        //}
    }
    // TODO: Rename and change types and number of parameters
    public static submitDisaster_fragment newInstance(String disObject) {
        submitDisaster_fragment fragment = new submitDisaster_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT, disObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get arguments here we get the disaster object the user picked
        if (getArguments() != null) {
            gson = new Gson();
            disaster = gson.fromJson(getArguments().getString(ARG_EVENT), Disaster.class);

        }
        if(savedInstanceState != null)
        {
            String fileN = savedInstanceState.getString(DISASTER_IMAGE);
            if(fileN != null){
                currentFile = new File(fileN);
            }

        }


        //get user location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());


    }
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_submit_disaster_fragment, container, false);
         //DisasterImage
        disasterImage = (ImageView) rootView.findViewById(R.id.imageView_submitDisaster);

        //Maps
        //https://developers.google.com/maps/documentation/android-sdk/start

        //take pic button
        btn_cam = rootView.findViewById(R.id.btn_cam);
        btn_cam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go to cam
                Intent intent = new Intent("GoToCamera");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                //go to cam
                //FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                //FragmentTransaction transaction = fragmentmanager.beginTransaction();

                //transaction.replace(R.id.mainactivity_framelayout, new camera_fragment());
                //transaction.addToBackStack(null);
                //transaction.commit();

            }
        });
        //submit button
        btn_submit = (Button) rootView.findViewById(R.id.submit_btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Date date = new Date();
                date.getTime();
                disaster.setDate(date);

                if (currentFile != null) {
                    String ImageName =  ((MainActivity)getActivity()).disasterService.UploadImage(currentFile.getAbsolutePath());
                    disaster.setUserImage(ImageName);
                }

                ((MainActivity)getActivity()).disasterService.UsersDisasters.add(disaster);
                ((MainActivity)getActivity()).disasterService.InsertDisaster(disaster);

                Toast.makeText( ((MainActivity)getActivity()).disasterService, "You got "+disaster.getPoints()+" point(s)!", Toast.LENGTH_LONG).show();
                //go back to ongoing fragment
                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();


            }
        });

        //back button
        btn_back = (Button) rootView.findViewById(R.id.submit_btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentmanager = getActivity().getSupportFragmentManager();
                fragmentmanager.popBackStack();


            }
        });

        // Inflate the layout for this fragment
         return rootView;
    }
    @Override
    public void onStart() {

        super.onStart();
        if(currentFile != null)
        {
            int rotateImage = getCameraPhotoOrientation(getActivity(), Uri.fromFile(currentFile),
                    currentFile.getAbsolutePath());

            disasterImage.setRotation(rotateImage);
            //Bitmap bitmap = decodefile(currentFile.getAbsolutePath());
            Glide.with(rootView).load(currentFile).placeholder(R.drawable.disasterdude).into(disasterImage);
        }



    }
    @Override
    public void onStop() {

        super.onStop();
    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {

        googleMap = gMap;

        googleMap.setMyLocationEnabled(true);

        final LatLng disasterCords = new LatLng(disaster.getLatDisaster(), disaster.getLonDisaster());

        googleMap.addMarker(new MarkerOptions().position(disasterCords).title(disaster.getDisasterType().toString()).alpha(0.7f));

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
                        googleMap.animateCamera(CameraUpdateFactory.zoomIn());

                        float[] results = new float[1];
                        Location.distanceBetween(user.latitude, user.longitude,
                                disaster.getLatDisaster(), disaster.getLonDisaster(), results);


                        Log.wtf("Distance","distance: "+results[0]/1000+" KM");
                       // https://developers.google.com/maps/documentation/android-sdk/polygon-tutorial
                        googleMap.addPolyline(new PolylineOptions().add(user,disasterCords).color(Color.BLUE));


                        //northern latitude must NOT!!! exceed southern latitude
                        //thats why this is in a try catch if it exceed that swap the coordinates
                        LatLngBounds disaster_user;
                        try {
                            disaster_user = new LatLngBounds(disasterCords,user);
                        }catch (Exception e)
                        {
                            disaster_user = new LatLngBounds(user,disasterCords);
                            Log.wtf("LatLngBounds", "swapping user and disaster location");
                        }
                        //set camera posistion and zoomto 0.1f
                        //TODO: Find out why the coordinates dosent center between the two points sometimes.
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(disaster_user.getCenter(),0.1f));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(0.1f), 2000, null);

                        //set users disaster TODO: find out how to set emblem and rank onLocationResult
                        disaster.setLatUser(user.latitude);
                        disaster.setLonUser(user.longitude);
                        disaster.setDistance(results[0]/1000);
                        disaster.setPoints();
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
                            googleMap.animateCamera(CameraUpdateFactory.zoomIn());

                            float[] results = new float[1];
                            Location.distanceBetween(user.latitude, user.longitude,
                                    disaster.getLatDisaster(), disaster.getLonDisaster(), results);


                            Log.wtf("Distance","distance: "+results[0]/1000+" KM");
                            // https://developers.google.com/maps/documentation/android-sdk/polygon-tutorial
                            googleMap.addPolyline(new PolylineOptions().add(user,disasterCords).color(Color.BLUE));



                            //northern latitude must NOT!!! exceed southern latitude
                            //thats why this is in a try catch if it exceed that swap the coordinates
                            LatLngBounds disaster_user;
                            try {
                                disaster_user = new LatLngBounds(disasterCords,user);
                            }catch (Exception e)
                            {
                                disaster_user = new LatLngBounds(user,disasterCords);
                                Log.wtf("LatLngBounds", "swapping user and disaster location");
                            }
                            //set camera position and zoomto 0.1f
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(disaster_user.getCenter(),0.1f));
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(0.1f), 2000, null);

                            //set users disaster TODO: find out how to set emblem and rank getLastLocation
                            disaster.setLatUser(user.latitude);
                            disaster.setLonUser(user.longitude);
                            disaster.setDistance(results[0]/1000);
                            disaster.setPoints();

                        }
                    }
                });

        googleMap.setOnMarkerClickListener(this);
    }
    public DisasterType DisasterTypeFromTitle(Disaster event)
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            currentFile = new File(savedInstanceState.getString(DISASTER_IMAGE));
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {


        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
        if(currentFile != null) {
            outState.putString(DISASTER_IMAGE, currentFile.getAbsolutePath());
        }
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri,
                                         String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.wtf("RotateImage", "Exif orientation: " + orientation);
            Log.wtf("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
        //return 90;
    }

    public static Bitmap decodefile(String path) {

        int orientation;

        try {

            if(path==null){

                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 4;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap bm = BitmapFactory.decodeFile(path,o2);


            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.e("orientation",""+orientation);
            Matrix m=new Matrix();

            if((orientation==3)){

                m.postRotate(180);
                m.postScale((float)bm.getWidth(), (float)bm.getHeight());

//               if(m.preRotate(90)){
                Log.e("in orientation",""+orientation);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                return  bitmap;
            }
            else if(orientation==6){

                m.postRotate(90);

                Log.e("in orientation",""+orientation);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                return  bitmap;
            }

            else if(orientation==8){

                m.postRotate(270);

                Log.e("in orientation",""+orientation);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                return  bitmap;
            }
            else if(orientation==0){

                m.postRotate(270);

                Log.e("in orientation",""+orientation);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                return  bitmap;
            }
            return bitmap;
        }
        catch (Exception e) {
        }
        return null;
    }

}
