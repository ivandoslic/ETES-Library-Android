package com.example.eteslibauthproto.firestore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
}
