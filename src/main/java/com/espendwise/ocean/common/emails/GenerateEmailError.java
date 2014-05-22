package com.espendwise.ocean.common.emails;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GenerateEmailError implements Serializable {

    public static enum Component { SUBJECT, BODY }
  
    public static String COMPONENT = "component";
    public static String MESSAGE = "message";

    private String component;
    private String message;

    public GenerateEmailError(Component component, String message) {
        this.component = component.name();
        this.message = message;
    }

    public GenerateEmailError(String component, String message) {
        this.component = component;
        this.message = message;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Map<String, String> toPoperties() {
        Map<String, String> m = new HashMap<String, String>();
        m.put(COMPONENT, getComponent());
        m.put(MESSAGE, getMessage());
        return m;
    }
   
    public static GenerateEmailError fromPoperties(Map<String, String> m) {
        return m == null ? null : new GenerateEmailError(m.get(COMPONENT), m.get(MESSAGE));
    }
}
