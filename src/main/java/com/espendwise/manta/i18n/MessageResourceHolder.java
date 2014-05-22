package com.espendwise.manta.i18n;


import org.springframework.beans.factory.annotation.Autowired;

public class MessageResourceHolder {

    @Autowired
    public MessageResource mesageResource;

    public MessageResource getMessageResource() {
        return mesageResource;
    }
}
