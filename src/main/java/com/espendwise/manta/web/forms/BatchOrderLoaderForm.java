package com.espendwise.manta.web.forms;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.controllers.BatchOrderLoaderController;
import com.espendwise.manta.web.validator.BatchOrderLoaderFormValidator;

@Validation(BatchOrderLoaderFormValidator.class)
public class BatchOrderLoaderForm extends WebForm implements Resetable, Initializable {
	private boolean init;
	private MultipartFile uploadedFile = null;
    private String accountId;
    private String accountName;
	private boolean applyToBudget;
	private boolean sendConfirmation;
	private String processOn;
	private String processWhen;
	private List<String> loaderDataErrors;
    private List<Pair<String, String>> processWhenChoices;

	@Override
    public void initialize() {
		reset();
        this.init = true;     
    }

    @Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        uploadedFile = null;
        accountId = null;
        accountName = null;
        applyToBudget = false;
        sendConfirmation = false;
        processOn = null;
        processWhen = BatchOrderLoaderController.evening;
        loaderDataErrors = null;
    }


	
	@Override
    public String toString() {
        return "ProfilePasswordMgrForm{" +
                ", uploadedFile=" + (uploadedFile == null ? "" : uploadedFile.getOriginalFilename()) + 
                ", accountId=" + accountId +
                ", accountName=" + accountName +
                ", applyToBudget=" + applyToBudget +
                ", sendConfirmation=" + sendConfirmation +
                ", processOn=" + processOn +
                ", processWhen=" + processWhen +
                ", init=" + init +
                '}';
    }

	public void setUploadedFile(MultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public MultipartFile getUploadedFile() {
		return uploadedFile;
	}
	
	public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }
    
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

	public void setApplyToBudget(boolean applyToBudget) {
		this.applyToBudget = applyToBudget;
	}

	public boolean isApplyToBudget() {
		return applyToBudget;
	}
	
	public void setSendConfirmation(boolean sendConfirmation) {
		this.sendConfirmation = sendConfirmation;
	}

	public boolean isSendConfirmation() {
		return sendConfirmation;
	}

	public void setProcessOn(String processOn) {
		this.processOn = processOn;
	}

	public String getProcessOn() {
		return processOn;
	}

	public String getProcessWhen() {
        return this.processWhen;
    }

    public void setProcessWhen(String processWhen) {
        this.processWhen = processWhen;
    }
    
	public void setProcessWhenChoices(List<Pair<String, String>> processWhenChoices) {
		this.processWhenChoices = processWhenChoices;
	}

	public List<Pair<String, String>> getProcessWhenChoices() {
		return processWhenChoices;
	}

	public void setLoaderDataErrors(List<String> loaderDataErrors) {
		this.loaderDataErrors = loaderDataErrors;
	}

	public List<String> getLoaderDataErrors() {
		return loaderDataErrors;
	}	
}
