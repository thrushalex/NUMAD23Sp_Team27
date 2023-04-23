package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {
    Canvas canvas;
    Post post;
    String postID;
    String diagramID;

    TextView titleTextView;
    TextView bodyTextView;
    ImageView drawingImageView;
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        post = new Post();
        if (getIntent().getStringExtra("postID") != null) {
            postID = getIntent().getStringExtra("postID");
        }
        populatePost(postID);
        //Toast.makeText(getApplicationContext(), "Size of diagramDrawingCommands = " + diagramDrawingCommands.size(), Toast.LENGTH_SHORT).show();
        titleTextView =findViewById(R.id.title_textview);
        bodyTextView =findViewById(R.id.content_textview);

    }

    public void draw(ArrayList<String> diagramDrawingCommands){
        if (!diagramDrawingCommands.isEmpty()) {
            drawingImageView = findViewById(R.id.imageView);
            canvas = new Canvas(drawingImageView);
            for (String ds: diagramDrawingCommands) {
                String[] d = ds.split(",");
                canvas.drawShape(Integer.parseInt(d[0]), Integer.parseInt(d[1]), d[2], d[3]);
                drawingImageView.invalidate();
            }
        }
    }

    public void populatePost(String postID){
        if (!Objects.equals(postID, "0")) {
            post.setPostId(postID);
            db.child("posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                ArrayList<String> tempArrayList;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            if (Objects.equals(child.getKey(), "body")){
                                post.setPostBody(child.getValue().toString());
                                bodyTextView.setText(post.getPostBody());
                            } else if (Objects.equals(child.getKey(), "title")) {
                                post.setPostTitle(child.getValue().toString());
                                titleTextView.setText(post.getPostTitle());
                            } else if (Objects.equals(child.getKey(), "author")) {
                                post.setPostAuthor(child.getValue().toString());
                            } else if (Objects.equals(child.getKey(), "dateTime")) {
                                post.setPostDatetime(child.getValue().toString());
                            } else if (Objects.equals(child.getKey(), "diagramID")) {
                                diagramID = child.getValue().toString();
                                getDiagram(Integer.parseInt(diagramID));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("Post Retrieval Error", error.getMessage());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Post id invalid", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDiagram(int diagramID) {
        db.child("diagrams").child(Integer.toString(diagramID)).child("diagram").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> drawingCommands;
                drawingCommands = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        drawingCommands.add(child.getValue().toString());
                    }
                    Canvas canvas = new Canvas(findViewById(R.id.post_imageview));
                    for (String ds: drawingCommands) {
                        String[] d = ds.split(",");
                        canvas.drawShape(Integer.parseInt(d[0]), Integer.parseInt(d[1]), d[2], d[3]);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Post Retrieval Error", error.getMessage());
            }
        });
    }
}