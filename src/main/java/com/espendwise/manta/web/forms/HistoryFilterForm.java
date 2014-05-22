package com.espendwise.manta.web.forms;


import java.util.List;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.HistoryFilterFormValidator;

@Validation(HistoryFilterFormValidator.class)
public class HistoryFilterForm extends WebForm implements Resetable, Initializable {

    private String objectId;
    private String objectType;
    private String transactionType;
	private String startDate;
    private String endDate;
    
    //reference data
    private List<Pair<String, String>> objectTypeChoices;
    private List<Pair<String, String>> transactionTypeChoices;
    
    private boolean init;

    public HistoryFilterForm() {
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<Pair<String, String>> getObjectTypeChoices() {
		return objectTypeChoices;
	}

	public void setObjectTypeChoices(List<Pair<String, String>> objectTypeChoices) {
		this.objectTypeChoices = objectTypeChoices;
	}

	public List<Pair<String, String>> getTransactionTypeChoices() {
		return transactionTypeChoices;
	}

	public void setTransactionTypeChoices(List<Pair<String, String>> transactionTypeChoices) {
		this.transactionTypeChoices = transactionTypeChoices;
	}

	@Override
    public void initialize() {
        reset();
        init = true;
    }

    @Override
    public boolean isInitialized() {
        return init;
    }

    @Override
    public void reset() {
        this.objectId = null;
		this.objectType = null;
        this.objectTypeChoices = null;
		this.transactionType = null;
        this.transactionTypeChoices = null;
        this.startDate = null;
        this.endDate = null;
    }
}
