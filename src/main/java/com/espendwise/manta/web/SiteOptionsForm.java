package com.espendwise.manta.web;


import com.espendwise.manta.web.forms.ValidForm;

import java.io.Serializable;

public class SiteOptionsForm  implements ValidForm,Serializable {

    private String allowCorporateScheduledOrder;
    private String byPassOrderRouting;
    private String consolidatedOrderWarehouse;
    private String enableInventory;
    private String shareBuyerOrderGuides;
    private String showRebillOrder;
    private String taxable;

    public String getTaxable() {
        return taxable;
    }

    public void setTaxable(String taxable) {
        this.taxable = taxable;
    }

    public String getShowRebillOrder() {
        return showRebillOrder;
    }

    public void setShowRebillOrder(String showRebillOrder) {
        this.showRebillOrder = showRebillOrder;
    }

    public String getShareBuyerOrderGuides() {
        return shareBuyerOrderGuides;
    }

    public void setShareBuyerOrderGuides(String shareBuyerOrderGuides) {
        this.shareBuyerOrderGuides = shareBuyerOrderGuides;
    }

    public String getEnableInventory() {
        return enableInventory;
    }

    public void setEnableInventory(String enableInventory) {
        this.enableInventory = enableInventory;
    }

    public String getConsolidatedOrderWarehouse() {
        return consolidatedOrderWarehouse;
    }

    public void setConsolidatedOrderWarehouse(String consolidatedOrderWarehouse) {
        this.consolidatedOrderWarehouse = consolidatedOrderWarehouse;
    }

    public String getByPassOrderRouting() {
        return byPassOrderRouting;
    }

    public void setByPassOrderRouting(String byPassOrderRouting) {
        this.byPassOrderRouting = byPassOrderRouting;
    }

    public String getAllowCorporateScheduledOrder() {
        return allowCorporateScheduledOrder;
    }

    public void setAllowCorporateScheduledOrder(String allowCorporateScheduledOrder) {
        this.allowCorporateScheduledOrder = allowCorporateScheduledOrder;
    }

    @Override
    public String toString() {
        return "SiteOptionsForm{" +
                "allowCorporateScheduledOrder='" + allowCorporateScheduledOrder + '\'' +
                ", byPassOrderRouting='" + byPassOrderRouting + '\'' +
                ", consolidatedOrderWarehouse='" + consolidatedOrderWarehouse + '\'' +
                ", enableInventory='" + enableInventory + '\'' +
                ", shareBuyerOrderGuides='" + shareBuyerOrderGuides + '\'' +
                ", showRebillOrder='" + showRebillOrder + '\'' +
                ", taxable='" + taxable + '\'' +
                '}';
    }
}
