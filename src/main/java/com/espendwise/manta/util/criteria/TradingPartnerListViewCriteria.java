package com.espendwise.manta.util.criteria;

import java.io.Serializable;

public class TradingPartnerListViewCriteria implements Serializable {
    private Long    distributorId;
    private String  tradingPartnerId;
    private String  tradingPartnerName;
    private String  tradingPartnerNameFilterType;

    private Boolean activeOnly;

    private Integer limit;

    public TradingPartnerListViewCriteria(Long distributorId, Integer limit) {
        this.distributorId = distributorId;
        this.limit = limit;
    }

    public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

	public String getTradingPartnerId() {
		return tradingPartnerId;
	}

	public void setTradingPartnerId(String tradingPartnerId) {
		this.tradingPartnerId = tradingPartnerId;
	}

	public String getTradingPartnerName() {
		return tradingPartnerName;
	}

	public void setTradingPartnerName(String tradingPartnerName) {
		this.tradingPartnerName = tradingPartnerName;
	}

	public String getTradingPartnerNameFilterType() {
		return tradingPartnerNameFilterType;
	}

	public void setTradingPartnerNameFilterType(String tradingPartnerNameFilterType) {
		this.tradingPartnerNameFilterType = tradingPartnerNameFilterType;
	}

	public Boolean getActiveOnly() {
		return activeOnly;
	}

	public void setActiveOnly(Boolean activeOnly) {
		this.activeOnly = activeOnly;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
    public String toString() {
        return "TradingPartnerListViewCriteria{" +
                "distributorId=" + distributorId +
                ", tradingPartnerId='" + tradingPartnerId + '\'' +
                ", tradingPartnerName='" + tradingPartnerName + '\'' +
                ", tradingPartnerNameFilterType='" + tradingPartnerNameFilterType + '\'' +
                ", activeOnly=" + activeOnly +
                ", limit=" + limit + '\'' +
                '}';
    }

}
