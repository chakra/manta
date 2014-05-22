package com.espendwise.manta.util;


public class AppResource {

    private DbConstantResource dbConstantsResource;
    private ApplicationSettings applicationSettings;

    public AppResource(ApplicationSettings applicationSettings,DbConstantResource dbConstantsResource) {
        this.dbConstantsResource = dbConstantsResource;
        this.applicationSettings = applicationSettings;
    }

    public  DbConstantResource getDbConstantsResource() {
        return dbConstantsResource;
    }

    public ApplicationSettings getApplicationSettings() {
        return this.applicationSettings;
    }

}
