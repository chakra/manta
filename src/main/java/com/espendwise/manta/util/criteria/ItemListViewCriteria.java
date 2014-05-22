package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.List;

public class ItemListViewCriteria implements Serializable {

    private Long    storeId;
    private Long    catalogId;
    private String  itemId;
    private List<Long> itemIds;

    private String  itemName;
    private String  itemCategory;

    private String  manufacturer;
    private String  distributor;

    private String  itemSku;

    private String  itemNameFilterType;
    private String  itemCategoryFilterType;

    private String  itemSkuFilterType;
    private String  itemSkuTypeFilterType;

    private Boolean greenCertified;
    private Boolean activeOnly;

    private Boolean showMfgInfo;
    private Boolean showDistInfo;

    private String  itemProperty;
    private String  itemPropertyCd;

    private Integer limit;

    public ItemListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public ItemListViewCriteria(Integer limit) {
        this.limit = limit;
    }
    public ItemListViewCriteria() {

    }

	public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCatalogId() {
        return this.catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }


    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }



	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public List<Long> getItemIds() {
        return this.itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
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

	public String getItemSkuTypeFilterType() {
		return itemSkuTypeFilterType;
	}

	public void setItemSkuTypeFilterType(String v) {
		this.itemSkuTypeFilterType = v;
	}

	public Boolean getActiveOnly() {
		return activeOnly;
	}

	public void setActiveOnly(Boolean activeOnly) {
		this.activeOnly = activeOnly;
	}

	public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String v) {
        manufacturer = v;
    }

	public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String v) {
        distributor = v;
    }

	public Boolean getGreenCertified() {
		return greenCertified;
	}

	public void setGreenCertified(Boolean greenCertified) {
		this.greenCertified = greenCertified;
	}

    public Boolean getShowMfgInfo() {
		return showMfgInfo;
	}

	public void setShowMfgInfo(Boolean showMfgInfo) {
		this.showMfgInfo = showMfgInfo;
	}

    public Boolean getShowDistInfo() {
		return showDistInfo;
	}

	public void setShowDistInfo(Boolean showDistInfo) {
		this.showDistInfo = showDistInfo;
	}

	public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String v) {
        itemProperty = v;
    }

	public String getItemPropertyCd() {
        return itemPropertyCd;
    }

    public void setItemPropertyCd(String v) {
        itemPropertyCd = v;
    }


	@Override
    public String toString() {
        return "ProductListViewCriteria{" +
                "storeId=" + storeId +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemNameFilterType='" + itemNameFilterType + '\'' +
                ", itemCategory='" + itemCategory + '\'' +
                ", itemCategoryFilterType='" + itemCategoryFilterType + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", distributor='" + distributor + '\'' +
                ", itemSku='" + itemSku + '\'' +
                ", itemSkuFilterType='" + itemSkuFilterType + '\'' +
                ", itemSkuTypeFilterType='" + itemSkuTypeFilterType + '\'' +
                ", itemProperty='" + itemProperty + '\'' +
                ", itemPropertyCd='" + itemPropertyCd + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit +
                '}';
    }
}
