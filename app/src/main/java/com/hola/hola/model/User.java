package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class User implements BasicUser{
    public User(Integer id, String phoneNumber, String createdAt, String updatedAt, String name,
                String avatar, Object email, String lastName, Pivot pivot) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.avatar = avatar;
        this.email = email;
        this.lastName = lastName;
        this.pivot = pivot;
    }

    @Override public String toString() {
        return "id= "+id+"\n"+
            "phoneNumber= "+phoneNumber+"\n"+
            "createdAt= "+createdAt+"\n"+
            "updatedAt= "+updatedAt+"\n"+
            "name= "+name+"\n"+
            "avatar= "+avatar+"\n"+
            "email= "+email+"\n"+
            "lastName= "+lastName+"\n"+
            "pivot= "+pivot+"\n";
    }

    public User() {
    }

    @SerializedName("id")
    public Integer id;
    @SerializedName("phone_number")
    public String phoneNumber;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("name")
    public String name;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("email")
    public Object email;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("pivot")
    public Pivot pivot;

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

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    public String getFullName() {
        if (getName() != null && getLastName() != null) {
            return getName() + " " + getLastName();
        } else if (getName() != null) {
            return getName();
        } else if (getLastName() != null) {
            return getLastName();
        } else {
            return "NULL";
        }
    }
}
