package com.example.virtual;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video); // Create this layout

        String category = getIntent().getStringExtra("category");

        TextView title = findViewById(R.id.videoTitle);
        title.setText(category + " Videos Coming Soon!");
    }
}
