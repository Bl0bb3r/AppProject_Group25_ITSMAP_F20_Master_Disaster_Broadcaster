package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Utility.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class camera_fragment extends Fragment{

    private static int CurrentCameraId = 1;
    private CameraFragmentListener listener;
    private Camera mCamera = null;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;

    String filename;
    FrameLayout preview;


    public camera_fragment() {
        // Required empty public constructor
    }

    public interface CameraFragmentListener{
        void onImageSent(String input);
    }

    // TODO: Rename and change types and number of parameters
    public static camera_fragment newInstance() {
        camera_fragment fragment = new camera_fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_camera_fragment, container, false);
        Button btn_swap = rootView.findViewById(R.id.btn_switch_cam);

        Button btn_closePreview = (Button) rootView.findViewById(R.id.btn_close_preview_cam);
        Button btn_back_with_img =(Button) rootView.findViewById(R.id.btn_back_with_img);

        btn_closePreview.setVisibility(View.INVISIBLE);
        btn_back_with_img.setVisibility(View.INVISIBLE);

        btn_closePreview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.wtf("CAMERA", "ClosePreview is called");
                        int currentCameraId = 0;
                        if (mPreview.ispreview) {
                            mCamera.stopPreview();
                        }
                        mCamera.release();
                        //swap the id of the camera to be used
                        //currentCameraId = FindCameraId();
                        if(CurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                            CurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }
                        else if(CurrentCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT){
                            CurrentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        }
                        else {
                            CurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }
                        mCamera = Camera.open(CurrentCameraId);

                        setCameraDisplayOrientation(getActivity(), CurrentCameraId, mCamera);
                        try {

                            mCamera.setPreviewDisplay(mPreview.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mCamera.startPreview();
                        btn_swap.setVisibility(View.VISIBLE);
                        btn_closePreview.setVisibility(View.INVISIBLE);
                        btn_back_with_img.setVisibility(View.INVISIBLE);

                    }
                }
        );

        btn_swap.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int currentCameraId = 0;
                        Log.wtf("CAMERA", "SWAP camera is called");
                        if (mPreview.ispreview) {
                            mCamera.stopPreview();
                        }

                        //NB: if you don't release the current camera before switching, you app will crash
                        mCamera.release();

                        //swap the id of the camera to be used
                         //currentCameraId = FindCameraId();
                            if (CurrentCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                CurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                            }
                            else if(CurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                                CurrentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                            }else {
                                CurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                            }

                        mCamera = Camera.open(CurrentCameraId);
                        setCameraDisplayOrientation(getActivity(), CurrentCameraId, mCamera);
                        try {

                            mCamera.setPreviewDisplay(mPreview.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mCamera.startPreview();
                    }

                }
        );

        btn_back_with_img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        ((MainActivity)getActivity()).fileName = filename;
                        listener.onImageSent(filename);

                        //go back
                        Intent intent = new Intent("ReturnFromCamera");
                        LocalBroadcastManager.getInstance(((MainActivity)getActivity())).sendBroadcast(intent);

                    }
                }
        );
        // Add a listener to the Capture button
        Button captureButton = (Button) rootView.findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                        btn_closePreview.setVisibility(View.VISIBLE);
                        btn_swap.setVisibility(View.INVISIBLE);
                        btn_back_with_img.setVisibility(View.VISIBLE);
                    }
                }
        );

        // Create our Preview view and set it as the content of our activity.

        preview = (FrameLayout) rootView.findViewById(R.id.camera_preview);

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getActivity(), mCamera);
        preview.addView(mPreview);
        setCameraDisplayOrientation(getActivity(), CurrentCameraId, mCamera);


    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.wtf("TAG", "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                filename = pictureFile.getAbsolutePath();
            } catch (FileNotFoundException e) {
                Log.wtf("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.wtf("TAG", "Error accessing file: " + e.getMessage());
            }
        }
    };
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CameraFragmentListener)
        {
            listener = (CameraFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement cameraFragmentListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(CurrentCameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DisasterMaster");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("DisasterMaster", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


        private void releaseCamera () {
            if (mCamera != null) {
                mCamera.release();        // release the camera for other applications
                mCamera = null;
            }
        }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        Camera.Parameters p = camera.getParameters();
        p.setRotation(result);
        camera.setParameters(p);
        camera.setDisplayOrientation(result);
    }



}


