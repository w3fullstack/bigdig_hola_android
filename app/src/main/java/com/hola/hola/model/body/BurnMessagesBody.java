package com.hola.hola.model.body;

import com.google.gson.annotations.SerializedName;

public class BurnMessagesBody {
    @SerializedName("message_id") int messageId;
    @SerializedName("after") int after;

    public BurnMessagesBody(int messageId, int after) {
        this.messageId = messageId;
        this.after = after;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }
}
