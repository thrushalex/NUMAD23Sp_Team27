package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private String logged_in_username;
    private DatabaseReference db;

    private EditText usernameET;

    private Button sendSticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sticker_to_send);
        logged_in_username = getIntent().getStringExtra("logged_in_username");
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        usernameET = findViewById(R.id.recipientUsernameEditText);
        sendSticker = findViewById(R.id.sendStickerButton2);
        selected_sticker = 0;

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
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Registration Error", error.getMessage());
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