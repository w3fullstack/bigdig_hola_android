package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class Registration {
    public Registration(String phone_number, String code, String name, String last_name, String password, String password_confirmation, String email) {
        this.phone_number = phone_number;
        this.code = code;
        this.name = name;
        this.last_name = last_name;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.email = email;
    }

    @SerializedName("phone_number")
    public String phone_number;
    @SerializedName("code")
    public String code;
    @SerializedName("name")
    public String name;
    @SerializedName("last_name")
    public String last_name;
    @SerializedName("password")
    public String password;
    @SerializedName("password_confirmation")
    public String password_confirmation;
    @SerializedName("email")
    public String email;
}
