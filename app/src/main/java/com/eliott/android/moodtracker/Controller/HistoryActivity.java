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

import com.eliott.android.moodtracker.Model.Keys;
import com.eliott.android.moodtracker.Model.MoodForHistory;
import com.eliott.android.moodtracker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/*
- commentaire à chaque variable d'instance et chaque méthode
- tout mettre en anglais
- avoir des noms de variables explicites
- quand une partie de code n'est pas clairement compréhensible, mettre un commentaire
 */

public class HistoryActivity extends AppCompatActivity {
    private LinearLayout mLinearLayoutActivityHistory; // LinearLayout that contain of the HistoryActivity
    private LinearLayout mLinearLayoutSizeFst; // First LinearLayout to set the size of the bar
    private LinearLayout mLinearLayoutSizeScd; // Second LinearLayout to set the size of the bar
    private TextView mDisplaymDate; // textView that contain the time between today and the day where the mood were saved
    private ImageButton mToastCommentButton; // ImageButton that contain the image to click to show a comment

    private SharedPreferences preferences; // Create Preferences shared between activty
    private Gson gson; // Object that allow to save ArrayList in the preferences as String

    private ArrayList<MoodForHistory> mHistoryMoodList; // ArrayList that contain all the Mood select by the user

    private ArrayList<Integer> mColor; // ArrayList that contain all the color of all the mood
    private ArrayList<String> mComment; // ArrayList that contain all the comment of all the mood

    final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24; // The time in ms of a day
    private int mIndex = 1; // Index to pass throw the first eight mood except the first

    private Context mContext = this; // Get the contect of the Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mLinearLayoutActivityHistory = findViewById(R.id.ll);  // Connect linearlayout of HistoryActivity

        preferences = getSharedPreferences(Keys.PREF_KEY_PREFERENCES, MODE_PRIVATE); // Get the preferences
        gson = new Gson(); // Create a Gson object

        /*
        If preferences contain the ArryaList of moodofhistory we recover it
        If the size of the ArrayList is over 1 element we inflate the layout for the 7 next elements
         */
        if (preferences.contains(Keys.PREF_KEY_MOODSFORHISTORY)) {
            getMoodList();

            if(mHistoryMoodList.size() > 1) {
                for(int i = 0; i < 7; i++) {
                    inflateLayout();
                    mIndex++;
                }
            }
        }
    }

    /*
    Method that recover the ArrayList and add to the color and comment ArrayList for each moodforhistory
    If the ArrayList is lower than 8 elements we set the last elements (that doesn't exist) to null
     */
    public void getMoodList() {
        String json = preferences.getString(Keys.PREF_KEY_MOODSFORHISTORY, null);

        mHistoryMoodList = null;
        if (json != null) {
            Type type = new TypeToken<ArrayList<MoodForHistory>>() {
            }.getType();
            mHistoryMoodList = gson.fromJson(json, type);
        }

        mColor = new ArrayList<>();
        mComment = new ArrayList<>();
        for (MoodForHistory e : mHistoryMoodList) {
            mColor.add(e.getColor());
            mComment.add(e.getComment());
        }

        if(mHistoryMoodList.size() < 8) for(int i = mHistoryMoodList.size(); i < 8; i++) mHistoryMoodList.add(null);
    }

    /*
    Conenct all the elements in MoodHistory.xml
    Set the params of the main LinearLayout mostly the weight
    Set the main Linear to visible
    Implement 3 other methods :
    1st : set the width of the bar
    2nd : set the text of the bar
    3rd : set the toastmessage on the imagebutton
    Finally inflate the view to the display view
     */
    public void inflateLayout() {
        LinearLayout mInflatedView = (LinearLayout) View.inflate(this, R.layout.mood_history, null);
        LinearLayout mLinearLayoutMood = mInflatedView.findViewById(R.id.mood_history_linear_layout);
        mLinearLayoutSizeFst = mInflatedView.findViewById(R.id.mood_history_linear_size_first);
        mLinearLayoutSizeScd = mInflatedView.findViewById(R.id.mood_history_frame_size_second);
        mDisplaymDate = mInflatedView.findViewById(R.id.mood_history_date_txt);
        mToastCommentButton = mInflatedView.findViewById(R.id.mood_history_comment_img_btn);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        mLinearLayoutMood.setLayoutParams(params);

        if(mHistoryMoodList.get(mIndex) == null) mLinearLayoutMood.setVisibility(View.INVISIBLE);
        else {
            mLinearLayoutMood.setVisibility(View.VISIBLE);
            setWidthHistoryBar();
            setTextHistoryBar();
            setImageButton();
        }

        mLinearLayoutActivityHistory.addView(mInflatedView);
    }

    /*
    According to the color set params of the two layout (mostly the weight)
     */
    public void setWidthHistoryBar() {
        int mColorTest = getResources().getColor(mColor.get(mIndex));
        if (mColorTest == getResources().getColor(R.color.banana_yellow)) paramsLinearLayout(0f, 1f);
        if (mColorTest == getResources().getColor(R.color.light_sage)) paramsLinearLayout(1f, 4f);
        if (mColorTest == getResources().getColor(R.color.cornflower_blue_65)) paramsLinearLayout(1f, 2f);
        if (mColorTest == getResources().getColor(R.color.warm_grey)) paramsLinearLayout(1f, 1f);
        if (mColorTest == getResources().getColor(R.color.faded_red)) paramsLinearLayout(1f, 0.5f);
    }

    /*
    Method that set the params of the two layout, take the weight in params
     */
    public void paramsLinearLayout(float mWeight1, float mWeight2){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, mWeight1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, mWeight2);
        mLinearLayoutSizeFst.setLayoutParams(params);
        mLinearLayoutSizeScd.setLayoutParams(params2);
    }

    /*
    Set the mesaage to display to the user, that show him how much time between current mood and the last seven mood
    Then comapre the dates and display a different message if it's yesterday, less than one week, less than one month and less than one year
    Moreover set the background color of the first linearlayout which is use to set the width of the bar
     */
    public void setTextHistoryBar() {
        long mDateToComparate = mHistoryMoodList.get(0).getDate();
        long mNextDate = mHistoryMoodList.get(mIndex).getDate();
        long mDelta = (mDateToComparate - mNextDate) / MILLISECONDS_PER_DAY;

        if (mDelta == 1) mDisplaymDate.setText(getString(R.string.yesterday));
        else if (mDelta >= 2 && mDelta < 7)
            mDisplaymDate.setText(String.format(getString(R.string.x_days), mDelta));
        else if (mDelta >= 7 && mDelta < 30) {
            long mDeltaWeek = mDelta / 7;
            mDisplaymDate.setText(String.format(getString(R.string.x_weeks), mDeltaWeek));
        } else if (mDelta >= 30 && mDelta < 365) {
            long mDeltaMonth = mDelta / 30;
            mDisplaymDate.setText(String.format(getString(R.string.x_months), mDeltaMonth));
        } else if (mDelta >= 365) {
            long mDeltaYear = mDelta / 365;
            mDisplaymDate.setText(String.format(getString(R.string.x_years), mDeltaYear));
        }
        mLinearLayoutSizeFst.setBackgroundColor(getResources().getColor(mColor.get(mIndex)));
    }

    /*
    If the comment of the mood is != "", we set an onClickListener on the ImageButton
    On the click display a toast message that contain the comment
     */
    public void setImageButton(){
        final int mIndexComment = mIndex;
        if (!mHistoryMoodList.get(mIndex).getComment().equals("")) {
            mToastCommentButton.setImageResource(R.drawable.ic_comment_black_48px);
            mToastCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mComment.get(mIndexComment), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
