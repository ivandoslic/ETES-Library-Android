package com.example.eteslibauthproto.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Author;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.models.Review;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibButton;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.example.eteslibauthproto.utils.GlideLoader;
import com.example.eteslibauthproto.utils.adapters.ReviewsVerticalListAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class BookPreviewActivity extends BaseActivity {

    private boolean hasReviewed = false;
    private Review oldReview = null;

    private Book currentBook;
    private User currentUser;
    private Author currentAuthor;
    private ImageView authorImageView, coverImageView;
    private ETESLibTextView bookTitleTextView, authorNameTextView, yearWrittenTextView, languageTextView, genreTextView, bookDescriptionTextView, averageRatingTextView;
    private LinearLayout showReviewsLinearLayout, reviewsLinearLayout;
    private ConstraintLayout scrollViewConstraintLayout;
    private CoordinatorLayout rootLayout;
    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private MaterialToolbar topAppBar;
    private NestedScrollView bookPreviewScrollView;
    private Menu topBarMenu;
    boolean bookSaved = false;
    boolean reviewsExits = false;
    private View.OnClickListener startCreateReviewActivity;
    private ArrayList<Review> currentReviews;
    private RecyclerView reviewsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_preview);

        Intent i = getIntent();
        currentBook = i.getParcelableExtra(Constants.BOOK_PREVIEW_INTENT_NAME);
        // currentUser = i.getParcelableExtra(Constants.EXTRA_USER_DETAILS);
        // for some reason sometimes user object is null and app cannot read his ID so I'll make only one instance
        // of user object for further code so it is public and static so it is accessible through out a whole app
        currentUser = FirestoreClass.getCurrentUserInstance();

        if(currentBook == null || currentUser == null) {
            // TODO: make snack message in Home fragment that error has occurred
            finish();
        }

        startCreateReviewActivity = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookPreviewActivity.this, CreateReviewActivity.class);

                i.putExtra(Constants.BOOK_FOR_CREATING_REVIEW, currentBook);
                i.putExtra(Constants.EXTRA_USER_DETAILS, currentUser);
                i.putExtra(Constants.OLD_USER_REVIEW, oldReview);

                startActivityForResult(i, Constants.CREATE_REVIEW_REQUEST_CODE);
            }
        };

        currentAuthor = currentBook.getAuthor();
        rootLayout = (CoordinatorLayout) findViewById(R.id.bookPreviewRootLayout);
        bookPreviewScrollView = (NestedScrollView) findViewById(R.id.bookPreviewScrollView);
        authorImageView = (ImageView) findViewById(R.id.bookPreviewAuthorImageView);
        coverImageView = (ImageView) findViewById(R.id.bookPreviewBookCoverView);
        bookTitleTextView = (ETESLibTextView) findViewById(R.id.bookPreviewBookTitleTV);
        authorNameTextView = (ETESLibTextView) findViewById(R.id.bookPreviewAuthorNameTV);
        yearWrittenTextView = (ETESLibTextView) findViewById(R.id.bookPreviewYearWrittenTV);
        languageTextView = (ETESLibTextView) findViewById(R.id.bookPreviewLanguageTV);
        genreTextView = (ETESLibTextView) findViewById(R.id.bookPreviewGenreTV);
        bookDescriptionTextView = (ETESLibTextView) findViewById(R.id.bookPreviewDescriptionTV);
        averageRatingTextView = (ETESLibTextView) findViewById(R.id.reviewAverageRatingTV);
        topAppBar = (MaterialToolbar) findViewById(R.id.bookPreviewTopAppBar);
        reviewsLinearLayout = (LinearLayout) findViewById(R.id.bookPreviewReviewsLL);
        showReviewsLinearLayout = (LinearLayout) findViewById(R.id.bookPreviewSeeReviewsLL);
        scrollViewConstraintLayout = (ConstraintLayout) findViewById(R.id.bookPreviewScrollViewConstraintLayout);
        extendedFloatingActionButton = (ExtendedFloatingActionButton) findViewById(R.id.bookPreviewExtendedFAB);

        extendedFloatingActionButton.hide();

        extendedFloatingActionButton.setOnClickListener(this.startCreateReviewActivity);

        topAppBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.button_save) {
                    bookSaved = !bookSaved;
                    item.setIcon(bookSaved ? R.drawable.ic_baseline_bookmark_24 : R.drawable.ic_baseline_bookmark_border_32);
                    if(bookSaved) {
                        showErrorSnackBar("Added to saved books!", false);
                    } else {
                        showErrorSnackBar("Removed from saved books!", true);
                    }
                }
                return false;
            }
        });

        GlideLoader glideLoader = new GlideLoader(this);
        glideLoader.loadUserPicture(currentAuthor.getImage(), authorImageView);
        glideLoader.loadUserPicture(currentBook.getCover(), coverImageView);
        bookTitleTextView.setText(currentBook.getTitle());
        authorNameTextView.setText(currentAuthor.getName());
        yearWrittenTextView.setText("Written: " + currentBook.getYearWritten());
        languageTextView.setText("Language: " + currentBook.getLanguage());
        genreTextView.setText("Genre: " + currentBook.getGenre());
        bookDescriptionTextView.setText(currentBook.getDescription());
        averageRatingTextView.setText(String.valueOf(currentBook.getAverageRating()));

        showReviewsLinearLayout.setOnClickListener(v -> {
            loadReviews();
        });
    }

    private void loadReviews() {
        scrollViewConstraintLayout.removeView(showReviewsLinearLayout);

        FirestoreClass.loadReviews(this, currentBook);

        extendedFloatingActionButton.show();
    }

    private boolean userAlreadyReviewed() {
        if(currentReviews != null && currentReviews.size() > 0) {
            for(Review review : currentReviews) {
                if((review.getUserId()).compareTo(currentUser.getId()) == 0)
                    return true;
            }
        }

        return false;
    }

    private Review getOldReview() {
        for(Review review : currentReviews) {
            if((review.getUserId()).compareTo(currentUser.getId()) == 0)
                return review;
        }

        return null;
    }

    public void gotReviewsSuccessfully(ArrayList<Review> loadedReviewArrayList) {
        if(currentReviews == null) {
            currentReviews = new ArrayList<>();
        }

        currentReviews.addAll(loadedReviewArrayList);

        reviewsRecyclerView = new RecyclerView(this);
        reviewsLinearLayout.addView(reviewsRecyclerView);

        if(userAlreadyReviewed()) {
            extendedFloatingActionButton.setText("EDIT REVIEW");
            extendedFloatingActionButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_review_24));
            this.hasReviewed = true;
            if(oldReview == null)
                oldReview = getOldReview();
        }

        Handler handler = new Handler();

        handler.postDelayed(this::updateUI, 300);

        handler.postDelayed(this::scrollToBottom, 300);
    }

    private void scrollToBottom() {
        bookPreviewScrollView.smoothScrollBy(0, scrollViewConstraintLayout.getHeight(), 500);
    }

    private void updateUI() {

        reviewsRecyclerView.invalidate(); // POTENTIAL ERROR

        if(userAlreadyReviewed()) {
            extendedFloatingActionButton.setText("EDIT REVIEW");
            extendedFloatingActionButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_review_24));
            this.hasReviewed = true;
            if(oldReview == null)
                oldReview = getOldReview();
        } // TODO : Maybe create a function for this

        ReviewsVerticalListAdapter reviewsVerticalListAdapter = new ReviewsVerticalListAdapter(this, currentReviews);

        // adding click listeners

        reviewsRecyclerView.setAdapter(reviewsVerticalListAdapter);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    // TODO LATER :
    /*
    *  - Change that document of review doesn't contain hardcoded username and profile picture but rather
    *    get the user dynamically so that all data is actual
    * */

    public void noReviewsExist() {
        ETESLibTextView sorryMessage = new ETESLibTextView(this);
        sorryMessage.setText("There are no reviews yet! Read the book and be the first one to review it! :D");
        sorryMessage.setHeight(500);
        sorryMessage.setGravity(Gravity.CENTER);
        reviewsLinearLayout.setPadding(35, 40, 35, 0);
        reviewsLinearLayout.addView(sorryMessage);

        ETESLibButton createFirstReviewButton = new ETESLibButton(this);
        createFirstReviewButton.setText("Create first review");
        createFirstReviewButton.setGravity(Gravity.CENTER);
        createFirstReviewButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gunmetal));
        createFirstReviewButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        createFirstReviewButton.setHeight(150);
        createFirstReviewButton.setOnClickListener(this.startCreateReviewActivity);
        reviewsLinearLayout.addView(createFirstReviewButton);
    }

    public void couldNotGetReviews() {
        showErrorSnackBar("Couldn't get reviews from the server", true);
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.CREATE_REVIEW_REQUEST_CODE) {
            if(resultCode == Constants.REVIEW_UPLOAD_SUCCEEDED) {
                reviewUploadedSuccessfully(data != null ? data.getParcelableExtra("new_review") : null);
            } else {
                reviewFailedToUpload();
            }
        }
    }
    */

    private void reviewFailedToUpload() {
        showErrorSnackBar("Error occurred while uploading your review!!", true);
    }

    private void reviewUploadedSuccessfully(Review newReview) {
        showErrorSnackBar("Your review was uploaded successfully!", false);

        if (currentReviews == null) {
            currentReviews = new ArrayList<>(); // Potential fix
        }

        currentReviews.add(newReview);
        updateUI();
    }
}