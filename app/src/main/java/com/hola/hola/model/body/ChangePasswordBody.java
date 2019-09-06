package com.hola.hola.model.body;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordBody {
    @SerializedName("old_password") String oldPassword;
    @SerializedName("new_password") String newPassword;
    @SerializedName("new_password_confirmation") String confirmPassword;

    public ChangePasswordBody(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
