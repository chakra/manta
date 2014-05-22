package com.espendwise.manta.util.criteria;

import java.io.Serializable;

public class CatalogStructureListViewCriteria implements Serializable {

    private Long busEntityId;
    private String catalogType;

    private Integer limit;
    
    private Long itemId;
    
    private String catalogStructureCd;

    public CatalogStructureListViewCriteria(Long busEntityId, Integer limit) {
        this.busEntityId = busEntityId;
        this.limit = limit;
    }

    public CatalogStructureListViewCriteria(Integer limit) {
        this.limit = limit;
    }

    public Long getBusEntityId() {
		return busEntityId;
	}

	public void setBusEntityId(Long busEntityId) {
		this.busEntityId = busEntityId;
	}

	public String getCatalogType() {
        return this.catalogType;
    }

    public void setCatalogType(String catalogType) {
        this.catalogType = catalogType;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    // Newly added
    public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	public String getCatalogStructureCd() {
        return this.catalogStructureCd;
    }

    public void setCatalogStructureCd(String catalogStructureCd) {
        this.catalogStructureCd = catalogStructureCd;
    }

    @Override
    public String toString() {
        return "CatalogListViewCriteria{" +
                "busEntityId=" + busEntityId +
                ", catalogType='" + catalogType + '\'' +
                ", limit=" + limit +
                '}';
    }
}
