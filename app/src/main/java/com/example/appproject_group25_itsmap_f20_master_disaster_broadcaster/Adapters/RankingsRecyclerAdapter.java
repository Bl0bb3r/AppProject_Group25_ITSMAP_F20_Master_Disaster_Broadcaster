package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Activities.MainActivity;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Service.DisasterService;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class RankingsRecyclerAdapter extends FirestoreRecyclerAdapter<User, RankingsRecyclerAdapter.MyViewHolder> {
    OnRankListener onRankListener;


    public RankingsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, OnRankListener onRankListener) {
        super(options);
        this.onRankListener = onRankListener;

    }

    @NonNull
    @Override
    public RankingsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rankings_customlayout, parent,false);

        return new RankingsRecyclerAdapter.MyViewHolder(view, onRankListener);
    }


    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull User model) {
        Log.wtf("RankingsFragment","Current model -- name: "+model.getName()+" rank: "+model.getRank()+" totalPoints: "+model.getTotalPoints());

        //Rank placement - ordinal end letters
        // https://www.java67.com/2018/05/java-string-chartat-example-how-to-get-first-last-character.html

        if(model != null) {
            int Sample = model.getRank();
            String rank = String.valueOf(Sample);
            char lastChar = rank.charAt(rank.length() - 1);

            if (lastChar == 1) {
                holder.textView_placementEnd.setText(R.string.first);
            } else if (lastChar == 2) {
                holder.textView_placementEnd.setText(R.string.second);
            } else if (lastChar == 3) {
                holder.textView_placementEnd.setText(R.string.third);
            } else {
                holder.textView_placementEnd.setText(R.string.fourthAndRest);
            }

            holder.textView_placement.setText(""+model.getRank());
            //Nickname
            if (model.getName() != null) {
                holder.textView_Nickname.setText(model.getName());
            }

            //Points
            holder.textView_totalPoints.setText("" + model.getTotalPoints());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        OnRankListener onRankListener;
        TextView textView_placement;
        TextView textView_placementEnd;
        TextView textView_Nickname;
        TextView textView_totalPoints;

        public MyViewHolder(View rankView, OnRankListener onRankListener)
        {
            super (rankView);
            this.onRankListener = onRankListener;
            textView_placement = (TextView) rankView.findViewById(R.id.textView_rankings_position_rank);
            textView_placementEnd = (TextView) rankView.findViewById(R.id.textView_rankings_position_end);
            textView_Nickname = (TextView) rankView.findViewById(R.id.textView_rankings_name);
            textView_totalPoints = (TextView) rankView.findViewById(R.id.textView_rankings_points);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() != RecyclerView.NO_POSITION && onRankListener != null)
            {
                onRankListener.onRankClick(getAdapterPosition(), getSnapshots().getSnapshot(getAdapterPosition()));
            }
        }
    }
    public interface OnRankListener {
        void onRankClick(int position, DocumentSnapshot documentSnapshot);

    }

}
