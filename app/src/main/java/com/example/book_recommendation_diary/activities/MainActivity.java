package com.example.book_recommendation_diary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.book_recommendation_diary.adapters.BookAdapter;
import com.example.book_recommendation_diary.database.DatabaseHelper;
import com.example.book_recommendation_diary.databinding.ActivityMainBinding;
import com.example.book_recommendation_diary.models.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {

    private ActivityMainBinding binding;
    private DatabaseHelper db;
    private int userId;

    private BookAdapter adapter;
    private List<Book> bookList = new ArrayList<>();

    private ActivityResultLauncher<Intent> editBookLauncher;
    private ActivityResultLauncher<Intent> addBookLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);

        // Get user ID from intent, default to 1 if not provided
        userId = getIntent().getIntExtra("USER_ID", 1);
        Log.d("MainActivity", "User ID: " + userId);

        setupRecyclerView();
        setupLaunchers();

        binding.btnAddBook.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            intent.putExtra("USER_ID", userId);
            addBookLauncher.launch(intent);
        });
    }

    private void setupRecyclerView() {
        binding.recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));

        // Load initial list
        loadBooks();

        adapter = new BookAdapter(bookList, this, this);
        binding.recyclerViewBooks.setAdapter(adapter);
    }

    private void setupLaunchers() {
        // Launcher for EditBookActivity result
        editBookLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d("MainActivity", "EditBookActivity returned OK");
                        loadBooks(); // Reload list and notify adapter
                    }
                }
        );

        // Launcher for AddBookActivity result
        addBookLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d("MainActivity", "AddBookActivity returned OK");
                        loadBooks(); // Reload list and notify adapter
                    }
                }
        );
    }

    private void loadBooks() {
        List<Book> newList = db.getBooksByUser(userId);
        bookList.clear();
        bookList.addAll(newList);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        Log.d("MainActivity", "Loaded " + bookList.size() + " books");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks(); // Refresh when returning to activity
    }

    @Override
    public void onBookClick(Book book) {
        Log.d("MainActivity", "Book clicked: " + book.getTitle() + " (ID: " + book.getId() + ")");

        Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
        intent.putExtra("BOOK_ID", book.getId());
        intent.putExtra("BOOK_TITLE", book.getTitle());
        intent.putExtra("BOOK_AUTHOR", book.getAuthor());
        intent.putExtra("BOOK_RATING", book.getRating());
        intent.putExtra("BOOK_REVIEW", book.getReview());

        editBookLauncher.launch(intent);
    }
}