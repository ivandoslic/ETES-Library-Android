package com.example.eteslibauthproto.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class Review implements Parcelable {
    private String reviewId;
    private String userId;
    private String bookId;
    private String username;
    private String userImage;
    private String text;
    private float rating;

    public Review(String reviewId, String userId, String bookId, String username, String userImage, String text, int rating) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.bookId = bookId;
        this.username = username;
        this.userImage = userImage;
        this.text = text;
        this.rating = rating;
    }

    public Review(DocumentSnapshot doc) {
        this.reviewId = (String) doc.getId();
        this.userId = (String) doc.get("userId");
        this.bookId = (String) doc.get("bookId");
        this.username = (String) doc.get("username");
        this.userImage = (String) doc.get("userImage");
        this.text = (String) doc.get("text");

        Number numberRating = (Number) doc.get("rating");
        assert numberRating != null;

        this.rating = numberRating.floatValue();
    }

    public Review(User user, Book book, String reviewText, float reviewRating) {
        this.reviewId = user.getId() + "--" + book.getId();
        this.userId = user.getId();
        this.bookId = book.getId();
        this.username = user.getName();
        this.userImage = user.getImage();
        this.text = reviewText;
        this.rating = reviewRating;
    }

    protected Review(Parcel in) {
        reviewId = in.readString();
        userId = in.readString();
        bookId = in.readString();
        username = in.readString();
        userImage = in.readString();
        text = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review review = (Review) o;
        return getRating() == review.getRating() && Objects.equals(getReviewId(), review.getReviewId()) && Objects.equals(getUserId(), review.getUserId()) && Objects.equals(getBookId(), review.getBookId()) && Objects.equals(getUsername(), review.getUsername()) && Objects.equals(getUserImage(), review.getUserImage()) && Objects.equals(getText(), review.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReviewId(), getUserId(), getBookId(), getUsername(), getUserImage(), getText(), getRating());
    }

    @NonNull
    @Override
    public String toString() {
        return "Review{" +
                "reviewId='" + reviewId + '\'' +
                ", userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", username='" + username + '\'' +
                ", userImage='" + userImage + '\'' +
                ", text='" + text + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewId);
        dest.writeString(userId);
        dest.writeString(bookId);
        dest.writeString(username);
        dest.writeString(userImage);
        dest.writeString(text);
        dest.writeFloat(rating);
    }
}
