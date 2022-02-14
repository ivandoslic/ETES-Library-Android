package com.example.eteslibauthproto.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.models.Review;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibButton;
import com.example.eteslibauthproto.utils.ETESLibEditText;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.example.eteslibauthproto.utils.GlideLoader;
import com.google.android.material.appbar.MaterialToolbar;

public class CreateReviewActivity extends BaseActivity {

    Book currentBook;
    Review oldReview = null;
    MaterialToolbar topAppBar;
    ETESLibTextView bookTitleTV, bookDescriptionTV;
    ImageView bookCoverImageView;
    ETESLibEditText reviewTextArea;
    RatingBar reviewRatingBar;
    ETESLibButton submitButton;

    float bookRating = 0;
    boolean editingReview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_review);

        currentBook = getIntent().getParcelableExtra(Constants.BOOK_FOR_CREATING_REVIEW);
        oldReview = getIntent().getParcelableExtra(Constants.OLD_USER_REVIEW);

        if(currentBook == null) {
            // TODO: alert user that problem has occurred!
            finish();
        }

        if(oldReview != null)
            editingReview = true;

        topAppBar = (MaterialToolbar) findViewById(R.id.createReviewTopAppBar);
        bookTitleTV = (ETESLibTextView) findViewById(R.id.createReviewBookTitle);
        bookDescriptionTV = (ETESLibTextView) findViewById(R.id.createReviewBookDescription);
        bookCoverImageView = (ImageView) findViewById(R.id.createReviewBookImageView);
        reviewTextArea = (ETESLibEditText) findViewById(R.id.createReviewReviewTextArea);
        reviewRatingBar = (RatingBar) findViewById(R.id.createReviewRatingBar);
        submitButton = (ETESLibButton) findViewById(R.id.createReviewSubmitButton);

        topAppBar.setNavigationOnClickListener(l -> {
            onBackPressed();
        });

        bookTitleTV.setText(currentBook.getTitle());
        bookDescriptionTV.setText(currentBook.getDescription());

        GlideLoader glideLoader = new GlideLoader(this);
        glideLoader.loadUserPicture(currentBook.getCover(), bookCoverImageView);

        reviewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) {
                    bookRating = rating;
                    switch ((int) rating) {
                        case 0:
                            showErrorSnackBar("Ohh! Was it really that bad?", true);
                            break;
                        case 1:
                            showErrorSnackBar("Sorry you didn't like it!", true);
                            break;
                        case 2:
                            showErrorSnackBar("Maybe it was just not your type!", true);
                            break;
                        case 3:
                            showErrorSnackBar("Thanks for your feedback!", false);
                            break;
                        case 4:
                            showErrorSnackBar("Glad you liked it!", false);
                            break;
                        case 5:
                            showErrorSnackBar("Awesome! Can't wait to read your review!", false);
                            break;
                        default:
                            showErrorSnackBar("Oops! Something went wrong...", true);
                    }
                }
            }
        });

        submitButton.setOnClickListener(v -> submitReview());

        if(editingReview) {
            reviewRatingBar.setRating(oldReview.getRating());
            reviewTextArea.setText(oldReview.getText());
        }
    }

    private void submitReview() {
        showProgressDialog("Validating and uploading the review...");

        validateInfo();
    }

    private void validateInfo() {
        String text = String.valueOf(reviewTextArea.getText());

        if(bookRating < 0 || bookRating > 5) {
            reviewFailedPushToServer(Constants.INVALID_RATING_FLOAT);
            return;
        }

        if(text.trim().equals("")) {
            reviewFailedPushToServer(Constants.EMPTY_RATING_TEXT);
            return;
        }

        FirestoreClass.getUserDetails(this);
    }

    public void gotUserSuccessfully(User u) {
        String text = String.valueOf(reviewTextArea.getText());
        Review review = new Review(u, currentBook, text.trim(),bookRating);
        FirestoreClass.createReview(this, review, oldReview);
    }

    public void reviewSuccessfullyPushedToTheServer(Review review) {
        Intent resultIntent = new Intent();

        resultIntent.putExtra("new_review", review);
        setResult(Constants.REVIEW_UPLOAD_SUCCEEDED, resultIntent);
        finish();
        /* finish the activity and when back in BookPreviewActivity alert user that
        * everything went ok, then update the UI to show the new review alongside
        * others...
        */
    }

    public void reviewFailedPushToServer(int constantsErrorCode) {
        switch (constantsErrorCode) {
            case Constants.INVALID_RATING_FLOAT:
                hideProgressDialog();
                showErrorSnackBar("Please try setting your rating again", true);
                bookRating = 0;
                reviewRatingBar.setRating(0);
                break;
            case Constants.EMPTY_RATING_TEXT:
                hideProgressDialog();
                showErrorSnackBar("You need to write something in a review", true);
                break;
            case Constants.REVIEW_UPLOAD_FAILED:
                hideProgressDialog();
                showErrorSnackBar("Oops! Couldn't upload review right now. Try again later", true);
                break;
        }
    }
}