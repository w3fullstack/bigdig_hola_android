package com.hola.hola.model.body;

public class LoginBody {
  String phone;
  String password;

  public LoginBody(String phone, String password){
    this.phone = phone;
    this.password = password;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "{phone=" + phone + "; password="+password+"}";
  }
}
