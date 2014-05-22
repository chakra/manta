package com.espendwise.manta.web.forms;

import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.spi.Resetable;

import org.springframework.web.multipart.MultipartFile;

public class UserLoaderForm extends WebForm implements Resetable, Initializable {

    private MultipartFile uploadedFile = null;

    private boolean init;


    public UserLoaderForm() {
        super();
    }

    
    public void setUploadedFile(MultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public MultipartFile getUploadedFile() {
		return uploadedFile;
	}

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
    }
}
