package com.example.adressgenerator;

import static java.util.Arrays.asList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Account extends AppCompatActivity {

    static ArrayList<String> accountInfo;
    static ArrayAdapter<String> arrayAdapter;
    String name;
    static String email;
    Uri photoUrl;

    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(Account.this, logInActivity.class) );
        }
    }

    public void signOutButton(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Account.this, logInActivity.class) );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //account info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        //listView
        ListView listView = (ListView) findViewById(R.id.accountListView);
        accountInfo = new ArrayList<String>(asList("Name: " + name, "E-mail: " + email));
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountInfo);
        listView.setAdapter(arrayAdapter);

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