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

            if (event.getTitle() != null) {

                String title = event.getTitle();
                eventTitle.setText(title);

                if (title.contains("Wildfire"))
                {
                    emblemImage.setImageResource(R.drawable.fire);
                }
                else if (title.contains("Cyclone"))
                {
                    emblemImage.setImageResource(R.drawable.cloud);
                }
                else if (title.contains("volcano"))
                {
                    emblemImage.setImageResource(R.drawable.volcano);
                }
                else if (title.contains("Iceberg"))
                {
                    emblemImage.setImageResource(R.drawable.freez);
                }
                else{
                    emblemImage.setImageResource(R.drawable.flood);
                }
            } else {
                emblemImage.setImageResource(R.drawable.flood);
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