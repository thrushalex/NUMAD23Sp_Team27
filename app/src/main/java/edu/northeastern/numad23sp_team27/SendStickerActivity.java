package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class SendStickerActivity extends AppCompatActivity {
    TextView userPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        SharedPreferences sharedPreferences = getSharedPreferences("application", MODE_PRIVATE);
        String usernm = sharedPreferences.getString("name", "");
    }
}