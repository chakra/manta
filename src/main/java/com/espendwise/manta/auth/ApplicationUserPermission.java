package com.espendwise.manta.auth;


import java.io.Serializable;
import java.util.Set;

public class ApplicationUserPermission implements Serializable {

    private Set<String> applicationFunctions;
    private Set<String> woApplicationFunctions;

    public void setApplicationFunctions(Set<String> applicationFunctions) {
        this.applicationFunctions = applicationFunctions;
    }

    public void setWoApplicationFunctions(Set<String> woApplicationFunctions) {
        this.woApplicationFunctions = woApplicationFunctions;
    }

    public Set<String> getApplicationFunctions() {
        return applicationFunctions;
    }

    public Set<String> getWoApplicationFunctions() {
        return woApplicationFunctions;
    }

    @Override
    public String toString() {
        return "ApplicationUserPermission{" +
                "applicationFunctions=" + applicationFunctions +
                ", woApplicationFunctions=" + woApplicationFunctions +
                '}';
    }
}
