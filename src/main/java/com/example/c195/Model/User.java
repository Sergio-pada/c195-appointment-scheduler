package com.example.c195.Model;

public class User {
    private int userId;
    private String userName;
    private String password;

    // Constructors
    public User(int userID, String userName, String password) {
        this.userId = userID;
        this.userName = userName;
        this.password = password;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
