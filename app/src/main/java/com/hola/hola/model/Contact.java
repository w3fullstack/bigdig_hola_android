package com.hola.hola.model;

import android.net.Uri;

public class Contact {
    String name;
    String phoneNumber;
    Uri imageUri;

    public Contact(String name, String phoneNumber, Uri imageUri) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imageUri=" + imageUri +
                '}';
    }
}
