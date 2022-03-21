package com.example.eteslibauthproto.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.models.Review;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.example.eteslibauthproto.utils.GlideLoader;

import java.util.ArrayList;
import java.util.Random;

public class UserProfileActivity extends AppCompatActivity {

    private User currentUser;
    private ImageView profileImageView;
    private ETESLibTextView usernameTextView, reviewOnTextView, reviewTextTextView, numberOfReviewsTextView;
    private CardView reviewCardView;
    private RatingBar reviewRatingBar;
    private ArrayList<Review> usersReviews;
    private Book currentBookOnReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS);

        profileImageView = findViewById(R.id.usersOtherProfProfileImageImage);
        usernameTextView = findViewById(R.id.otherProfileUsername);
        reviewOnTextView = findViewById(R.id.otherProfileReviewOnTV);
        reviewTextTextView = findViewById(R.id.otherProfileReviewTextTV);
        numberOfReviewsTextView = findViewById(R.id.otherProfileNoOfReviewsTV);
        reviewRatingBar = findViewById(R.id.otherProfileReviewRatingBar);
        reviewCardView = findViewById(R.id.otherProfileCardView);

        initialLoad();
    }

    private void initialLoad() {
        if(currentUser != null) {
            Glide.with(this)
                    .load(currentUser.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImageView);
            usernameTextView.setText(currentUser.getName());
            FirestoreClass.getSpecificUserReviews(this, currentUser.getId());
        }
    }

    public void gotUsersReviewsSuccessfully(ArrayList<Review> reviewsList) {
        if(reviewsList.isEmpty())
            return;
        usersReviews = reviewsList;
        Random rand = new Random();
        int randomPosition = rand.nextInt(reviewsList.size());
        Review randomReview = reviewsList.get(randomPosition);
        reviewTextTextView.setText(randomReview.getText());
        numberOfReviewsTextView.setText(String.valueOf(usersReviews.size()));
        reviewRatingBar.setRating(randomReview.getRating());
        FirestoreClass.getUserProfileReviewBook(this, randomReview.getBookId());
    }

    public void gotUserReviewBookSuccessfully(Book tempBook) {
        currentBookOnReview = tempBook;
        reviewOnTextView.setText(tempBook.getTitle());
        reviewCardView.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookPreviewActivity.class);
            intent.putExtra(Constants.BOOK_PREVIEW_INTENT_NAME, tempBook);
            intent.putExtra(Constants.EXTRA_USER_DETAILS, FirestoreClass.getCurrentUserInstance());
            startActivity(intent);
        });
    }
}