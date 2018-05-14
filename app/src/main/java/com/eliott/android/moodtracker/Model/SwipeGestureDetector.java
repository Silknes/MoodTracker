package com.eliott.android.moodtracker.Model;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private final static int DELTA_MIN = 50;
    private static char mDirection;

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
        float deltaX = e2.getX() - e1.getX();
        float deltaY = e2.getY() - e1.getY();

        if(Math.abs(deltaY) >= Math.abs(deltaX)){
            if(Math.abs(deltaY) > DELTA_MIN){
                if((deltaY*-1) >= DELTA_MIN){
                    mDirection = 'B';
                }
                if(deltaY >= DELTA_MIN){
                    mDirection = 'T';
                }
            }
        }
        return true;
    }

    public static char getDirection(){
        return mDirection;
    }
}
