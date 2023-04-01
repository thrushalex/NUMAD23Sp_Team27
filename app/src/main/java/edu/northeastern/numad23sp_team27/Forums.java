package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Forums extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);
        FloatingActionButton makePostButton = findViewById(R.id.floatingActionButton2);
        makePostButton.setOnClickListener(v -> startPostMakerActivity());
    }

    public void startPostMakerActivity() {
        Intent intent = new Intent(this, PostMaker.class);
        startActivity(intent);
    }

}