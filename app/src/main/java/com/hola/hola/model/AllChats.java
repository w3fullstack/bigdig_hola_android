package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllChats {
    public AllChats(List<Chat> chats, Integer chatsCount) {
        this.chats = chats;
        this.chatsCount = chatsCount;
    }

    public AllChats() {
    }

    @SerializedName("chats")
    public List<Chat> chats = null;
    @SerializedName("chatsCount")
    public Integer chatsCount;

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public Integer getChatsCount() {
        return chatsCount;
    }

    public void setChatsCount(Integer chatsCount) {
        this.chatsCount = chatsCount;
    }
}
