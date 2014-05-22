package com.espendwise.ocean.common.emails;


import java.io.Serializable;

public class EmailValue implements Serializable {

    private String subject;
    private String text;

    public EmailValue() {
    }

    public EmailValue(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
