package com.example.virtual;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MoodActivity extends AppCompatActivity {

    private Map<String, Integer> moodLevels = new HashMap<>();
    private TextView moodEmoji, moodMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        moodEmoji = findViewById(R.id.currentMoodEmoji);
        moodMessage = findViewById(R.id.moodMessage);

        // Set default moods
        moodLevels.put("Happy", 65);
        moodLevels.put("Tired", 9);
        moodLevels.put("Sad", 28);
        moodLevels.put("Joyful", 23);

        setupMoodBar(R.id.moodName, R.id.moodProgress, R.id.btnPlus, "Happy");
        setupMoodBar(R.id.moodName, R.id.moodProgress, R.id.btnPlus, "Tired");
        setupMoodBar(R.id.moodName, R.id.moodProgress, R.id.btnPlus, "Sad");
        setupMoodBar(R.id.moodName, R.id.moodProgress, R.id.btnPlus, "Joyful");

        updateTopMood();
    }

    private void setupMoodBar(int nameId, int progressId, int buttonId, String mood) {
        TextView moodName = findViewById(nameId);
        ProgressBar moodProgress = findViewById(progressId);
        Button plusButton = findViewById(buttonId);

        moodName.setText(mood);
        moodProgress.setProgress(moodLevels.get(mood));

        plusButton.setOnClickListener(v -> {
            int current = moodLevels.get(mood);
            if (current < 100) {
                moodLevels.put(mood, current + 5);
                moodProgress.setProgress(current + 5);
                updateTopMood();
            }
        });
    }

    private void updateTopMood() {
        String topMood = "Happy";
        int topValue = 0;

        for (Map.Entry<String, Integer> entry : moodLevels.entrySet()) {
            if (entry.getValue() > topValue) {
                topMood = entry.getKey();
                topValue = entry.getValue();
            }
        }

        switch (topMood) {
            case "Happy":
                moodEmoji.setText("😊");
                moodMessage.setText("You can do a good job!");
                break;
            case "Tired":
                moodEmoji.setText("😴");
                moodMessage.setText("Take some time to rest.");
                break;
            case "Sad":
                moodEmoji.setText("😢");
                moodMessage.setText("It’s okay to feel sad. You are strong.");
                break;
            case "Joyful":
                moodEmoji.setText("😁");
                moodMessage.setText("Spread your joy today!");
                break;
        }
    }
}



