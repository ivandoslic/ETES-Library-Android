package com.example.eteslibauthproto.models;

import androidx.annotation.NonNull;

public class BookSearchItem {
    private String genre;
    private String image;
    private String title;
    private String uid;

    public BookSearchItem(String genre, String image, String title, String uid) {
        this.genre = genre;
        this.image = image;
        this.title = title;
        this.uid = uid;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public String toString() {
        return "BookSearchItem{" +
                "genre='" + genre + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
