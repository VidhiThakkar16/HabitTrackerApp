package com.example.myapplication; // Package name of the app, change as per your project

import android.annotation.SuppressLint;
import android.content.Intent; // Used to navigate from one activity to another
import android.os.Bundle;    // Used for passing data between activities
import android.os.Handler;   // Used to introduce delay for splash screen

import androidx.appcompat.app.AppCompatActivity; // Base class for activities using AppCompat support

@SuppressLint("CustomSplashScreen") // Suppresses the lint warning for custom splash screens
public class SplashActivity extends AppCompatActivity {

    // Duration for which the splash screen will be visible (in milliseconds)
    private static final int SPLASH_TIME = 4000; // 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the splash screen
        setContentView(R.layout.activity_splash);

        // Handler to introduce a delay before navigating to the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent to navigate from SplashActivity to LoginActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent); // Start LoginActivity

                finish(); // Close SplashActivity to prevent returning when back button is pressed
            }
        }, SPLASH_TIME); // Delay duration before executing the runnable
    }
}
