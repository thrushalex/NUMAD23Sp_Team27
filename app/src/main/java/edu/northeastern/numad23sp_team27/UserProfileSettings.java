package edu.northeastern.numad23sp_team27;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserProfileSettings extends AppCompatActivity implements UpdateNameEmailDialog.NameEmailDialogListener, ChangePasswordDialog.ChangePasswordDialogListener {

    TextView dispNameTV;
    TextView emailTV;
    Button updateBtn;
    Button changePBtn;
    Button deleteAcctBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);

        dispNameTV = findViewById(R.id.dispNameTV);
        emailTV = findViewById(R.id.emailTV);
        updateBtn = findViewById(R.id.updateNameEmailBtn);
        changePBtn = findViewById(R.id.changePasswordBtn);
        deleteAcctBtn = findViewById(R.id.deleteAcctBtn);

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

        changePBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });

        deleteAcctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteAccountDialog();
            }
        });
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure? This cannot be undone")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccount();
                        finish();
                        startActivity(new Intent(UserProfileSettings.this, ProjectLogin.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfileSettings.this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                                Log.d("deleteUser", "User account deleted.");
                            }
                        }
                    });
        }
    }

    private void showChangePasswordDialog() {
        DialogFragment newFrag = new ChangePasswordDialog();
        newFrag.show(getSupportFragmentManager(), "changePassword");
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
            if (email.length() < 1) {
                if (displayName.length() > 0) {
                    user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(displayName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfileSettings.this, "User Profile updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        }
                    });
                }
            } else {
                user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("email", "email updated");
                        }
                    }
                });

                if (displayName.length() > 0) {
                    user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(displayName).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfileSettings.this, "User Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                finish();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String newPassword) {
        changePassword(newPassword);
    }

    private void changePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (newPassword.length() < 1) {
                Toast.makeText(this, "New Password cannot be blank", Toast.LENGTH_SHORT).show();
            } else {
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserProfileSettings.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}