package com.eliott.android.moodtracker.Model;

import java.io.Serializable;

public class MoodForHistory implements Serializable{
    private String mComment;
    private int mColor;
    //private int mDate;

    public MoodForHistory() {
        mComment = "No Comment";
        mColor = 0;
        //mDate = 0;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }
/*
    public int getDate() {
        return mDate;
    }

    public void setDate(int mDate) {
        this.mDate = mDate;
    }*/

    @Override
    public String toString() {
        return "MoodForHistory{" +
                "mComment='" + mComment + '\'' +
                ", mColor=" + mColor +
                '}';
    }
}
