package com.example.eteslibauthproto.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Objects;

public class Author implements Parcelable {
    // TODO: add uid String
    private String name;
    private String movement;
    private String birthday;
    private String nationality;
    private String image;

    public Author(String name, String movement, String birthday, String nationality, String image) {
        this.setName(name);
        this.setMovement(movement);
        this.setBirthday(birthday);
        this.setNationality(nationality);
        this.setImage(image);
    }

    public Author(DocumentSnapshot documentSnapshot) {
        this.setName((String) documentSnapshot.get("name"));
        this.setMovement((String) documentSnapshot.get("movement"));
        this.setBirthday((String) documentSnapshot.get("birthday"));
        this.setNationality((String) documentSnapshot.get("nationality"));
        this.setImage((String) documentSnapshot.get("author_image"));
    }

    public Author(HashMap<String, Object> authorHashmap) {
        if(authorHashmap != null) {
            this.setName((String) authorHashmap.get("name"));
            this.setMovement((String) authorHashmap.get("movement"));
            this.setBirthday((String) authorHashmap.get("birthday"));
            this.setNationality((String) authorHashmap.get("nationality"));
            this.setImage((String) authorHashmap.get("image"));
        }
    }

    protected Author(Parcel in) {
        name = in.readString();
        movement = in.readString();
        birthday = in.readString();
        nationality = in.readString();
        image = in.readString();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return Objects.equals(getName(), author.getName()) && Objects.equals(getMovement(), author.getMovement()) && Objects.equals(getBirthday(), author.getBirthday()) && Objects.equals(getNationality(), author.getNationality()) && Objects.equals(getImage(), author.getImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMovement(), getBirthday(), getNationality(), getImage());
    }

    @NonNull
    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", movement='" + movement + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nationality='" + nationality + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(movement);
        dest.writeString(birthday);
        dest.writeString(nationality);
        dest.writeString(image);
    }
}
