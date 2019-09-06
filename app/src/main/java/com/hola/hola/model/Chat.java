package com.hola.hola.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Chat {
    public Chat(Integer id, String lastMessageDate, Message lastMessage, Integer memberCount,
                String createdAt, String updatedAt, Integer userId, Integer dialogId,
                List<User> users, List<Other> others) {
        this.id = id;
        this.lastMessageDate = lastMessageDate;
        this.lastMessage = lastMessage;
        this.memberCount = memberCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.dialogId = dialogId;
        this.users = users;
        this.others = others;
    }

    @Override public String toString() {
        return "id= "+id+"\n"+
            "lastMessageDate= "+lastMessageDate+"\n"+
            "lastMessage= "+lastMessage+"\n"+
            "memberCount= "+memberCount+"\n"+
            "createdAt= "+createdAt+"\n"+
            "updatedAt= "+updatedAt+"\n"+
            "userID= "+userId+"\n"+
            "dialogId= "+dialogId+"\n"+
            "users= "+users+"\n"+
            "others= "+others+"\n";
    }

    public String getTitle(){
        if(getOthers().size() == 1) {
            Other recipient = getOthers().get(0);
            return recipient.getFullName();
        } else {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < getOthers().size() -1; ++i){
                String name = getOthers().get(i).getName();
                sb.append(name).append(", ");
            }
            sb.append(getOthers().get(getOthers().size() -1).getFullName());
            return sb.toString();
        }
    }

    public Chat() {
    }

    @SerializedName("id")
    public Integer id;
    @SerializedName("last_message_date")
    public String lastMessageDate;
    @SerializedName("last_message")
    public Message lastMessage;
    @SerializedName("member_count")
    public Integer memberCount;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("user_id")
    public Integer userId;
    @SerializedName("dialog_id")
    public Integer dialogId;
    @SerializedName("users")
    public List<User> users = null;
    @SerializedName("others")
    public List<Other> others = null;
    @SerializedName("picture")
    public String picture;
    @SerializedName("admin_id")
    public int adminId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDialogId() {
        return dialogId;
    }

    public void setDialogId(Integer dialogId) {
        this.dialogId = dialogId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Other> getOthers() {
        return others;
    }

    public void setOthers(List<Other> others) {
        this.others = others;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
