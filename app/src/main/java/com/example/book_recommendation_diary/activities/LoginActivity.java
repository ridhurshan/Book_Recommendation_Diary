package com.example.book_recommendation_diary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.book_recommendation_diary.R;
import com.example.book_recommendation_diary.database.DatabaseHelper;
import com.example.book_recommendation_diary.utils.PasswordUtils;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        databaseHelper = new DatabaseHelper(this);

        // Login button click
        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this,
                        "All fields are required",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String hashedPassword = PasswordUtils.hashPassword(password);

            int userId = databaseHelper.loginUser(email, hashedPassword);

            if (userId != -1) {

                Toast.makeText(LoginActivity.this,
                        "Login successful!",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Go to Register page
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
