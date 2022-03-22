package com.example.eteslibauthproto.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import java.util.HashMap;

public class EditProfileActivity extends BaseActivity {

    private User mUser;
    private ImageView profileImage;
    private EditText nameField;
    private RadioGroup genderSelection;
    private RadioButton maleRadio, femaleRadio;
    private Spinner yearSelector;

    private Uri selectedImageUri;
    private String userProfileImgURL;
    private Bitmap savedImageCropped;
    private boolean imageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUser = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS);
        }

        // Profile Image
        profileImage = (ImageView) findViewById(R.id.usersEditProfProfileImageImage);
        ImageButton changeProfileButton = (ImageButton) findViewById(R.id.addNewProfileButton);
        changeProfileButton.setOnClickListener(v -> {
            changeProfile();
        });

        // Back Button
        ImageButton backButton = (ImageButton) findViewById(R.id.backButtonEditProf);

        // Gender selection
        genderSelection = (RadioGroup) findViewById(R.id.genderSelection);
        maleRadio = (RadioButton) findViewById(R.id.rbMale);
        femaleRadio = (RadioButton) findViewById(R.id.rbFemale);

        // Year selection drop down
        yearSelector = (Spinner) findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classes, android.R.layout.simple_spinner_dropdown_item);
        yearSelector.setAdapter(adapter);

        // TODO: Do a better implementation of this with action bar that says 'Complete profile'/'Edit profile'

        if(mUser.isProfileCompleted()) {
            backButton.setOnClickListener(v -> {
                onBackPressed();
            });

            GlideLoader gl = new GlideLoader(this);
            gl.loadUserPicture(mUser.getImage(), profileImage);

            if(mUser.getGender().equals(Constants.MALE)){
                maleRadio.setChecked(true);
            } else {
                femaleRadio.setChecked(true);
            }

            switch (mUser.getSchoolingYear()){
                case 2:
                    yearSelector.setSelection(1);
                    break;
                case 3:
                    yearSelector.setSelection(2);
                    break;
                case 4:
                    yearSelector.setSelection(3);
                    break;
                default:
                    yearSelector.setSelection(0);
                    break;
            }

        } else {

            backButton.setOnClickListener(v -> {
                showErrorSnackBar("Please complete your profile when logging first time!", true);
            });

        }

        // Name field
        nameField = (EditText) findViewById(R.id.editProfileNameInput);
        nameField.setText(mUser.getName());

        // UUID Display
        EditText uuidField = (EditText) findViewById(R.id.userUUIDText);
        uuidField.setText(mUser.getId());

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

        Intent i = new Intent(EditProfileActivity.this, SettingsActivity.class);
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
                        Bundle extras = data.getExtras();
                        Bitmap selectedBitMap = extras.getParcelable("data");
                        GlideLoader gLoader = new GlideLoader(this);
                        gLoader.loadUserPicture(selectedBitMap, profileImage);
                        imageChanged = true;
                        savedImageCropped = selectedBitMap;
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

            if(savedImageCropped != null && imageChanged) {
                FirestoreClass.uploadImageToCloud(this, savedImageCropped);
            } else {
                updateProfileDetails();
            }
            // showErrorSnackBar("Your details are valid!", false);

        }
    }

    private void updateProfileDetails() {

        // TODO: IF NOTHING IS CHANGED DON'T MAKE USELESS REQUESTS TO SERVER

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

        if(userProfileImgURL != null && imageChanged) {
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