package com.espendwise.manta.web.forms;


import java.io.Serializable;

public class SiteHierarchyLayerLevelForm implements Serializable {

    private Long layerId;
    private String layerName;
    private Long levelId;
    private String levelName;

    public SiteHierarchyLayerLevelForm() {
    }

    public SiteHierarchyLayerLevelForm(Long layerId, String layerName, Long levelId, String levelName) {
        this.layerId = layerId;
        this.layerName = layerName;
        this.levelId = levelId;
        this.levelName = levelName;
    }

    public Long getLayerId() {
        return layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
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

    @Override
    public String toString() {
        return "SiteHierarchyLayerLevelForm{" +
                "layerId=" + layerId +
                ", layerName='" + layerName + '\'' +
                ", levelId=" + levelId +
                ", levelName='" + levelName + '\'' +
                '}';
    }
}
