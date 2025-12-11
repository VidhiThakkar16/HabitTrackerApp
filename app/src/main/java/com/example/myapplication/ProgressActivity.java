package com.example.myapplication;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;


public class ProgressActivity extends AppCompatActivity {

    // UI components to display habit progress
    private TextView txtTotalHabits, txtCompletedHabits, txtCompletionPercent, txtMotivation;
    private ProgressBar progressBarCompletion;
    private HabitDatabaseHelper dbHelper; // Database helper to fetch habit data
    private FloatingActionButton fabLogoutProgress; // Logout button

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Initialize UI components from XML
        txtTotalHabits = findViewById(R.id.txtTotalHabits);
        txtCompletedHabits = findViewById(R.id.txtCompletedHabits);
        txtCompletionPercent = findViewById(R.id.txtCompletionPercent);
        txtMotivation = findViewById(R.id.txtMotivation);
        progressBarCompletion = findViewById(R.id.progressBarCompletion);
        fabLogoutProgress = findViewById(R.id.fabLogoutProgress);

        // Initialize database helper
        dbHelper = new HabitDatabaseHelper(this);

        // Load habit progress data and update UI
        loadProgress();

        // Logout functionality: Clears the current task and returns to LoginActivity
        fabLogoutProgress.setOnClickListener(v -> {
            Intent intent = new Intent(ProgressActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Prevent back navigation
        });
    }

    // Method to fetch habit data, calculate progress, and update UI
    @SuppressLint("SetTextI18n")
    private void loadProgress() {
        List<Habit> habits = dbHelper.getAllHabits(); // Fetch all habits
        int total = habits.size(); // Total number of habits
        int completed = 0; // Counter for completed habits

        // Count completed habits
        for (Habit habit : habits) {
            if (habit.isCompleted()) completed++;
        }

        // Calculate completion percentage
        int percent = total == 0 ? 0 : (completed * 100) / total;

        // Update UI components
        txtTotalHabits.setText(String.valueOf(total));
        txtCompletedHabits.setText(String.valueOf(completed));
        txtCompletionPercent.setText(percent + "%");
        progressBarCompletion.setProgress(percent);

        // Display motivational messages based on completion percentage
        if (percent == 0) {
            txtMotivation.setText("Let's start your habit journey today! 🌟");
        } else if (percent < 50) {
            txtMotivation.setText("Good start! Keep pushing forward! 💪");
        } else if (percent < 100) {
            txtMotivation.setText("Great work! Almost there! 🚀");
        } else {
            txtMotivation.setText("Excellent! All habits completed! 🎉");
        }
    }
}
