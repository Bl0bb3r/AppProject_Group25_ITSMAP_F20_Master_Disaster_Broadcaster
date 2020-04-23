package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;

import java.io.IOException;

public class camera_fragment extends Fragment implements SurfaceHolder.Callback{

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    final int CAMERA_REQUEST_CODE = 1;
    public camera_fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static camera_fragment newInstance(String param1, String param2) {
        camera_fragment fragment = new camera_fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_camera_fragment, container, false);
        surfaceView = rootView.findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        //if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        //{
        //    ActivityCompat.requestPermissions(getActivity(), new String[{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE]);
       // }
       // else{
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
       // }






        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();

        Camera.Parameters parameters;
        parameters = camera.getParameters();

        camera.setDisplayOrientation(90);
        //parameters.setPreviewFrameRate(30);

        //Make cam allways focus
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
