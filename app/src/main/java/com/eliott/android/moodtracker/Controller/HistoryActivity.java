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

import static com.eliott.android.moodtracker.Controller.MainActivity.PREF_KEY_NB_OF_SAVE_MOOD;

public class HistoryActivity extends AppCompatActivity {
    private TextView mDate1;
    private TextView mDate2;
    private TextView mDate3;
    private TextView mDate4;
    private TextView mDate5;
    private TextView mDate6;
    private TextView mDate7;
    private View mLinearLayoutMood1;
    private View mLinearLayoutMood2;
    private View mLinearLayoutMood3;
    private View mLinearLayoutMood4;
    private View mLinearLayoutMood5;
    private View mLinearLayoutMood6;
    private View mLinearLayoutMood7;
    private Gson gson;

    private int mNbOfMoodOfHistory;

    private ArrayList<MoodForHistory> mMoodsForHistory;
    private int mIndexOfMoodOfHistory = 0;
    private ArrayList<Integer> mColor;
    private ArrayList<String> mComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mDate1 = (TextView) findViewById(R.id.activity_history_date1_txt);
        mDate2 = (TextView) findViewById(R.id.activity_history_date2_txt);
        mDate3 = (TextView) findViewById(R.id.activity_history_date3_txt);
        mDate4 = (TextView) findViewById(R.id.activity_history_date4_txt);
        mDate5 = (TextView) findViewById(R.id.activity_history_date5_txt);
        mDate6 = (TextView) findViewById(R.id.activity_history_date6_txt);
        mDate7 = (TextView) findViewById(R.id.activity_history_date7_txt);
        mLinearLayoutMood1 = (View) findViewById(R.id.history_activity_mood1_layout);
        mLinearLayoutMood2 = (View) findViewById(R.id.history_activity_mood2_layout);
        mLinearLayoutMood3 = (View) findViewById(R.id.history_activity_mood3_layout);
        mLinearLayoutMood4 = (View) findViewById(R.id.history_activity_mood4_layout);
        mLinearLayoutMood5 = (View) findViewById(R.id.history_activity_mood5_layout);
        mLinearLayoutMood6 = (View) findViewById(R.id.history_activity_mood6_layout);
        mLinearLayoutMood7 = (View) findViewById(R.id.history_activity_mood7_layout);

        getMood();
        }

        public void getMood() {
            SharedPreferences preferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
            gson = new Gson();
            String json = preferences.getString("PREF_KEY_MOODSFORHISTORY", null);

            mMoodsForHistory = null;
            if(json != null){
                Type type = new TypeToken<ArrayList<MoodForHistory>>() {}.getType();
                mMoodsForHistory = gson.fromJson(json, type);
            }

            mColor = new ArrayList<Integer>();
            mComment = new ArrayList<String>();
            mNbOfMoodOfHistory = preferences.getInt(PREF_KEY_NB_OF_SAVE_MOOD, 0);
            mNbOfMoodOfHistory = mNbOfMoodOfHistory - 1;
            for(MoodForHistory e : mMoodsForHistory){
                mColor.add(e.getColor());
                mComment.add(e.getComment());
            }
            switch (mNbOfMoodOfHistory)
            {
                case 0 :
                    setLayout(mDate1, mLinearLayoutMood1);
                    break;
                case 1 :
                    setLayout(mDate1, mLinearLayoutMood1);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate2, mLinearLayoutMood2);
                    break;
                case 2 :
                    setLayout(mDate1, mLinearLayoutMood1);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate2, mLinearLayoutMood2);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate3, mLinearLayoutMood3);
                    break;
                case 3 :
                    setLayout(mDate1, mLinearLayoutMood1);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate2, mLinearLayoutMood2);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate3, mLinearLayoutMood3);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate4, mLinearLayoutMood4);
                    break;
                case 4 :
                    setLayout(mDate1, mLinearLayoutMood1);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate2, mLinearLayoutMood2);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate3, mLinearLayoutMood3);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate4, mLinearLayoutMood4);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate5, mLinearLayoutMood5);
                    break;
                case 5 :
                    setLayout(mDate1, mLinearLayoutMood1);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate2, mLinearLayoutMood2);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate3, mLinearLayoutMood3);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate4, mLinearLayoutMood4);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate5, mLinearLayoutMood5);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate6, mLinearLayoutMood6);
                    break;
                case 6 :
                    setLayout(mDate1, mLinearLayoutMood1);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate2, mLinearLayoutMood2);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate3, mLinearLayoutMood3);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate4, mLinearLayoutMood4);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate5, mLinearLayoutMood5);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate6, mLinearLayoutMood6);
                    mIndexOfMoodOfHistory++;
                    setLayout(mDate7, mLinearLayoutMood7);
                    break;
            }


            //mColor = mMoodsForHistory.get(mIndexOfMoodOfHistory).getColor();
            //mComment = mMoodsForHistory.get(mIndexOfMoodOfHistory).getComment();
            //mDate1.setText(mComment);
            //mLinearLayoutMood1.setBackgroundColor(mColor);
        }

        public void setLayout(TextView mDate, View mLinearLayoutMood){
            mDate.setText(mComment.get(mIndexOfMoodOfHistory));
            mLinearLayoutMood.setBackgroundColor(mColor.get(mIndexOfMoodOfHistory));
        }
}
