package com.sutralang.ide.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;
import com.sutralang.ide.R;

/**
 * AboutActivity - Displays information about SutraLang IDE
 */
public class AboutActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Set back button
        toolbar.setNavigationOnClickListener(v -> finish());
        
        // Set about content
        setupAboutContent();
    }
    
    /**
     * Setup about content text
     */
    private void setupAboutContent() {
        TextView aboutContent = findViewById(R.id.about_content);
        
        String aboutText = "🚀 SutraLang IDE\n" +
                "Version 1.0.0 • BETA\n\n" +
                "Revolutionizing mobile development with the world's first Hinglish IDE.\n\n" +
                "SutraLang bridges the gap between logic and language. Designed for the next billion developers, it allows you to write complex logic using intuitive Hinglish syntax while maintaining the power of high-performance modern compilers.\n\n" +
                "✨ Key Features:\n" +
                "• Syntax: Natural Hinglish expressions mapped directly to low-level optimizations\n" +
                "• Performance: Built on a custom LLVM backend for lightning-fast execution on mobile chips\n" +
                "• Cloud Workspace: Seamless sync with cloud storage\n" +
                "• Intelligence: Auto-complete powered by advanced language models\n\n" +
                "📱 Supported Statements:\n" +
                "• rakho x = 10;        (Variable declaration)\n" +
                "• dikhao x;             (Print output)\n" +
                "• agar (x > 5) { }     (Conditional)\n" +
                "• nahi to (x < 5) { }  (Else If)\n" +
                "• varna { }            (Else)\n" +
                "• pucho(\"Naam?\")       (User Input)\n" +
                "• jabtak (i < 10) { }  (While loop)\n" +
                "• i = 0 se 10 tak { }  (For loop)\n" +
                "• ++x; --x; x++; x--;  (Increment/Decrement)\n\n" +
                "👨‍💻 Lead Developer:\n" +
                "ParmanandKS\n\n" +
                "📄 License:\n" +
                "This is a diploma major project.\n\n" +
                "🙏 Thank you for using SutraLang IDE!";
        
        aboutContent.setText(aboutText);
    }
}
