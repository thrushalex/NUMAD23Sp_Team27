package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;


import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{


    //commentId
    private int commentIdInt;

    //commentBody
    private EditText commentBodyEditText;

    //postId
    private int postIdInt;

    private AlertDialog dialog;

    //Database
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String preferences = "projTalkPreferences";
    private DatabaseReference db;

    SharedPreferences sharedpreferences;
    String postID;
    String postTitle;
    String postBody;
    String postDatetime;
    String postAuthor;
    String bodyStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        String postId;
        if(intent.hasExtra("postId")){
            postId = intent.getStringExtra("postId");
            try{
                postIdInt = Integer.parseInt(postId);
            } catch (Exception exception) {
                finish();
            }
        } else {
            finish();
        }
        if(intent.hasExtra("postTitle")){
            postTitle = intent.getStringExtra("postTitle");
        } else {
            finish();
        }
        if(intent.hasExtra("postAuthor")){
            postAuthor = intent.getStringExtra("postAuthor");
        } else {
            finish();
        }
        if(intent.hasExtra("postBody")){
            postBody = intent.getStringExtra("postBody");
        } else {
            finish();
        }
        if(intent.hasExtra("postDatetime")){
            postDatetime = intent.getStringExtra("postDatetime");
        } else {
            finish();
        }

        Button commentBtn = findViewById(R.id.comment_button);
        commentBtn.setOnClickListener(this);

        TextView postTitleText = findViewById(R.id.title_textview);
        TextView postDateText = findViewById(R.id.datetime_textview);
        TextView postAuthorText = findViewById(R.id.author_textview);
        TextView postBodyText = findViewById(R.id.content_textview);

        postTitleText.setText(postTitle);
        postDateText.setText(postDatetime);
        postAuthorText.setText(postAuthor);
        postBodyText.setText(postBody);
    }

    @Override
    public void onClick(View view) {
        Log.d("CommentActivity", "On Click Recieved");
        switch (view.getId()) {
            case R.id.comment_button:
                Log.d("CommentActivity", "On Click Recieved - Make new Comment");
                saveCommentMethod();
                break;
            default:
                Log.d("CommentActivity", "On Click Recieved - Default");
                break;
        }
    }

    public void saveCommentMethod(){
        //LayoutInflater layoutInflater = LayoutInflater.from(this);
        //View view = layoutInflater.inflate(com.neu.numad23sp_blakekoontz.R.layout.add_link, null);
        AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(this);
        alertDialogBuild.setTitle("Comment");
        alertDialogBuild.setMessage("Type your comment to a post");
        commentBodyEditText = new EditText(this);
        commentBodyEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuild.setView(commentBodyEditText);

        alertDialogBuild.setPositiveButton("Ok", ((dialogInterface, i) -> {
            bodyStr = commentBodyEditText.getText().toString();
            db.child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int maxInt = 0;
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            int currentID = Integer.parseInt(child.child("commentId").getValue().toString());
                            if (currentID > maxInt) {
                                maxInt = currentID;
                            }
                        }
                    }
                    commentIdInt = maxInt + 1;

                    db.child("comments").child(Integer.toString(commentIdInt)).child("postID").setValue(postIdInt);
                    db.child("comments").child(Integer.toString(commentIdInt)).child("author").setValue(sharedpreferences.getString("userEmail", "DEFAULT"));
                    db.child("comments").child(Integer.toString(commentIdInt)).child("body").setValue(bodyStr);
                    db.child("comments").child(Integer.toString(commentIdInt)).child("dateTime").setValue(java.time.Clock.systemUTC().instant().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("Diagram Save Error", error.getMessage());
                }
            });
        }));
        alertDialogBuild.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        dialog = alertDialogBuild.create();
        dialog.show();
    }


}