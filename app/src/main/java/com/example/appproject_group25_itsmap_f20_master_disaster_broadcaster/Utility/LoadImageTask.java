package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.telecom.Call;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.common.base.Stopwatch;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
//https://stackoverflow.com/questions/19344100/android-createscaledbitmap-slow-when-creating-multiple-bitmaps


public class LoadImageTask extends AsyncTask<Bitmap, Void, Bitmap> {

    private Bitmap mBitmap;

    @Override
    protected Bitmap doInBackground(Bitmap... calls) {

        Bitmap bitmap = calls[0];
        Bitmap ScaledBitmap = null;
        ScaledBitmap = Bitmap.createScaledBitmap(bitmap, 75, 75, false);

        return ScaledBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
       // callback.onResponse(result);
    }
}