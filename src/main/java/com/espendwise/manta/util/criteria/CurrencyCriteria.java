package com.espendwise.manta.util.criteria;

import java.io.Serializable;

public class CurrencyCriteria implements Serializable {

   private Long currencyId;
   private String locale;
   private String shortDesc;
   private String localCode;
   private String currencyPositionCd;
   private Long decimals;
   private String globalCode;   
   
   private Integer limit;

   public Long getCurrencyId() {
	return currencyId;
   }

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getShortDesc() {
		return shortDesc;
	}
	
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	
	public String getLocalCode() {
		return localCode;
	}
	
	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}
	
	public String getCurrencyPositionCd() {
		return currencyPositionCd;
	}
	
	public void setCurrencyPositionCd(String currencyPositionCd) {
		this.currencyPositionCd = currencyPositionCd;
	}
	
	public Long getDecimals() {
		return decimals;
	}
	
	public void setDecimals(Long decimals) {
		this.decimals = decimals;
	}
	
	public String getGlobalCode() {
		return globalCode;
	}
	
	public void setGlobalCode(String globalCode) {
		this.globalCode = globalCode;
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
    public String toString() {
        return "CurrencyCriteria{" +
                "currencyId=" + currencyId +
                ", locale=" + locale +
                ", shortDesc=" + shortDesc +
                ", localCode='" + localCode + '\'' +
                ", currencyPositionCd='" + currencyPositionCd + '\'' +
                ", decimals='" + decimals + '\'' +
                ", globalCode='" + globalCode + '\'' +
                ", limit=" + limit +
                '}';
    }
}
