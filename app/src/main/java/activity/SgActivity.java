package com.example.virtual;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SgActivity extends AppCompatActivity {

    EditText usernameInput, emailInput, passwordInput;
    Button signUpBtn;
    TextView gotoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sg);

        usernameInput = findViewById(R.id.signup_username);
        emailInput = findViewById(R.id.signup_email);
        passwordInput = findViewById(R.id.signup_password);
        signUpBtn = findViewById(R.id.signup_button);
        gotoLogin = findViewById(R.id.goto_login);

        signUpBtn.setOnClickListener(v -> {
            String name = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Enter your name.", Toast.LENGTH_SHORT).show();
            } else if (!email.contains("@")) {
                Toast.makeText(this, "Enter a valid email.", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 8) {
                Toast.makeText(this, "Password too short.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SgActivity.this, DashActivity.class));
                finish();
            }
        });

        gotoLogin.setOnClickListener(v ->
                startActivity(new Intent(SgActivity.this, LgActivity.class)));
    }
}
