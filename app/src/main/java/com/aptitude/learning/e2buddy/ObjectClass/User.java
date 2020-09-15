package com.aptitude.learning.e2buddy.ObjectClass;

public class User {

    private int id;
    private String username, email, schoolCode, adminImage;

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public User(int id, String username, String email, String schoolCode, String adminImage) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.schoolCode = schoolCode;
        this.adminImage = adminImage;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public String getAdminImage() {
        return adminImage;
    }
}
