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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        Button profileSetBtn = findViewById(R.id.btnProfileSettings);

        profileSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserProfileSettings();
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

    private void goToUserProfileSettings() {
        startActivity(new Intent(this, UserProfileSettings.class));
    }
}