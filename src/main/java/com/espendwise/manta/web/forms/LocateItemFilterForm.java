package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.LocateItemFilterFormValidator;


@Validation(LocateItemFilterFormValidator.class)
public class LocateItemFilterForm extends WebForm implements Resetable, Initializable {

    private Long siteId;
    private Long assetId;
    private Long contractId;
    private String itemId;
    private String itemName;
    private String itemCategory;
    private String itemSku;
    private String itemNameFilterType = Constants.FILTER_TYPE.CONTAINS;
    private String itemCategoryFilterType = Constants.FILTER_TYPE.CONTAINS;
    private String itemSkuFilterType = RefCodeNames.SKU_TYPE_CD.STORE;
    private String itemSkuFilterSubType = Constants.FILTER_TYPE.START_WITH;

    private Boolean showInactive;

    private boolean init;

    public LocateItemFilterForm() {
        super();
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getItemCategoryFilterType() {
        return itemCategoryFilterType;
    }

    public void setItemCategoryFilterType(String itemCategoryFilterType) {
        this.itemCategoryFilterType = itemCategoryFilterType;
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

    public String getItemNameFilterType() {
        return itemNameFilterType;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public void setItemNameFilterType(String itemNameFilterType) {
        this.itemNameFilterType = itemNameFilterType;
    }


    @Override
    public void reset() {
//        this.itemNameFilterType = Constants.FILTER_TYPE.CONTAINS;
//        this.itemCategoryFilterType = Constants.FILTER_TYPE.CONTAINS;
//        this.itemSkuFilterType = RefCodeNames.SKU_TYPE_CD.STORE;
        this.itemId = null;
        this.itemName = null;
    }

    @Override
    public void initialize() {
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return this.init;
    }

}
