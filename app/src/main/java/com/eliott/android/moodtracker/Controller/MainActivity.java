package com.eliott.android.moodtracker.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.eliott.android.moodtracker.Model.Mood;
import com.eliott.android.moodtracker.Model.MoodForHistory;
import com.eliott.android.moodtracker.Model.SwipeGestureDetector;
import com.eliott.android.moodtracker.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView mMoodImage;
    private ImageButton mCommentButton;
    private ImageButton mHistoryButton;
    private Button mSave;

    private Mood[] mMoods;
    private Mood mMood;

    private int mIndexOfMood = 3;
    private int mNbOfMoodOfHistory;

    private View mViewMainActivity;

    private GestureDetectorCompat mGestureDetector;

    private Context mContext = this;

    public final static String PREF_KEY_MOODSFORHISTORY = "PREF_KEY_MOODSFORHISTORY";
    public final static String PREF_KEY_NB_OF_SAVE_MOOD = "PREF_KEY_NB_OF_SAVE_MOOD";
    public final static String PREF_KEY_FIRSTTIME_SEVEN_ITEM = "PREF_KEY_FIRSTTIME_SEVEN_ITEM";

    private ArrayList<MoodForHistory> mMoodsForHistory;
    private MoodForHistory mMoodForHistory;
    private String mComment;
    private int mFirstTimeSevenItem;

    private Gson gson;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);
        char newGestureDetector = SwipeGestureDetector.getDirection();
        if(newGestureDetector == 'T'){
            if(mIndexOfMood < 4){
                mIndexOfMood++;
                setMood();
            }
        }
        if(newGestureDetector == 'B'){
            if(mIndexOfMood > 0){
                mIndexOfMood--;
                setMood();
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoodImage = (ImageView) findViewById(R.id.main_activity_mood_img);
        mCommentButton = (ImageButton) findViewById(R.id.main_activity_comment_btn);
        mHistoryButton = (ImageButton) findViewById(R.id.main_activity_history_button);
        mSave = (Button) findViewById(R.id.main_activity_save_btn);

        mViewMainActivity = (View) findViewById(R.id.main_activity_layout);

        mGestureDetector = new GestureDetectorCompat(this, new SwipeGestureDetector());

        initMoods();
        setMood();
        listenerOnCommentButton();
        listenerOnHistoryButton();
        saveMood();
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

    public void setMood(){
        mMood = mMoods[mIndexOfMood];
        mViewMainActivity.setBackgroundColor(getResources().getColor(mMood.getColor()));
        mMoodImage.setImageResource(mMood.getImage());
    }

    public void listenerOnCommentButton() {
        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                View view = getLayoutInflater().inflate(R.layout.add_comment, null);
                final EditText addCommentTxt = view.findViewById(R.id.add_comment_edit_comment);

                builder.setView(view)
                        .setMessage(getString(R.string.comment))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mComment = addCommentTxt.getText().toString();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void listenerOnHistoryButton() {
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mHistoryActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(mHistoryActivity);
            }
        });
    }

    private void saveMood() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoodForHistory = new MoodForHistory();
                mMoodForHistory.setColor(getResources().getColor(mMood.getColor()));
                mMoodForHistory.setComment(mComment);
                SharedPreferences preferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(PREF_KEY_NB_OF_SAVE_MOOD, mNbOfMoodOfHistory).apply();
                mNbOfMoodOfHistory = preferences.getInt(PREF_KEY_NB_OF_SAVE_MOOD, 0);
                editor.putInt(PREF_KEY_FIRSTTIME_SEVEN_ITEM, mFirstTimeSevenItem).apply();
                if(mNbOfMoodOfHistory >= 0 && mNbOfMoodOfHistory <= 6){
                    if(mNbOfMoodOfHistory == 0){
                        mMoodsForHistory = new ArrayList<MoodForHistory>();
                        mMoodsForHistory.add(mNbOfMoodOfHistory, mMoodForHistory);
                    }
                    if(mNbOfMoodOfHistory >= 1 && mNbOfMoodOfHistory <= 6){
                        mMoodsForHistory.add(mNbOfMoodOfHistory, mMoodForHistory);
                    }
                    mNbOfMoodOfHistory++;
                }
                if(mNbOfMoodOfHistory == 7){
                    mFirstTimeSevenItem = preferences.getInt(PREF_KEY_FIRSTTIME_SEVEN_ITEM, 0);
                    if(mFirstTimeSevenItem == 0){
                        mMoodsForHistory.add(mNbOfMoodOfHistory, mMoodForHistory);
                    }
                    if(mFirstTimeSevenItem == 1){
                        mMoodsForHistory.set(0, mMoodsForHistory.get(1));
                        mMoodsForHistory.set(1, mMoodsForHistory.get(2));
                        mMoodsForHistory.set(2, mMoodsForHistory.get(3));
                        mMoodsForHistory.set(3, mMoodsForHistory.get(4));
                        mMoodsForHistory.set(4, mMoodsForHistory.get(5));
                        mMoodsForHistory.set(5, mMoodsForHistory.get(6));
                        mMoodsForHistory.set(6, mMoodForHistory);
                    }
                    mFirstTimeSevenItem = 1;
                    /*
                    for(MoodForHistory e : mMoodsForHistory) {
                        //mMoodForHistory = mMoodsForHistory.get(mIndexOfNewArrayList++);
                        if(mIndexOfNewArrayList >= 0 && mIndexOfNewArrayList <= 6){
                            mMoodsForHistory.set(mIndexOfNewArrayList, mMoodsForHistory.get(mIndexOfNewArrayList++));
                            mIndexOfNewArrayList++;
                        }
                    }
                    */
                }
                gson = new Gson();
                String json = gson.toJson(mMoodsForHistory);
                editor.putString(PREF_KEY_MOODSFORHISTORY, json).apply();
                editor.putInt(PREF_KEY_NB_OF_SAVE_MOOD, mNbOfMoodOfHistory).apply();
                editor.putInt(PREF_KEY_FIRSTTIME_SEVEN_ITEM, mFirstTimeSevenItem).apply();
            }
        });
    }
}
