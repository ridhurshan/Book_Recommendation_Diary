package com.example.book_recommendation_diary.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String review; // Named 'review' to match your Activity code
    private float rating;
    private int userId;

    public Book() {}

    public Book(int id, String title, String author, String review, float rating, int userId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.review = review;
        this.rating = rating;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    // Added as an alias for backward compatibility with database code
    public String getDescription() {
        return review;
    }

    public void setDescription(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
