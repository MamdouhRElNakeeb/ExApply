package com.exapply.mobapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exapply.mobapp.Objects.CommentData;
import com.exapply.mobapp.Objects.ExperienceData;
import com.exapply.mobapp.R;
import com.exapply.mobapp.adapter.CommentsRVAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 3/1/17.
 */

public class CommentsFragments extends Fragment {

    String type = "comments";
    ExperienceData experienceData;
    CommentsRVAdapter commentsRVAdapter;
    RecyclerView commentsRV;


    private DatabaseReference mDatabase;

    ArrayList <CommentData> commentDataArrayList;

    public CommentsFragments(){

    }

    public CommentsFragments(ExperienceData experienceData, String type){
        this.experienceData = experienceData;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comments_fragment, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("experiences")
                .child(experienceData.category)
                .child(experienceData.exID)
                .child(type);

        commentsRV = (RecyclerView) view.findViewById(R.id.commentsRV);

        commentDataArrayList = new ArrayList<CommentData>();

        commentsRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        commentsRVAdapter = new CommentsRVAdapter(getActivity(), commentDataArrayList, type);
        syncComments();

        return view;
    }

    private void syncComments(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                commentDataArrayList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    CommentData commentData = postSnapshot.getValue(CommentData.class);
                    commentDataArrayList.add(0, commentData);
                    Log.d("item Added", commentData.content);

                }

                commentsRV.setAdapter(commentsRVAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(valueEventListener);
    }

}
