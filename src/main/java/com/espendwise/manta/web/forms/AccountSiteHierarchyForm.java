package com.espendwise.manta.web.forms;


import com.espendwise.manta.util.IdNamePair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.SiteHierarchyLevelFormBinder;
import com.espendwise.manta.web.validator.AccountSiteHierarchyFormValidator;

import java.util.ArrayList;
import java.util.List;


@Validation(AccountSiteHierarchyFormValidator.class)
public class AccountSiteHierarchyForm  extends WebForm  {

    private List<SiteHierarchyLevelForm> levelCollection = new ArrayList<SiteHierarchyLevelForm>();
    private List<IdNamePair> levels = new ArrayList<IdNamePair>();
    private Long accountId;
    private SiteHierarchyTotalReportForm hierarchyReport = new SiteHierarchyTotalReportForm();
    private boolean allLevelsEmpty = true;


    public AccountSiteHierarchyForm() {
    }


    public List<SiteHierarchyLevelForm> getLevelCollection() {
        return levelCollection;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public boolean isCanBeManaged() {
        return SiteHierarchyLevelFormBinder.isLevelCanBeManaged(getLevelCollection());
    }

    public void setLevelCollection(List<SiteHierarchyLevelForm> levelCollection) {
        this.levelCollection = levelCollection;
    }

    public void setHierarchyReport(SiteHierarchyTotalReportForm hierarchyReport) {
        this.hierarchyReport = hierarchyReport;
    }

    public SiteHierarchyTotalReportForm getHierarchyReport() {
        return hierarchyReport;
    }

    public List<IdNamePair> getLevels() {
        return levels;
    }

    public void setLevels(List<IdNamePair> levels) {
        this.levels = levels;
    }

    public boolean isAllLevelsEmpty() {
        return allLevelsEmpty;
    }

    public void setAllLevelsEmpty(boolean allLevelsEmpty) {
        this.allLevelsEmpty = allLevelsEmpty;
    }

    @Override
    public String toString() {
        return "AccountSiteHierarchyForm{" +
                "levelCollection=" + levelCollection +
                ", levels=" + levels +
                ", accountId=" + accountId +
                ", hierarchyReport=" + hierarchyReport +
                ", allLevelsEmpty=" + allLevelsEmpty +
                '}';
    }
}
