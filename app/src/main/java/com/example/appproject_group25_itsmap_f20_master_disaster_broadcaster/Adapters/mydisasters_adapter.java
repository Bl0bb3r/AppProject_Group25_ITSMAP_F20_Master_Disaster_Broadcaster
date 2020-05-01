package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class mydisasters_adapter extends BaseAdapter {
    public mydisasters_adapter() {
        super();
    }

    private Context context;
    private ArrayList<Disaster> disasters;
    private Disaster disaster;
    private StorageReference storageRef;

    public mydisasters_adapter(Context c, ArrayList<Disaster> disasters) {
        this.context = c; //we need the context to inflate views
        this.disasters = disasters;
    }

    @Override
    public int getCount() {
        //return size of the array list
        if (disasters != null) {
            return disasters.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        //return the item at the given position
        if (disasters != null) {
            return disasters.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //we only need to create the views once, if not null we will reuse the existing view and update its values
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.personaldisasters, null);
        }

        disaster = disasters.get(position);

        // fill the current position with animal information
        if (disaster != null) {

            FirebaseStorage storage = FirebaseStorage.getInstance("gs://disastermasterbroadcaster.appspot.com/");
            // Create a storage reference from our app
            storageRef = storage.getReference();

            TextView points = (TextView) convertView.findViewById(R.id.disasterPoints_textview);
            points.setText(""+disaster.getPoints());
            // set image
            ImageView Typeimage = (ImageView) convertView.findViewById(R.id.disasterTypeImageView);
            ImageView userImage = (ImageView) convertView.findViewById(R.id.userImageView);

            if (disaster.getUserImage() != null) {
                storageRef.child(disaster.getUserImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        Glide.with(parent).load(uri).placeholder(R.drawable.ic_launcher_foreground).into(userImage);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
            Log.wtf("CustomAdapter", "Image ID: " + disaster.getEmblemImage());

            if (disaster.getEmblemImage() != null)
            {
                Typeimage.setImageResource(Integer.parseInt(disaster.getEmblemImage()));
            }
            else{
                Typeimage.setImageResource(R.drawable.flood);
            }
        }
        return convertView;
    }
    public void updateList(ArrayList<Disaster> updatedDisasters)
    {
        disasters = updatedDisasters;
        notifyDataSetChanged();
    }
}
