package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DashboardActivity.java
 * ✅ Acts as the **main screen** of the app after login.
 * ✅ Displays all habits in a RecyclerView.
 * ✅ Provides search, category filtering, progress viewing, and logout options.
 * ✅ Acts as the central hub for navigation to Add Habit and Progress screens.
 */
public class DashboardActivity extends AppCompatActivity {

    // ===============================
    // 📌 UI Components
    // ===============================
    private RecyclerView rvHabits;                     // List to display habits
    private FloatingActionButton fabAddHabit;         // Button to add a new habit
    private FloatingActionButton fabProgress;         // Button to open progress screen
    private FloatingActionButton fabLogout;           // Button to logout
    private AutoCompleteTextView autoSearchHabit;     // Search bar for habits
    private Spinner spFilterCategory;                 // Dropdown to filter habits by category

    // ===============================
    // 📌 Data & Adapters
    // ===============================
    private HabitAdapter habitAdapter;                // Adapter to bind habit data to RecyclerView
    private List<Habit> habitList;                    // All habits fetched from DB
    private List<Habit> filteredList;                 // Filtered habits based on search/filter
    private HabitDatabaseHelper dbHelper;            // Database helper for CRUD operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // ===============================
        // 🔸 Initialize Views & DB Helper
        // ===============================
        rvHabits = findViewById(R.id.recyclerViewHabits);
        fabAddHabit = findViewById(R.id.fabAddHabit);
        fabProgress = findViewById(R.id.fabProgress);
        fabLogout = findViewById(R.id.fabLogout);
        autoSearchHabit = findViewById(R.id.autoSearchHabit);
        spFilterCategory = findViewById(R.id.spFilterCategory);
        dbHelper = new HabitDatabaseHelper(this);

        // ===============================
        // 🔸 Setup Category Spinner (Filter)
        // ===============================
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_array, // Array defined in strings.xml (e.g., All, Health, Work...)
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilterCategory.setAdapter(categoryAdapter);

        // Load habits initially (for the logged-in user)
        loadHabits();

        // ===============================
        // 🟡 Floating Action Buttons - Navigation
        // ===============================

        // ➕ Add Habit
        fabAddHabit.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, AddHabitActivity.class))
        );

        // 📈 View Progress
        fabProgress.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, ProgressActivity.class))
        );

        // 🚪 Logout
        fabLogout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            // Clear the activity stack to prevent back navigation after logout
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // ===============================
        // 🔍 Search & Filter Functionality
        // ===============================

        // Live search while typing in AutoCompleteTextView
        autoSearchHabit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHabits(s.toString(), spFilterCategory.getSelectedItem().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter by category from Spinner
        spFilterCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                filterHabits(autoSearchHabit.getText().toString(), spFilterCategory.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload habit list every time Dashboard becomes visible
        loadHabits();
    }

    // ===============================
    // 📌 Load All Habits for Current User
    // ===============================
    private void loadHabits() {
        // Fetch habits from DB (already filtered for logged-in user)
        habitList = dbHelper.getAllHabits();
        filteredList = new ArrayList<>(habitList);

        // Setup suggestions in the search box
        updateAutoCompleteSuggestions();

        // Initialize adapter and RecyclerView
        habitAdapter = new HabitAdapter(this, filteredList);
        rvHabits.setLayoutManager(new LinearLayoutManager(this));
        rvHabits.setAdapter(habitAdapter);
    }

    // ===============================
    // 📌 Update AutoCompleteTextView Suggestions
    // ===============================
    private void updateAutoCompleteSuggestions() {
        List<String> habitNames = habitList.stream()
                .map(Habit::getName)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                habitNames
        );
        autoSearchHabit.setAdapter(adapter);
    }

    // ===============================
    // 📌 Filter Habits by Search Query & Category
    // ===============================
    private void filterHabits(String query, String category) {
        filteredList.clear();

        for (Habit habit : habitList) {
            boolean matchesQuery = habit.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchesCategory = category.equals("All") || habit.getCategory().equals(category);

            if (matchesQuery && matchesCategory) {
                filteredList.add(habit);
            }
        }

        // Refresh RecyclerView
        habitAdapter.notifyDataSetChanged();
    }
}
