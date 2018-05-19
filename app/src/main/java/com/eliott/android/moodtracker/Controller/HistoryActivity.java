package com.eliott.android.moodtracker.Controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.eliott.android.moodtracker.Model.MoodForHistory;
import com.eliott.android.moodtracker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private TextView mDate;
    private View mLinearLayoutMood1;
    private Gson gson;

    private ArrayList<MoodForHistory> mMoodsForHistory;
    private int mIndexOfMoodOfHistory = 0;
    private int mColor;
    private String mComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mDate = (TextView) findViewById(R.id.activity_history_date_txt);
        mLinearLayoutMood1 = (View) findViewById(R.id.history_activity_mood1_layout);

        getMood();
        }

        public void getMood() {
            SharedPreferences preferences = getSharedPreferences("TEST", MODE_PRIVATE);
            gson = new Gson();
            String json = preferences.getString("PREF_KEY_MOODSFORHISTORY", null);

            mMoodsForHistory = null;
            if(json != null){
                Type type = new TypeToken<ArrayList<MoodForHistory>>() {}.getType();
                mMoodsForHistory = gson.fromJson(json, type);
            }

            mColor = mMoodsForHistory.get(mIndexOfMoodOfHistory).getColor();
            mComment = mMoodsForHistory.get(mIndexOfMoodOfHistory).getComment();

            mDate.setText(mComment);
            mLinearLayoutMood1.setBackgroundColor(mColor);
        }
}
