package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Forums extends AppCompatActivity implements View.OnClickListener{

    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String preferences = "projTalkPreferences";
    private DatabaseReference db;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    private List<Post> listPost = new ArrayList<>();
    private Button showPost;
    private ArrayList<Integer> postIDs;

    private FloatingActionButton floatingActionAddItem;

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
        floatingActionAddItem = findViewById(R.id.floatingActionButton2);
        floatingActionAddItem.setOnClickListener(this);

        //RecyclerView
        recyclerView = findViewById(R.id.recyclerViewForums);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(listPost);
        recyclerView.setAdapter(adapter);

        getMostRecentPosts();
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

    //Get 10 most recent posts
    public void getMostRecentPosts() {
        db.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        int currentID = Integer.parseInt(child.child("postID").getValue().toString());
                        Post tempPost = new Post();
                        tempPost.setPostId(String.valueOf(currentID));
                        tempPost.setPostTitle(child.child("title").getValue().toString());
                        tempPost.setPostTitle(child.child("body").getValue().toString());
                        //if (postIDs.size() <= 10) {
                        //    listPost.add(0, tempPost);
                        //} else {
                        //    listPost.remove(postIDs.size() - 1);
                        //    listPost.add(0, tempPost);
                        //}
                        listPost.add(tempPost);

                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Post Retrieval Error", error.getMessage());
            }
        });
    }

    /*
    public void toastRecentPostsIDs(){
        for (int i = 0; i < listPost.size(); ++i) {
            Toast.makeText(getApplicationContext(), "Post ID is:" + listPost.get(i).postId, Toast.LENGTH_SHORT).show();
        }
    }

     */

    public void startPostMakerActivity() {
        Intent intent = new Intent(this, PostMaker.class);
        startActivity(intent);
    }

}