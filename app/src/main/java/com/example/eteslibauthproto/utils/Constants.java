package com.example.eteslibauthproto.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

public final class Constants {
    private Constants() {}

    public static final String USERS = "users";
    public static final String ETES_LIB_PREFERENCES = "ETESLibPrefs";
    public static final String LOGGED_IN_USERNAME = "logged_in_username";
    public static final String EXTRA_USER_DETAILS = "extra_user_details";

    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String SCHOOLING_YEAR = "schoolingYear";
    public static final String IMAGE = "image";
    public static final String COMPLETED_PROFILE = "profileCompleted";

    public static final String MALE = "male";
    public static final String FEMALE = "female";

    public static final String USER_PROFILE_IMAGE = "User_Profile_Image";

    public static final int READ_STORAGE_PERMISSION_CODE = 2;
    public static final int PICK_IMAGE_REQUEST_CODE = 3;

    public static void showImageChooser(Activity a) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        a.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE);
    }

    public static String getFileExtension(Activity a, Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(a.getContentResolver().getType(uri));
    }

}
