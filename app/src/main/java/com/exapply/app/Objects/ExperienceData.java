package com.exapply.app.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 2/28/17.
 */

public class ExperienceData implements Parcelable{

    public String userID;
    public String userName;
    public String exID;
    public String title;
    public String description;
    public String category;
    public String location;
    public String savedID;

    public ExperienceData(String userID, String userName, String exID, String title, String description, String category, String location){
        this.userID = userID;
        this.userName = userName;
        this.exID = exID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
    }

    public ExperienceData(){

    }

    protected ExperienceData(Parcel in) {
        userID = in.readString();
        userName = in.readString();
        exID = in.readString();
        title = in.readString();
        description = in.readString();
        category = in.readString();
        location = in.readString();
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
        result.put("description", description);
        result.put("category", category);
        result.put("location", location);
        result.put("savedID", savedID);

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
        parcel.writeString(exID);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(category);
        parcel.writeString(location);
    }
    // [END post_to_map]
}
