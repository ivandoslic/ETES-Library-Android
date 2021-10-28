package com.example.eteslibauthproto.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.GlideLoader;
import com.google.android.material.textfield.TextInputLayout;
import com.google.protobuf.Any;

import java.io.IOException;
import java.util.HashMap;

public class EditProfileActivity extends BaseActivity {

    private User user;
    private ImageView profileImage;
    private EditText nameField;
    private RadioGroup genderSelection;
    private RadioButton maleRadio;
    private Spinner yearSelector;

    private Uri selectedImageUri;
    private String userProfileImgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            user = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS);
        }

        // Profile Image
        profileImage = (ImageView) findViewById(R.id.usersProfileImageImage);
        ImageButton changeProfileButton = (ImageButton) findViewById(R.id.addNewProfileButton);
        changeProfileButton.setOnClickListener(v -> {
            changeProfile();
        });

        // Name field
        nameField = (EditText) findViewById(R.id.editProfileNameInput);
        nameField.setText(user.getName());

        // Gender selection
        genderSelection = (RadioGroup) findViewById(R.id.genderSelection);
        maleRadio = (RadioButton) findViewById(R.id.rbMale);

        // Year selection drop down
        yearSelector = (Spinner) findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classes, android.R.layout.simple_spinner_dropdown_item);
        yearSelector.setAdapter(adapter);

        // UUID Display
        EditText uuidField = (EditText) findViewById(R.id.userUUIDText);
        uuidField.setText(user.getId());

        // Save Changes
        ImageButton saveButton = (ImageButton) findViewById(R.id.confirmChangesEditProfile);
        saveButton.setOnClickListener(v -> {
            saveChanges();
        });
    }

    private void changeProfile() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //showErrorSnackBar("You have a storage permission!", false);
            Constants.showImageChooser(this);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    Constants.READ_STORAGE_PERMISSION_CODE
            );
        }
    }

    public void userProfileUpdateSuccess() {
        hideProgressDialog();
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //showErrorSnackBar("The storage permission is granted.", false);
                Constants.showImageChooser(this);
            } else {
                Toast.makeText(this, getResources().getString(R.string.read_storage_perm_denied), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if(data != null) {
                    try {
                        selectedImageUri = data.getData();
                        // profileImage.setImageURI(selectedImageUri);

                        GlideLoader gLoader = new GlideLoader(this);
                        gLoader.loadUserPicture(selectedImageUri, profileImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, getResources().getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private boolean validateProfileDetails() {
        String name = String.valueOf(nameField.getText()).trim();

        if(name.isEmpty()) {
            showErrorSnackBar("Name can not be empty!", true);
            return false;
        }

        if(genderSelection.getCheckedRadioButtonId() == -1) {
            showErrorSnackBar("Gender can not be empty!", true);
            return false;
        }

        return true;
    }

    private void saveChanges() {
        if(validateProfileDetails()) {
            showProgressDialog("Please wait...");

            if(selectedImageUri != null) {
                FirestoreClass.uploadImageToCloud(this, selectedImageUri);
            } else {
                updateProfileDetails();
            }
            // showErrorSnackBar("Your details are valid!", false);

        }
    }

    private void updateProfileDetails() {

        // TODO: AKO NEMA PROMJENA U USER DETAILS ONDA NE PRAVITI NEPOTREBAN/NEUCINKOVIT REQUEST!

        String name = String.valueOf(nameField.getText()).trim();
        String gender = maleRadio.isChecked() ? Constants.MALE : Constants.FEMALE;
        String yearOS = yearSelector.getSelectedItem().toString();
        int yr;

        switch (yearOS) {
            case "First Year":
                yr = 1;
                break;
            case "Second Year":
                yr = 2;
                break;
            case "Third Year":
                yr = 3;
                break;
            default:
                yr = 4;
                break;
        }

        HashMap<String, Object> userHM = new HashMap<String, Object>();

        userHM.put(Constants.NAME, name);
        userHM.put(Constants.GENDER, gender);
        userHM.put(Constants.SCHOOLING_YEAR, yr);

        userHM.put(Constants.COMPLETED_PROFILE, true);

        if(!(userProfileImgURL.isEmpty())) {
            userHM.put(Constants.IMAGE, userProfileImgURL);
        }

        // showProgressDialog("Please wait...");

        FirestoreClass.updateUserProfile(this, userHM);
    }

    public void imageUploadSuccess(String imageURL) {
        // hideProgressDialog();
        // Toast.makeText(this, "Your image is uploaded successfully! Your image URL is: " + imageURL, Toast.LENGTH_SHORT).show();

        userProfileImgURL = imageURL;
        updateProfileDetails();
    }
}