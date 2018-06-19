package com.eliott.android.moodtracker.Model;

import java.io.Serializable;

public class MoodForHistory implements Serializable{
    private String mComment; // Contain the comment
    private int mColor; // Contain the color
    private long mDate; // Contain the date

    public MoodForHistory() {
        mComment = "";
        mColor = 0;
        mDate = 0;
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

    public long getDate() {
        return mDate;
    }

    public void setDate(long mDate) {
        this.mDate = mDate;
    }
}
