package com.example.myapplication;

/**
 * Model class representing a single Habit entity.
 * This class defines the structure of a habit, including its attributes,
 * constructors for different use cases, and getter/setter methods.

 * It acts as a Data Model used across the app to store and retrieve
 * habit details from the database and display them in the UI.
 */
public class Habit {

    // ===============================
    // >> Fields - Habit Attributes
    // ===============================
    private int id;                 // Unique ID of the habit (Primary key in DB)
    private String name;            // Name/title of the habit (e.g., "Morning Walk")
    private String frequency;       // How often the habit should be done (e.g., "Daily", "Weekly")
    private String description;     // Additional details or notes about the habit
    private String category;        // Category of the habit (e.g., Health, Work, Personal)
    private String username;        // Username of the owner (helps when multiple users are supported)
    private boolean completed;      // Status indicating if the habit is completed for the current period

    // Optional streak fields for habit tracking over time
    private int streakCount;        // Number of consecutive completions
    private String lastCompletedDate; // Last date on which this habit was marked as completed

    // ===============================
    // 📌 Constructors
    // ===============================

    /**
     * Constructor for creating a new habit without ID (e.g., before inserting into DB).
     */
    public Habit(String name, String frequency, String description, String category, boolean completed) {
        this.name = name;
        this.frequency = frequency;
        this.description = description;
        this.category = category;
        this.completed = completed;
        this.streakCount = 0;
        this.lastCompletedDate = "";
        this.username = "";
    }

    /**
     * Backward-compatible constructor without category parameter.
     * Assigns default category as "General".
     */
    public Habit(String name, String frequency, String description, boolean completed) {
        this(name, frequency, description, "General", completed);
    }

    /**
     * Constructor for retrieving habit from DB without username.
     */
    public Habit(int id, String name, String frequency, String description, String category, boolean completed) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.description = description;
        this.category = category;
        this.completed = completed;
        this.streakCount = 0;
        this.lastCompletedDate = "";
        this.username = "";
    }

    /**
     * Constructor for retrieving habit from DB with username.
     */
    public Habit(int id, String name, String frequency, String description, String category, boolean completed, String username) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.description = description;
        this.category = category;
        this.completed = completed;
        this.username = username;
        this.streakCount = 0;
        this.lastCompletedDate = "";
    }

    /**
     * Full constructor with streak fields and username.
     * Useful when habits include historical tracking.
     */
    public Habit(int id, String name, String frequency, String description, String category,
                 boolean completed, int streakCount, String lastCompletedDate, String username) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.description = description;
        this.category = category;
        this.completed = completed;
        this.streakCount = streakCount;
        this.lastCompletedDate = lastCompletedDate;
        this.username = username;
    }

    // ===============================
    // 📌 Getters - For Accessing Values
    // ===============================
    public int getId() { return id; }
    public String getName() { return name; }
    public String getFrequency() { return frequency; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getUsername() { return username; }
    public boolean isCompleted() { return completed; }
    public int getStreakCount() { return streakCount; }
    public String getLastCompletedDate() { return lastCompletedDate; }

    // ===============================
    // 📌 Setters - For Modifying Values
    // ===============================
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setUsername(String username) { this.username = username; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setStreakCount(int streakCount) { this.streakCount = streakCount; }
    public void setLastCompletedDate(String lastCompletedDate) { this.lastCompletedDate = lastCompletedDate; }
}
