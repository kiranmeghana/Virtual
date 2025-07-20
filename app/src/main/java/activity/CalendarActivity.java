package com.example.virtual;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class CalendarActivity extends AppCompatActivity {

    GridView calendarGrid;
    TextView monthLabel;
    Calendar displayCalendar;
    List<Date> gridDates = new ArrayList<>();
    SharedPreferences prefs;
    Set<String> completedDates = new HashSet<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        monthLabel = findViewById(R.id.monthLabel);
        calendarGrid = findViewById(R.id.calendarGrid);
        prefs = getSharedPreferences("AgendaPrefs", Context.MODE_PRIVATE);

        completedDates = prefs.getStringSet("completedDates", new HashSet<>());
        displayCalendar = Calendar.getInstance();

        generateCalendarGrid();
        updateGrid();

        calendarGrid.setOnItemClickListener((adapterView, view, position, id) -> {
            Date selectedDate = gridDates.get(position);
            showTaskDialog(selectedDate);
        });
    }

    void generateCalendarGrid() {
        gridDates.clear();
        Calendar calendar = (Calendar) displayCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int startOffset = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -startOffset);
        for (int i = 0; i < 42; i++) {
            gridDates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    void updateGrid() {
        monthLabel.setText(new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(displayCalendar.getTime()));
        calendarGrid.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() { return gridDates.size(); }

            @Override
            public Object getItem(int i) { return gridDates.get(i); }

            @Override
            public long getItemId(int i) { return i; }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView tv = new TextView(CalendarActivity.this);
                tv.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(16);
                Calendar cal = Calendar.getInstance();
                cal.setTime(gridDates.get(i));
                tv.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));

                String dateKey = sdf.format(gridDates.get(i));
                if (completedDates.contains(dateKey)) {
                    tv.setBackgroundColor(Color.parseColor("#BB86FC"));
                    tv.setTextColor(Color.WHITE);
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                    tv.setTextColor(Color.BLACK);
                }

                tv.setPadding(8, 8, 8, 8);
                tv.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                return tv;
            }
        });
    }

    void showTaskDialog(Date selectedDate) {
        String key = sdf.format(selectedDate);
        Set<String> tasks = prefs.getStringSet(key, new HashSet<>());
        List<String> taskList = new ArrayList<>(tasks);

        EditText input = new EditText(this);
        input.setHint("Enter new task");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        layout.addView(input);

        for (String t : taskList) {
            CheckBox cb = new CheckBox(this);
            cb.setText(t);
            cb.setChecked(true);
            layout.addView(cb);
        }

        new AlertDialog.Builder(this)
                .setTitle("Tasks on " + new SimpleDateFormat("MMM dd", Locale.getDefault()).format(selectedDate))
                .setView(layout)
                .setPositiveButton("Add", (dialogInterface, i) -> {
                    String newTask = input.getText().toString().trim();
                    if (!newTask.isEmpty()) {
                        taskList.add(newTask);
                        prefs.edit().putStringSet(key, new HashSet<>(taskList)).apply();
                        showTaskDialog(selectedDate); // refresh
                    }
                })
                .setNegativeButton("Done", (dialogInterface, i) -> {
                    if (!taskList.isEmpty()) {
                        completedDates.add(key);
                        prefs.edit().putStringSet("completedDates", completedDates).apply();
                        updateGrid();
                        new AlertDialog.Builder(this)
                                .setTitle("🎉 Completed!")
                                .setMessage("You've completed tasks for this day!")
                                .setPositiveButton("Nice!", null)
                                .show();
                    }
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
}
