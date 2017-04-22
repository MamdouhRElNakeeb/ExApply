package com.exapply.mobapp.Objects;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 3/1/17.
 */

public class CommentData {

    public String content;
    public String price;
    public String userName;
    public String date;
    public String commentID;
    public String exID;

    public CommentData(){

    }

    public CommentData(String content, String price, String userName, String date, String commentID, String exID){
        this.content = content;
        this.price = price;
        this.userName = userName;
        this.date = date;
        this.commentID = commentID;
        this.exID = exID;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("price", price);
        result.put("userName", userName);
        result.put("date", date);
        result.put("commentID", commentID);
        result.put("exID", exID);

        return result;
    }
}
