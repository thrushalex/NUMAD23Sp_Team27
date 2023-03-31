package edu.northeastern.numad23sp_team27;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileSettings extends AppCompatActivity {

    TextView dispNameTV;
    TextView emailTV;
    Button updateBtn;
    Button changePBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        dispNameTV = findViewById(R.id.dispNameTV);
        emailTV = findViewById(R.id.emailTV);
        updateBtn = findViewById(R.id.updateNameEmailBtn);
        changePBtn = findViewById(R.id.changePasswordBtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String dName = "Display Name: " + user.getDisplayName();
            String em = "Email: " + user.getEmail();

            dispNameTV.setText(dName);
            emailTV.setText(em);
        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialog();
            }
        });
    }

    private void showUpdateDialog() {
        DialogFragment newFrag = new UpdateNameEmailDialog();
        newFrag.show(getSupportFragmentManager(), "updateNameEmail");
    }
}