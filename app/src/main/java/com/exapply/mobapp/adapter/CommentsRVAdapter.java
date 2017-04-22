package com.exapply.mobapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exapply.mobapp.Objects.CommentData;
import com.exapply.mobapp.R;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 3/1/17.
 */

public class CommentsRVAdapter extends RecyclerView.Adapter<CommentsRVAdapter.ViewHolder> {

    String type;
    Context context;
    ArrayList<CommentData> commentDataArrayList;


    public CommentsRVAdapter(Context context, ArrayList<CommentData> commentDataArrayList, String type){
        this.context = context;
        this.commentDataArrayList = commentDataArrayList;
        this.type = type;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CommentData commentData = commentDataArrayList.get(position);

        if (type.equals("comments")) {
            holder.price.setVisibility(View.INVISIBLE);
        }
        else {
            holder.price.setText(commentData.price);
            holder.price.setVisibility(View.VISIBLE);
        }
        holder.content.setText(commentData.content);
        holder.date.setText(commentData.date);
        holder.userName.setText(commentData.userName);

        Log.d("commentData", commentData.content + " - " + commentData.price);
    }

    @Override
    public int getItemCount() {
        return commentDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView content, price, date, userName;
        public ViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.comment_content);
            price = (TextView) itemView.findViewById(R.id.comment_price);
            date = (TextView) itemView.findViewById(R.id.commentDate);
            userName = (TextView) itemView.findViewById(R.id.commentUserName);
        }
    }
}
