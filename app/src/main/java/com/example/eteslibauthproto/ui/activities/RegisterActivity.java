package com.example.eteslibauthproto.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.ETESLibCheckBox;
import com.example.eteslibauthproto.utils.ETESLibEditText;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity {

    private ETESLibEditText mailField, passField, confPassField, nameField;
    private String email, password, confirmPassword, name;
    private ETESLibCheckBox termsAndConditionsCheckbox;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ETESLibTextView loginTV = (ETESLibTextView) findViewById(R.id.alreadyAnUserActionText);
        ETESLibTextView regButton = (ETESLibTextView) findViewById(R.id.registerButton);
        mailField = (ETESLibEditText) findViewById(R.id.emailFieldInputReg);
        passField = (ETESLibEditText) findViewById(R.id.passwordFieldInputReg);
        confPassField = (ETESLibEditText) findViewById(R.id.confirmPasswordFieldInput);
        nameField = (ETESLibEditText) findViewById(R.id.nameFieldInputReg);
        termsAndConditionsCheckbox = (ETESLibCheckBox) findViewById(R.id.termsAndConditionsCheckbox);

        loginTV.setOnClickListener(v -> onBackPressed());

        regButton.setOnClickListener(v -> {
            if(verifyInput()){
                showProgressDialog("Please wait...");
                registerUser();
            }
        });
    }

    private boolean verifyInput() {
        email = String.valueOf(mailField.getText()).trim();
        password = String.valueOf(passField.getText()).trim();
        confirmPassword = String.valueOf(confPassField.getText()).trim();
        name = String.valueOf(nameField.getText()).trim();
        String[] values = new String[]{email, password, confirmPassword, name};

        for(String value: values){
            if(value.isEmpty()){
                showErrorSnackBar("None of the fields should be empty!", true);
                return false;
            }
        }

        if(name.length() < 4) {
            showErrorSnackBar("Your name should contain at least 4 characters!\n" +
                    "e. g. Abe A.", true);
            return false;
        }

        if(!validateMail(email)){
            showErrorSnackBar("Email address not valid!", true);
            return false;
        }

        if(!validatePassword(password)){
            showErrorSnackBar("Invalid password!\n" +
                    "Click the lock in password field for more info!", true);
            return false;
        }

        if(!password.equals(confirmPassword)){
            showErrorSnackBar("Passwords do not match!", true);
            return false;
        }

        if(!termsAndConditionsCheckbox.isChecked()){
            // TODO: Define terms and conditions
            showErrorSnackBar("To create account you need to agree with terms and conditions!", true);
            return false;
        }

        return true;
    }

    public static boolean validateMail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean validatePassword(String passwordString) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(passwordString);
        return matcher.find();
    }

    private void registerUser() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                firebaseUser = task.getResult().getUser();

                User user = new User(firebaseUser.getUid(), name, email);

                // TODO: Find out why the hell did you create the object bellow
                FirestoreClass mFirestoreClass = new FirestoreClass();

                FirestoreClass.registerUser(RegisterActivity.this, user);

            } else {
                hideProgressDialog();
                showErrorSnackBar(String.valueOf(task.getException().getMessage()), true);
            }
        });
    }

    public void userRegSuccess() {
        hideProgressDialog();

        Toast.makeText(RegisterActivity.this, "You are registered successfully!", Toast.LENGTH_SHORT).show();

        // TODO: Redirect user to LoginActivity after registration is successful
    }

}