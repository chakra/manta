package com.espendwise.manta.web.forms;


import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.util.UrlPathAssistent;
import com.espendwise.manta.web.validator.AccountSiteHierarchyManageFilterFormValidator;

import java.util.List;

public class AccountSiteHierarchyManageForm extends WebForm {


    public static  interface  ACTION_CD { public static String ADD_LINE = "addline", SAVE = "save"; }
    private String action;

    private int levelNum;
    private List<SiteHierarchyLayerLevelForm> layerLevels;
    private String levelName;
    private Long levelId;
    private Long parentLevelId;
    private List<SiteHierarchyLevelForm> items;
    private Long accountId;
    private List<Long> levels;
    private String layerName;
    private Long layerId;
    private boolean configure;


    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }


    public String getLayerName() {
        return layerName;
    }

    public Long getLayerId() {
        return layerId;
    }

    public void setLevels(List<Long> levels) {
        this.levels = levels;
    }

    public List<Long> getLevels() {
        return levels;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public void setLayerLevels(List<SiteHierarchyLayerLevelForm> layerLevels) {
        this.layerLevels = layerLevels;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public void setParentLevelId(Long parentLevelId) {
        this.parentLevelId = parentLevelId;
    }

    public void setItems(List<SiteHierarchyLevelForm> items) {
        this.items = items;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public List<SiteHierarchyLayerLevelForm> getLayerLevels() {
        return layerLevels;
    }

    public String getLevelName() {
        return levelName;
    }

    public Long getParentLevelId() {
        return parentLevelId;
    }

    public List<SiteHierarchyLevelForm> getItems() {
        return items;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getLevelId() {
        return levelId;
    }

    public String getLevelIdUrlPath() throws Exception {
        return UrlPathAssistent.urlPath(getLevels());
    }

    public boolean isConfigure() {
        return configure;
    }

    public boolean getConfigure() {
        return configure;
    }



    public void setConfigure(boolean configure) {
        this.configure = configure;
    }

    @Override
    public String toString() {
        return "AccountSiteHierarchyManageForm{" +
                "action='" + action + '\'' +
                ", levelNum=" + levelNum +
                ", layerLevels=" + layerLevels +
                ", levelName='" + levelName + '\'' +
                ", levelId=" + levelId +
                ", parentLevelId=" + parentLevelId +
                ", items=" + items +
                ", accountId=" + accountId +
                ", levels=" + levels +
                ", layerName='" + layerName + '\'' +
                ", layerId=" + layerId +
                ", configure=" + configure +
                '}';
    }
}
