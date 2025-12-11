package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent; // To navigate to LoginActivity after registration
import android.database.sqlite.SQLiteDatabase; // To manage local database for users
import android.os.Bundle;
import android.text.TextUtils; // For input validation
import android.widget.Toast; // To show short messages

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    // Declare UI components
    TextInputEditText usernameEt, passwordEt;
    MaterialButton registerBtn;
    SQLiteDatabase db; // SQLite database instance

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views from XML
        usernameEt = findViewById(R.id.regUsername);
        passwordEt = findViewById(R.id.regPassword);
        registerBtn = findViewById(R.id.registerBtn);

        // Open or create a local database named "HabitMateDB"
        db = openOrCreateDatabase("HabitMateDB", MODE_PRIVATE, null);
        // Create users table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS users(username VARCHAR PRIMARY KEY, password VARCHAR);");

        // Register button click listener
        registerBtn.setOnClickListener(v -> {
            String username = usernameEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            // Validate input fields
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Insert new user into the database
                db.execSQL("INSERT INTO users(username, password) VALUES(?, ?);", new Object[]{username, password});
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

                // Redirect user to LoginActivity after successful registration
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Prevent back navigation to registration

            } catch (Exception e) {
                // Handle case when username already exists
                Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
