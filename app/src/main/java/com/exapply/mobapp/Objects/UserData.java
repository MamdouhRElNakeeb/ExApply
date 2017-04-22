package com.exapply.mobapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mamdouhelnakeeb on 2/25/17.
 */

public class UserData implements Parcelable{

    public String name;
    public String email;
    public String mobile;
    public String  type;
    public String brief;
    public String specialization;
    public String workdays;
    public String rate;
    public String interests;

    public UserData(String name, String email, String mobile, String type){
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.type = type;
    }

    public UserData(String email, String mobile, String interests){
        this.email = email;
        this.mobile = mobile;
        this.interests = interests;
    }

    public UserData(){

    }

    protected UserData(Parcel in) {
        name = in.readString();
        email = in.readString();
        mobile = in.readString();
        type = in.readString();
        brief = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(mobile);
        parcel.writeString(type);
        parcel.writeString(brief);
    }
}