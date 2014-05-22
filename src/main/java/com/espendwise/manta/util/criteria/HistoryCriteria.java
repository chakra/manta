package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.Date;

public class HistoryCriteria implements Serializable {

    private Long objectId;
    public String objectType;
    private String transactionType;
	private Date startDate;
    private Date endDate;
    private Integer limit;

    public HistoryCriteria() {
    }

    public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
    public String toString() {
        return "HistoryCriteria {" +
                "objectId=" + objectId +
                ", objectType=" + objectType + '\'' +
                ", transactionType=" + transactionType + '\'' +
                ", startDate=" + startDate + '\'' +
                ", endDate=" + endDate + '\'' +
                ", limit=" + limit + '\'' +
                '}';
    }

}
