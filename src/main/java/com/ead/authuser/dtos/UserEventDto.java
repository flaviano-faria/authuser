package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.UUID;


public class UserEventDto {

    private UUID userId;
    private String userName;
    private String email;
    private String fullName;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String imageUrl;
    private String actionType;

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getActionType() {
        return actionType;
    }

}
