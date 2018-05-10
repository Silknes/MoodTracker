package com.eliott.android.moodtracker.Model;

public class Mood {
    private int mColor;
    private int mImage;

    public Mood(int mColor, int mImage){
        this.mColor = mColor;
        this.mImage = mImage;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int mImage) {
        this.mImage = mImage;
    }
}
