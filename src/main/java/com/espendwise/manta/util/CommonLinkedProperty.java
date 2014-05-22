package com.espendwise.manta.util;


import java.util.HashMap;
import java.util.Map;

public class CommonLinkedProperty extends CommonProperty {

    private Map<String, CommonProperty> linkedMap  = new HashMap<String, CommonProperty>();

    public CommonLinkedProperty() {
        super();
        this.linkedMap = new HashMap<String, CommonProperty>();
    }

    public CommonLinkedProperty(String typeCd, String shortDesc, String value, String status, String locale) {
        super(typeCd, shortDesc, value, status, locale);
        this.linkedMap = new HashMap<String, CommonProperty>();
    }

    public CommonLinkedProperty(String typeCd, String shortDesc, String value, String status, String locale, Map<String, CommonProperty> linkedMap) {
        super(typeCd, shortDesc, value, status, locale);
        this.linkedMap = linkedMap;
    }

    public Map<String, CommonProperty> getLinkedMap() {
        return linkedMap;
    }

    public void setLinkedMap(Map<String, CommonProperty> linkedMap) {
        this.linkedMap = linkedMap;
    }

    @Override
    public String toString() {
        return "CommonLinkedProperty{*******" +
                super.toString()+
                '}';
    }
}
