package com.exapply.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.exapply.app.Objects.ExperienceData;
import com.exapply.app.R;
import com.exapply.app.activity.ExperienceDetails;
import com.exapply.app.adapter.FeedRVAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by mamdouhelnakeeb on 2/14/17.
 */

public class SavedFragment extends Fragment {


    RecyclerView savedRV;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase, mDatabase2;

    ArrayList<ExperienceData> experienceDataArrayList;

    FeedRVAdapter feedRVAdapter;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (feedRVAdapter != null) {
            feedRVAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.feed_fragment, container, false);

        savedRV = (RecyclerView) view.findViewById(R.id.feedRV);
        prefs = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid()).child("savedExs");

        experienceDataArrayList = new ArrayList<ExperienceData>();

        savedRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        feedRVAdapter = new FeedRVAdapter(getActivity(), experienceDataArrayList, new FeedRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ExperienceData experienceData, int position) {
                Intent intent = new Intent(getActivity(), ExperienceDetails.class);
                intent.putExtra("exItem", experienceData);
                startActivity(intent);
            }
        });

        syncExperiences();

        return view;
    }

    private void syncExperiences(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                experienceDataArrayList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ExperienceData experienceData = postSnapshot.getValue(ExperienceData.class);
                    experienceDataArrayList.add(0, experienceData);
                    //Log.d("item Added", experienceData.title);
                }
                savedRV.setAdapter(feedRVAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(valueEventListener);
    }

}