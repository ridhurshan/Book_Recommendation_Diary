package com.example.book_recommendation_diary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.book_recommendation_diary.activities.AddBookActivity;
import com.example.book_recommendation_diary.adapters.BookAdapter;
import com.example.book_recommendation_diary.database.DatabaseHelper;
import com.example.book_recommendation_diary.databinding.ActivityMainBinding;
import com.example.book_recommendation_diary.models.Book;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {

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
        // Pass 'this' as the third argument because MainActivity implements OnBookClickListener
        BookAdapter adapter = new BookAdapter(bookList, this, this);
        binding.recyclerViewBooks.setAdapter(adapter);
    }

    @Override
    public void onBookClick(Book book) {
        // Handle the book click, for example, show a message or open details
        Toast.makeText(this, "Clicked: " + book.getTitle(), Toast.LENGTH_SHORT).show();
    }


}
