package com.example.eteslibauthproto.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.localdatabase.UsersLocalDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mUser = getUserIfAuthenticated();

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            if(mUser != null) {
                FirestoreClass.getUserDetails(this);
            } else {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 2500);
    }

    private FirebaseUser getUserIfAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void gotUserDataSuccess(User u) {
        Intent i;

        if(u.isProfileCompleted()) {
            i = new Intent(SplashActivity.this, DashboardActivity.class);
        } else {
            i = new Intent(SplashActivity.this, EditProfileActivity.class);
        }

        i.putExtra(Constants.EXTRA_USER_DETAILS, u);

        startActivity(i);
        finish();
    }

    public void cantFetchUserData() {
        Toast.makeText(this, "Can't get user info from server currently", Toast.LENGTH_SHORT).show();

        // try getUserFromLocalDB();

        // if user is found locally start DashboardActivity.class in offline mode...

        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void getUserFromLocalDB() {
        UsersLocalDatabase usersLocalDatabase = new UsersLocalDatabase(this);
        // try to get user locally and return User object...
    }


}