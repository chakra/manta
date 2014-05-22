package com.espendwise.manta.web.forms;


import java.io.Serializable;

public class SiteHierarchyAsynchForm implements Serializable {

    private SiteHierarchySubLevelsAsynchForm subLevels;

    public SiteHierarchySubLevelsAsynchForm getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(SiteHierarchySubLevelsAsynchForm subLevels) {
        this.subLevels = subLevels;
    }

    @Override
    public String toString() {
        return "SiteHierarchyAsynchForm{" +
                "subLevels=" + subLevels +
                '}';
    }
}
