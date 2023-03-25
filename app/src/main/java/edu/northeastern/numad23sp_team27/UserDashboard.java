package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        welcomeTV = findViewById(R.id.welcomeTV);

        String greeting = "Welcome, " + user.getDisplayName();

        welcomeTV.setText(greeting);
    }
}