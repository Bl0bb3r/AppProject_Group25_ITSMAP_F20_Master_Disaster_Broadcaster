package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class EmblemAdapter extends FirestoreRecyclerAdapter<Disaster, EmblemAdapter.EmblemViewHolder> {
    private OnEmblemListener mEmblemlistener;
    public EmblemAdapter(@NonNull FirestoreRecyclerOptions<Disaster> options,OnEmblemListener onEmblemListener)
    {
        super(options);
        this.mEmblemlistener = onEmblemListener;

    }

    @Override
    protected void onBindViewHolder(@NonNull EmblemAdapter.EmblemViewHolder holder, int position, @NonNull Disaster model) {
            if (model.getEmblemImage() != null)
            {

                holder.emblemImage.setImageResource(Integer.parseInt(model.getEmblemImage()));
            }
            else{
                holder.emblemImage.setImageResource(R.drawable.cancel);
            }

    }

    @NonNull
    @Override
    public EmblemAdapter.EmblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.emblems,parent, false);

        return new EmblemAdapter.EmblemViewHolder(view, mEmblemlistener);
    }

    public class EmblemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public OnEmblemListener onEmblemListener;
        public ImageView emblemImage;

        public EmblemViewHolder(View view, OnEmblemListener emblemListener)
        {
         super(view);
            this.onEmblemListener = emblemListener;
            emblemImage = (ImageView) view.findViewById(R.id.emblem_list_image);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() != RecyclerView.NO_POSITION && onEmblemListener != null)
            {
                onEmblemListener.onEmblemClick(getAdapterPosition(), getSnapshots().getSnapshot(getAdapterPosition()));
            }
        }
    }

    public interface OnEmblemListener {
        void onEmblemClick(int position, DocumentSnapshot documentSnapshot);
    }
}
