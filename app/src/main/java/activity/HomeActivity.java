package com.example.virtual;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import com.example.virtual.MentalHealthChatActivity;



public class HomeActivity extends AppCompatActivity {

    private TextView textQuote;
    private LinearLayout cardChatbot, cardMood, cardJournal;

    private String[] quotes = {
            "Your mental health is a priority.",
            "It's okay to ask for help.",
            "Self-care is how you take your power back.",
            "Healing takes time, and that's okay.",
            "You are not alone in this journey.",
            "Progress, not perfection.",
            "You matter. Always.",
            "Small steps every day.",
            "Breathe in peace, breathe out stress.",
            "You’ve survived 100% of your worst days."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Bind Views
        textQuote = findViewById(R.id.textQuote);
        cardChatbot = findViewById(R.id.cardChatbot);
        cardMood = findViewById(R.id.cardMood);
        cardJournal = findViewById(R.id.cardJournal);

        // Set random quote
        setRandomQuote();

        // Navigate to Chatbot
        cardChatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(HomeActivity.this, MentalHealthChatActivity.class));

        }
        });

        // Navigate to Mood Tracker
        cardMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DailyAgendaActivity.class));
            }
        });

        // Navigate to Journal
        cardJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, JournalActivity.class));
            }
        });
    }

    private void setRandomQuote() {
        Random random = new Random();
        int index = random.nextInt(quotes.length);
        textQuote.setText("“" + quotes[index] + "”");
    }
}





