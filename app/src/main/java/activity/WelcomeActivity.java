package com.example.virtual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button loginBtn = findViewById(R.id.login_button);
        Button signupBtn = findViewById(R.id.signup_button);

        // WelcomeActivity.java
        loginBtn.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this, LgActivity.class)));
        signupBtn.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this, SgActivity.class)));

    }
}
