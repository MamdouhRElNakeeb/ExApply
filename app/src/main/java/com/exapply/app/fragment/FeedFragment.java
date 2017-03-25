package com.exapply.app.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.exapply.app.Objects.UserData;
import com.exapply.app.R;
import com.exapply.app.activity.ExperienceDetails;
import com.exapply.app.activity.Login;
import com.exapply.app.adapter.FeedRVAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/14/17.
 */

public class FeedFragment extends Fragment {

    RecyclerView feedRV;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase, mDatabase2;

    String category = "Travel";

    ArrayList<ExperienceData> experienceDataArrayList;

    ArrayList<ExperienceData> experienceDataArrayList2;

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

        setHasOptionsMenu(true);

        feedRV = (RecyclerView) view.findViewById(R.id.feedRV);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("experiences");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid()).child("savedExs");

        experienceDataArrayList = new ArrayList<ExperienceData>();
        experienceDataArrayList2 = new ArrayList<ExperienceData>();

        feedRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

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

        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                experienceDataArrayList2.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ExperienceData experienceData = postSnapshot.getValue(ExperienceData.class);
                    experienceDataArrayList2.add(experienceData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase2.addValueEventListener(valueEventListener2);

        Log.d("category", category);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                experienceDataArrayList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.child(category).getChildren()) {
                    ExperienceData experienceData = postSnapshot.getValue(ExperienceData.class);
                    if (experienceDataArrayList2.contains(experienceData)){
                        experienceData.savedID = experienceDataArrayList2.get(experienceDataArrayList2.indexOf(experienceData)).savedID;
                    }
                    experienceDataArrayList.add(0, experienceData);
                    Log.d("item Added", experienceData.title);

                }

                feedRV.setAdapter(feedRVAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(valueEventListener);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        category = item.getTitle().toString();
        syncExperiences();

        return true;

    }
}
