package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendStickerActivity extends AppCompatActivity {
    TextView userPage;
    private DatabaseReference db;
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        SharedPreferences sharedPreferences = getSharedPreferences("application", MODE_PRIVATE);
        String usernm = sharedPreferences.getString("name", "");

        userPage = findViewById(R.id.userTextView);
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        userPage.setText(usernm);
        getLastStickerForUser(usernm);
    }

    public void getLastStickerForUser(String username) {
        db.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Process historyOfStickersReceived into ArrayList
                    ArrayList<Integer> stickersReceived = new Utils().convertStringListToList(snapshot.child("historyOfStickersReceived").getValue().toString());
                    //Get last sticker
                    Integer array_length = stickersReceived.size() - 1;
                    Integer stickerID = stickersReceived.get(array_length);
                    setDisplayedSticker(stickerID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Registration Error", error.getMessage());
            }
        });
    }
    public void setDisplayedSticker(Integer stickerID) {
        if (stickerID == 0) {
            ImageView IV = findViewById(R.id.stickerImageView);
            IV.setImageResource(R.drawable.empty_placeholder);
        } else if (stickerID == 1) {
            ImageView IV = findViewById(R.id.stickerImageView);
            IV.setImageResource(R.drawable.sticker1);
        } else if (stickerID == 2) {
            ImageView IV = findViewById(R.id.stickerImageView);
            IV.setImageResource(R.drawable.sticker2);
        } else if (stickerID == 3) {
            ImageView IV = findViewById(R.id.stickerImageView);
            IV.setImageResource(R.drawable.sticker3);
        } else if (stickerID == 4) {
            ImageView IV = findViewById(R.id.stickerImageView);
            IV.setImageResource(R.drawable.sticker4);
        }

    }

    public void startSelectStickerActivity(View view) {
        Intent myIntent = new Intent(SendStickerActivity.this, SelectStickerToSend.class);
        myIntent.putExtra("logged_in_username", userPage.getText().toString());
        SendStickerActivity.this.startActivity(myIntent);
    }
}