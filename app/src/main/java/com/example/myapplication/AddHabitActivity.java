package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * AddHabitActivity:
 * -----------------
 * This activity allows the user to add a new habit by providing its
 * name, description, frequency, and category. The habit is then stored
 * in the local SQLite database using HabitDatabaseHelper.
 */
public class AddHabitActivity extends AppCompatActivity {

    // UI components
    private EditText etHabitName, etHabitDescription;
    private Spinner spFrequency, spCategory;
    private Button btnSaveHabit;

    // Database helper object for habit operations
    private HabitDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // Initialize UI components
        etHabitName = findViewById(R.id.etHabitName);
        etHabitDescription = findViewById(R.id.etHabitDescription);
        spFrequency = findViewById(R.id.spFrequency);
        spCategory = findViewById(R.id.spCategory);
        btnSaveHabit = findViewById(R.id.btnSaveHabit);

        // Initialize database helper
        dbHelper = new HabitDatabaseHelper(this);

        // Handle save button click event
        btnSaveHabit.setOnClickListener(v -> {
            // Get user input values from fields
            String name = etHabitName.getText().toString().trim();
            String desc = etHabitDescription.getText().toString().trim();
            String freq = spFrequency.getSelectedItem().toString();
            String category = spCategory.getSelectedItem().toString();

            // Validate habit name
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter habit name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Habit object
            // (dbHelper will automatically attach the currently logged-in user)
            Habit habit = new Habit(name, freq, desc, category, false);

            // Insert habit into database
            dbHelper.addHabit(habit);

            // Show confirmation and close the activity to return to Dashboard
            Toast.makeText(this, "Habit added!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
