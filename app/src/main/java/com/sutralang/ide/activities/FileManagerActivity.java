package com.sutralang.ide.activities;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sutralang.ide.R;
import com.sutralang.ide.adapters.SutraFileAdapter;
import com.sutralang.ide.utils.FileUtils;
import java.util.List;

/**
 * FileManagerActivity - Displays list of saved .sutra files
 * Features: Open, delete, and rename files
 */
public class FileManagerActivity extends AppCompatActivity implements SutraFileAdapter.OnFileClickListener {
    private RecyclerView fileRecyclerView;
    private SutraFileAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        // Initialize RecyclerView
        initializeRecyclerView();
        
        // Load files
        loadFiles();
        
        // Set back button
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    /**
     * Initialize RecyclerView with adapter
     */
    private void initializeRecyclerView() {
        fileRecyclerView = findViewById(R.id.files_recycler_view);
        adapter = new SutraFileAdapter(this);
        
        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileRecyclerView.setAdapter(adapter);
    }
    
    /**
     * Load all .sutra files and display in RecyclerView
     */
    private void loadFiles() {
        List<String> files = FileUtils.listFiles(this);
        adapter.setFileList(files);
        
        if (files.isEmpty()) {
            Toast.makeText(this, "No files saved yet", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Open file in MainActivity editor
     */
    @Override
    public void onFileClick(String fileName) {
        // Read file content
        String content = FileUtils.readFile(this, fileName);
        
        if (content != null) {
            // Pass content to MainActivity
            Intent intent = new Intent(FileManagerActivity.this, MainActivity.class);
            intent.putExtra("fileName", fileName);
            intent.putExtra("fileContent", content);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to load file", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Delete file with confirmation
     */
    @Override
    public void onFileDelete(String fileName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete File");
        builder.setMessage("Are you sure you want to delete " + fileName + "?");
        
        builder.setPositiveButton("Delete", (dialog, which) -> {
            if (FileUtils.deleteFile(FileManagerActivity.this, fileName)) {
                Toast.makeText(FileManagerActivity.this, "✓ File deleted", Toast.LENGTH_SHORT).show();
                loadFiles(); // Reload list
            } else {
                Toast.makeText(FileManagerActivity.this, "✗ Failed to delete file", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    
    /**
     * Rename file
     */
    @Override
    public void onFileRename(String fileName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename File");
        builder.setMessage("Enter new filename (without .sutra):");
        
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        String displayName = fileName.endsWith(".sutra") 
                ? fileName.substring(0, fileName.length() - 6) 
                : fileName;
        input.setText(displayName);
        builder.setView(input);
        
        builder.setPositiveButton("Rename", (dialog, which) -> {
            String newFileName = input.getText().toString().trim();
            if (!newFileName.isEmpty()) {
                if (FileUtils.renameFile(FileManagerActivity.this, fileName, newFileName)) {
                    Toast.makeText(FileManagerActivity.this, "✓ File renamed", Toast.LENGTH_SHORT).show();
                    loadFiles(); // Reload list
                } else {
                    Toast.makeText(FileManagerActivity.this, "✗ Failed to rename file", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(FileManagerActivity.this, "Please enter a filename", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadFiles(); // Refresh files when returning to this activity
    }
}
