package com.example.newbiechen.ireader.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by newbiechen on 17-5-3.
 */

public class BookSubSortBean implements Parcelable{
    /**
     * major : 玄幻
     * mins : ["东方玄幻","异界大陆","异界争霸","远古神话"]
     */

    private String major;
    private List<String> mins;


    protected BookSubSortBean(Parcel in) {
        major = in.readString();
        mins = in.createStringArrayList();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public List<String> getMins() {
        return mins;
    }

    public void setMins(List<String> mins) {
        this.mins = mins;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(major);
        dest.writeStringList(mins);
    }

    public static final Creator<BookSubSortBean> CREATOR = new Creator<BookSubSortBean>() {
        @Override
        public BookSubSortBean createFromParcel(Parcel in) {
            return new BookSubSortBean(in);
        }

        @Override
        public BookSubSortBean[] newArray(int size) {
            return new BookSubSortBean[size];
        }
    };
}