package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostMaker extends AppCompatActivity {
    private Button addDiagramButton;
    private String postTitle;
    private String postBody;
    private TextInputEditText titleTextInputEditText;
    private TextInputEditText bodyTextInputEditText;
    private Button submitButton;
    private int postID;
    private int diagramID;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String preferences = "projTalkPreferences";
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_maker);
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        submitButton = findViewById(R.id.submitButton);
        titleTextInputEditText = findViewById(R.id.titleTextInputEditText);
        bodyTextInputEditText = findViewById(R.id.bodyTextInputEditText);
        addDiagramButton = findViewById(R.id.addDiagramButton);
        addDiagramButton.setOnClickListener(view -> {
            startDrawingActivity();
        });
        submitButton.setOnClickListener(view -> {
            getPostData();
            savePost();
            //finish();
        });
    }

    public void startDrawingActivity() {
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivity(intent);
    }

    public void getPostData() {
        postTitle = titleTextInputEditText.getText().toString();
        postBody = bodyTextInputEditText.getText().toString();
        String tempDiagramID = sharedpreferences.getString("diagramID", "DEFAULT");
        diagramID = 0;
        if (!tempDiagramID.equals("DEFAULT")) {
            Toast.makeText(getApplicationContext(),"tempDiagramID is " + tempDiagramID, Toast.LENGTH_SHORT).show();
            diagramID = Integer.parseInt(tempDiagramID);
        } else {
            Toast.makeText(getApplicationContext(),"diagram not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void savePost() {
        db.child("posttest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxInt = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        int currentID = Integer.parseInt(child.child("postID").getValue().toString());
                        if (currentID > maxInt) {
                            maxInt = currentID;
                        }
                    }
                }
                postID = maxInt + 1;
                db.child("posttest").child(Integer.toString(postID)).child("postID").setValue(postID);
                db.child("posttest").child(Integer.toString(postID)).child("author").setValue("email goes here");
                db.child("posttest").child(Integer.toString(postID)).child("title").setValue(postTitle);
                db.child("posttest").child(Integer.toString(postID)).child("body").setValue(postBody);
                db.child("posttest").child(Integer.toString(postID)).child("diagramID").setValue(diagramID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Diagram Save Error", error.getMessage());
            }
        });
    }
}