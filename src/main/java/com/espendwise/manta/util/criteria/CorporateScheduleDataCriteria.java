package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.Date;

public class CorporateScheduleDataCriteria implements Serializable {

   private String corporateScheduleId;
   private String  name;
   private String  nameFilterType;
   private Long storeId;
   private Date corporateScheduleDateFrom;
   private Date corporateScheduleDateTo;
   private String  corporateScheduleDetailCd;
   
   
   private Integer limit;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    
    
    public String getCorporateScheduleId() {
		return corporateScheduleId;
	}

	public void setCorporateScheduleId(String corporateScheduleId) {
		this.corporateScheduleId = corporateScheduleId;
	}

	public Date getCorporateScheduleDateFrom() {
		return corporateScheduleDateFrom;
	}

	public void setCorporateScheduleDateFrom(Date corporateScheduleDateFrom) {
		this.corporateScheduleDateFrom = corporateScheduleDateFrom;
	}

	public Date getCorporateScheduleDateTo() {
		return corporateScheduleDateTo;
	}

	public void setCorporateScheduleDateTo(Date corporateScheduleDateTo) {
		this.corporateScheduleDateTo = corporateScheduleDateTo;
	}


    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }
    

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameFilterType() {
		return nameFilterType;
	}

	public void setNameFilterType(String nameFilterType) {
		this.nameFilterType = nameFilterType;
	}


	public String getCorporateScheduleDetailCd() {
		return corporateScheduleDetailCd;
	}

	public void setCorporateScheduleDetailCd(String corporateScheduleDetailCd) {
		this.corporateScheduleDetailCd = corporateScheduleDetailCd;
	}

	@Override
    public String toString() {
        return "CorporateScheduleDataCriteria{" +
                "storeId=" + storeId +
                ", corporateScheduleId=" + corporateScheduleId +
                ", name=" + name +
                ", corporateScheduleDateFrom=" + corporateScheduleDateFrom +
                ", corporateScheduleDateTo=" + corporateScheduleDateTo +
                ", corporateScheduleDetailCd=" + corporateScheduleDetailCd +
                ", limit=" + limit +
                '}';
    }
}
