package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.content.Context;
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

import java.util.List;

public class RankingsRecyclerAdapter extends RecyclerView.Adapter<RankingsRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> mUserList;


    public RankingsRecyclerAdapter(Context mContext, List<User> mUserList) {
        this.mContext = mContext;
        this.mUserList = mUserList;

    }

    @NonNull
    @Override
    public RankingsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.rankings_customlayout, parent,false);

        return new RankingsRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingsRecyclerAdapter.MyViewHolder holder, int position) {

        //Rank Placement
        holder.textView_placement.setText(mUserList.get(position).getRank());

        //Rank placement - ordinal end letters
        // https://www.java67.com/2018/05/java-string-chartat-example-how-to-get-first-last-character.html
        int Sample = mUserList.get(position).getRank();
        String rank = String.valueOf(Sample);
        char lastChar = rank.charAt(rank.length()-1);

        if (lastChar == 1) {
            holder.textView_placementEnd.setText(R.string.first);
        }
        else if (lastChar == 2) {
            holder.textView_placementEnd.setText(R.string.second);
        }
        else if (lastChar == 3) {
            holder.textView_placementEnd.setText(R.string.third);
        }
        else {
            holder.textView_placementEnd.setText(R.string.fourthAndRest);
        }

        //Nickname
        holder.textView_Nickname.setText(mUserList.get(position).getName());

        //Points
        holder.textView_totalPoints.setText(mUserList.get(position).getTotalPoints());

        holder.cardViewRankings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Future work - could implement viewing the user when clicking Users placement in RankingFragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView_placement;
        TextView textView_placementEnd;
        TextView textView_Nickname;
        TextView textView_totalPoints;
        CardView cardViewRankings;

        public MyViewHolder(View rankView)
        {
            super (rankView);

            textView_placement = (TextView) itemView.findViewById(R.id.textView_rankings_position_rank);
            textView_placementEnd = (TextView) itemView.findViewById(R.id.textView_rankings_position_end);
            textView_Nickname = (TextView) itemView.findViewById(R.id.textView_rankings_name);
            textView_totalPoints = (TextView) itemView.findViewById(R.id.textview_label_points);
            cardViewRankings = (CardView) itemView.findViewById(R.id.cardview_rankings);

        }

    }

}
