package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class PostMaker extends AppCompatActivity {
    private Button addDiagramButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_maker);
        addDiagramButton = findViewById(R.id.addDiagramButton);
        addDiagramButton.setOnClickListener(view -> {
            startDrawingActivity();
        });
    }

    public void startDrawingActivity() {
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivity(intent);
    }
}