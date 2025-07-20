package com.example.virtual;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class JournalActivity extends AppCompatActivity {

    EditText editJournal;
    Button buttonSave;
    LinearLayout entryList;
    SharedPreferences sharedPreferences;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        editJournal = findViewById(R.id.editJournal);
        buttonSave = findViewById(R.id.buttonSave);
        entryList = findViewById(R.id.entryList);

        sharedPreferences = getSharedPreferences("JournalPrefs", Context.MODE_PRIVATE);
        loadEntries();

        buttonSave.setOnClickListener(v -> {
            String text = editJournal.getText().toString().trim();
            if (!text.isEmpty()) {
                String time = dateFormat.format(new Date());
                sharedPreferences.edit().putString(time, text).apply();
                editJournal.setText("");
                addEntryToLayout(time, text);
            }
        });
    }

    private void loadEntries() {
        Map<String, ?> entries = sharedPreferences.getAll();
        entryList.removeAllViews();
        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            addEntryToLayout(entry.getKey(), entry.getValue().toString());
        }
    }

    private void addEntryToLayout(String date, String content) {
        TextView entryView = new TextView(this);
        entryView.setText("📅 " + date + "\n📝 " + content);
        entryView.setTextSize(16);
        entryView.setPadding(24, 20, 24, 20);
        entryView.setBackgroundColor(0xFFEDE7F6);
        entryView.setTextColor(0xFF000000);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 20);
        entryView.setLayoutParams(lp);

        entryList.addView(entryView, 0); // add to top
    }
}
