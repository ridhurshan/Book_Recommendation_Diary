package com.example.book_recommendation_diary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BookDiary.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + DatabaseContract.UserTable.TABLE_NAME + "("
                + DatabaseContract.UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseContract.UserTable.COLUMN_USERNAME + " TEXT,"
                + DatabaseContract.UserTable.COLUMN_EMAIL + " TEXT,"
                + DatabaseContract.UserTable.COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_BOOKS_TABLE = "CREATE TABLE " + DatabaseContract.BookTable.TABLE_NAME + "("
                + DatabaseContract.BookTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseContract.BookTable.COLUMN_TITLE + " TEXT,"
                + DatabaseContract.BookTable.COLUMN_AUTHOR + " TEXT,"
                + DatabaseContract.BookTable.COLUMN_DESCRIPTION + " TEXT,"
                + DatabaseContract.BookTable.COLUMN_RATING + " REAL,"
                + DatabaseContract.BookTable.COLUMN_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + DatabaseContract.BookTable.COLUMN_USER_ID + ") REFERENCES "
                + DatabaseContract.UserTable.TABLE_NAME + "(" + DatabaseContract.UserTable._ID + "))";
        db.execSQL(CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.UserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.BookTable.TABLE_NAME);
        onCreate(db);
    }

    public boolean registerUser(String username, String email, String passwordHash) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if user already exists
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseContract.UserTable.TABLE_NAME +
                        " WHERE " + DatabaseContract.UserTable.COLUMN_EMAIL + " = ?",
                new String[]{email}
        );

        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // user already exists
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserTable.COLUMN_USERNAME, username);
        values.put(DatabaseContract.UserTable.COLUMN_EMAIL, email);
        values.put(DatabaseContract.UserTable.COLUMN_PASSWORD, passwordHash);

        long result = db.insert(DatabaseContract.UserTable.TABLE_NAME, null, values);
        return result != -1;
    }

    public int loginUser(String email, String passwordHash) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Use _ID as defined in DatabaseContract/BaseColumns
        // and match column names to the ones defined in Register/Contract
        Cursor cursor = db.rawQuery(
                "SELECT " + DatabaseContract.UserTable._ID +
                        " FROM " + DatabaseContract.UserTable.TABLE_NAME +
                        " WHERE " + DatabaseContract.UserTable.COLUMN_EMAIL + " = ? AND " +
                        DatabaseContract.UserTable.COLUMN_PASSWORD + " = ?",
                new String[]{email, passwordHash}
        );

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        return userId; 
    }
}
