package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileSettings extends AppCompatActivity {

    TextView dispNameTV;
    TextView emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        dispNameTV = findViewById(R.id.dispNameTV);
        emailTV = findViewById(R.id.emailTV);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String dName = "Display Name: " + user.getDisplayName();
            String em = "Email: " + user.getEmail();

            dispNameTV.setText(dName);
            emailTV.setText(em);
        }
    }
}