package com.example.myapplication;

import android.content.Intent; // To navigate between activities
import android.content.SharedPreferences; // To store user data persistently
import android.database.Cursor; // To traverse query results from SQLite
import android.database.sqlite.SQLiteDatabase; // To manage local database
import android.os.Bundle;
import android.text.TextUtils; // For checking empty input fields
import android.widget.Toast; // To show short messages to the user

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    // Declare UI components
    TextInputEditText usernameEt, passwordEt;
    MaterialButton loginBtn;
    SQLiteDatabase db; // SQLite database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views from XML
        usernameEt = findViewById(R.id.username);
        passwordEt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        // Open or create a local database named "HabitMateDB"
        db = openOrCreateDatabase("HabitMateDB", MODE_PRIVATE, null);
        // Create users table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS users(username VARCHAR PRIMARY KEY, password VARCHAR);");

        // Login button click listener
        loginBtn.setOnClickListener(v -> {
            String username = usernameEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            // Check for empty fields
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Query database to check for valid username and password
            Cursor c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
            if (c.moveToFirst()) {
                // Save logged-in username in SharedPreferences for session management
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                prefs.edit().putString("username", username).apply();

                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                // Redirect to DashboardActivity after successful login
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish(); // Prevent returning to login on back press
            } else {
                // Show error if username/password are incorrect
                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
            c.close(); // Close the cursor to free resources
        });

        // Link to registration activity
        findViewById(R.id.registerText).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
