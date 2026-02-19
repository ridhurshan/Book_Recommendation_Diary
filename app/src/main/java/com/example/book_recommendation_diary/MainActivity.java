package com.example.book_recommendation_diary;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.book_recommendation_diary.activities.AddBookActivity;
import com.example.book_recommendation_diary.adapters.BookAdapter;
import com.example.book_recommendation_diary.database.DatabaseHelper;
import com.example.book_recommendation_diary.databinding.ActivityMainBinding;
import com.example.book_recommendation_diary.models.Book;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);
        // Getting user ID from intent (default to 1 for testing)
        userId = getIntent().getIntExtra("USER_ID", 1);

        binding.recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        loadBooks();

        binding.btnAddBook.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks(); // Refresh list when returning from AddBookActivity
    }

    private void loadBooks() {
        List<Book> bookList = db.getBooksByUser(userId);
        BookAdapter adapter = new BookAdapter(bookList, this);
        binding.recyclerViewBooks.setAdapter(adapter);
    }
}
