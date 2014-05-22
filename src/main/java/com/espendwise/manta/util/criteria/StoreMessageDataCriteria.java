package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.Date;

public class StoreMessageDataCriteria implements Serializable {

   private String messageId;
   private String  name;
   private String  nameFilterType;
   private String  messageTitle;
   private String  messageTitleFilterType;
   private String status;
   private Long storeId;
   private Date postedDateFrom;
   private Date postedDateTo;
   private String  languageCd;
   private String  country;
   private Boolean published;
   private Boolean unpublished;
   private Boolean inactive;
   
   
   private Boolean showInactive;
    private Integer limit;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Date getPostedDateFrom() {
        return postedDateFrom;
    }

    public void setPostedDateFrom(Date postedDateFrom) {
        this.postedDateFrom = postedDateFrom;
    }

    public Date getPostedDateTo() {
        return postedDateTo;
    }

    public void setPostedDateTo(Date postedDateTo) {
        this.postedDateTo = postedDateTo;
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageTitleFilterType() {
        return messageTitleFilterType;
    }

    public void setMessageTitleFilterType(String messageTitleFilterType) {
        this.messageTitleFilterType = messageTitleFilterType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getShowInactive() {
        return showInactive;
    }

    public void setShowInactive(Boolean showInactive) {
        this.showInactive = showInactive;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }
    
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public Boolean getUnpublished() {
		return unpublished;
	}

	public void setUnpublished(Boolean unpublished) {
		this.unpublished = unpublished;
	}

	public Boolean getInactive() {
		return inactive;
	}

	public void setInactive(Boolean inactive) {
		this.inactive = inactive;
	}

	@Override
    public String toString() {
        return "StoreMessageDataCriteria{" +
                "storeId=" + storeId +
                ", postedDateFrom=" + postedDateFrom +
                ", postedDateTo=" + postedDateTo +
                ", languageCd='" + languageCd + '\'' +
                ", country='" + country + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                ", messageTitleFilterType='" + messageTitleFilterType + '\'' +
                ", showInactive=" + showInactive +
                ", limit=" + limit +
                '}';
    }
}
