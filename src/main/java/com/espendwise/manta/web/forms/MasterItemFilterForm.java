package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.web.validator.MasterItemFilterFormValidator;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.spi.Initializable;

@Validation(MasterItemFilterFormValidator.class)
public class MasterItemFilterForm extends WebForm implements Resetable, Initializable {

    private String itemId;
    private Long cloneItemId;
    private String itemName;
    private String itemNameFilterType;

    private String  itemCategory;
    private String  itemCategoryFilterType;
    private String  distributor;
    private String  manufacturer;
    private String  itemSku;
    private String  itemSkuFilterType;
    private String  itemSkuTypeFilterType;

    private String  itemProperty;
    private String  itemPropertyCd;

    private Boolean greenCertified;
    private Boolean showInactive;

    private Boolean showMfgInfo;
    private Boolean showDistInfo;


    private boolean init;

    public MasterItemFilterForm() {
        super();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Long getCloneItemId() {
        return cloneItemId;
    }

    public void setCloneItemId(Long id) {
        this.cloneItemId = id;
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


    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
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
    public void initialize() {
        reset();
        init = true;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }


    @Override
    public void reset() {
        this.itemId = null;
        this.itemName = null;
        this.itemNameFilterType = Constants.FILTER_TYPE.CONTAINS;
        this.itemCategory = null;
        this.itemCategoryFilterType = Constants.FILTER_TYPE.CONTAINS;

        this.itemSku = null;
        this.itemSkuFilterType = Constants.FILTER_TYPE.START_WITH;
        this.itemSkuTypeFilterType = RefCodeNames.SKU_TYPE_CD.STORE; 

        this.distributor = null;
        this.manufacturer = null;

        this.itemProperty = null;
        this.itemPropertyCd = null;

        this.greenCertified = false;
        this.showInactive = false;

        this.showMfgInfo = true;
        this.showDistInfo = true;
    }

}
