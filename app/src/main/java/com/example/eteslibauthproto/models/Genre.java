package com.example.eteslibauthproto.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Genre implements Parcelable {
    private String name;
    private List<String> bookSearchBooks;
    private List<String> bookSearchCovers;

    public Genre(String name, String book, String cover) {
        this.name = name;
        this.bookSearchBooks = new ArrayList<>();
        this.bookSearchCovers = new ArrayList<>();
        this.bookSearchBooks.add(book);
        this.bookSearchCovers.add(cover);
    }

    protected Genre(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBookSearchBooks() {
        return bookSearchBooks;
    }

    public void setBookSearchBooks(List<String> bookSearchBooks) {
        this.bookSearchBooks = bookSearchBooks;
    }

    public List<String> getBookSearchCovers() {
        return bookSearchCovers;
    }

    public void setBookSearchCovers(List<String> bookSearchCovers) {
        this.bookSearchCovers = bookSearchCovers;
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    public void addBookAndCover(String title, String image) {
        bookSearchBooks.add(title);
        bookSearchCovers.add(image);
    }

    @Override
    public String toString() {
        return "Genre{" +
                "name='" + name + '\'' +
                ", bookSearchBooks=" + bookSearchBooks +
                ", bookSearchCovers=" + bookSearchCovers +
                '}';
    }
}
