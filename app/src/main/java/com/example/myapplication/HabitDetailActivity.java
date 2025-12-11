package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
/*
Fetches the habit ID from the previous activity.
Displays all details of the selected habit in editable fields.
Lets the user update or delete the habit using the database helper.
Uses a spinner to show categories and a checkbox for completion status.
 */

public class HabitDetailActivity extends AppCompatActivity {

    // 📝 UI components for displaying and editing habit details
    private EditText txtName, txtFrequency, txtDescription;
    private Spinner spCategory;
    private CheckBox chkCompleted;
    private Button btnDelete, btnSave;

    // 📦 Database helper instance for performing CRUD operations
    private HabitDatabaseHelper dbHelper;

    // 🌿 The current habit object being displayed/edited
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        // 🔗 Link UI elements with their XML counterparts
        txtName = findViewById(R.id.txtDetailName);
        txtFrequency = findViewById(R.id.txtDetailFrequency);
        txtDescription = findViewById(R.id.txtDetailDescription);
        spCategory = findViewById(R.id.spDetailCategory);
        chkCompleted = findViewById(R.id.chkDetailCompleted);
        btnDelete = findViewById(R.id.btnDeleteHabit);
        btnSave = findViewById(R.id.btnSaveHabit);

        // 🛠 Initialize database helper
        dbHelper = new HabitDatabaseHelper(this);

        // 🌀 Setup category spinner using predefined string array (from strings.xml)
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_array,                 // Array resource for categories
                android.R.layout.simple_spinner_item   // Default spinner item layout
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        // 📥 Retrieve the habit ID sent from HabitAdapter (via Intent)
        int habitId = getIntent().getIntExtra("habit_id", -1);

        // 🔍 If a valid ID is received, fetch all habits and find the matching one
        if (habitId != -1) {
            List<Habit> allHabits = dbHelper.getAllHabits();
            habit = allHabits.stream()
                    .filter(h -> h.getId() == habitId)  // Match by ID
                    .findFirst()
                    .orElse(null);                      // Return null if not found
        }

        // 🖊 Populate all input fields with existing habit details (if habit exists)
        if (habit != null) {
            txtName.setText(habit.getName());
            txtFrequency.setText(habit.getFrequency());
            txtDescription.setText(habit.getDescription());
            chkCompleted.setChecked(habit.isCompleted());

            // 🌀 Set category spinner position to match the habit’s category
            String category = habit.getCategory();
            if (category != null) {
                int spinnerPosition = categoryAdapter.getPosition(category);
                spCategory.setSelection(spinnerPosition);
            }
        }

        // 💾 Save Button → Updates habit details in the database
        btnSave.setOnClickListener(v -> {
            // Update habit object with new values from the UI
            habit.setName(txtName.getText().toString());
            habit.setFrequency(txtFrequency.getText().toString());
            habit.setDescription(txtDescription.getText().toString());
            habit.setCategory(spCategory.getSelectedItem().toString());
            habit.setCompleted(chkCompleted.isChecked());

            // Update in DB
            dbHelper.updateHabit(habit);
            Toast.makeText(this, "Habit updated!", Toast.LENGTH_SHORT).show();

            // Go back to previous screen
            finish();
        });

        // 🗑 Delete Button → Removes the habit record permanently from DB
        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteHabit(habit.getId());
            Toast.makeText(this, "Habit deleted!", Toast.LENGTH_SHORT).show();

            // Go back to previous screen
            finish();
        });
    }
}
