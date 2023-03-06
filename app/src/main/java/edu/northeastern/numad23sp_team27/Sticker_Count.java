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
import java.util.HashMap;
import java.util.Map;

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
                    Map<Integer, Integer> stickerCount = getArrayOfCounts(stickers);
                    //Toast.makeText(getApplicationContext(), "test" + stickerCount.toString(),Toast.LENGTH_SHORT).show();
                    updateCounts(stickerCount);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Registration Error", error.getMessage());
            }
        });
    }

    public Map<Integer, Integer> getArrayOfCounts(ArrayList<Integer> countOfStickers) {
        Map<Integer, Integer> countMap = new HashMap<>();
        Integer size = countOfStickers.size();
        countMap.put(0,0);
        countMap.put(1,0);
        countMap.put(2,0);
        countMap.put(3,0);
        countMap.put(4,0);
        for (int index = 0; index < size; ++index) {
            Integer currentValue = countOfStickers.get(index);
            System.out.println(currentValue);
            Integer mapValue = countMap.get(currentValue);
            countMap.put(currentValue, mapValue + 1);
        }
        return countMap;
    }

    public void updateCounts(Map<Integer, Integer> stickerCounts) {
        Integer size = stickerCounts.size();
        for (int index = 1; index < size; ++index) {
            Integer stickerCount = stickerCounts.get(index);
            String stickerCountString = stickerCount.toString();
            if (index == 1) {
                TextView TV = findViewById(R.id.sticker1CountTV);
                TV.setText(stickerCountString);
            } else if (index == 2) {
                TextView TV = findViewById(R.id.sticker2CountTV);
                TV.setText(stickerCountString);
            } else if (index == 3) {
                TextView TV = findViewById(R.id.sticker3CountTV);
                TV.setText(stickerCountString);
            } else if (index == 4) {
                TextView TV = findViewById(R.id.sticker4CountTV);
                TV.setText(stickerCountString);
            }
        }
    }

}