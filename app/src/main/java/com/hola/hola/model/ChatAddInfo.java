package com.hola.hola.model;

/**
 * Created by dan on 05.10.18.
 */

public class ChatAddInfo {

  public String created_at;
  public int id;
  public String last_message_date;
  public int member_count;
  public String updated_at;

  public ChatAddInfo(String created_at, int id, String last_message_date, int member_count, String updated_at) {
    this.created_at = created_at;
    this.id = id;
    this.last_message_date = last_message_date;
    this.member_count = member_count;
    this.updated_at = updated_at;
  }

  public String getCreatedAt() {
    return created_at;
  }

  public void setCreatedAt(String created_at) {
    this.created_at = created_at;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLast_message_date() {
    return last_message_date;
  }

  public void setLast_message_date(String last_message_date) {
    this.last_message_date = last_message_date;
  }

  public int getMember_count() {
    return member_count;
  }

  public void setMember_count(int member_count) {
    this.member_count = member_count;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }
}
