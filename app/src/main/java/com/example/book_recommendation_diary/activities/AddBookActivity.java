package com.example.book_recommendation_diary.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.book_recommendation_diary.R;
import com.example.book_recommendation_diary.database.DatabaseHelper;
import com.example.book_recommendation_diary.models.Book;

public class AddBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etDescription;
    private RatingBar rbRating;
    private Button btnSave;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etTitle = findViewById(R.id.etBookTitle);
        etAuthor = findViewById(R.id.etBookAuthor);
        etDescription = findViewById(R.id.etBookDescription);
        rbRating = findViewById(R.id.rbBookRating);
        btnSave = findViewById(R.id.btnSaveBook);

        db = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        btnSave.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        float rating = rbRating.getRating();

        if (title.isEmpty() || author.isEmpty()) {
            Toast.makeText(this, "Please fill in title and author", Toast.LENGTH_SHORT).show();
            return;
        }

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setReview(description); // DatabaseHelper uses setReview/getReview
        book.setRating(rating);
        book.setUserId(userId);

        boolean success = db.addBook(book);
        if (success) {
            Toast.makeText(this, "Book saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving book", Toast.LENGTH_SHORT).show();
        }
    }
}
