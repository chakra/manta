package com.espendwise.manta.util.criteria;


import java.io.Serializable;

public class FilterValue  implements Serializable {

    private String filterType;
    private String filterValue;

    public FilterValue() {
    }

    public FilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public FilterValue(String filterValue, String filterType) {
        this.filterValue = filterValue;
        this.filterType = filterType;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    @Override
    public String toString() {
        return "FilterValue{" +
                "filterType='" + filterType + '\'' +
                ", filterValue='" + filterValue + '\'' +
                '}';
    }
}
