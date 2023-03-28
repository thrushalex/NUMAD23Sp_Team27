package edu.northeastern.numad23sp_team27;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProjectLogin extends AppCompatActivity {
    private static final String DB_ADDRESS = "https://at-your-service-4ab17-default-rtdb.firebaseio.com";
    FirebaseAuth auth;
    private DatabaseReference db;

    FirebaseUser user;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_login);
        db = FirebaseDatabase.getInstance(DB_ADDRESS).getReference();
        auth = FirebaseAuth.getInstance();
        Button loginUserButton = findViewById(R.id.loginButton);
        Button createUserButton = findViewById(R.id.createAccountButton);
        createUserButton.setOnClickListener(v -> {
            getEmailPassword();
            if (verifyEmail(email) & !password.isEmpty()) {
                Log.i("create", "Attempting to create new user");
                createUser();
            } else {
                loginToastMaker(verifyEmail(email), password.isEmpty());
            }
        });

        loginUserButton.setOnClickListener(v -> {
            getEmailPassword();
            if (verifyEmail(email) & !password.isEmpty()) {
                loginUser();
            } else {
                loginToastMaker(verifyEmail(email), password.isEmpty());
            }
        });
    }

    protected void getEmailPassword() {
        EditText emailEditText = findViewById(R.id.emailEditText);
        email = emailEditText.getText().toString();
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        password = passwordEditText.getText().toString();
    }

    protected boolean verifyEmail(String email){
        String emailPattern = "[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+";
        return email.trim().matches(emailPattern);
    }

    protected void loginToastMaker(boolean emailFormatted, boolean passwordEmpty){
        if (!emailFormatted & passwordEmpty) {
            Toast.makeText(getApplicationContext(), "Invalid email format and missing password", Toast.LENGTH_SHORT).show();
        } else if (!emailFormatted) {
            Toast.makeText(getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
        } else if (passwordEmpty) {
            Toast.makeText(getApplicationContext(), "Missing password", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
        }
    }

    protected void createUser(){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(ProjectLogin.this, task -> {
            if (task.isSuccessful()) {
                user = task.getResult().getUser();
                if (!(user == null)) {
                    Toast.makeText(getApplicationContext(), "user creation successful", Toast.LENGTH_SHORT).show();
                    goToUserDashboard();
                }
            } else {
                Toast.makeText(getApplicationContext(), "user creation unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void loginUser(){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(ProjectLogin.this, task -> {
            if (task.isSuccessful()) {
                user = task.getResult().getUser();
                if (!(user == null)) {
                    Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_SHORT).show();
                    goToUserDashboard();
                }
            } else {
                Toast.makeText(getApplicationContext(), "login unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToUserDashboard() {
        startActivity(new Intent(this, UserDashboard.class));
    }
}