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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
//for all adapters https://www.youtube.com/watch?v=3WR4QAiVuCw
//https://github.com/firebase/FirebaseUI-Android/tree/master/firestore
public class DisasterAdapter extends FirestoreRecyclerAdapter<Disaster, DisasterAdapter.MyDisastersViewHolder> {

    private ArrayList<Disaster> disasters;
    private Disaster disaster;
    private StorageReference storageRef;
    private OnDisasterListener mDisasterListener;
    // each data item is just a string in this case
    // Create a storage reference from our app

    public DisasterAdapter(@NonNull FirestoreRecyclerOptions<Disaster> options, OnDisasterListener onDisasterListener)
    {
        super(options);
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
    protected void onBindViewHolder(@NonNull MyDisastersViewHolder holder, int position, @NonNull Disaster model) {


        holder.points.setText(""+ model.getPoints());

        if (model.getUserImage() != null) {
            storageRef.child(model.getUserImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Glide.with(holder.userImage).load(uri).into(holder.userImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        if (model.getEmblemImage() != null)
        {
            holder.typeImage.setImageResource(Integer.parseInt(model.getEmblemImage()));
        }
        else{
            holder.typeImage.setImageResource(R.drawable.flood);
        }
    }


    public class MyDisastersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            if (getAdapterPosition() != RecyclerView.NO_POSITION && onDisasterListener != null)
            {
                onDisasterListener.onDisasterClick(getAdapterPosition(), getSnapshots().getSnapshot(getAdapterPosition()));
            }

        }
    }


    public interface OnDisasterListener {
        void onDisasterClick(int position, DocumentSnapshot documentSnapshot);

    }
}
