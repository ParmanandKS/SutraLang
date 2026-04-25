package com.sutralang.ide.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.sutralang.ide.R;
import com.sutralang.ide.data.database.AppDatabase;
import com.sutralang.ide.data.entity.Snippet;
import com.sutralang.ide.engine.SutraEngine;
import com.sutralang.ide.utils.FileUtils;

/**
 * MainActivity - Main code editor activity
 * Features: Code editing, execution, saving, loading, and navigation drawer
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText codeEditor;
    private TextView outputView;
    private Button runButton;
    private Button formatButton;
    private Button saveButton;
    
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SutraEngine engine;
    private AppDatabase database;
    
    private String currentFileName = null;
    private float currentTextSize = 14f; // Default text size in sp
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load theme before super.onCreate
        SharedPreferences sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE);
        boolean isNightMode = sharedPref.getBoolean("night_mode", true); // Default to dark
        AppCompatDelegate.setDefaultNightMode(isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize
        database = AppDatabase.getDatabase(this);
        initializeViews();
        configureToolbar();
        configureNavigationDrawer();
        
        // Handle incoming file content if opened from FileManager
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("fileContent")) {
            String fileName = intent.getStringExtra("fileName");
            String fileContent = intent.getStringExtra("fileContent");
            codeEditor.setText(fileContent);
            currentFileName = fileName;
            Toast.makeText(this, "Loaded: " + fileName, Toast.LENGTH_SHORT).show();
        }

        engine = new SutraEngine();
        engine.setInputProvider(prompt -> {
            // This is tricky because we are on a background/worker thread usually,
            // but SutraEngine currently runs on the UI thread in executeCode().
            // Ideally engine should run on a background thread.
            // For now, let's use a simple synchronous way if possible, or just a placeholder.
            return "10"; // Defaulting to 10 for now as a placeholder
        });
        
        // Set button click listeners
        setupButtonListeners();
    }
    
    /**
     * Initialize UI views
     */
    private void initializeViews() {
        codeEditor = findViewById(R.id.code_editor);
        outputView = findViewById(R.id.output_view);
        runButton = findViewById(R.id.btn_run);
        formatButton = findViewById(R.id.btn_format);
        saveButton = findViewById(R.id.btn_save);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
    }
    
    /**
     * Configure ActionBar toolbar
     */
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    
    /**
     * Configure Navigation Drawer
     */
    private void configureNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar),
                R.string.nav_open, R.string.nav_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        
        navigationView.setNavigationItemSelectedListener(this);
    }
    
    /**
     * Setup button click listeners
     */
    private void setupButtonListeners() {
        // Run button - execute code
        runButton.setOnClickListener(v -> executeCode());

        // Format button - format code
        formatButton.setOnClickListener(v -> formatCode());
        
        // Save button - save code to file and database
        saveButton.setOnClickListener(v -> saveCode());
    }
    
    /**
     * Execute the code in the editor
     */
    private void executeCode() {
        String code = codeEditor.getText().toString().trim();
        
        if (code.isEmpty()) {
            outputView.setText(getString(R.string.msg_no_code_error));
            return;
        }
        
        outputView.setText("Chalu ho raha hai..."); // Running...
        
        engine.setInputProvider(prompt -> {
            final java.util.concurrent.SynchronousQueue<String> queue = new java.util.concurrent.SynchronousQueue<>();
            runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(prompt.isEmpty() ? "Input" : prompt);
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", (dialog, which) -> {
                    new Thread(() -> {
                        try {
                            queue.put(input.getText().toString());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                });
                builder.setCancelable(false);
                builder.show();
            });
            try {
                return queue.take();
            } catch (InterruptedException e) {
                return "";
            }
        });

        new Thread(() -> {
            try {
                // Execute code using SutraEngine
                final String result = engine.run(code);
                runOnUiThread(() -> {
                    outputView.setText(result);
                    // Start OutputActivity with the result
                    Intent intent = new Intent(MainActivity.this, OutputActivity.class);
                    intent.putExtra("OUTPUT_TEXT", result);
                    startActivity(intent);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    String errorMessage = "❌ Error: " + e.getMessage();
                    outputView.setText(errorMessage);
                    // Start OutputActivity even for errors
                    Intent intent = new Intent(MainActivity.this, OutputActivity.class);
                    intent.putExtra("OUTPUT_TEXT", errorMessage);
                    startActivity(intent);
                });
            }
        }).start();
    }
    
    /**
     * Format the code in the editor
     */
    private void formatCode() {
        String code = codeEditor.getText().toString();
        if (code.isEmpty()) return;

        StringBuilder formatted = new StringBuilder();
        String[] lines = code.split("\n");
        int indentLevel = 0;

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) continue;

            if (trimmedLine.startsWith("}")) {
                indentLevel = Math.max(0, indentLevel - 1);
            }

            for (int i = 0; i < indentLevel; i++) {
                formatted.append("    ");
            }

            formatted.append(trimmedLine).append("\n");

            if (trimmedLine.endsWith("{") || (trimmedLine.contains("{") && !trimmedLine.contains("}"))) {
                indentLevel++;
            }
        }

        codeEditor.setText(formatted.toString());
        Toast.makeText(this, "Code Formatted", Toast.LENGTH_SHORT).show();
    }

    /**
     * Save code to a file and database
     */
    private void saveCode() {
        // Show dialog to enter filename
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_save));
        builder.setMessage(getString(R.string.dialog_msg_filename));
        
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(currentFileName != null ? currentFileName.replace(".sutra", "") : "");
        builder.setView(input);
        
        builder.setPositiveButton(getString(R.string.btn_save_dialog), (dialog, which) -> {
            String fileName = input.getText().toString().trim();
            if (!fileName.isEmpty()) {
                String code = codeEditor.getText().toString();
                
                // 1. Save to Physical File
                if (FileUtils.saveFile(MainActivity.this, fileName, code)) {
                    currentFileName = fileName + ".sutra";
                    
                    // 2. Save to Database (Off-thread)
                    new Thread(() -> {
                        Snippet snippet = new Snippet(
                            "default_user",
                            fileName,
                            code,
                            System.currentTimeMillis()
                        );
                        database.snippetDao().insertSnippet(snippet);
                        
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, getString(R.string.msg_file_saved_success, fileName), Toast.LENGTH_SHORT).show();
                        });
                    }).start();

                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.msg_save_failed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.msg_enter_filename), Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton(getString(R.string.btn_cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }
    
    /**
     * Insert code template at cursor position
     */
    private void insertCodeTemplate(String template) {
        int cursorPos = codeEditor.getSelectionStart();
        String code = codeEditor.getText().toString();
        
        String newCode = code.substring(0, cursorPos) + template + code.substring(cursorPos);
        codeEditor.setText(newCode);
        codeEditor.setSelection(cursorPos + template.length());
    }
    
    /**
     * Navigation drawer menu item click handler
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.menu_var) {
            // Insert variable template
            insertCodeTemplate(getString(R.string.template_variable));
        } else if (id == R.id.menu_if) {
            // Insert if template
            insertCodeTemplate(getString(R.string.template_if));
        } else if (id == R.id.menu_else_if) {
            // Insert else if template
            insertCodeTemplate(getString(R.string.template_else_if));
        } else if (id == R.id.menu_else) {
            // Insert else template
            insertCodeTemplate(getString(R.string.template_else));
        } else if (id == R.id.menu_loop) {
            // Insert while loop template
            insertCodeTemplate(getString(R.string.template_while));
        } else if (id == R.id.menu_for) {
            // Insert for loop template
            insertCodeTemplate(getString(R.string.template_for));
        } else if (id == R.id.menu_files) {
            // Open file manager
            startActivity(new Intent(MainActivity.this, FileManagerActivity.class));
        } else if (id == R.id.menu_about) {
            // Open about screen
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        
        drawerLayout.closeDrawer(navigationView);
        return true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        updateThemeIcon(themeItem);
        
        return true;
    }

    private void updateThemeIcon(MenuItem item) {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            item.setIcon(R.drawable.ic_sun);
        } else {
            item.setIcon(R.drawable.ic_moon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_theme) {
            toggleTheme(item);
            return true;
        } else if (id == R.id.action_zoom_in) {
            changeFontSize(2);
            return true;
        } else if (id == R.id.action_zoom_out) {
            changeFontSize(-2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme(MenuItem item) {
        // Animation
        View itemView = findViewById(R.id.action_theme);
        if (itemView != null) {
            RotateAnimation rotate = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(500);
            itemView.startAnimation(rotate);
        }

        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            saveThemePreference(false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            saveThemePreference(true);
        }
    }

    private void saveThemePreference(boolean isNightMode) {
        SharedPreferences sharedPref = getSharedPreferences("theme_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("night_mode", isNightMode);
        editor.apply();
    }

    private void changeFontSize(float delta) {
        currentTextSize += delta;
        if (currentTextSize < 8) currentTextSize = 8;
        if (currentTextSize > 40) currentTextSize = 40;
        
        codeEditor.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSize);
        outputView.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentTextSize);
        Toast.makeText(this, "Font size: " + (int)currentTextSize + "sp", Toast.LENGTH_SHORT).show();
    }
}
