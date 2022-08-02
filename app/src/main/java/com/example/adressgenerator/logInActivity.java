package com.example.adressgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class logInActivity extends AppCompatActivity implements View.OnClickListener {

    boolean loginProgress = true;
    TextView changeLogInModeTextView;
    //Firebase account
    private FirebaseAuth mAuth;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.changeLogInModetextView){

            Button loginButton = (Button) findViewById(R.id.logInbutton);

            if (loginProgress){
                loginProgress = false;
                loginButton.setText("Register");
                changeLogInModeTextView.setText("Or, Login");
            }
            else{
                loginProgress = true;
                loginButton.setText("Login");
                changeLogInModeTextView.setText("Or, Register");
            }
        }
    }

    public void logInButton(View view){
        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        String email =emailEditText.getText().toString();
        String password =passwordEditText.getText().toString();

        if (loginProgress == true) {
            signIn(email, password);
        }
        else{
            createAccount(email, password);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Change textField
        changeLogInModeTextView = (TextView) findViewById(R.id.changeLogInModetextView);
        changeLogInModeTextView.setOnClickListener(this);

        //Firebase account
        mAuth = FirebaseAuth.getInstance();

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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    public void createAccount(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("success", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Log.w("fail", "Authentication failed", task.getException());
                    Toast.makeText(logInActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

            }
        });
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("Success", "SignInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Log.d("Failed", "Authentication failed");
                    Toast.makeText(logInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
        // [END send_email_verification]
    }


    private void updateUI(FirebaseUser user) {
    }

    private void reload() {
    }

}