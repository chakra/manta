package com.espendwise.manta.web.forms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserEmailMappingValueForm  implements Serializable {

    private boolean active;
    private String name;
    private Map<String, String> properies;


    public UserEmailMappingValueForm(String name, boolean active, HashMap<String, String> properties) {
        this.name = name;
        this.active = active;
        this.properies =properties;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, String> getProperies() {
        return properies;
    }

    public void setProperies(Map<String, String> properies) {
        this.properies = properies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
