package com.espendwise.manta.web.util.smac;


import java.io.Serializable;
import java.util.Set;

public class SmacDesc implements Serializable {
    
    private String handlerPath;
    private String name;
    private Set<String> pathMapping;
    private String controller;

    public SmacDesc(String jandlerPath, String name, Set<String> pathMapping, String controller) {
        this.handlerPath = jandlerPath;
        this.name = name;
        this.pathMapping = pathMapping;
        this.controller = controller;
 }

    public String getHandlerPath() {
        return handlerPath;
    }

    public void setHandlerPath(String handlerPath) {
        this.handlerPath = handlerPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<String> getPathMapping() {
        return pathMapping;
    }

    public void setPathMapping(Set<String> pathMapping) {
        this.pathMapping = pathMapping;
    }


    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    @Override
    public String toString() {
        return "SmacDesc{" +
                "handlerPath='" + handlerPath + '\'' +
                ", name='" + name + '\'' +
                ", controller='" + controller + '\'' +
                '}';
    }
}
