package com.espendwise.manta.model.view;
// Generated by Hibernate Tools


import com.espendwise.manta.model.ValueObject;
import com.espendwise.manta.model.data.BusEntityData;
/**
 * SiteAccountView generated by hbm2java
*/
public class SiteAccountView extends ValueObject implements java.io.Serializable {

    private static final long serialVersionUID = -1;

    public static final String BUS_ENTITY_DATA = "busEntityData";
    public static final String ACCOUNT_ID = "accountId";
    public static final String ACCOUNT_NAME = "accountName";

    private BusEntityData busEntityData;
    private Long accountId;
    private String accountName;

    public SiteAccountView() {
    }
	
    public SiteAccountView(BusEntityData busEntityData) {
        this.setBusEntityData(busEntityData);
    }

    public SiteAccountView(BusEntityData busEntityData, Long accountId, String accountName) {
        this.setBusEntityData(busEntityData);
        this.setAccountId(accountId);
        this.setAccountName(accountName);
    }

    public BusEntityData getBusEntityData() {
        return this.busEntityData;
    }

    public void setBusEntityData(BusEntityData busEntityData) {
        this.busEntityData = busEntityData;
        setDirty(true);
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
        setDirty(true);
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
        setDirty(true);
    }




}

