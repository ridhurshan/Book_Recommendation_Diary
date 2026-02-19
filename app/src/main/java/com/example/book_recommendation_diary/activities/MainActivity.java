package com.example.book_recommendation_diary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_recommendation_diary.R;
import com.example.book_recommendation_diary.adapters.BookAdapter;
import com.example.book_recommendation_diary.database.DatabaseHelper;
import com.example.book_recommendation_diary.models.Book;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private DatabaseHelper db;
    private int userId;
    private Button btnAddBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get userId from LoginActivity
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            // Handle error, maybe return to login
        }

        recyclerView = findViewById(R.id.recyclerViewBooks);
        btnAddBook = findViewById(R.id.btnAddBook);
        db = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadBooks();

        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    private void loadBooks() {
        List<Book> bookList = db.getBooksByUser(userId);
        adapter = new BookAdapter(bookList, this);
        recyclerView.setAdapter(adapter);
    }
}
