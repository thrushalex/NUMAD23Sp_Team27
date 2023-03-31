package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDashboard extends AppCompatActivity {

    FirebaseUser user;
    TextView welcomeTV;
    Button profileSetBtn;
    Button forums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        profileSetBtn = findViewById(R.id.btnProfileSettings);
        forums = findViewById(R.id.btnForums);

        profileSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserProfileSettings();
            }
        });

        forums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForums();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        welcomeTV = findViewById(R.id.welcomeTV);

        if (user != null) {
            // Log.i("user", user.getEmail());

            String greeting = "Welcome, " + user.getEmail();

            welcomeTV.setText(greeting);
        }
    }

    private void goToForums() {
        startActivity(new Intent(this, Forums.class));
    }

    private void goToUserProfileSettings() {
        startActivity(new Intent(this, UserProfileSettings.class));
    }
}