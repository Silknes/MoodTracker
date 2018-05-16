package com.eliott.android.moodtracker.Model;

public class MoodForHistory {
    private String mComment;
    private int mColor;
    private int mDate;

    public MoodForHistory(String mComment, int mColor, int mDate) {
        this.mComment = mComment;
        this.mColor = mColor;
        this.mDate = mDate;
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

    public int getDate() {
        return mDate;
    }

    public void setDate(int mDate) {
        this.mDate = mDate;
    }
}
