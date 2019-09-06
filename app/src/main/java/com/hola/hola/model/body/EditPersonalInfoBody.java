package com.hola.hola.model.body;

import com.google.gson.annotations.SerializedName;

public class EditPersonalInfoBody {
    String name;
    @SerializedName("last_name")
    String lastName;
    String email;

    public EditPersonalInfoBody(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
