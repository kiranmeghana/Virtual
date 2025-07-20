package com.example.virtual;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class CheckinActivity extends AppCompatActivity {

    TextView todayDate, historyTitle;
    EditText inputGoal;
    CheckBox task1, task2, task3;
    RadioGroup moodGroup;
    Button btnSave;
    LinearLayout historyLayout;
    SharedPreferences prefs;
    String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        todayDate = findViewById(R.id.todayDate);
        inputGoal = findViewById(R.id.inputGoal);
        task1 = findViewById(R.id.task1);
        task2 = findViewById(R.id.task2);
        task3 = findViewById(R.id.task3);
        moodGroup = findViewById(R.id.moodGroup);
        btnSave = findViewById(R.id.btnSave);
        historyLayout = findViewById(R.id.historyLayout);
        historyTitle = findViewById(R.id.historyTitle);

        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        todayDate.setText("📅 " + new SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(new Date()));

        prefs = getSharedPreferences("Checkins", MODE_PRIVATE);
        loadHistory();

        btnSave.setOnClickListener(v -> saveCheckin());
    }

    void saveCheckin() {
        String goal = inputGoal.getText().toString().trim();
        String mood = ((RadioButton)findViewById(moodGroup.getCheckedRadioButtonId())).getText().toString();
        List<String> doneTasks = new ArrayList<>();
        if (task1.isChecked()) doneTasks.add("Meditated");
        if (task2.isChecked()) doneTasks.add("Journaled");
        if (task3.isChecked()) doneTasks.add("Breathed");

        String result = today + "|" + mood + "|" + goal + "|" + String.join(",", doneTasks);
        Set<String> all = prefs.getStringSet("entries", new HashSet<>());
        all.add(result);
        prefs.edit().putStringSet("entries", all).apply();

        if (doneTasks.size() == 3) {
            new AlertDialog.Builder(this)
                    .setTitle("🎉 Great Job!")
                    .setMessage("You completed all your tasks today!")
                    .setPositiveButton("Awesome!", null)
                    .show();
        }

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        loadHistory();
    }

    void loadHistory() {
        historyLayout.removeAllViews();
        Set<String> all = prefs.getStringSet("entries", new HashSet<>());
        List<String> list = new ArrayList<>(all);
        Collections.sort(list, Collections.reverseOrder());

        for (String entry : list) {
            String[] parts = entry.split("\\|");
            if (parts.length >= 4) {
                TextView t = new TextView(this);
                t.setText("✔️ " + parts[0] + " - " + parts[1] + " - " + parts[3]);
                t.setTextSize(14f);
                historyLayout.addView(t);
            }
        }
    }
}

