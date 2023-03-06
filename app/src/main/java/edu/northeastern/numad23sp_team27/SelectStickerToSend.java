package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SelectStickerToSend extends AppCompatActivity {

    private Integer selected_sticker;
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private static final String CHANNEL_ID = "notification_channel";
    private String logged_in_username;
    private NotificationManagerCompat notificationManager;
    private DatabaseReference db;

    private EditText usernameET;
    private EditText recipient;
    private Button sendSticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sticker_to_send);
        logged_in_username = getIntent().getStringExtra("logged_in_username");
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        usernameET = findViewById(R.id.recipientUsernameEditText);
        recipient = findViewById(R.id.recipientUsernameEditText);
        sendSticker = findViewById(R.id.sendStickerButton2);
        selected_sticker = 0;

        // create notification channel
        createNotificationChannel();
        // notification manager
        notificationManager = NotificationManagerCompat.from(this);
        sendSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameET.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Username field cannot be blank", Toast.LENGTH_SHORT).show();
                } else {
                    sendSticker(username);
                    receiveSticker(logged_in_username);
                }
            }
        });

    }

    public void sendSticker(String username) {

        db.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // for recipient
                    // user exists
                    Toast.makeText(getApplicationContext(), "User found", Toast.LENGTH_SHORT).show();
                    //Process countOfStickersSent into ArrayList
                    ArrayList<Integer> stickersSent = convertStringListToList(snapshot.child("countOfStickersSent").getValue().toString());
                    //Process historyOfStickersReceived into ArrayList
                    ArrayList<Integer> stickersReceived = convertStringListToList(snapshot.child("historyOfStickersReceived").getValue().toString());
                    //Add sticker received
                    stickersReceived.add(selected_sticker);
                    User updatedRecipientUser = new User(username, stickersSent, stickersReceived);
                    db.child("users").child(username).setValue(updatedRecipientUser);
                    sendNotification(recipient.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Registration Error", error.getMessage());
            }
        });
    }

    public void sendNotification(String recipient) {
        db.child("users").child(recipient).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // show notification
                NotificationCompat.Builder builder = createNotificationBuilder(selected_sticker, logged_in_username);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManager.notify(0, builder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void receiveSticker(String username) {

        db.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // for sender
                    // user exists
                    Toast.makeText(getApplicationContext(), "User found", Toast.LENGTH_SHORT).show();
                    //Process countOfStickersSent into ArrayList
                    ArrayList<Integer> stickersSent = convertStringListToList(snapshot.child("countOfStickersSent").getValue().toString());
                    //Add sticker sent
                    stickersSent.add(selected_sticker);
                    //Process historyOfStickersReceived into ArrayList
                    ArrayList<Integer> stickersReceived = convertStringListToList(snapshot.child("historyOfStickersReceived").getValue().toString());
                    User updatedRecipientUser = new User(username, stickersSent, stickersReceived);
                    db.child("users").child(username).setValue(updatedRecipientUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Registration Error", error.getMessage());
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "notification Channel", importance);
            channel.setDescription("sticker received notification");
            channel.enableVibration(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public int getSticker(int i) {
        if (i == 1)
            return R.drawable.sticker1;
        else if (i == 2)
            return R.drawable.sticker2;
        else if (i == 3)
            return R.drawable.sticker3;
        else if (i == 4)
            return R.drawable.sticker4;
        return -1;
    }
    public NotificationCompat.Builder createNotificationBuilder(int selected_sticker, String sender) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("You Received a New Sticker!")
                .setSmallIcon(getSticker(selected_sticker))
                .setContentText("You have a new sticker from " + sender)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), getSticker(selected_sticker)))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        return builder;
    }

    public void onClick(View view) {
        int imageViewID = view.getId();
        if (imageViewID == R.id.sticker1ImageView) {
            selected_sticker = 1;
        } else if (imageViewID == R.id.sticker2ImageView) {
            selected_sticker = 2;
        } else if (imageViewID == R.id.sticker3ImageView) {
            selected_sticker = 3;
        } else if (imageViewID == R.id.sticker4ImageView) {
            selected_sticker = 4;
        }
    }

    public void sendSticker(View view) {
        if (selected_sticker == 0) {
            Toast.makeText(getApplicationContext(), "No emoji selected", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList convertStringListToList(String stringFormList) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        stringFormList = stringFormList.replace("[","");
        stringFormList = stringFormList.replace("]","");
        stringFormList = stringFormList.replace(" ","");
        ArrayList<String> sentStringList = new ArrayList<>(Arrays.asList(stringFormList.split(",")));
        sentStringList.forEach((n) -> arrayList.add(Integer.parseInt(n)));
        return arrayList;
    }
}