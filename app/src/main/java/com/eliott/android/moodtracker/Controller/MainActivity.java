package com.eliott.android.moodtracker.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.eliott.android.moodtracker.Model.Mood;
import com.eliott.android.moodtracker.R;

public class MainActivity extends AppCompatActivity {
    private ImageView mMoodImage;
    private ImageButton mCommentButton;
    private ImageButton mHistoryButton;

    private Mood[] mMoods;
    private Mood mMood;

    private int mIndexOfMood = 3;

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoodImage = (ImageView) findViewById(R.id.main_activity_mood_img);
        mCommentButton = (ImageButton) findViewById(R.id.main_activity_comment_btn);
        mHistoryButton = (ImageButton) findViewById(R.id.main_activity_history_button);

        mView = (View) findViewById(R.id.main_activity_layout);
        initMoods();
        mMood = mMoods[mIndexOfMood];
        mView.setBackgroundColor(mMood.getColor());

    }

    public void initMoods(){
        mMoods = new Mood[]{
                new Mood(R.color.faded_red, R.drawable.smiley_sad),
                new Mood(R.color.warm_grey, R.drawable.smiley_disappointed),
                new Mood(R.color.cornflower_blue_65, R.drawable.smiley_normal),
                new Mood(R.color.light_sage, R.drawable.smiley_happy),
                new Mood(R.color.banana_yellow, R.drawable.smiley_super_happy),
        };
    }

    /*
    public Mood getMoods(int mIndexOfMood){
        return mMoods[mIndexOfMood];
    }
    */
}
