package com.eliott.android.moodtracker.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eliott.android.moodtracker.Model.MoodForHistory;
import com.eliott.android.moodtracker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.eliott.android.moodtracker.Controller.MainActivity.PREF_KEY_MOODSFORHISTORY;
import static com.eliott.android.moodtracker.Controller.MainActivity.PREF_KEY_PREFERENCES;

public class HistoryActivity extends AppCompatActivity {
    private LinearLayout mLinearLayoutActivityHistory;
    private LinearLayout mInflatedView;
    private LinearLayout mLinearLayoutMood;
    private TextView mDisplaymDate;
    private ImageButton mToastCommentButton;

    private SharedPreferences preferences;
    private Gson gson;

    private int mNbOfMoodOfHistory;

    private ArrayList<MoodForHistory> mMoodsForHistory;

    private ArrayList<Integer> mColor;
    private ArrayList<String> mComment;

    final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
    private int INDEX = 1;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mLinearLayoutActivityHistory = (LinearLayout) findViewById(R.id.ll);

        preferences = getSharedPreferences(PREF_KEY_PREFERENCES, MODE_PRIVATE);

        // Vérification de l'existence de la liste d'objet
        if(preferences.contains(PREF_KEY_MOODSFORHISTORY)){
            getMood();
            int nbMood = mMoodsForHistory.size();
            if(nbMood > 1){
                // Vérifier combien de fois on exécute inflateLayout et inflateDefaultLayout
                if(nbMood < 8){
                    for(int i = nbMood; i < 8; i++) inflateDefaultLayout();
                    for(int i = 1; i < nbMood; i++) {
                        int mIndex = nbMood - i;
                        inflateLayout(mIndex);
                    }
                }
                if(nbMood >= 8) for(int i = 0; i < --nbMood; i++) inflateLayout(i);
            }
            else {
                for(int i = 0; i < 7; i++){
                    inflateDefaultLayout();
                }
            }
        }
    }

        public void getMood() {
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
        }

        public void inflateLayout(int mIndex){
            mInflatedView = (LinearLayout) View.inflate(this, R.layout.mood_history, null);
            mLinearLayoutMood = (LinearLayout) mInflatedView.findViewById(R.id.mood_history_linear_layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            mDisplaymDate = (TextView) mInflatedView.findViewById(R.id.mood_history_date_txt);

            if(mMoodsForHistory.get(mIndex).getComment().equals("")){ }
            else{
                mToastCommentButton = (ImageButton) mInflatedView.findViewById(R.id.mood_history_comment_img_btn);
                mToastCommentButton.setImageResource(R.drawable.ic_comment_black_48px);
                listenerToastComment(mIndex);
            }

            setLayout(mDisplaymDate, mLinearLayoutMood, mIndex);
            mLinearLayoutMood.setLayoutParams(params);
            mLinearLayoutActivityHistory.addView(mInflatedView);
        }

        public void inflateDefaultLayout(){
            mInflatedView = (LinearLayout) View.inflate(this, R.layout.mood_history, null);
            mLinearLayoutMood = (LinearLayout) mInflatedView.findViewById(R.id.mood_history_linear_layout);
            mLinearLayoutMood.setBackgroundColor(getResources().getColor(R.color.light_sage));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            mLinearLayoutMood.setLayoutParams(params);
            mLinearLayoutActivityHistory.addView(mInflatedView);
        }

        public void setLayout(TextView mDate, View mLinearLayoutMood, int mIndex){
            long mDateToComparate = mMoodsForHistory.get(0).getDate();
            long mNextDate = mMoodsForHistory.get(mIndex).getDate();
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
            else if (mDelta == 0) mDate.setText("Delta à 0");
            mLinearLayoutMood.setBackgroundColor(getResources().getColor(mColor.get(mIndex)));
        }

        public void listenerToastComment(int index){
            final int mIndex = index;
            mToastCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mComment.get(mIndex), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void sizeCommentLayout(){
            int colorCode = mMoodsForHistory.get(0).getColor();
            int lightsageCode = getResources().getColor(R.color.light_sage);
            if(colorCode == lightsageCode)
        }
}
