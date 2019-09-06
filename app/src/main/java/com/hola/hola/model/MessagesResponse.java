package com.hola.hola.model;

import com.hola.hola.model.Chat;
import com.hola.hola.model.Message;
import java.util.List;

/**
 * Created by dan on 05.10.18.
 */

public class MessagesResponse {
  Chat chat;
  List<Message> messages;

  public Chat getChat() {
    return chat;
  }

  public void setChat(Chat chat) {
    this.chat = chat;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }
}
