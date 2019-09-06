package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    public Login(Boolean success, Integer userId, User user, String centrifugeToken, Integer timestamp) {
        this.success = success;
        this.userId = userId;
        this.user = user;
        this.centrifugeToken = centrifugeToken;
        this.timestamp = timestamp;
    }

    public Login() {
    }

    @SerializedName("success")
    public Boolean success;
    @SerializedName("userId")
    public Integer userId;
    @SerializedName("user")
    public User user;
    @SerializedName("centrifugeToken")
    public String centrifugeToken;
    @SerializedName("timestamp")
    public Integer timestamp;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCentrifugeToken() {
        return centrifugeToken;
    }

    public void setCentrifugeToken(String centrifugeToken) {
        this.centrifugeToken = centrifugeToken;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
