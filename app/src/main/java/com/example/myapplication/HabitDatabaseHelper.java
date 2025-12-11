package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * HabitDatabaseHelper:
 * --------------------
 * This class manages all database-related operations for the Habit Tracker app.
 * It handles creating/upgrading the SQLite database, and provides CRUD methods
 * for inserting, reading, updating, and deleting habits. Each habit is tied to
 * the currently logged-in user via the username column.
 */
public class HabitDatabaseHelper extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME = "habittracker.db";
    private static final int DATABASE_VERSION = 3;

    // Table and column names
    private static final String TABLE_HABITS = "habits";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_FREQUENCY = "frequency";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_USERNAME = "username"; // stores habit owner

    private Context context;

    public HabitDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Called when the database is first created.
     * Creates the "habits" table with all required columns.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_HABITS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_FREQUENCY + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER DEFAULT 0, " +
                COLUMN_USERNAME + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    /**
     * Called when the database version is increased.
     * Used to alter existing tables and add missing columns without data loss.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add 'category' column if upgrading from version < 2
        try {
            if (oldVersion < 2) {
                db.execSQL("ALTER TABLE " + TABLE_HABITS + " ADD COLUMN " +
                        COLUMN_CATEGORY + " TEXT DEFAULT 'General'");
            }
        } catch (Exception ignored) {}

        // Add 'username' column if upgrading from version < 3
        try {
            if (oldVersion < 3) {
                db.execSQL("ALTER TABLE " + TABLE_HABITS + " ADD COLUMN " + COLUMN_USERNAME + " TEXT");
            }
        } catch (Exception ignored) {}
    }

    /**
     * Insert a new habit into the database.
     * The username of the currently logged-in user is automatically attached.
     */
    public long addHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, habit.getName());
        values.put(COLUMN_FREQUENCY, habit.getFrequency());
        values.put(COLUMN_DESCRIPTION, habit.getDescription());
        values.put(COLUMN_CATEGORY, habit.getCategory() != null ? habit.getCategory() : "General");
        values.put(COLUMN_COMPLETED, habit.isCompleted() ? 1 : 0);
        values.put(COLUMN_USERNAME, getLoggedInUsername()); // attach owner

        long id = db.insert(TABLE_HABITS, null, values);
        db.close();
        return id;
    }

    /**
     * Retrieve all habits belonging to the currently logged-in user.
     */
    public List<Habit> getAllHabits() {
        List<Habit> habitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String username = getLoggedInUsername();

        // Query to fetch only habits for the current user
        Cursor cursor = db.query(
                TABLE_HABITS,
                null,
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null
        );

        // Convert each row into a Habit object
        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FREQUENCY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                );
                habitList.add(habit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return habitList;
    }

    /**
     * Search habits by name and optionally filter by category for the current user.
     */
    public List<Habit> searchHabits(String query, String categoryFilter) {
        List<Habit> habitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String username = getLoggedInUsername();

        // Build query with optional category filter
        String selection = COLUMN_USERNAME + "=? AND " + COLUMN_NAME + " LIKE ?";
        ArrayList<String> argsList = new ArrayList<>();
        argsList.add(username);
        argsList.add("%" + query + "%");

        if (categoryFilter != null && !categoryFilter.equals("All")) {
            selection += " AND " + COLUMN_CATEGORY + "=?";
            argsList.add(categoryFilter);
        }

        Cursor cursor = db.query(TABLE_HABITS, null, selection,
                argsList.toArray(new String[0]), null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Habit habit = new Habit(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FREQUENCY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                );
                habitList.add(habit);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return habitList;
    }

    /**
     * Update an existing habit in the database.
     * Username is preserved to ensure ownership remains intact.
     */
    public int updateHabit(Habit habit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, habit.getName());
        values.put(COLUMN_FREQUENCY, habit.getFrequency());
        values.put(COLUMN_DESCRIPTION, habit.getDescription());
        values.put(COLUMN_CATEGORY, habit.getCategory());
        values.put(COLUMN_COMPLETED, habit.isCompleted() ? 1 : 0);
        values.put(COLUMN_USERNAME, habit.getUsername() != null ?
                habit.getUsername() : getLoggedInUsername());

        int rows = db.update(TABLE_HABITS, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(habit.getId())});
        db.close();
        return rows;
    }

    /**
     * Delete a habit by its ID.
     */
    public void deleteHabit(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HABITS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * Helper method to get the username of the currently logged-in user.
     * Stored in SharedPreferences during login.
     */
    private String getLoggedInUsername() {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return prefs.getString("username", "");
    }

    /**
     * Optional method: fetch habits for any specific user (e.g., admin use or sharing features).
     */
    public List<Habit> getHabitsForUser(String username) {
        List<Habit> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HABITS, null, COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Habit h = new Habit(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FREQUENCY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                );
                list.add(h);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
