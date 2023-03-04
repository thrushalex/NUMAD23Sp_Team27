package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StickerActivity extends AppCompatActivity {

    private static final String TAG = StickerActivity.class.getSimpleName();

    //private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String DB_ADDRESS = "https://cs5520-test-f2993-default-rtdb.firebaseio.com";
    private DatabaseReference db;
    private EditText currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        db.setValue("firebase demo");
        // Update the score in realtime
        db.child("users").addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Toast.makeText(getApplicationContext(), "User added to DB", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                }
        );
    }

    //Get entered username
    public void getUsername(View view) {
        currentUser = findViewById(R.id.enterUsername);
    }

    //Create user
    public void createUser(View view) {
        getUsername(this.findViewById(android.R.id.content).getRootView());
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean tempUserFound = Boolean.FALSE;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (currentUser.getText().toString().equals(user.username)) {
                                tempUserFound = Boolean.TRUE;
                            }
                        }
                        if (tempUserFound) {
                            Toast.makeText(getApplicationContext(), "User already exists, please login", Toast.LENGTH_SHORT).show();
                        } else {
                            User user;
                            user = new User(currentUser.getText().toString());
                            Task t1 = db.child("users").child(user.username).setValue(user);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    //Login user
    public void loginUser(View view) {
        getUsername(this.findViewById(android.R.id.content).getRootView());
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean tempUserFound = Boolean.FALSE;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (currentUser.getText().toString().equals(user.username)) {
                                tempUserFound = Boolean.TRUE;
                            }
                        }
                        if (tempUserFound) {
                            Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                            startSendStickerActivity(StickerActivity.this.findViewById(android.R.id.content).getRootView());
                        } else {
                            Toast.makeText(getApplicationContext(), "User does not exist, please create an account", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void startSendStickerActivity(View view) {
        Intent myIntent = new Intent(this, SendStickerActivity.class);
        myIntent.putExtra("logged_in_username", currentUser.getText().toString());
        startActivity(myIntent);
    }


}