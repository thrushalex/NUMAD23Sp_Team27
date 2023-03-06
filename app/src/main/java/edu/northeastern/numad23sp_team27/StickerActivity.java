package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;

public class StickerActivity extends AppCompatActivity {
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private DatabaseReference db;
    private Button createUserBtn;

    private String CHANNEL_ID;
    private Button loginBtn;
    private TextView usernameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        //db.setValue("users");

        // references to buttons/textView
        createUserBtn = findViewById(R.id.createUserBtn);
        loginBtn = findViewById(R.id.loginBtn);
        usernameTV = findViewById(R.id.enterUsername);
        CHANNEL_ID = "channel ID";
        /*createNotificationBuilder(1, "test");*/

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
                    setNotificationListener(username);
                    login(username);
                }
            }
        });
    }

    public NotificationCompat.Builder createNotificationBuilder(Integer stickerID) {
        if (stickerID == 1) {
            return new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("New sticker!")
                    .setSmallIcon(R.drawable.sticker1)
                    .setContentText("Test text")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
        } else if (stickerID == 2) {
            return new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("New sticker!")
                    .setSmallIcon(R.drawable.sticker2)
                    .setContentText("Test text")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
        } else if (stickerID == 3) {
            return new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("New sticker!")
                    .setSmallIcon(R.drawable.sticker3)
                    .setContentText("Test text")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
        } else if (stickerID == 4) {
            return new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("New sticker!")
                    .setSmallIcon(R.drawable.sticker4)
                    .setContentText("Test text")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
        } else {
            return new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("New sticker!")
                    .setSmallIcon(R.drawable.empty_placeholder)
                    .setContentText("Test text")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
        }
    }

    public void sendNotification(Integer stickerID) {
        NotificationCompat.Builder builder = createNotificationBuilder(stickerID);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(0, builder.build());
    }

    public void setNotificationListener(String username) {
        db.child("users").child(username).child("historyOfStickersReceived").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Process historyOfStickersReceived into ArrayList
                    ArrayList<Integer> stickers = new Utils().convertStringListToList(snapshot.getValue().toString());
                    if (stickers.size() >0 ) {
                        Integer sticker = stickers.get(stickers.size() - 1);
                        sendNotification(sticker);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Registration Error", error.getMessage());
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