package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.IUserContact;
import java.io.Serializable;

public class UserContactInputForm implements ValidForm,IUserContact,Serializable {

    private String escalationEmail;
    private String textingAddress;
    private ContactInputForm contact;

    public ContactInputForm getContact() {
        return contact;
    }

    public void setContact(ContactInputForm contact) {
        this.contact = contact;
    }

    public String getEscalationEmail() {
        return escalationEmail;
    }

    public void setEscalationEmail(String escalationEmail) {
        this.escalationEmail = escalationEmail;
    }

    public String getTextingAddress() {
        return textingAddress;
    }

    public void setTextingAddress(String textingAddress) {
        this.textingAddress = textingAddress;
    }

}
