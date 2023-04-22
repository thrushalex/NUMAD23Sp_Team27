package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Forums extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    private List<Post> listLink;

    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String preferences = "projTalkPreferences";
    private DatabaseReference db;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);

        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);

        //sense orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        //Floating action button
        FloatingActionButton makePostButton = findViewById(R.id.floatingActionButton2);
        makePostButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("MainActivity", "On Click Recieved");
        switch (view.getId()) {
            case R.id.floatingActionButton2:
                Log.d("ForumsActivity", "On Click Recieved - Make new Post");
                startPostMakerActivity();
                break;
            default:
                Log.d("ForumsActivity", "On Click Recieved - Default");
                break;
        }

    }


    public void startPostMakerActivity() {
        Intent intent = new Intent(this, PostMaker.class);
        startActivity(intent);
    }

}