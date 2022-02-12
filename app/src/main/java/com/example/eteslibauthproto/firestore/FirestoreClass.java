package com.example.eteslibauthproto.firestore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.models.Review;
import com.example.eteslibauthproto.ui.activities.BookPreviewActivity;
import com.example.eteslibauthproto.ui.activities.CreateReviewActivity;
import com.example.eteslibauthproto.ui.activities.EditProfileActivity;
import com.example.eteslibauthproto.ui.activities.LoginActivity;
import com.example.eteslibauthproto.ui.activities.RegisterActivity;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.ui.activities.SettingsActivity;
import com.example.eteslibauthproto.ui.activities.SplashActivity;
import com.example.eteslibauthproto.ui.fragments.BaseFragment;
import com.example.eteslibauthproto.ui.fragments.HomeFragment;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.localdatabase.AppDataManager;
import com.example.eteslibauthproto.utils.misc.DatesInStringComparator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FirestoreClass {

    private static final FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();

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

    public static void getUserDetailsFragment(Fragment fragment) {
        mFirebaseFirestore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(doc -> {
                    User u = new User(doc);

                    if(fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).storeUser(u);
                    }

                }).addOnFailureListener(err -> {
        });
    }

    public static void getUserDetails(Activity activity) {
        mFirebaseFirestore.collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener(doc -> {
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

                    if(compareLastFetchedTime(activity) || isFirstAccess(activity)) {
                        editor.putString(
                            Constants.USER_LAST_LOGGED_TIME,
                            Calendar.getInstance().getTime().toString()
                        );
                    }

                    editor.apply();

                    if(activity instanceof LoginActivity){
                        ((LoginActivity) activity).userLoggedInSuccess(u);
                    }

                    if(activity instanceof SettingsActivity){
                        ((SettingsActivity) activity).userDetailsSuccess(u);
                    }

                    if(activity instanceof SplashActivity){
                        ((SplashActivity) activity).gotUserDataSuccess(u);
                    }

                    if(activity instanceof CreateReviewActivity){
                        ((CreateReviewActivity) activity).gotUserSuccessfully(u);
                    }

                }).addOnFailureListener(err -> {
                    if(activity instanceof LoginActivity){
                        ((LoginActivity) activity).hideProgressDialog();
                    }

                    if(activity instanceof SettingsActivity) {
                        ((SettingsActivity) activity).hideProgressDialog();
                    }

                    if(activity instanceof SplashActivity){
                        ((SplashActivity) activity).cantFetchUserData();
                    }

                    // Log.e(activity.getLocalClassName(), err.getMessage());
        });
    }

    private static boolean isFirstAccess(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.ETES_LIB_PREFERENCES, Context.MODE_PRIVATE);
        String alreadyLoggedInFirstTime = sharedPreferences.getString(Constants.LOGGED_FIRST_TIME, "false");

        return alreadyLoggedInFirstTime.compareTo("false") == 0;
    }

    private static boolean compareLastFetchedTime(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.ETES_LIB_PREFERENCES, Context.MODE_PRIVATE);
        String lastFetched = sharedPreferences.getString(Constants.USER_LAST_LOGGED_TIME, "There is a problem...");
        String currentTime = Calendar.getInstance().getTime().toString();

        if(lastFetched.compareTo("There is a problem...") == 0) {
            return true;
        }

        return DatesInStringComparator.areDatesTwoHourDifference(currentTime, lastFetched);
    }

    public static void updateUserProfile(Activity a, HashMap<String, Object> userHM){
        mFirebaseFirestore.collection(Constants.USERS).document(getCurrentUserID())
                .update(userHM)
                .addOnSuccessListener(task -> {
                    if(a instanceof EditProfileActivity) {
                        ((EditProfileActivity) a).userProfileUpdateSuccess();
                        setFirstLoggedTrue(a);
                    }
                }).addOnFailureListener(err -> {
                    if(a instanceof EditProfileActivity) {
                        ((EditProfileActivity) a).hideProgressDialog();
                    }

                    Log.e(a.getLocalClassName(), "Error while updating the user details", err);
                });
    }

    private static void setFirstLoggedTrue(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.ETES_LIB_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(
                Constants.LOGGED_FIRST_TIME,
                "true"
        );
        editor.apply();
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

    public static void getTenBooks(AppDataManager appDataManager) {
        Query query = mFirebaseFirestore.collection("books").limit(10);
        ArrayList<DocumentSnapshot> snaps = new ArrayList<>();

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    snaps.add(queryDocumentSnapshots.getDocuments().get(i));
                }

                appDataManager.setLoadedBooks(snaps);

                DocumentSnapshot lastVisible = queryDocumentSnapshots.getDocuments()
                        .get(queryDocumentSnapshots.size() - 1);
            }
        });
    }

    public static void createReview(CreateReviewActivity activity, Review review) {
        mFirebaseFirestore.collection(Constants.REVIEWS)
                .document(review.getReviewId())
                .set(review, SetOptions.merge())
                .addOnSuccessListener(s -> {
                    updateBookAvgRating(review);
                    activity.reviewSuccessfullyPushedToTheServer(review);
                })
                .addOnFailureListener(e -> {
                    activity.reviewFailedPushToServer(Constants.REVIEW_UPLOAD_FAILED);
                });
    }

    private static void updateBookAvgRating(Review review) {

        // TODO: also check if user already reviewed, then remove old rating and add new an then recalculate avg rating

        mFirebaseFirestore.collection("books")
                .document(review.getBookId())
                .get()
                .addOnSuccessListener(s -> {
                   Number avg = (Number) s.get("averageRating");
                   Number noOfReviews = (Number) s.get("numberOfReviews");
                   float avgFloat;
                   int noOfReviewsInt;
                   if(avg == null && noOfReviews == null) {
                       avgFloat = review.getRating();
                       noOfReviewsInt = 1;
                   } else if (avg != null && noOfReviews != null) {
                       avgFloat = avg.floatValue();
                       noOfReviewsInt = noOfReviews.intValue();

                       float sumOfRatings = (avgFloat * noOfReviewsInt) + review.getRating();
                       noOfReviewsInt++;

                       avgFloat = sumOfRatings / noOfReviewsInt;
                   } else {
                       return;
                   }

                   Map<String, Object> ratingData = new HashMap<>();
                   ratingData.put("averageRating", avgFloat);
                   ratingData.put("numberOfReviews", noOfReviewsInt);

                   mFirebaseFirestore.collection("books")
                           .document(s.getId())
                           .set(ratingData, SetOptions.merge())
                           .addOnSuccessListener(task -> {
                               Log.d("UPDATED RATING", "updateBookAvgRating: succeeded");
                           }).addOnFailureListener(error -> {
                               Log.d("UPDATED RATING", "updateBookAvgRating: failed -> " + error);
                   });
                });
    }

    public static void loadReviews(BookPreviewActivity activity, Book bookForReviews) {
        mFirebaseFirestore.collection(Constants.REVIEWS)
                .whereEqualTo("bookId", bookForReviews.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult() != null) {
                                ArrayList<Review> reviewsList = new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    reviewsList.add(new Review(documentSnapshot));
                                }
                                activity.gotReviewsSuccessfully(reviewsList);
                            } else {
                                activity.noReviewsExist();
                            }
                        } else {
                            activity.couldNotGetReviews();
                        }
                    }
                });
    }
}
