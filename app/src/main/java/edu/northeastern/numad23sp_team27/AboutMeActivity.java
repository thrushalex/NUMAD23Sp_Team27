package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class AboutMeActivity extends AppCompatActivity {

    final static String TEAM_KEY = "team";
    final static String MEMBER1_KEY = "member1";
    final static String MEMBER2_KEY = "member2";
    final static String MEMBER3_KEY = "member3";
    final static String MEMBER4_KEY = "member4";
    TextView teamTextView;
    TextView member1Text, member2Text, member3Text, member4Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        teamTextView = findViewById(R.id.teamNameText);
        member1Text = findViewById(R.id.memberText1);
        member2Text = findViewById(R.id.memberText2);
        member3Text = findViewById(R.id.memberText3);
        member4Text = findViewById(R.id.memberText4);

        if (savedInstanceState != null) {
            teamTextView.setText(savedInstanceState.getString(TEAM_KEY));
            member1Text.setText(savedInstanceState.getString(MEMBER1_KEY));
            member2Text.setText(savedInstanceState.getString(MEMBER2_KEY));
            member3Text.setText(savedInstanceState.getString(MEMBER3_KEY));
            member4Text.setText(savedInstanceState.getString(MEMBER4_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data before orientation change
        outState.putString(TEAM_KEY, teamTextView.getText().toString());
        outState.putString(MEMBER1_KEY, member1Text.getText().toString());
        outState.putString(MEMBER2_KEY, member2Text.getText().toString());
        outState.putString(MEMBER3_KEY, member3Text.getText().toString());
        outState.putString(MEMBER4_KEY, member4Text.getText().toString());
    }
}