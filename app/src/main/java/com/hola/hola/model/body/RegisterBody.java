package com.hola.hola.model.body;

import com.google.gson.annotations.SerializedName;

public class RegisterBody {

  @SerializedName("phone_number") String phoneNumber;
  int code;
  String name;
  @SerializedName("last_name") String lastName;
  String password;
  @SerializedName("password_confirmation") String passwordConfirmation;
  String email;

  public RegisterBody(String phoneNumber, int code, String name, String lastName, String password,
      String passwordConfirmation, String email){
    this.phoneNumber = phoneNumber;
    this.code = code;
    this.name = name;
    this.lastName = lastName;
    this.password = password;
    this.passwordConfirmation = passwordConfirmation;
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
