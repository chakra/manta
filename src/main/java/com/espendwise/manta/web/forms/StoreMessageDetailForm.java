package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;

public class StoreMessageDetailForm extends WebForm implements Initializable {

    private boolean initialize;

    private Long storeMessageDetailId;
    private Long storeMessageId;
    private String messageDetailTypeCd;
    private String messageTitle;
    private String languageCd;
    private String country;
    private String messageAuthor;
    private String messageAbstract;
    private String messageBody;
    
    
    public Long getStoreMessageDetailId() {
		return storeMessageDetailId;
	}

	public void setStoreMessageDetailId(Long storeMessageDetailId) {
		this.storeMessageDetailId = storeMessageDetailId;
	}

	public Long getStoreMessageId() {
        return storeMessageId;
    }

    public void setStoreMessageId(Long storeMessageId) {
        this.storeMessageId = storeMessageId;
    }
    
    public String getMessageDetailTypeCd() {
		return messageDetailTypeCd;
	}

	public void setMessageDetailTypeCd(String messageDetailTypeCd) {
		this.messageDetailTypeCd = messageDetailTypeCd;
	}

	public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
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

    public boolean getIsNew() {
       return isInitialized() && (storeMessageId  == null || storeMessageId == 0);
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


