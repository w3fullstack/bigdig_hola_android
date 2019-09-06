package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
  boolean success;
  @SerializedName("user_id") int userId;
  User user;
  @SerializedName("centrifuge_token") String centifugeToken;
  long timestamp;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getCentifugeToken() {
    return centifugeToken;
  }

  public void setCentifugeToken(String centifugeToken) {
    this.centifugeToken = centifugeToken;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
