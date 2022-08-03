package com.example.adressgenerator;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditAccount extends AppCompatActivity {

    private static final String TAG = "Edit Account";

    //save changes buttons
    public void saveUsernameButton(View view) {
        EditText editUsername = (EditText) findViewById(R.id.usernameEditText);
        String newUsername = editUsername.getText().toString();
        updateUsername(newUsername);
    }

    public void saveEmailButton(View view){
        EditText editEmail = (EditText) findViewById(R.id.emailEditText);
        String newEmail = editEmail.getText().toString();
        updateEmail(newEmail);
    }

    public void savePasswordButton(View view){
        EditText editPassword = (EditText) findViewById(R.id.passwordEditText);
        String newPassword = editPassword.getText().toString();
        updatePassword(newPassword);
    }

    public void deleteAccountButton(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
    }

    public void cancelButton(View view) {
        startActivity(new Intent(EditAccount.this, Account.class));
    }


    //edit username and profile pic
    public void updateUsername(String username) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Username updated.");
                            Toast.makeText(EditAccount.this, "Username updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //edit e mail
    public void updateEmail(String email) {
        // [START update_email]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            Toast.makeText(EditAccount.this, "E-mail updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //edit password
    public void updatePassword(String password) {
        // [START update_password]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = password;

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                            Toast.makeText(EditAccount.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        //get user info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            TextView usernameTextView = (TextView) findViewById(R.id.usernameTextView);
            TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
            usernameTextView.setText(name);
            emailTextView.setText(email);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }

        //bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.logInActivity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.listAddresses:
                        startActivity(new Intent(getApplicationContext(), ListAddresses.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.logInActivity:
                        return true;
                }
                return false;
            }
        });
    }
}