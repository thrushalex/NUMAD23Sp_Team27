package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SelectStickerToSend extends AppCompatActivity {

    private Integer selected_sticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sticker_to_send);
        selected_sticker = 0;
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