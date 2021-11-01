package com.example.eteslibauthproto.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibButton;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.example.eteslibauthproto.utils.GlideLoader;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

// TODO: Enable switching between night and day (dark and light) mode, independent of system default
// TODO: Enable user to turn the notifications on and off
// TODO: Establish EditFavoritesActivity so user can edit his favorite authors
// TODO: Establish DownloadsActivity to allow user to see all PDFs, audio books, etc. that he downloaded

public class SettingsActivity extends BaseActivity {

    private ImageView profileImage;
    private ETESLibTextView usernameTV, emailTV;
    private User mUser;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImage = (ImageView) findViewById(R.id.usersSettingsProfileImageImage);
        usernameTV = (ETESLibTextView) findViewById(R.id.settingsUserNameTV);
        emailTV = (ETESLibTextView) findViewById(R.id.settingsUserEmailTV);

        MaterialToolbar actionBar = (MaterialToolbar) findViewById(R.id.settingsActionBar);
        actionBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        CardView editProfileButton = (CardView) findViewById(R.id.settingsEditProfileButtonCV);
        editProfileButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    editProfileButton.setCardBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.charcoal_50transparent));
                    break;
                case MotionEvent.ACTION_UP:
                    editProfileButton.setCardBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.gunmetal));
                    showErrorSnackBar("Starting edit profile activity", false);
                    Intent i = new Intent(SettingsActivity.this, EditProfileActivity.class);
                    i.putExtra(Constants.EXTRA_USER_DETAILS, mUser);
                    startActivity(i);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    editProfileButton.setCardBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.gunmetal));
                    break;
                default:
                    break;
            }
            return true;
        });

        ETESLibButton logoutButton = (ETESLibButton) findViewById(R.id.settingsLogoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });

    }

    private void getUserDetails() {
        showProgressDialog("Please wait..."); // TODO: Make 'Please wait...' a constant in xml resources
        FirestoreClass.getUserDetails(this);
    }

    public void userDetailsSuccess(User user) {
        mUser = user;

        hideProgressDialog();

        GlideLoader gl = new GlideLoader(this);
        gl.loadUserPicture(user.getImage(), profileImage);

        usernameTV.setText(user.getName());
        emailTV.setText(user.getEmail());
    }

    @Override
    protected void onResume() { // TODO: Look up onResume() method and how it works
        super.onResume();
        getUserDetails();   // TODO: Save users data to local storage, that way we can save the total amount of requests to server
    }
}