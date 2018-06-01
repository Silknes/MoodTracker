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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ImageView mMoodImage;
    private ImageButton mCommentButton;
    private ImageButton mHistoryButton;
    private Button mSave;

    private Mood[] mMoods;
    private Mood mMood;

    private int mIndexOfMood = 3;

    private View mViewMainActivity;

    private GestureDetectorCompat mGestureDetector;

    private Context mContext = this;

    public final static String PREF_KEY_MOODSFORHISTORY = "PREF_KEY_MOODSFORHISTORY";
    public final static String PREF_KEY_DATE = "PREF_KEY_DATE";
    public final static String PREF_KEY_PREFERENCES = "PREF_KEY_PREFERENCES";

    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    private ArrayList<MoodForHistory> mHistoryMoodsList;
    private MoodForHistory mMoodForHistory;
    private String mComment;

    private long firstUseMillisec;

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

        // Création des préférences
        preferences = getSharedPreferences(PREF_KEY_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        // Création de l'objet Gson
        gson = new Gson();

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

    public void listenerOnHistoryButton() {
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mHistoryActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(mHistoryActivity);
            }
        });
    }

    public void saveMood() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création/Récupération de la liste d'objet
                if(preferences.contains(PREF_KEY_MOODSFORHISTORY)){
                    String jsonList = preferences.getString(PREF_KEY_MOODSFORHISTORY, null);
                    Type type = new TypeToken<ArrayList<MoodForHistory>>() {}.getType();
                    mHistoryMoodsList = gson.fromJson(jsonList, type);
                }

                if (mHistoryMoodsList == null) mHistoryMoodsList = new ArrayList<MoodForHistory>();

                // Gestion de la date
                if(preferences.contains(PREF_KEY_DATE)) firstUseMillisec = preferences.getLong(PREF_KEY_DATE, System.currentTimeMillis());
                else firstUseMillisec = System.currentTimeMillis();

                long secondUseMillisec = System.currentTimeMillis();

                Calendar firstUseCal = Calendar.getInstance();
                Calendar secondUseCal = Calendar.getInstance();
                firstUseCal.setTimeInMillis(firstUseMillisec);
                secondUseCal.setTimeInMillis(secondUseMillisec);

                // Création de l'objet et attribution de sa couleur, son commentaire et sa date
                mMoodForHistory = new MoodForHistory();
                mMoodForHistory.setColor(mMood.getColor());
                mMoodForHistory.setComment(mComment);
                mMoodForHistory.setDate(secondUseMillisec);

                boolean isUniqueDay = firstUseCal.get(Calendar.DAY_OF_YEAR) == secondUseCal.get(Calendar.DAY_OF_YEAR);
                if(isUniqueDay == true) {
                    if(mHistoryMoodsList.size() == 0) mHistoryMoodsList.add(0, mMoodForHistory);
                    else mHistoryMoodsList.set(0, mMoodForHistory);
                }
                if(isUniqueDay == false) {
                    mHistoryMoodsList.add(0, mMoodForHistory);
                    firstUseMillisec = System.currentTimeMillis();
                }

                // Ajout de la liste d'objet et du tableau de date dans les préférences
                editor.putLong(PREF_KEY_DATE, firstUseMillisec);
                String jsonList = gson.toJson(mHistoryMoodsList);
                editor.putString(PREF_KEY_MOODSFORHISTORY, jsonList);
                editor.apply();
            }
        });
    }
}
