package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

public class Document {
    int id;
    String filename;
    @SerializedName("original_name")
    String originalName;
    @SerializedName("message_id") int messageId;
    @SerializedName("user_id") int userId;
    int type;
    int parent;
    @SerializedName("created_at") String createdAt;
    @SerializedName("updated_at") String updatedAt;
    User user;

    public Document(int id, String filename, String originalName, int messageId, int userId, int type, int parent, String createdAt, String updatedAt, User user) {
        this.id = id;
        this.filename = filename;
        this.originalName = originalName;
        this.messageId = messageId;
        this.userId = userId;
        this.type = type;
        this.parent = parent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
