package com.exapply.app.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 2/28/17.
 */

public class ExperienceData implements Parcelable{

    public String userID;
    public String userName;
    public String userImg;
    public String exID;
    public String title;
    public String eventImg;
    public String eventDate;
    public String eventTime;
    public String description;
    public String category;
    public String location;
    public String savedID;
    public String keywords;
    public String privacy;
    public String maxNo;
    public String minNo;

    public ExperienceData(String userID, String userName, String userImg, String exID, String title,
                          String eventImg, String description, String category, String location, String eventDate,
                          String eventTime, String keywords, String privacy, String maxNo, String minNo){
        this.userID = userID;
        this.userName = userName;
        this.userImg = userImg;
        this.exID = exID;
        this.title = title;
        this.eventImg = eventImg;
        this.description = description;
        this.category = category;
        this.location = location;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.keywords = keywords;
        this.privacy = privacy;
        this.maxNo = maxNo;
        this.minNo = minNo;
    }

    public ExperienceData(String userID, String userName, String exID, String title,
                          String description, String category, String location, String eventDate,
                          String eventTime, String keywords, String privacy, String maxNo, String minNo){
        this.userID = userID;
        this.userName = userName;
        this.exID = exID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.keywords = keywords;
        this.privacy = privacy;
        this.maxNo = maxNo;
        this.minNo = minNo;
    }


    public ExperienceData(){

    }

    protected ExperienceData(Parcel in) {
        userID = in.readString();
        userName = in.readString();
        userImg = in.readString();
        exID = in.readString();
        title = in.readString();
        eventImg = in.readString();
        eventDate = in.readString();
        eventTime = in.readString();
        description = in.readString();
        category = in.readString();
        location = in.readString();
        savedID = in.readString();
    }

    public static final Creator<ExperienceData> CREATOR = new Creator<ExperienceData>() {
        @Override
        public ExperienceData createFromParcel(Parcel in) {
            return new ExperienceData(in);
        }

        @Override
        public ExperienceData[] newArray(int size) {
            return new ExperienceData[size];
        }
    };

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("userName", userName);
        result.put("exID", exID);
        result.put("title", title);
        result.put("time", eventTime);
        result.put("description", description);
        result.put("category", category);
        result.put("location", location);
        result.put("date", eventDate);
        result.put("savedID", savedID);
        result.put("privacy", privacy);
        result.put("minNo", minNo);
        result.put("maxNo", maxNo);
        result.put("keywords", keywords);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userID);
        parcel.writeString(userName);
        parcel.writeString(userImg);
        parcel.writeString(exID);
        parcel.writeString(title);
        parcel.writeString(eventImg);
        parcel.writeString(eventDate);
        parcel.writeString(eventTime);
        parcel.writeString(description);
        parcel.writeString(category);
        parcel.writeString(location);
        parcel.writeString(savedID);
    }

    // [END post_to_map]
}