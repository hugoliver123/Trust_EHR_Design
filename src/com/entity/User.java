package com.entity;

/*
*  User 实体类
*/
public class User {
    private Integer userId; //uid
    private String userName;
    private String userPass;
    private String userPosition; // 用户身份

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPass;
    }

    public void setUserPassword(String userPassword) {
        this.userPass = userPassword;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }
}
