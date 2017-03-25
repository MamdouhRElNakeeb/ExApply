package com.exapply.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exapply.app.Objects.ExperienceData;
import com.exapply.app.R;
import com.exapply.app.activity.ExperienceDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 2/28/17.
 */

public class FeedRVAdapter extends RecyclerView.Adapter<FeedRVAdapter.ViewHolder> {

    private Context context;
    ArrayList<ExperienceData> experienceDataArrayList;

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;

    public interface OnItemClickListener {
        void onItemClick(ExperienceData experienceData, int position);
    }


    private final OnItemClickListener onItemClickListener;


    public FeedRVAdapter(Context context, ArrayList<ExperienceData> experienceDataArrayList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.experienceDataArrayList = experienceDataArrayList;
        this.onItemClickListener = onItemClickListener;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseAuth.getCurrentUser().getUid());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ExperienceData experienceData = experienceDataArrayList.get(position);

        holder.exTitle.setText(experienceData.title);
        holder.exDescription.setText(experienceData.description);
        holder.exCategory.setText(experienceData.category);
        holder.exLocation.setText(experienceData.location);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(experienceData, holder.getAdapterPosition());
            }
        });

        if (experienceData.savedID != null) {
            holder.favExBtn.setImageResource(R.drawable.favorite_btn);
            holder.favExBtn.setTag(R.drawable.favorite_btn);
        }
        else {
            holder.favExBtn.setImageResource(R.drawable.unfavorite_btn);
            holder.favExBtn.setTag(R.drawable.unfavorite_btn);
        }

        holder.favExBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Integer) holder.favExBtn.getTag() == R.drawable.unfavorite_btn){
                    holder.favExBtn.setImageResource(R.drawable.favorite_btn);
                    holder.favExBtn.setTag(R.drawable.favorite_btn);
                    favExperience(experienceData);

                }
                else {
                    holder.favExBtn.setImageResource(R.drawable.unfavorite_btn);
                    holder.favExBtn.setTag(R.drawable.unfavorite_btn);
                    unFavExperience(experienceData);
                }
                //notifyDataSetChanged();
            }
        });
    }


    private void favExperience(ExperienceData experienceData) {

        String key = mDatabase.child("/savedExs/").push().getKey();

        experienceData.savedID = key;
        Map<String, Object> postValues = experienceData.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/savedExs/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }

    private void unFavExperience(ExperienceData experienceData) {

        mDatabase.child("/savedExs/").child(experienceData.savedID).removeValue();

    }

    @Override
    public int getItemCount() {
        return experienceDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView exTitle, exDescription, exCategory, exLocation;
        ImageView favExBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            exTitle = (TextView) itemView.findViewById(R.id.exTitle);
            exDescription = (TextView) itemView.findViewById(R.id.exDescription);
            exCategory = (TextView) itemView.findViewById(R.id.exCategory);
            exLocation = (TextView) itemView.findViewById(R.id.exLocation);
            favExBtn = (ImageView) itemView.findViewById(R.id.favExBtn);

        }
    }
}
