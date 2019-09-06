package com.hola.hola.model.body;

public class BugReportBody {
    String email;
    String text;

    public BugReportBody(String email, String text) {
        this.email = email;
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
