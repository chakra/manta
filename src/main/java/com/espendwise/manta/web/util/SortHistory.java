package com.espendwise.manta.web.util;


import java.io.Serializable;

public class SortHistory implements Serializable {  

    private String sortField;
    private boolean asc;

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    @Override
    public String toString() {
        return "SortHistory{" +
                "sortField='" + sortField + '\'' +
                ", asc=" + asc +
                '}';
    }
}
