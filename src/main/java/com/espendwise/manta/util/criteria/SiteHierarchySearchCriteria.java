package com.espendwise.manta.util.criteria;


import java.io.Serializable;

public class SiteHierarchySearchCriteria implements Serializable {

    private FilterValue name;
    private  Long id;

    public FilterValue getName() {
        return name;
    }

    public void setName(FilterValue name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
