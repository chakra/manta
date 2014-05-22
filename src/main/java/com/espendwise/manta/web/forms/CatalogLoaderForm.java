package com.espendwise.manta.web.forms;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.controllers.CatalogLoaderController;
import com.espendwise.manta.web.validator.CatalogLoaderFormValidator;
import com.espendwise.manta.util.RefCodeNames;

@Validation(CatalogLoaderFormValidator.class)
public class CatalogLoaderForm extends WebForm implements Resetable, Initializable {
	private boolean init;
	private MultipartFile uploadedFile = null;
	private String effectiveDate;
	private String timeZone;
	private List<String> loaderDataErrors;
	private boolean isProcessNow;
    //private List<Pair<String, String>> processWhenChoices;

	@Override
    public void initialize() {
		reset();
        this.init = true;     
    }

    public boolean isProcessNow() {
		return isProcessNow;
	}

	public void setProcessNow(boolean isProcessNow) {
		this.isProcessNow = isProcessNow;
	}

	@Override
    public boolean isInitialized() {
        return  this.init;

    }

    @Override
    public void reset() {
        uploadedFile = null;
        effectiveDate = null;
        timeZone = RefCodeNames.TIME_ZONE_CD.EST;
        loaderDataErrors = null;
        isProcessNow = false;
    }


	
	@Override
    public String toString() {
        return "CatalogLoaderForm{" +
                ", uploadedFile=" + (uploadedFile == null ? "" : uploadedFile.getOriginalFilename()) + 
                ", effectiveDate=" + effectiveDate +
                ", timeZone=" + timeZone +
                ", init=" + init +
                '}';
    }

	public void setUploadedFile(MultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public MultipartFile getUploadedFile() {
		return uploadedFile;
	}

    
	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}


	public void setLoaderDataErrors(List<String> loaderDataErrors) {
		this.loaderDataErrors = loaderDataErrors;
	}

	public List<String> getLoaderDataErrors() {
		return loaderDataErrors;
	}	
}
