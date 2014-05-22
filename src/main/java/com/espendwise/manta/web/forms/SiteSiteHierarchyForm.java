package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.SiteHierarchyView;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.SiteSiteHierarchyFormValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


@Validation(SiteSiteHierarchyFormValidator.class)
public class SiteSiteHierarchyForm  extends WebForm{

    private Long locationId;
    private SiteHierarchyLevelItemsForm levelItems = new SiteHierarchyLevelItemsForm();
    private List<SiteHierarchyView> siteHierarchy;
    private List<SiteHierarchyLevelForm> levels = new ArrayList<SiteHierarchyLevelForm>();
    private SortedMap<Integer, Long>  selectedLevelItems = new TreeMap<Integer, Long>();


    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setLevelItems(SiteHierarchyLevelItemsForm levelItems) {
        this.levelItems = levelItems;
    }

    public void setSiteHierarchy(List<SiteHierarchyView> siteHierarchy) {
        this.siteHierarchy = siteHierarchy;
    }


    public Long getLocationId() {
        return locationId;
    }

    public SiteHierarchyLevelItemsForm getLevelItems() {
        return levelItems;
    }

    public List<SiteHierarchyView> getSiteHierarchy() {
        return siteHierarchy;
    }

    public void setLevels(List<SiteHierarchyLevelForm> levels) {
        this.levels = levels;
    }

    public List<SiteHierarchyLevelForm> getLevels() {
        return levels;
    }

    public SortedMap<Integer, Long> getSelectedLevelItems() {
        return selectedLevelItems;
    }

    public void setSelectedLevelItems(SortedMap<Integer, Long> selectedLevelItems) {
        this.selectedLevelItems = selectedLevelItems;
    }

    @Override
    public String toString() {
        return "SiteSiteHierarchyForm{" +
                "locationId=" + locationId +
                ", levelItems=" + levelItems +
                ", siteHierarchy=" + siteHierarchy +
                ", levels=" + levels +
                ", selectedLevelItems=" + selectedLevelItems +
                '}';
    }
}
