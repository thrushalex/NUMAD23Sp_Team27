package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StickerActivity extends AppCompatActivity {
    // private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String DB_ADDRESS = "https://send-sticker-test-default-rtdb.firebaseio.com/";
    private DatabaseReference db;
    private Button createUserBtn;
    private Button loginBtn;
    private TextView usernameTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        db.setValue("users");

        // references to buttons/textView
        createUserBtn = findViewById(R.id.createUserBtn);
        loginBtn = findViewById(R.id.loginBtn);
        usernameTV = findViewById(R.id.enterUsername);

        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTV.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(StickerActivity.this, "Username field cannot be blank", Toast.LENGTH_SHORT).show();
                } else {
                    createAUser(username);
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameTV.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(StickerActivity.this, "Username field cannot be blank", Toast.LENGTH_SHORT).show();
                } else {
                    login(username);
                }
            }
        });
    }

    public void createAUser(String username) {

        db.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // user exists
                    Toast.makeText(StickerActivity.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(username);
                    db.child("users").child(username).setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Registration Error", error.getMessage());
            }
        });
    }

    public void login(String username) {
        db.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    // user has an account
                    SharedPreferences sharedPreferences = getSharedPreferences("application", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sharedPreferences.edit();

                    ed.putString("name", username);
                    ed.apply();

                    startActivity(new Intent(StickerActivity.this, SendStickerActivity.class));
                } else {
                    Toast.makeText(StickerActivity.this, "No account for this user! Please create an account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Login Error", error.getMessage());
            }
        });
    }
}