package com.sutralang.ide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.sutralang.ide.R;

/**
 * SplashActivity - Initial screen displayed when app starts
 * Routes to MainActivity after a delay
 */
public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 2000; // 2 seconds
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Hide action bar for splash screen
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        // Delayed navigation after splash timeout
        new Handler(Looper.getMainLooper()).postDelayed(
                this::navigateToNextActivity,
                SPLASH_DELAY
        );
    }
    
    /**
     * Navigate to MainActivity directly
     */
    private void navigateToNextActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
