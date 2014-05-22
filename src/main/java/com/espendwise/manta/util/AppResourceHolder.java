package com.espendwise.manta.util;


import org.springframework.beans.factory.annotation.Autowired;

public class AppResourceHolder {

    private static AppResource appResource;

    public static AppResource getAppResource() {
        return appResource;
    }

    @Autowired
    public void setAppResource(AppResource appResource) {
        AppResourceHolder.appResource = appResource;
    }
}
