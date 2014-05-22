package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;


public class LocateItemCostFilterForm extends WebForm implements Resetable, Initializable {
    
    private Long itemId;
    private Long accountId;
    private Long distId;
    private Long itemSkuNum;
    private String distErpNum;
    private String feedBackFieldName;
    private String accountDesc;
    private String itemDesc;
    private String distDesc;

    private boolean init;

    public LocateItemCostFilterForm() {
        super();
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public String getAccountDesc() {
        return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        this.accountDesc = accountDesc;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getDistDesc() {
        return distDesc;
    }

    public void setDistDesc(String distDesc) {
        this.distDesc = distDesc;
    }

    public String getDistErpNum() {
        return distErpNum;
    }

    public void setDistErpNum(String distErpNum) {
        this.distErpNum = distErpNum;
    }

    public Long getDistId() {
        return distId;
    }

    public void setDistId(Long distId) {
        this.distId = distId;
    }

    public String getFeedBackFieldName() {
        return feedBackFieldName;
    }

    public void setFeedBackFieldName(String feedBackFieldName) {
        this.feedBackFieldName = feedBackFieldName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemSkuNum() {
        return itemSkuNum;
    }

    public void setItemSkuNum(Long itemSkuNum) {
        this.itemSkuNum = itemSkuNum;
    }

    @Override
    public void initialize() {
        reset();
        this.init = true;
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        this.itemId = null;
        this.accountId = null;
        this.distId = null;
        this.itemSkuNum = null;
        this.distErpNum = null;
        this.feedBackFieldName = null;
        this.accountDesc = null;
        this.itemDesc = null;
        this.distDesc = null;
    }

}
