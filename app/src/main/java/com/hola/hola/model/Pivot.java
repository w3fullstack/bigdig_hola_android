package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class Pivot {
    public Pivot(Integer dialogId, Integer userId) {
        this.dialogId = dialogId;
        this.userId = userId;
    }

    public Pivot() {
    }

    @SerializedName("dialogId")
    public Integer dialogId;
    @SerializedName("userId")
    public Integer userId;

    public Integer getDialogId() {
        return dialogId;
    }

    public void setDialogId(Integer dialogId) {
        this.dialogId = dialogId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
