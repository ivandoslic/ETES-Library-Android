package com.example.eteslibauthproto.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibEditText;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    private FirebaseUser mUser;

    private ETESLibEditText emailField, passwordField;
    private String mail, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ETESLibTextView registerTV = (ETESLibTextView) findViewById(R.id.createAccountText);
        ETESLibTextView loginButton = (ETESLibTextView) findViewById(R.id.loginButton);
        ETESLibTextView forgotPassword = (ETESLibTextView) findViewById(R.id.forgotPassword);
        emailField = (ETESLibEditText) findViewById(R.id.emailFieldInputLog);
        passwordField = (ETESLibEditText) findViewById(R.id.editProfileNameInput);

        registerTV.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        loginButton.setOnClickListener(v -> {
            if(verifyInput()){
                showProgressDialog("Logging in...");
                login();
            }
        });

        forgotPassword.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(i);
        });
    }

    private boolean verifyInput(){
        mail = String.valueOf(emailField.getText());
        password = String.valueOf(passwordField.getText());

        if(mail.isEmpty()){
            showErrorSnackBar("You need to enter your email address...", true);
            return false;
        }

        if(password.isEmpty()){
            showErrorSnackBar("You need to enter your password", true);
            return false;
        }

        return true;
    }

    private void login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               FirestoreClass.getUserDetails(LoginActivity.this);
           } else {
                hideProgressDialog();
                showErrorSnackBar(String.valueOf(task.getException().getMessage()), true);
                emailField.setText("");
                passwordField.setText("");
            }
        });
    }

    public void userLoggedInSuccess(User u) {
        hideProgressDialog();

        Intent i;

        if(u.isProfileCompleted()) {
            i = new Intent(LoginActivity.this, DashboardActivity.class);
            i.putExtra(Constants.EXTRA_USER_DETAILS, u);
        } else {
            i = new Intent(LoginActivity.this, EditProfileActivity.class);
            i.putExtra(Constants.EXTRA_USER_DETAILS, u);
        }

        startActivity(i);
        finish();
    }
}