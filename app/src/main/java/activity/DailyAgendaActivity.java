package com.example.virtual;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DailyAgendaActivity extends AppCompatActivity {

    DatePicker datePicker;
    EditText editTask;
    Button btnAddTask;
    LinearLayout taskListLayout;
    String selectedDateKey;
    SharedPreferences prefs;
    List<CheckBox> currentCheckboxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_agenda);

        datePicker = findViewById(R.id.datePicker);
        editTask = findViewById(R.id.editTask);
        btnAddTask = findViewById(R.id.btnAddTask);
        taskListLayout = findViewById(R.id.taskListLayout);
        prefs = getSharedPreferences("AgendaPrefs", Context.MODE_PRIVATE);

        selectedDateKey = getDateKey();

        loadTasks(selectedDateKey);

        btnAddTask.setOnClickListener(v -> {
            String taskText = editTask.getText().toString().trim();
            if (!taskText.isEmpty()) {
                addTaskCheckbox(taskText, false);
                saveTaskToPrefs(taskText, false);
                editTask.setText("");
            }
        });

        datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            selectedDateKey = formatDate(year, monthOfYear, dayOfMonth);
            loadTasks(selectedDateKey);
        });
    }

    String getDateKey() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        return formatDate(year, month, day);
    }

    String formatDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
    }

    void loadTasks(String key) {
        taskListLayout.removeAllViews();
        currentCheckboxes.clear();
        Set<String> taskSet = prefs.getStringSet(key, new HashSet<>());
        for (String item : taskSet) {
            String[] split = item.split("\\|");
            if (split.length == 2) {
                addTaskCheckbox(split[0], split[1].equals("1"));
            }
        }
    }

    void addTaskCheckbox(String task, boolean isChecked) {
        CheckBox cb = new CheckBox(this);
        cb.setText(task);
        cb.setChecked(isChecked);
        cb.setTextSize(16f);
        cb.setPadding(8, 8, 8, 8);
        cb.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        taskListLayout.addView(cb);
        currentCheckboxes.add(cb);

        cb.setOnCheckedChangeListener((buttonView, checked) -> {
            saveAllCheckboxes();
            if (allChecked()) {
                showCelebration();
            }
        });
    }

    void saveTaskToPrefs(String task, boolean isChecked) {
        Set<String> updatedTasks = prefs.getStringSet(selectedDateKey, new HashSet<>());
        updatedTasks = new HashSet<>(updatedTasks); // Clone it
        updatedTasks.add(task + "|" + (isChecked ? "1" : "0"));
        prefs.edit().putStringSet(selectedDateKey, updatedTasks).apply();
    }

    void saveAllCheckboxes() {
        Set<String> newSet = new HashSet<>();
        for (CheckBox cb : currentCheckboxes) {
            newSet.add(cb.getText().toString() + "|" + (cb.isChecked() ? "1" : "0"));
        }
        prefs.edit().putStringSet(selectedDateKey, newSet).apply();
    }

    boolean allChecked() {
        for (CheckBox cb : currentCheckboxes) {
            if (!cb.isChecked()) return false;
        }
        return currentCheckboxes.size() > 0;
    }

    void showCelebration() {
        new AlertDialog.Builder(this)
                .setTitle("🎉 Great Job!")
                .setMessage("You've completed all your tasks today!")
                .setPositiveButton("Awesome!", null)
                .show();
    }
}

