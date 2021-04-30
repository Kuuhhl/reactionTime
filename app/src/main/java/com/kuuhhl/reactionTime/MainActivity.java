package com.kuuhhl.reactionTime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity {
    Long start;
    Button button;
    TextView textview;
    View root;
    Boolean inTimer = false;
    TextView highscoreTextview;
    Button resetButton;

    public long getHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getLong("highScore", -1);

    }

    public void writeHighscore(long value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("highScore", value);
        editor.apply();
    }

    public void resetHighscore(View view){
        writeHighscore(-1);
        highscoreTextview.setText("");
        resetButton.setVisibility(View.INVISIBLE);
    }

    public void showHighscore(long score) {
        highscoreTextview.setText(getString(R.string.highscoreText) + ": " + score + " ms");
    }

    public void updateHighScore(long value) {
        long highscore = getHighScore();

        // update highscore if necessary
        if (getHighScore() > value || highscore == -1) {
            writeHighscore(value);
        }

        // show highscore as text
        showHighscore(getHighScore());

    }

    public void showTime() {
        Long timeTaken = currentTimeMillis() - start;

        textview.setText(getString(R.string.timeTakenText) + ": \n" + timeTaken.toString() + " ms");
        updateHighScore(timeTaken);
        resetButton.setVisibility(View.VISIBLE);
        highscoreTextview.setVisibility(View.VISIBLE);
        button.setText(getString(R.string.startGameText));
        button.setBackgroundColor(getColor(R.color.green));
    }

    public void startTimer() {
        // Generate random number between 1000 and 5000
        Random rand = new Random();
        int random = rand.nextInt(4000) + 1000;

        // Pre-waiting text
        button.setText(getString(R.string.waitText));
        textview.setText(getString(R.string.waitText));

        // Disable / hide buttons and highscore
        button.setEnabled(false);
        button.setBackgroundColor(getColor(R.color.gray));
        resetButton.setVisibility(View.INVISIBLE);
        highscoreTextview.setVisibility(View.INVISIBLE);

        // wait for the random amount of time,
        // then continue
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // change messages
                button.setText(getString(R.string.pressText));
                textview.setText(getString(R.string.pressText));

                // change colors
                button.setBackgroundColor(getColor(R.color.purple));

                // get start time
                start = currentTimeMillis();

                // Enable button
                button.setEnabled(true);
            }
        }, random);
    }

    public void onClick(View view) {
        if (inTimer == true) {
            showTime();
            inTimer = false;
        } else {
            startTimer();
            inTimer = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get objects
        button = (Button) this.findViewById(R.id.button);
        textview = (TextView) this.findViewById(R.id.textView);
        highscoreTextview = (TextView) this.findViewById(R.id.highscoreText);
        resetButton = (Button) this.findViewById(R.id.resetButton);

        // Show highscore
        long highscore = getHighScore();
        if (highscore != -1) {
            showHighscore(highscore);
            resetButton.setVisibility(View.VISIBLE);
        }
        else {
            resetButton.setVisibility(View.INVISIBLE);
        }
    }
}