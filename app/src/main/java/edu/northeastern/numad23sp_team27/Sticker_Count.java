package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Sticker_Count extends AppCompatActivity {

    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";

    private String logged_in_username;
    private DatabaseReference db;

    private ArrayList<Integer> stickers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_count);
        logged_in_username = getIntent().getStringExtra("logged_in_username");
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        getStickerIDs(logged_in_username);
    }

    public void getStickerIDs(String username) {

        db.child("users").child(username).child("countOfStickersSent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Process countOfStickersSent into ArrayList
                    stickers = new Utils().convertStringListToList(snapshot.getValue().toString());
                    ArrayList<Integer> stickerCount = getArrayOfCounts(stickers);
                    updateCounts(stickerCount);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Registration Error", error.getMessage());
            }
        });
    }

    public ArrayList<Integer> getArrayOfCounts(ArrayList<Integer> countOfStickers) {
        ArrayList<Integer> countArray = new ArrayList<>();
        Integer size = countOfStickers.size();
        for (int index = 0; index <= size; ++index) {
            Integer currentValue = countOfStickers.get(index);
            if(countArray.get(index) != null) {
                countArray.set(index, currentValue + 1);
            } else {
                countArray.set(index, 1);
            }
        }
        return countArray;
    }

    public void updateCounts(ArrayList<Integer> idCounts) {
        Integer size = idCounts.size();
        for (int index = 0; index <= size; ++index) {
            Integer intCount = idCounts.get(index);
            String stringCount = intCount.toString();
            if (index == 1) {
                TextView TV = findViewById(R.id.sticker1CountTV);
                TV.setText(stringCount);
            } else if (index == 2) {
                TextView TV = findViewById(R.id.sticker2CountTV);
                TV.setText(stringCount);
            } else if (index == 3) {
                TextView TV = findViewById(R.id.sticker3CountTV);
                TV.setText(stringCount);
            } else if (index == 4) {
                TextView TV = findViewById(R.id.sticker4CountTV);
                TV.setText(stringCount);
            }
        }
    }

}