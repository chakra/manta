package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.CMSFormValidator;

@Validation(CMSFormValidator.class)
public class CMSForm extends WebForm implements  Initializable {
	
	private boolean initialize;
	
	private Long primaryEntityId;
    private String primaryEntityName;
	private boolean displayGenericContent;
	private String customContentURL;
	private String customContentEditor;
	private String customContentViewer;

	public CMSForm() {
    }

	/**
	 * @return the primaryEntityId
	 */
	public final Long getPrimaryEntityId() {
		return primaryEntityId;
	}

	/**
	 * @param primaryEntityId the primaryEntityId to set
	 */
	public final void setPrimaryEntityId(Long primaryEntityId) {
		this.primaryEntityId = primaryEntityId;
	}

	/**
	 * @return the primaryEntityName
	 */
	public final String getPrimaryEntityName() {
		return primaryEntityName;
	}

	/**
	 * @param primaryEntityName the primaryEntityName to set
	 */
	public final void setPrimaryEntityName(String primaryEntityName) {
		this.primaryEntityName = primaryEntityName;
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
      return isInitialized() && (primaryEntityId  == null || primaryEntityId == 0);
    }

}
