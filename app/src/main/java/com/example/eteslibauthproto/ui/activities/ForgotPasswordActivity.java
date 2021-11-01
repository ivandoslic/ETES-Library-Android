package com.example.eteslibauthproto.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.utils.ETESLibEditText;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;

public class ForgotPasswordActivity extends BaseActivity {

    private ETESLibEditText emailInputField;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ImageView backButton = (ImageView) findViewById(R.id.backButtonForgot);
        emailInputField = (ETESLibEditText) findViewById(R.id.emailFieldInputForgot);
        ETESLibTextView resetPasswordButton = (ETESLibTextView) findViewById(R.id.passResetButton);

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        resetPasswordButton.setOnClickListener(v -> {
            if(validateMail()){
                showProgressDialog("Sending reset email...");
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    hideProgressDialog();
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this, "Reset email has been successfully sent!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        showErrorSnackBar(String.valueOf(task.getException().getMessage()), true);
                        emailInputField.setText("");
                    }
                });
            }
        });
    }

    private boolean validateMail(){
        email = String.valueOf(emailInputField.getText()).trim();
        Matcher matcher = RegisterActivity.VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if(email.isEmpty()){
            showErrorSnackBar("You need to enter your email address!", true);
            return false;
        }

        if(!matcher.find()){
            showErrorSnackBar("Your email is not in valid format!", true);
            return false;
        }

        return true;
    }
}