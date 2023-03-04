package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SendStickerActivity extends AppCompatActivity {

    String logged_in_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        logged_in_username = getIntent().getStringExtra("logged_in_username");
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(logged_in_username);
    }
}