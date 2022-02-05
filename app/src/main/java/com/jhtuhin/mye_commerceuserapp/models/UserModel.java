package com.jhtuhin.mye_commerceuserapp.models;

public class UserModel {
    private String userName;
    private String userId;
    private String userEmail;
    private String userPhoto;

    public UserModel() {
    }

    public UserModel(String userName, String userId, String userEmail, String userPhoto) {
        this.userName = userName;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
