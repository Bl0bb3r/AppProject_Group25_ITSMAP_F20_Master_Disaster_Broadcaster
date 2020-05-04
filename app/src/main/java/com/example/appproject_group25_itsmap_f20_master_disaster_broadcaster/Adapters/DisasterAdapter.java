package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DisasterAdapter extends RecyclerView.Adapter<DisasterAdapter.MyDisastersViewHolder> {

    private ArrayList<Disaster> disasters;
    private Disaster disaster;
    private StorageReference storageRef;
    private OnDisasterListener mDisasterListener;
    // each data item is just a string in this case
    // Create a storage reference from our app

    public DisasterAdapter() {
        super();
    }

    public DisasterAdapter(ArrayList<Disaster> disasters, OnDisasterListener onDisasterListener) {
        this.disasters = disasters;
        this.mDisasterListener = onDisasterListener;
    }

    @NonNull
    @Override
    public DisasterAdapter.MyDisastersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://disastermasterbroadcaster.appspot.com/");
        storageRef = storage.getReference();
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.personaldisasters,parent, false);

        MyDisastersViewHolder viewholder = new MyDisastersViewHolder(view, mDisasterListener);

        return viewholder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyDisastersViewHolder holder, int position) {

        Disaster disaster = disasters.get(position);

        //set the views from the holder
        TextView points = holder.points;
        ImageView typeImage = holder.typeImage;
        ImageView userImage = holder.userImage;

        points.setText(""+ disaster.getPoints());

        if (disaster.getUserImage() != null) {
            storageRef.child(disaster.getUserImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(holder.userImage).load(uri).into(userImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        if (disaster.getEmblemImage() != null)
        {
            typeImage.setImageResource(Integer.parseInt(disaster.getEmblemImage()));
        }
        else{
            typeImage.setImageResource(R.drawable.flood);
        }

    }



    public static class MyDisastersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public OnDisasterListener onDisasterListener;
        public TextView points;
        // set image
        public ImageView typeImage;
        public ImageView userImage ;

        public MyDisastersViewHolder(View view, OnDisasterListener onDisasterListener)  {
            super(view);

            this.onDisasterListener = onDisasterListener;
            points = view.findViewById(R.id.disasterPoints_textview);
            typeImage = view.findViewById(R.id.disasterTypeImageView);
            userImage = view.findViewById(R.id.userImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDisasterListener.onDisasterClick(getAdapterPosition());
        }
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return disasters.size();
    }

    public interface OnDisasterListener {
        void onDisasterClick(int position);

    }
}
