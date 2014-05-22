package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.SiteListViewSelectableObjects;
import com.espendwise.manta.web.util.SortHistory;

import java.util.List;

public class AccountSiteHierarchyConfigurationForm extends SiteFilterForm implements FilterResult<SiteListViewSelectableObjects.SelectableObject>, Resetable {


    private Long accountId;

    private int levelNum;
    private List<SiteHierarchyLevelForm> items;
    private List<SiteHierarchyLayerLevelForm> layerLevels;
    private List<Long> levels;
    private String levelName;
    private Long levelId;
    private String layerName;
    private Long layerId;

    private Boolean showConfiguredOnly;
    private SiteListViewSelectableObjects sites;

    private SortHistory sortHistory;

    public AccountSiteHierarchyConfigurationForm() {
        super();
        reset();
    }

    @Override
    public List<SiteListViewSelectableObjects.SelectableObject> getResult() {
        return sites != null ? sites.getSelectableObjects() : null;
    }

    public List<SiteListViewSelectableObjects.SelectableObject> getSiteObjects() {
        if(sites == null) { sites = new SiteListViewSelectableObjects();  }
        return getResult();
    }

    @Override
    public void setSortHistory(SortHistory history) {
        this.sortHistory = history;
    }

    @Override
    public SortHistory getSortHistory() {
        return this.sortHistory;
    }

    public void reset() {
        sortHistory = null;
    }

    public SiteListViewSelectableObjects getSites() {
        return sites;
    }

    public void setSites(SiteListViewSelectableObjects sites) {
        this.sites = sites;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public List<SiteHierarchyLayerLevelForm> getLayerLevels() {
        return layerLevels;
    }

    public void setLayerLevels(List<SiteHierarchyLayerLevelForm> layerLevels) {
        this.layerLevels = layerLevels;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public List<SiteHierarchyLevelForm> getItems() {
        return items;
    }

    public void setItems(List<SiteHierarchyLevelForm> items) {
        this.items = items;
    }

    public List<Long> getLevels() {
        return levels;
    }

    public void setLevels(List<Long> levels) {
        this.levels = levels;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public Long getLayerId() {
        return layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    public Boolean getShowConfiguredOnly() {
        return showConfiguredOnly;
    }

    public void setShowConfiguredOnly(Boolean showConfiguredOnly) {
        this.showConfiguredOnly = showConfiguredOnly;
    }

    @Override
    public String toString() {
        return "AccountSiteHierarchyConfigurationForm{" +
                "accountId=" + accountId +
                ", levelNum=" + levelNum +
                ", items=" + items +
                ", layerLevels=" + layerLevels +
                ", levels=" + levels +
                ", levelName='" + levelName + '\'' +
                ", levelId=" + levelId +
                ", layerName='" + layerName + '\'' +
                ", layerId=" + layerId +
                ", showConfiguredOnly=" + showConfiguredOnly +
                ", sites=" + sites +
                ", sortHistory=" + sortHistory +
                '}';
    }
}
