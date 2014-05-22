package com.espendwise.manta.i18n;


import org.springframework.beans.factory.annotation.Autowired;

public class I18nResource {

    private static UserMessageResource userResource;
    private static MessageResource resource;

    public static UserMessageResource getUserResource() {
        return userResource;
    }

    @Autowired
    public  void setUserResource(UserMessageResourceImpl userResource) {
        I18nResource.userResource = userResource;
    }

    public static MessageResource getResource() {
        return resource;
    }

    @Autowired
    public void setResource(MessageResourceImpl resource) {
        I18nResource.resource = resource;
    }
}
