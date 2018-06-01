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
import java.text.DateFormat;
import java.util.ArrayList;

import static com.eliott.android.moodtracker.Controller.MainActivity.PREF_KEY_MOODSFORHISTORY;
import static com.eliott.android.moodtracker.Controller.MainActivity.PREF_KEY_PREFERENCES;

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
    private MoodForHistory mMoodForHistory;

    private int mIndexOfMoodOfHistory = 0;
    private ArrayList<Integer> mColor;
    private ArrayList<String> mComment;
    private DateFormat df;

    private int[] mYear;
    private int[] mMonth;
    private int[] mDay;

    final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
    private int INDEX = 1;

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
            SharedPreferences preferences = getSharedPreferences(PREF_KEY_PREFERENCES, MODE_PRIVATE);
            gson = new Gson();
            String json = preferences.getString(PREF_KEY_MOODSFORHISTORY, null);

            mMoodsForHistory = null;
            if(json != null){
                Type type = new TypeToken<ArrayList<MoodForHistory>>() {}.getType();
                mMoodsForHistory = gson.fromJson(json, type);
            }

            mColor = new ArrayList<Integer>();
            mComment = new ArrayList<String>();
            mNbOfMoodOfHistory = mMoodsForHistory.size();
            mNbOfMoodOfHistory = mNbOfMoodOfHistory - 1;
            for(MoodForHistory e : mMoodsForHistory){
                mColor.add(e.getColor());
                mComment.add(e.getComment());
            }

            if(mMoodsForHistory.size() >= 2){
                if (mNbOfMoodOfHistory >= 1)  setLayout(mDate7, mLinearLayoutMood7);
                if (mNbOfMoodOfHistory >= 2)  setLayout(mDate6, mLinearLayoutMood6);
                if (mNbOfMoodOfHistory >= 3)  setLayout(mDate5, mLinearLayoutMood5);
                if (mNbOfMoodOfHistory >= 4)  setLayout(mDate4, mLinearLayoutMood4);
                if (mNbOfMoodOfHistory >= 5)  setLayout(mDate3, mLinearLayoutMood3);
                if (mNbOfMoodOfHistory >= 6)  setLayout(mDate2, mLinearLayoutMood2);
                if (mNbOfMoodOfHistory >= 7)  setLayout(mDate1, mLinearLayoutMood1);
            }

            //mColor = mMoodsForHistory.get(mIndexOfMoodOfHistory).getColor();
            //mComment = mMoodsForHistory.get(mIndexOfMoodOfHistory).getComment();
            //mDate1.setText(mComment);
            //mLinearLayoutMood1.setBackgroundColor(mColor);
        }

        public void setLayout(TextView mDate, View mLinearLayoutMood){
            long mDateToComparate = mMoodsForHistory.get(0).getDate();
            if(INDEX <= 6) {
                long mNextDate = mMoodsForHistory.get(INDEX).getDate();
                long mDelta = (mDateToComparate - mNextDate)/MILLISECONDS_PER_DAY;
                if(mDelta == 1) mDate.setText(getString(R.string.yesterday));
                else if(mDelta >= 2 && mDelta < 7) mDate.setText(String.format(getString(R.string.x_days), mDelta));
                else if(mDelta >= 7 && mDelta < 30){
                    long mDeltaWeek = mDelta / 7;
                    mDate.setText(String.format(getString(R.string.x_weeks), mDeltaWeek));
                }
                else if(mDelta >= 30 && mDelta < 365){
                    long mDeltaMonth = mDelta / 30;
                    mDate.setText(String.format(getString(R.string.x_months), mDeltaMonth));
                }
                else if(mDelta >= 365){
                    long mDeltaYear = mDelta / 365;
                    mDate.setText(String.format(getString(R.string.x_years), mDeltaYear));
                }
                else if (mDelta < 0) mDate.setText("Erreur");
                mLinearLayoutMood.setBackgroundColor(getResources().getColor(mColor.get(INDEX)));
                INDEX++;
            }
        }

        /*
        LinearLayout mLinearLayout = findViewById(R.id.ll);
        View view = getinf...
        TextView txt = view.findViewById(...);
        mLinearLayouyt.add(view);
        */

        /*
        1 - dans activity_history laisser que le linerLayoiut superieur en veillant a ce que l'orientation soit verticale
        2 - créer un autre fichier layout puis faire un layout inflater
        3 - avec la view créée (gtace au layout inflater) charger le textview et le imageview
        4 - référencer (bind) le linearlayout supérieur et lui ajouter les views
         */
}
