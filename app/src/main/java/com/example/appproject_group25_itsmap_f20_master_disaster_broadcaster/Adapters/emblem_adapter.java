package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import static java.util.Comparator.comparingInt;
import androidx.annotation.RequiresApi;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;


import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

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
        //https://stackoverflow.com/questions/29670116/remove-duplicates-from-a-list-of-objects-based-on-property-in-java-8
        //removed this becuase we need to support 16 and this was 24 minimum
        //this.disasters = (ArrayList<Disaster>) disasters.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(Disaster::getEmblemImage))), ArrayList::new));

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
        ImageView emblemImage = (ImageView) convertView.findViewById(R.id.emblem_list_image);
        // fill the current position with animal information
        if (disaster != null) {

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
}

