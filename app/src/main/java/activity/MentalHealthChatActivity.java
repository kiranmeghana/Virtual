package com.example.virtual;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.method.LinkMovementMethod;


import java.util.ArrayList;
import java.util.List;

public class MentalHealthChatActivity extends AppCompatActivity {

    private LinearLayout chatLayout;
    private EditText editTextMessage;
    private ScrollView scrollView;
    private DrawerLayout drawerLayout;
    private ListView previousChatsList;
    private ImageView profileIcon;
    private Button buttonSend;

    private List<List<String>> chatHistory = new ArrayList<>();
    private List<String> currentSession = new ArrayList<>();
    private List<String> sessionLabels = new ArrayList<>();
    private ArrayAdapter<String> historyAdapter;
    private int sessionCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_health_chat);

        chatLayout = findViewById(R.id.chatLayout);
        editTextMessage = findViewById(R.id.editTextMessage);
        scrollView = findViewById(R.id.scrollView);
        drawerLayout = findViewById(R.id.drawerLayout);
        previousChatsList = findViewById(R.id.previousChatsList);
        profileIcon = findViewById(R.id.profileIcon);
        buttonSend = findViewById(R.id.buttonSend);

        historyAdapter = new ArrayAdapter<>(this, R.layout.drawer_session_item, R.id.sessionText, sessionLabels);

        previousChatsList.setAdapter(historyAdapter);

        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                addMessage(message, true); // user
                String reply = getBotReply(message);
                addMessage(reply, false); // bot
                saveCurrentSession(); // save
                editTextMessage.setText("");
            }
        });

        profileIcon.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));

        previousChatsList.setOnItemClickListener((parent, view, position, id) -> {
            loadChatSession(chatHistory.get(position));
            drawerLayout.closeDrawer(Gravity.START);
        });
    }

    private void addMessage(String text, boolean isUser) {
        View messageView = getLayoutInflater().inflate(R.layout.message_bubble, null);
        TextView textView = messageView.findViewById(R.id.messageText);
        ImageView icon = messageView.findViewById(R.id.avatarIcon);

        textView.setText(text);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(16, 8, 16, 8);

        if (isUser) {
            params.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.bg_user); // right bubble
            icon.setImageResource(R.drawable.profile_icon);
            icon.setVisibility(View.VISIBLE);
            ((LinearLayout) messageView).setGravity(Gravity.END); // Align entire layout right
        } else {
            params.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.bg_bot); // left bubble
            icon.setImageResource(R.drawable.ic_bot);
            icon.setVisibility(View.VISIBLE);
            ((LinearLayout) messageView).setGravity(Gravity.START); // Align entire layout left
        }


        messageView.setLayoutParams(params);
        icon.setVisibility(View.VISIBLE);

        chatLayout.addView(messageView);
        currentSession.add((isUser ? "User: " : "Bot: ") + text);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void saveCurrentSession() {
        if (!currentSession.isEmpty()) {
            if (chatHistory.isEmpty() || !chatHistory.get(chatHistory.size() - 1).equals(currentSession)) {
                chatHistory.add(new ArrayList<>(currentSession));
                sessionLabels.add("Session " + sessionCount++);
                historyAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadChatSession(List<String> session) {
        chatLayout.removeAllViews();
        for (String entry : session) {
            boolean isUser = entry.startsWith("User: ");
            String actualText = entry.replaceFirst("User: ", "").replaceFirst("Bot: ", "");
            addMessage(actualText, isUser);
        }
        currentSession.clear();
    }

    private String getBotReply(String msg) {
        msg = msg.toLowerCase();

        if (msg.contains("depressed") || msg.contains("sad"))
            return "I'm really sorry you're feeling this way. It's okay to have tough days. Try writing down your thoughts, talking to someone you trust, or going for a short walk. You're not alone.";

        if (msg.contains("anxiety"))
            return "Anxiety can be overwhelming. Try grounding yourself: name 5 things you can see, 4 you can touch, 3 you can hear, 2 you can smell, and 1 you can taste. It might help you feel more present.";

        if (msg.contains("stress"))
            return "You seem stressed. Take a deep breath. Maybe a 5-minute stretch, sipping water slowly, or closing your eyes for a moment can help. Remember, breaks are healthy.";

        if (msg.contains("happy"))
            return "That’s wonderful to hear! Take a moment to reflect on what made you happy today so you can return to it when you need a boost.";

        if (msg.contains("angry"))
            return "Anger is a valid emotion. Try releasing it safely—write it out, scream into a pillow, or punch a cushion. Then take a deep breath and reflect on what's really upsetting you.";

        if (msg.contains("lonely"))
            return "Loneliness can be heavy. Reach out to someone—a friend, family member, or even a support group. Sharing how you feel is powerful and brave.";

        if (msg.contains("help"))
            return "If you're in crisis or need professional support, please reach out to a local mental health professional or helpline. You are valued and your feelings matter.";

        if (msg.contains("joke"))
            return "Sure! Why don't skeletons fight each other? Because they don't have the guts! 😄 Want another one?";

        if (msg.contains("bored"))
            return "Feeling bored? Try drawing something random, journaling 3 things you're grateful for, or doing something creative like painting or listening to music.";

        if (msg.contains("motivate") || msg.contains("motivation"))
            return "You’ve come this far—keep going. Your future self will thank you for not giving up today. You’ve got this! 💪";

        if (msg.contains("exercise"))
            return "Movement boosts mood! Even a 2-minute dance, walk, or stretch can make a difference. Try setting a small goal today.";

        if (msg.contains("sleep"))
            return "If you're having trouble sleeping, try a calming ritual—dim the lights, play soft music, or do 4-7-8 breathing. Avoid screens 30 mins before bed.";

        if (msg.contains("music"))
            return "Music is a great emotional outlet. Put on your favorite playlist or explore new genres—music can match or shift your mood beautifully.";

        if (msg.contains("cry"))
            return "It’s okay to cry. Tears are your body’s way of releasing emotion. Let them flow if you need to—then take a deep breath and be kind to yourself.";

        if (msg.contains("panic"))
            return "During a panic attack, try grounding techniques or hold onto something soft and textured. Remind yourself that it will pass—it always does.";

        if (msg.contains("breathe") || msg.contains("breath"))
            return "Let’s take 3 deep breaths together. Inhale slowly through your nose… hold… and exhale gently through your mouth. Again. One more. How do you feel now?";

        if (msg.contains("fear") || msg.contains("nervous"))
            return "It’s normal to feel nervous. Try writing down your fears and challenging them logically. Sometimes, naming the fear shrinks its power.";

        if (msg.contains("mental health"))
            return "Caring for your mental health is a strength. Keep checking in with yourself, rest when you need to, and celebrate the small victories.";

        if (msg.contains("overwhelmed"))
            return "When everything feels too much, pause. Write down your top 3 tasks, take one tiny step, then reward yourself. You don’t have to do it all at once.";

        return "Tell me more about how you're feeling, or ask me for a tip, comfort, motivation, or even a light-hearted joke. I'm here for you.";
    }
}


