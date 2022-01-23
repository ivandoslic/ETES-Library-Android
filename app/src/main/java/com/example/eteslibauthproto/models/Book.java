package com.example.eteslibauthproto.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class Book implements Parcelable {
    private String id;
    private String title;
    private String authorName;

    // TODO : private Author author;

    private String cover;
    private String description;
    private String genre;
    private String language;
    private String amount;
    private String yearWritten;

    public Book(DocumentSnapshot doc) {
        this.setId((String) doc.getId());
        this.setTitle((String) doc.get("title"));
        this.setAuthorName((String) doc.get("authorName"));
        this.setCover((String) doc.get("cover"));
        this.setDescription((String) doc.get("description"));
        this.setGenre((String) doc.get("genre"));
        this.setLanguage((String) doc.get("language"));
        this.setYearWritten((String) doc.get("yearWritten"));
        this.setAmount((String) doc.get("amount"));
    }

    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        authorName = in.readString();
        cover = in.readString();
        description = in.readString();
        genre = in.readString();
        language = in.readString();
        amount = in.readString();
        yearWritten = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(authorName);
        dest.writeString(cover);
        dest.writeString(description);
        dest.writeString(genre);
        dest.writeString(language);
        dest.writeString(amount);
        dest.writeString(yearWritten);
    }

    // Getters and setters
    public void setId(String id) { this.id = id; }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return cover;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setYearWritten(String yearWritten) {
        this.yearWritten = yearWritten;
    }

    public String getYearWritten() {
        return yearWritten;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return amount == book.amount && Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(authorName, book.authorName) && Objects.equals(cover, book.cover) && Objects.equals(description, book.description) && Objects.equals(genre, book.genre) && Objects.equals(language, book.language) && Objects.equals(yearWritten, book.yearWritten);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authorName, cover, description, genre, language, amount, yearWritten);
    }

    @NonNull
    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", language='" + language + '\'' +
                ", amount=" + amount +
                ", yearWritten='" + yearWritten + '\'' +
                '}';
    }
}
