package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserProfileSettings extends AppCompatActivity implements UpdateNameEmailDialog.NameEmailDialogListener {

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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String displayName, String email) {
        updateNameEmail(displayName, email);
    }

    private void updateNameEmail(String displayName, String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String em = email == null ? user.getEmail() : email;
            assert em != null;

            String dName = displayName == null ? user.getDisplayName() : displayName;

            user.updateEmail(em);
            user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(dName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserProfileSettings.this, "User Profile updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}