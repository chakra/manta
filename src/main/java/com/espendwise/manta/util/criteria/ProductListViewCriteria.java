package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.List;

public class ProductListViewCriteria implements Serializable {

    private Long    storeId;
    private Long    catalogId;
    private String  itemId;
    private String  itemCategory;
    private String  itemCategoryFilterType;
    private String  itemName;
    private String  itemNameFilterType;
    private String  itemSku;
    private String  itemSkuFilterType;
    private String  itemSkuFilterSubType;
    private Boolean activeOnly;
    private Boolean substituteItemSKUbyCustSKU;
    private  List<Long> itemIds;
    
     private Integer limit;

    public ProductListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public ProductListViewCriteria(Integer limit) {
        this.limit = limit;
    }
    public ProductListViewCriteria() {

    }

    public List<Long> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
	}

	public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }


    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }



    public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getItemCategoryFilterType() {
		return itemCategoryFilterType;
	}

	public void setItemCategoryFilterType(String itemCategoryFilterType) {
		this.itemCategoryFilterType = itemCategoryFilterType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemNameFilterType() {
		return itemNameFilterType;
	}

	public void setItemNameFilterType(String itemNameFilterType) {
		this.itemNameFilterType = itemNameFilterType;
	}

	public String getItemSku() {
		return itemSku;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

	public String getItemSkuFilterType() {
		return itemSkuFilterType;
	}

	public void setItemSkuFilterType(String itemSkuFilterType) {
		this.itemSkuFilterType = itemSkuFilterType;
	}

	public String getItemSkuFilterSubType() {
		return itemSkuFilterSubType;
	}

	public void setItemSkuFilterSubType(String itemSkuFilterSubType) {
		this.itemSkuFilterSubType = itemSkuFilterSubType;
	}

	public Boolean getActiveOnly() {
		return activeOnly;
	}

	public void setActiveOnly(Boolean activeOnly) {
		this.activeOnly = activeOnly;
	}

        public Boolean getSubstituteItemSKUbyCustSKU() {
            return substituteItemSKUbyCustSKU;
        }

        public void setSubstituteItemSKUbyCustSKU(Boolean substituteItemSKUbyCustSKU) {
            this.substituteItemSKUbyCustSKU = substituteItemSKUbyCustSKU;
        }

	@Override
    public String toString() {
        return "ProductListViewCriteria{" +
                "storeId=" + storeId +
                ", catalogId='" + catalogId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemNameFilterType='" + itemNameFilterType + '\'' +
                ", itemCategory='" + itemCategory + '\'' +
                ", itemCategoryFilterType='" + itemCategoryFilterType + '\'' +
                ", itemSku='" + itemSku + '\'' +
                ", itemSkuFilterType='" + itemSkuFilterType + '\'' +
                ", itemSkuFilterSubType='" + itemSkuFilterSubType + '\'' +
                ", activeOnly=" + activeOnly + '\'' +
                ", substituteItemSKUbyCustSKU=" + substituteItemSKUbyCustSKU + '\'' +
                ", limit=" + limit +
                '}';
    }
}
