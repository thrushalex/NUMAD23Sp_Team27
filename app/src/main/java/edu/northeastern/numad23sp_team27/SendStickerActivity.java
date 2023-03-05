package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SendStickerActivity extends AppCompatActivity {
    TextView userPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        SharedPreferences sharedPreferences = getSharedPreferences("application", MODE_PRIVATE);
        String usernm = sharedPreferences.getString("name", "");

        userPage = findViewById(R.id.userTextView);

        userPage.setText(usernm);
    }

    public void startSelectStickerActivity(View view) {
        Intent myIntent = new Intent(SendStickerActivity.this, SelectStickerToSend.class);
        myIntent.putExtra("logged_in_username", userPage.getText().toString());
        SendStickerActivity.this.startActivity(myIntent);
    }
}