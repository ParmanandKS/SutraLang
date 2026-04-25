package com.sutralang.ide.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.sutralang.ide.R;

public class OutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        Toolbar toolbar = findViewById(R.id.toolbar_output);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Output");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView outputTextView = findViewById(R.id.output_text);
        String output = getIntent().getStringExtra("OUTPUT_TEXT");
        if (output != null) {
            outputTextView.setText(output);
        }
    }
}
