package com.example.book_recommendation_diary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.book_recommendation_diary.R;
import com.example.book_recommendation_diary.database.DatabaseHelper;

public class EditBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etRating, etReview;
    private Button btnUpdate;
    private DatabaseHelper db;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etRating = findViewById(R.id.etRating);
        etReview = findViewById(R.id.etReview);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = new DatabaseHelper(this);

        // Get data from intent
        Intent intent = getIntent();
        bookId = intent.getIntExtra("BOOK_ID", -1);
        String title = intent.getStringExtra("BOOK_TITLE");
        String author = intent.getStringExtra("BOOK_AUTHOR");
        float rating = intent.getFloatExtra("BOOK_RATING", 0);
        String review = intent.getStringExtra("BOOK_REVIEW");

        // Populate fields
        etTitle.setText(title);
        etAuthor.setText(author);
        etRating.setText(String.valueOf(rating));
        etReview.setText(review);

        btnUpdate.setOnClickListener(v -> updateBook());
    }

    private void updateBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String review = etReview.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Parse as float first, then cast to int
        float ratingFloat = Float.parseFloat(etRating.getText().toString().trim());
        int rating = (int) ratingFloat;

        boolean isUpdated = db.updateBook(bookId, title, author, rating, review);

        if (isUpdated) {
            Toast.makeText(this, "Book Updated!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
