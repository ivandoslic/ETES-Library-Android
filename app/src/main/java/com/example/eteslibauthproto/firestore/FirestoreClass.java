package com.example.eteslibauthproto.firestore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.eteslibauthproto.activities.EditProfileActivity;
import com.example.eteslibauthproto.activities.LoginActivity;
import com.example.eteslibauthproto.activities.RegisterActivity;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class FirestoreClass {

    private static FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();

    public static void registerUser(RegisterActivity activity, User userInfo) {
        mFirebaseFirestore.collection(Constants.USERS)
                .document(userInfo.getId())
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener(s -> {
                    activity.userRegSuccess();
                })
                .addOnFailureListener(e -> {
                    activity.hideProgressDialog();
                    Log.e("REGISTRATION_ERROR", "Error occurred while registering the user!", e);
                });
    }

    public static String getCurrentUserID() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUserID = "";

        if(currentUser != null)
            currentUserID = currentUser.getUid();

        return currentUserID;
    }

    public static void getUserDetails(Activity activity) {
        mFirebaseFirestore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(doc -> {
                    Log.i(activity.getLocalClassName(), doc.toString());

                    User u = new User(doc);

                    SharedPreferences sharedPreferences = activity.getSharedPreferences(
                            Constants.ETES_LIB_PREFERENCES,
                            Context.MODE_PRIVATE
                    );

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(
                            Constants.LOGGED_IN_USERNAME,
                            u.getName()
                    );
                    editor.apply();

                    if(activity instanceof LoginActivity){
                        ((LoginActivity) activity).userLoggedInSuccess(u);
                    }
                }).addOnFailureListener(err -> {
                    if(activity instanceof LoginActivity){
                        ((LoginActivity) activity).hideProgressDialog();
                    }
                    Log.e(activity.getLocalClassName(), err.getMessage());
        });
    }

    public static void updateUserProfile(Activity a, HashMap<String, Object> userHM){
        mFirebaseFirestore.collection(Constants.USERS).document(getCurrentUserID())
                .update(userHM)
                .addOnSuccessListener(task -> {
                    if(a instanceof EditProfileActivity) {
                        ((EditProfileActivity) a).userProfileUpdateSuccess();
                    }
                }).addOnFailureListener(err -> {
                    if(a instanceof EditProfileActivity) {
                        ((EditProfileActivity) a).hideProgressDialog();
                    }

                    Log.e(a.getLocalClassName(), "Error while updating the user details", err);
                });
    }

    public static void uploadImageToCloud(Activity a, Uri uri) {
        StorageReference reference = FirebaseStorage.getInstance()
                .getReference()
                .child(
                        Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "." + Constants.getFileExtension(a, uri)
                );

        reference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            Log.e("Firebase Image URL", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uriRes -> {
                Log.e("Downloadable Image URL", uriRes.toString());

                if(a instanceof EditProfileActivity){
                    ((EditProfileActivity) a).imageUploadSuccess(uriRes.toString());
                }
            });

        }).addOnFailureListener(err -> {

            if(a instanceof EditProfileActivity){
                ((EditProfileActivity) a).hideProgressDialog();
            }

            Log.e(a.getLocalClassName(), err.getMessage(), err);

        });
    }
}
