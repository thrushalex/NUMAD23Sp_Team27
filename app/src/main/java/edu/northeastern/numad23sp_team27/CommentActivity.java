package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String preferences = "projTalkPreferences";
    private DatabaseReference db;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        String postId;
        if(intent.hasExtra("postId")){
            postId = intent.getStringExtra("postId");
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        Log.d("CommentActivity", "On Click Recieved");
        switch (view.getId()) {
            case R.id.comment_button:
                Log.d("CommentActivity", "On Click Recieved - Make new Comment");
                break;
            default:
                Log.d("CommentActivity", "On Click Recieved - Default");
                break;
        }
    }

    public void savePost() {
        db.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
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
                db.child("posts").child(Integer.toString(postID)).child("postID").setValue(postID);
                db.child("posts").child(Integer.toString(postID)).child("author").setValue(sharedpreferences.getString("userEmail", "DEFAULT"));
                db.child("posts").child(Integer.toString(postID)).child("title").setValue(postTitle);
                db.child("posts").child(Integer.toString(postID)).child("body").setValue(postBody);
                db.child("posts").child(Integer.toString(postID)).child("diagramID").setValue(diagramID);
                db.child("posts").child(Integer.toString(postID)).child("dateTime").setValue(java.time.Clock.systemUTC().instant().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Diagram Save Error", error.getMessage());
            }
        });
    }


}