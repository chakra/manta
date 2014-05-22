package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.AccountContentManagementFormValidator;

@Validation(AccountContentManagementFormValidator.class)
public class AccountContentManagementForm extends WebForm implements  Initializable {
	
	private boolean initialize;
	
	private Long accountId;
	private boolean displayGenericContent;
	private String customContentURL;
	private String customContentEditor;
	private String customContentViewer;

	public AccountContentManagementForm() {
    }

	public AccountContentManagementForm(Long accountId) {
        this.accountId = accountId;
    }
	
	/**
	 * @return the accountId
	 */
	public final Long getAccountId() {
		return accountId;
	}


	/**
	 * @param accountId the accountId to set
	 */
	public final void setAccountId(Long accountId) {
		this.accountId = accountId;
	}


	/**
	 * @return the initialize
	 */
	public final boolean isInitialize() {
		return initialize;
	}

	/**
	 * @param initialize the initialize to set
	 */
	public final void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}

	/**
	 * @return the displayGenericContent
	 */
	public final boolean isDisplayGenericContent() {
		return displayGenericContent;
	}

	/**
	 * @param displayGenericContent the displayGenericContent to set
	 */
	public final void setDisplayGenericContent(boolean displayGenericContent) {
		this.displayGenericContent = displayGenericContent;
	}

	/**
	 * @return the customContentURL
	 */
	public final String getCustomContentURL() {
		return customContentURL;
	}

	/**
	 * @param customContentURL the customContentURL to set
	 */
	public final void setCustomContentURL(String customContentURL) {
		this.customContentURL = customContentURL;
	}

	/**
	 * @return the customContentEditor
	 */
	public final String getCustomContentEditor() {
		return customContentEditor;
	}

	/**
	 * @param customContentEditor the customContentEditor to set
	 */
	public final void setCustomContentEditor(String customContentEditor) {
		this.customContentEditor = customContentEditor;
	}

	/**
	 * @return the customContentViewer
	 */
	public final String getCustomContentViewer() {
		return customContentViewer;
	}

	/**
	 * @param customContentViewer the customContentViewer to set
	 */
	public final void setCustomContentViewer(String customContentViewer) {
		this.customContentViewer = customContentViewer;
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
      return isInitialized() && (accountId  == null || accountId == 0);
    }

}
