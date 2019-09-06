package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class PostSendCode {
//
//  public PostSendCode(String phone) {
//    this.phone = phone;
//    this.code = 555;
//  }

  public PostSendCode(String phone, int code) {
    this.phone = phone;
    this.code = code;
  }

  public String phone;
  public int code;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
