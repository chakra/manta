package com.espendwise.manta.model.view;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
import java.util.List;
/**
 * SiteHierarchyView generated by hbm2java
*/
public class SiteHierarchyView extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String SITE_HIERARCHY_ID = "siteHierarchyId";
    public static final String SITE_HIERARCHY_NUM = "siteHierarchyNum";
    public static final String SITE_HIERARCHY_NAME = "siteHierarchyName";
    public static final String VALUE_ID = "valueId";
    public static final String VALUE_NAME = "valueName";

    private Long siteHierarchyId;
    private String siteHierarchyNum;
    private String siteHierarchyName;
    private Long valueId;
    private String valueName;

    public SiteHierarchyView() {
    }
	
    public SiteHierarchyView(Long siteHierarchyId) {
        this.setSiteHierarchyId(siteHierarchyId);
    }

    public SiteHierarchyView(Long siteHierarchyId, String siteHierarchyNum, String siteHierarchyName, Long valueId, String valueName) {
        this.setSiteHierarchyId(siteHierarchyId);
        this.setSiteHierarchyNum(siteHierarchyNum);
        this.setSiteHierarchyName(siteHierarchyName);
        this.setValueId(valueId);
        this.setValueName(valueName);
    }

    public Long getSiteHierarchyId() {
        return this.siteHierarchyId;
    }

    public void setSiteHierarchyId(Long siteHierarchyId) {
        this.siteHierarchyId = siteHierarchyId;
        setDirty(true);
    }

    public String getSiteHierarchyNum() {
        return this.siteHierarchyNum;
    }

    public void setSiteHierarchyNum(String siteHierarchyNum) {
        this.siteHierarchyNum = siteHierarchyNum;
        setDirty(true);
    }

    public String getSiteHierarchyName() {
        return this.siteHierarchyName;
    }

    public void setSiteHierarchyName(String siteHierarchyName) {
        this.siteHierarchyName = siteHierarchyName;
        setDirty(true);
    }

    public Long getValueId() {
        return this.valueId;
    }

    public void setValueId(Long valueId) {
        this.valueId = valueId;
        setDirty(true);
    }

    public String getValueName() {
        return this.valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
        setDirty(true);
    }




}


