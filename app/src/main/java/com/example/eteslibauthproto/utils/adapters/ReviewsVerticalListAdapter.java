package com.example.eteslibauthproto.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.models.Review;
import com.example.eteslibauthproto.utils.ETESLibTextView;

import java.util.ArrayList;

public class ReviewsVerticalListAdapter extends RecyclerView.Adapter<ReviewsVerticalListAdapter.ViewHolder>{

    private ArrayList<Review> reviews;
    private LayoutInflater layoutInflater;
    private Context context;
    private AdapterView.OnItemClickListener itemClickListener;

    public ReviewsVerticalListAdapter(Context context, ArrayList<Review> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.reviews = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.review_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review currentReview = reviews.get(position);

        Glide.with(context)
                .load(currentReview.getUserImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.userProfileImageView);

        holder.reviewTextTextView.setText(currentReview.getText());
        holder.ratingValueTextView.setText(String.valueOf(currentReview.getRating()));
        holder.usernameTextView.setText(currentReview.getUsername());

        holder.reviewRatingBar.setRating(currentReview.getRating());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView userProfileImageView, optionsMenuImageView;
        ETESLibTextView usernameTextView, ratingValueTextView, reviewTextTextView;
        RatingBar reviewRatingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileImageView = itemView.findViewById(R.id.reviewUserProfileImage);
            optionsMenuImageView = itemView.findViewById(R.id.reviewDropdownOptionButton);
            usernameTextView = itemView.findViewById(R.id.reviewUsernameTV);
            ratingValueTextView = itemView.findViewById(R.id.reviewRatingValueTV);
            reviewTextTextView = itemView.findViewById(R.id.reviewReviewTextTV);
            reviewRatingBar = itemView.findViewById(R.id.reviewRatingBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            listener.onItemClick(itemView, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
