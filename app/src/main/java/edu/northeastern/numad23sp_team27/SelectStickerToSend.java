package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;

public class SelectStickerToSend extends AppCompatActivity {

    private Integer selected_sticker;
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    private DatabaseReference db;

    private EditText usernameET;

    private Button sendSticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sticker_to_send);
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
                }
            }
        });
    }

    public void sendSticker(String username) {

        db.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // user exists
                    Toast.makeText(getApplicationContext(), "User found", Toast.LENGTH_SHORT).show();
                    // get recipient user
                    User recipientUser = snapshot.getValue(User.class);
                    // add sticker to array
                    recipientUser.addStickerToArray(selected_sticker);
                    // update entry in DB
                    db.child("users").child(username).setValue(recipientUser);
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
}