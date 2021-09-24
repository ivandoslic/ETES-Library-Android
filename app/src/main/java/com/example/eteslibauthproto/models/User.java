package com.example.eteslibauthproto.models;

import com.google.firebase.firestore.DocumentSnapshot;

public class User {
    private String id;
    private String name;
    private String email;
    private String image;
    private String gender;
    private int schoolingYear;
    private boolean profileCompleted;

    public User(String id, String name, String email) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setImage("");
        this.setGender("");
        this.setSchoolingYear(0);
        this.setProfileCompleted(false);
    }

    public User(DocumentSnapshot doc) {
        Number schYr = (Number) doc.get("schoolingYear");
        int schYrInt = schYr.intValue();

        this.setId((String) doc.get("id"));
        this.setName((String) doc.get("name"));
        this.setEmail((String) doc.get("email"));
        this.setImage((String) doc.get("image"));
        this.setGender((String) doc.get("gender"));
        this.setSchoolingYear(schYrInt);
        this.setProfileCompleted((boolean) doc.get("profileCompleted"));
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setSchoolingYear(int schoolingYear) {
        this.schoolingYear = schoolingYear;
    }

    public int getSchoolingYear() {
        return schoolingYear;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }
}
