package com.example.eteslibauthproto.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.databinding.FragmentProfileBinding;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.models.Review;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.ui.activities.SettingsActivity;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.example.eteslibauthproto.utils.GlideLoader;
import com.example.eteslibauthproto.utils.localdatabase.AppDataManager;

import java.util.ArrayList;
import java.util.Random;

public class ProfileFragment extends Fragment {

    // private NotificationsViewModel notificationsViewModel;
    private FragmentProfileBinding binding;

    private User currentUser;
    private ImageView profileImageView;
    private ETESLibTextView usernameTextView, reviewOnTextView, reviewTextTextView, numberOfReviewsTextView;
    private RatingBar reviewRatingBar;
    private ArrayList<Review> usersReviews;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FirestoreClass.getUserDetailsFragment(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton settings = binding.profileSettingsButton;
        profileImageView = binding.usersProfProfileImageImage;
        usernameTextView = binding.profileUsername;
        reviewOnTextView = binding.profileReviewOnTV;
        reviewTextTextView = binding.profileReviewTextTV;
        reviewRatingBar = binding.profileReviewRatingBar;
        numberOfReviewsTextView = binding.profileNoOfReviewsTV;

        settings.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void storeUser(User user) {
        currentUser = user;
        initialLoad();
        FirestoreClass.getUsersReviews(this, currentUser);
    }

    private void initialLoad() {
        if(currentUser != null) {
            GlideLoader.loadImageFromURLToFragment(this, currentUser.getImage(), profileImageView);
            usernameTextView.setText(currentUser.getName());
        }
    }

    public void updateUI() {
        if(usersReviews != null && usersReviews.size() > 0) {
            Review randomRev = pickRandomReview();
            FirestoreClass.getBookFromIDForProfile(this, randomRev.getBookId());
            reviewTextTextView.setText(randomRev.getText());
            numberOfReviewsTextView.setText(String.valueOf(usersReviews.size()));
            reviewRatingBar.setRating(randomRev.getRating());
        }
    }

    public void gotBookOfReview(Book book) {
        reviewOnTextView.setText("Review on " + book.getTitle());
    }

    private Review pickRandomReview() {
        Random rand = new Random();
        int randomPosition = rand.nextInt(usersReviews.size());
        return usersReviews.get(randomPosition);
    }

    public void gotUserReviewsSuccessfully(ArrayList<Review> reviewsList) {
        if(usersReviews == null)
            usersReviews = new ArrayList<>();

        usersReviews.addAll(reviewsList);
        updateUI();
    }
}