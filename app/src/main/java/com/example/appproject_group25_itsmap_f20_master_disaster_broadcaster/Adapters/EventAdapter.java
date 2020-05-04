package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Event;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    public EventAdapter() {
        super();
    }

    private ArrayList<Event> events;
    private OnEventListener mEventListener;
    public EventAdapter(ArrayList<Event> _events, OnEventListener onEventListener) {
        this.events = _events;
        this.mEventListener = onEventListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView emblemImage;
        TextView eventTitle;
        OnEventListener onEventListener;

        public EventViewHolder(View view, OnEventListener onEventListener) {
            super(view);
            emblemImage = view.findViewById(R.id.ongoing_disaster_image);
            eventTitle = view.findViewById(R.id.ongoing_title_disaster_text);
            this.onEventListener = onEventListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onEventListener.onEventClick(getAdapterPosition());

        }
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.ongoing_list,parent, false);

        EventAdapter.EventViewHolder viewHolder = new EventAdapter.EventViewHolder(view, mEventListener);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        ImageView emblemImage = holder.emblemImage;
        TextView eventTitle = holder.eventTitle;
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
    public interface OnEventListener {
        void onEventClick(int position);

    }
}