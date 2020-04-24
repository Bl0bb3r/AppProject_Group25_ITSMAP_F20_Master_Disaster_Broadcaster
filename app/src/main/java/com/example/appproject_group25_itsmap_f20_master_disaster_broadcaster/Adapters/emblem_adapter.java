package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;



import java.util.ArrayList;

public class emblem_adapter extends BaseAdapter {
    public emblem_adapter() {
        super();
    }

    private Context context;
    private ArrayList<Disaster> disasters;
    private Disaster disaster;


    public emblem_adapter(Context c, ArrayList<Disaster> disasters) {
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
            convertView = inflator.inflate(R.layout.emblems, null);
        }

        disaster = disasters.get(position);

        // fill the current position with animal information
        if (disaster != null) {


            // set image
            ImageView emblemImage = (ImageView) convertView.findViewById(R.id.emblem_list_image);

            Log.wtf("CustomAdapter", "Image ID: " + disaster.getEmblemImage());

            if (disaster.getEmblemImage() != null)
            {
                emblemImage.setImageResource(Integer.parseInt(disaster.getEmblemImage()));
            }
            else{
                emblemImage.setImageResource(R.drawable.cancel);
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

