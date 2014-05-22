package com.espendwise.manta.web.forms;


import java.io.Serializable;

public class SiteHierarchyLevelForm implements Serializable {

    public static String LONG_DESC = "longDesc";

    private Long busEntityId;

    private String name;
    private String level;
    private String number;
    private String longDesc;

    public Long getBusEntityId() {
        return busEntityId;
    }

    public void setBusEntityId(Long busEntityId) {
        this.busEntityId = busEntityId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    @Override
    public String toString() {
        return "SiteHierarchyLevelForm{" +
                "busEntityId=" + busEntityId +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", number='" + number + '\'' +
                ", longDesc='" + longDesc + '\'' +
                '}';
    }
}
