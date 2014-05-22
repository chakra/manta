package com.espendwise.manta.web.forms;


import java.util.List;

import com.espendwise.manta.model.view.TradingPartnerListView;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.DistributorFormValidator;

@Validation(DistributorFormValidator.class)
public class DistributorForm extends WebForm implements  Initializable {

    //base attributes
    private Long distributorId;
    private String distributorName;
    private String distributorStatus;
    private String distributorLocale;
    
    //properties
    private String distributorDisplayName;
    private String distributorType;
    private String distributorCallCenterHours;
    private String distributorCompanyCode;
    private String distributorCustomerReferenceCode;

    //contact
    private ContactInputForm contact;
    
    //associated trading partners
    private List<TradingPartnerListView> tradingPartners;
    
    //reference data
    private List<Pair<String, String>> localeChoices;

    private boolean initialize;

    public DistributorForm() {
    }

    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorStatus() {
        return distributorStatus;
    }

    public void setDistributorStatus(String distributorStatus) {
        this.distributorStatus = distributorStatus;
    }

    public String getDistributorLocale() {
		return distributorLocale;
	}

	public void setDistributorLocale(String distributorLocale) {
		this.distributorLocale = distributorLocale;
	}

	public String getDistributorDisplayName() {
		return distributorDisplayName;
	}

	public void setDistributorDisplayName(String distributorDisplayName) {
		this.distributorDisplayName = distributorDisplayName;
	}

    public String getDistributorType() {
		return distributorType;
	}

	public void setDistributorType(String distributorType) {
		this.distributorType = distributorType;
	}

	public String getDistributorCallCenterHours() {
		return distributorCallCenterHours;
	}

	public void setDistributorCallCenterHours(String distributorCallCenterHours) {
		this.distributorCallCenterHours = distributorCallCenterHours;
	}

	public String getDistributorCompanyCode() {
		return distributorCompanyCode;
	}

	public void setDistributorCompanyCode(String distributorCompanyCode) {
		this.distributorCompanyCode = distributorCompanyCode;
	}

	public String getDistributorCustomerReferenceCode() {
		return distributorCustomerReferenceCode;
	}

	public void setDistributorCustomerReferenceCode(
			String distributorCustomerReferenceCode) {
		this.distributorCustomerReferenceCode = distributorCustomerReferenceCode;
	}

	public ContactInputForm getContact() {
        return contact;
    }

    public void setContact(ContactInputForm contact) {
        this.contact = contact;
    }

	public List<TradingPartnerListView> getTradingPartners() {
		return tradingPartners;
	}

	public void setTradingPartners(List<TradingPartnerListView> tradingPartners) {
		this.tradingPartners = tradingPartners;
	}

	public List<Pair<String, String>> getLocaleChoices() {
		return localeChoices;
	}

	public void setLocaleChoices(List<Pair<String, String>> localeChoices) {
		this.localeChoices = localeChoices;
	}

	@Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public boolean isNew() {
      return isInitialized() && (distributorId  == null || distributorId == 0);
    }

    @Override
    public String toString() {
        return "DistributorForm{" +
                "initialize=" + initialize +
                ", distributorId=" + distributorId +
                ", distributorName='" + distributorName + '\'' +
                ", distributorStatus='" + distributorStatus + '\'' +
                ", distributorDisplayName='" + distributorDisplayName + '\'' +
                ", distributorType='" + distributorType + '\'' +
                ", distributorCallCenterHours='" + distributorCallCenterHours + '\'' +
                ", distributorCompanyCode='" + distributorCompanyCode + '\'' +
                ", distributorCustomerReferenceCode='" + distributorCustomerReferenceCode + '\'' +
                ", contact=" + contact +
                '}';
    }
}
