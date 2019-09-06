package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class UserContact implements BasicUser {
    @SerializedName("id") public Integer id;
    @SerializedName("phone_number") public String phoneNumber;
    @SerializedName("created_at") public String createdAt;
    @SerializedName("updated_at") public String updatedAt;
    @SerializedName("name") public String name;
    @SerializedName("avatar") public String avatar;
    @SerializedName("email") public Object email;
    @SerializedName("last_name") public String lastName;
    @SerializedName("first_letter") public String firstLetter;
    @SerializedName("settings") public UserSettings settings;

    public UserContact(Integer id, String phoneNumber, String createdAt,
                       String updatedAt, String name, String avatar, Object email,
                       String lastName, String firstLetter, UserSettings settings) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.avatar = avatar;
        this.email = email;
        this.lastName = lastName;
        this.firstLetter = firstLetter;
        this.settings = settings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    public String getFullName() {
        if (getName() != null && getLastName() != null) {
            return getName() + " " + getLastName();
        } else if (getName() != null) {
            return getName();
        } else if (getLastName() != null) {
            return getLastName().toString();
        } else {
            return "NULL";
        }
    }
}
