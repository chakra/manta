package com.espendwise.manta.web.forms;


import com.espendwise.manta.util.Pair;

import java.io.Serializable;
import java.util.List;

public class SiteHierarchySubLevelsAsynchForm implements Serializable {

    private boolean redraw;
    private Integer level;
    private String selectedValue;
    private List<Pair<Long, String>> list;


    public boolean isRedraw() {
        return redraw;
    }

    public void setRedraw(boolean redraw) {
        this.redraw = redraw;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public List<Pair<Long, String>> getList() {
        return list;
    }

    public void setList(List<Pair<Long, String>> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SiteHierarchySubLevelsAsynchForm{" +
                "redraw=" + redraw +
                ", level=" + level +
                ", selectedValue='" + selectedValue + '\'' +
                ", list=" + list +
                '}';
    }
}
