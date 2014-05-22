package com.espendwise.manta.web.forms;


import java.util.ArrayList;
import java.util.List;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.StoreMessageFormValidator;

@Validation(StoreMessageFormValidator.class)
public class StoreMessageForm extends WebForm implements Initializable {

    private boolean initialize;

    private Long storeMessageId;
    private String messageTitle;
    private String postedDate;
    private String endDate;
    private String forcedReadCount;
    private boolean published;
    private String languageCd;
    private String country;
    private String messageAuthor;
    private String messageAbstract;
    private String messageBody;
    private String storeMessageStatusCd;
    private String modBy;
    private boolean forcedRead;
    private String name;
    private String displayOrder;
    private boolean active;
    private String messageType = RefCodeNames.MESSAGE_TYPE_CD.REGULAR;
    private List<StoreMessageDetailForm> detail = new ArrayList<StoreMessageDetailForm>();

    public StoreMessageForm(Long storeMessageId) {
        super();
        this.storeMessageId = storeMessageId;
    }

    public Long getStoreMessageId() {
        return storeMessageId;
    }

    public void setStoreMessageId(Long storeMessageId) {
        this.storeMessageId = storeMessageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public boolean getForcedRead() {
        return forcedRead;
    }

    public void setForcedRead(boolean forcedRead) {
        this.forcedRead = forcedRead;
    }

    public String getForcedReadCount() {
        return forcedReadCount;
    }

    public void setForcedReadCount(String forcedReadCount) {
        this.forcedReadCount = forcedReadCount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public  boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMessageAuthor() {
        return messageAuthor;
    }

    public void setMessageAuthor(String messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    public String getMessageAbstract() {
        return messageAbstract;
    }

    public void setMessageAbstract(String messageAbstract) {
        this.messageAbstract = messageAbstract;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getStoreMessageStatusCd() {
        return storeMessageStatusCd;
    }

    public void setStoreMessageStatusCd(String storeMessageStatusCd) {
        this.storeMessageStatusCd = storeMessageStatusCd;
    }

    public String getModBy() {
        return modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
    }

    public boolean getIsNew() {
       return isInitialized() && (storeMessageId  == null || storeMessageId == 0);
    }
    
    

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public List<StoreMessageDetailForm> getDetail() {
		return detail;
	}

	public void setDetail(List<StoreMessageDetailForm> detail) {
		this.detail = detail;
	}

	@Override
    public void initialize() {
      this.initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return this.initialize; 
    }

}


