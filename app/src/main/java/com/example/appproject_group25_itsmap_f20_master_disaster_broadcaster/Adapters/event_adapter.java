package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;

import java.util.ArrayList;

public class event_adapter extends BaseAdapter {
    public event_adapter() {
        super();
    }

    private Context context;
    private ArrayList<Event> events;
    private Event event;

    public event_adapter(Context c, ArrayList<Event> events) {
        this.context = c; //we need the context to inflate views
        this.events = events;
    }

    @Override
    public int getCount() {
        //return size of the array list
        if (events != null) {
            return events.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        //return the item at the given position
        if (events != null) {
            return events.get(position);
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
            convertView = inflator.inflate(R.layout.ongoing_list, null);
        }

        event = events.get(position);

        // fill the current position with animal information
        if (event != null) {


            // set image
            ImageView emblemImage = (ImageView) convertView.findViewById(R.id.ongoing_disaster_image);
            TextView eventTitle = (TextView) convertView.findViewById(R.id.ongoing_title_disaster_text);

            eventTitle.setText(event.getTitle());
            if (event.getTitle() != null) {

                String title = event.getTitle().toLowerCase();

                if (title.contains("fire"))
                {
                    emblemImage.setImageResource(R.drawable.fire);
                }
                else if (title.contains("cyclone"))
                {
                    emblemImage.setImageResource(R.drawable.tornado);
                }
                else if (title.contains("tornado"))
                {
                    emblemImage.setImageResource(R.drawable.tornado);
                }
                else if (title.contains("volcano"))
                {
                    emblemImage.setImageResource(R.drawable.volcano);
                }
                else if (title.contains("iceberg"))
                {
                    emblemImage.setImageResource(R.drawable.iceberg);
                }
                else if (title.contains("flood"))
                {
                    emblemImage.setImageResource(R.drawable.flood);
                }
                else if (title.contains("blizzard"))
                {
                    emblemImage.setImageResource(R.drawable.blizzard);
                }
                else if (title.contains("hail"))
                {
                    emblemImage.setImageResource(R.drawable.hail);
                }
                else if (title.contains("drought"))
                {
                    emblemImage.setImageResource(R.drawable.drought);
                }
                else if (title.contains("dust"))
                {
                    emblemImage.setImageResource(R.drawable.dust);
                }
                else if (title.contains("meteor"))
                {
                    emblemImage.setImageResource(R.drawable.meteor);
                }
                else if (title.contains("earthquake"))
                {
                    emblemImage.setImageResource(R.drawable.ground);
                }
                else if (title.contains("landslide"))
                {
                    emblemImage.setImageResource(R.drawable.danger);
                }
                else if (title.contains("avalance"))
                {
                    emblemImage.setImageResource(R.drawable.season);
                }
                else if (title.contains("thunder"))
                {
                    emblemImage.setImageResource(R.drawable.storm);
                }
                else if (title.contains("tsunamien"))
                {
                    emblemImage.setImageResource(R.drawable.wave);
                }
                else if (title.contains("heat"))
                {
                    emblemImage.setImageResource(R.drawable.sun);
                }
                else{
                    emblemImage.setImageResource(R.drawable.cancel);
                }
            } else {
                emblemImage.setImageResource(R.drawable.cancel);
            }
        }
        return convertView;
    }
    public void updateList(ArrayList<Event> Updatedevents)
    {
        events = Updatedevents;
        notifyDataSetChanged();
    }

}