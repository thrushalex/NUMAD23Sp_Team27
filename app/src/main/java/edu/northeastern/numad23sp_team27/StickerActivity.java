package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


public class StickerActivity extends AppCompatActivity {

    private static final String TAG = StickerActivity.class.getSimpleName();

    //private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String DB_ADDRESS = "https://cs5520-test-f2993-default-rtdb.firebaseio.com";
    private DatabaseReference db;
    private EditText currentUser;

    private Boolean userFound;
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

    public void checkUsername(View view) {
        userFound = false;
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
                        userFound = tempUserFound;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    //Create user
    public void createUser(View view) {
        userFound = false;
        getUsername(this.findViewById(android.R.id.content).getRootView());
        checkUsername(this.findViewById(android.R.id.content).getRootView());
        if (userFound) {
            Toast.makeText(getApplicationContext(), "User already exists, please login", Toast.LENGTH_SHORT).show();
        } else {
            User user;
            user = new User(currentUser.getText().toString());
            Task t1 = db.child("users").child(user.username).setValue(user);
        }
    }

    //Login user
    public void loginUser(View view) {
        userFound = false;
        getUsername(this.findViewById(android.R.id.content).getRootView());
        checkUsername(this.findViewById(android.R.id.content).getRootView());
        if (userFound) {
            Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "User does not exist, please create an account", Toast.LENGTH_SHORT).show();
        }

    }



}