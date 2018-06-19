package com.eliott.android.moodtracker.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.eliott.android.moodtracker.Model.Keys;
import com.eliott.android.moodtracker.Model.Mood;
import com.eliott.android.moodtracker.Model.MoodForHistory;
import com.eliott.android.moodtracker.Model.SwipeGestureDetector;
import com.eliott.android.moodtracker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ImageView mMoodImage;  // ImageView that contains the MoodImage
    private ImageButton mCommentButton;  // ImageButton to leave a comment
    private ImageButton mSendSmsButton; // ImageButton to send sms
    private ImageButton mHistoryButton;  // ImageButton to start a HistoryActivity that contains the seven previous save Mood
    private View mViewMainActivity;  // View of MainActivity use to set the backgroundcolor

    private ArrayList<MoodForHistory> mHistoryMoodsList; // ArrayList that contain all the Mood select by the user
    private Mood[] mMoodTab;  // Contain the 5 differents Moods
    private Mood mMood;  // Object Mood define by a color and an Image

    private int mIndexOfMood = 3;  // Variable that allows to display happy mood by default and to set an other Mood with incrementation

    private GestureDetectorCompat mGestureDetector;  // Use to manage the swipe and to change the display mood

    private Context mContext = this;  // Get the context of the activity

    public SharedPreferences preferences;  // Create Preferences shared between activty
    public SharedPreferences.Editor editor; // Editor use to modify the preferences
    private Gson gson; // Object that allow to save ArrayList in the preferences as String

    private String mComment; // Variable that store the comment leave by the user
    private long firstUseMillisec; // Variable that contain the date in ms save in the preferences

    // Method that set the view (Image & Color) according to the direction of the gesture direction (Top or Bottom)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);
        char newGestureDetector = SwipeGestureDetector.getDirection(); // Get a caracter (T or B) according to the gesture direction
        if(newGestureDetector == 'T'){ // Display a better mood with the incrementation of the variable mIndexOfMood
            if(mIndexOfMood < 4){
                mIndexOfMood++;
                setMood();
            }
        }
        if(newGestureDetector == 'B'){
            if(mIndexOfMood > 0){ // Display a lower mood with the decrementation of the variable mIndexOfMood
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

        // Connect elements of the layout (Image, ImageButton x3 and a View use to set the backgroundcolor)
        mMoodImage = findViewById(R.id.main_activity_mood_img);
        mCommentButton = findViewById(R.id.main_activity_comment_btn);
        mSendSmsButton = findViewById(R.id.main_activity_sendsms_button);
        mHistoryButton = findViewById(R.id.main_activity_history_button);

        mViewMainActivity = findViewById(R.id.main_activity_layout);

        // Instanciation of the preferences and the editor
        preferences = getSharedPreferences(Keys.PREF_KEY_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        // Instanciation of the Gson object
        gson = new Gson();

        // Instanciation of the GestureDector object
        mGestureDetector = new GestureDetectorCompat(this, new SwipeGestureDetector());

        // Call of the mathod include in this Activity
        initMoods(); // Initialize a Mood tab that contain all the Mood with the Color and the Image source
        setMood(); // Set the display view (Color and Image)
        listenerOnCommentButton(); // Add a listener to the CommentButton
        listenerOnHistoryButton(); // Add a listener to the HistoryButton
        listenerOnSendSmsButton(); // Add a listener to the SendSmsButton
    }

    public void initMoods(){ // Initialize a Mood tab that contain all the Mood with the Color and the Image source
        mMoodTab = new Mood[]{
                new Mood(R.color.faded_red, R.drawable.smiley_sad),
                new Mood(R.color.warm_grey, R.drawable.smiley_disappointed),
                new Mood(R.color.cornflower_blue_65, R.drawable.smiley_normal),
                new Mood(R.color.light_sage, R.drawable.smiley_happy),
                new Mood(R.color.banana_yellow, R.drawable.smiley_super_happy),
        };
    }

    public void setMood(){ // Set the display view (Color and Image)
        mMood = mMoodTab[mIndexOfMood];
        mViewMainActivity.setBackgroundColor(getResources().getColor(mMood.getColor()));
        mMoodImage.setImageResource(mMood.getImage());
    }

    /*
    Add a listener to the CommentButton
    When the user click on the button that open an AlertDialog
    Inflate a view to add an EditText to allow the user to leave a comment
    Retrieves the comment in the variable mComment
    */
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

    /*
    Add a listener on the sendSmsButton
    Ask a phone number in an alertdialog
    Start a new Activity with intent and ask to the user which sms app he wants to use to send a sms
    According to the Mood in the view we send a different sms
    */
    public void listenerOnSendSmsButton(){
        mSendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mSendSmsActivity = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "0761256954", null));
                if (mIndexOfMood == 4) mSendSmsActivity.putExtra("sms_body", getString(R.string.super_happy));
                else if (mIndexOfMood == 3) mSendSmsActivity.putExtra("sms_body", getString(R.string.happy));
                else if (mIndexOfMood == 2) mSendSmsActivity.putExtra("sms_body", getString(R.string.normal));
                else if (mIndexOfMood == 1) mSendSmsActivity.putExtra("sms_body", getString(R.string.disappointed));
                else if (mIndexOfMood == 0) mSendSmsActivity.putExtra("sms_body", getString(R.string.sad));
                startActivity(mSendSmsActivity);
            }
        });
    }

    // Add a listener to the HistoryButton and start the History Activity
    public void listenerOnHistoryButton() {
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mHistoryActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(mHistoryActivity);
            }
        });
    }

    /*
    Method that oompare 2 date, the current date and th enext date
    If tha date is different, this is a new day
    */
    public boolean isUniqueDay(){
        if(preferences.contains(Keys.PREF_KEY_DATE)) firstUseMillisec = preferences.getLong(Keys.PREF_KEY_DATE, System.currentTimeMillis());
        else firstUseMillisec = System.currentTimeMillis();

        long secondUseMillisec = System.currentTimeMillis();

        Calendar firstUseCal = Calendar.getInstance();
        Calendar secondUseCal = Calendar.getInstance();
        firstUseCal.setTimeInMillis(firstUseMillisec);
        secondUseCal.setTimeInMillis(secondUseMillisec);

        return firstUseCal.get(Calendar.DAY_OF_YEAR) == secondUseCal.get(Calendar.DAY_OF_YEAR);
    }

    /*
    Method use to save all my data in the preferences
    Create the ArrayList if isn't in the preferences
    Create a MoodForHistory object that will be save in the preferences and use in the History Activity and set the Color, the Comment and the Date
    If it's a new day we add a entry to the ArrayList
    Else we replace the first entry
    */
    public void saveMood() {
        if(preferences.contains(Keys.PREF_KEY_MOODSFORHISTORY)){
            String jsonList = preferences.getString(Keys.PREF_KEY_MOODSFORHISTORY, null);
            Type type = new TypeToken<ArrayList<MoodForHistory>>() {}.getType();
            mHistoryMoodsList = gson.fromJson(jsonList, type);
        }

        if(mHistoryMoodsList == null) mHistoryMoodsList = new ArrayList<>();

        MoodForHistory mMoodForHistory = new MoodForHistory();
        mMoodForHistory.setComment(mComment);
        mMoodForHistory.setColor(mMood.getColor());
        mMoodForHistory.setDate(System.currentTimeMillis());

        if(isUniqueDay()) {
            if(mHistoryMoodsList.size() == 0) mHistoryMoodsList.add(0, mMoodForHistory);
            else mHistoryMoodsList.set(0, mMoodForHistory);
        }
        else {
            mHistoryMoodsList.add(0, mMoodForHistory);
            firstUseMillisec = System.currentTimeMillis();
        }

        editor.putLong(Keys.PREF_KEY_DATE, firstUseMillisec);
        String jsonList = gson.toJson(mHistoryMoodsList);
        editor.putString(Keys.PREF_KEY_MOODSFORHISTORY, jsonList);
        editor.apply();
    }

    // Call the method saveMood to save the data when the Activity goes in Pause
    @Override
    protected void onPause() {
        super.onPause();
        saveMood();
    }

    // If it's an other day we reset the variable mComment
    @Override
    protected void onResume() {
        super.onResume();
        if(!isUniqueDay()) mComment = "";
    }
}
