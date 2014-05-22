package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.web.validator.UploadFileFilterFormValidator;
import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.spi.Initializable;

@Validation(UploadFileFilterFormValidator.class)
public class UploadFileFilterForm extends WebForm implements Resetable, Initializable {

    private String uploadId;
    private String uploadFileName;
    private String uploadFileNameFilterType;

    private String addDate;
    private String modifiedDate;


    private Boolean processingStatus;
    private Boolean processedStatus;

    private boolean init;

    public UploadFileFilterForm() {
        super();
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String fileName) {
        this.uploadFileName = fileName;
    }

    public String getUploadFileNameFilterType() {
        return uploadFileNameFilterType;
    }

    public void setUploadFileNameFilterType(String fileNameFilterType) {
        this.uploadFileNameFilterType = fileNameFilterType;
    }

    public String getAddDate() {
        return this.addDate;
    }

    public void setAddDate(String v) {
        this.addDate = v;
    }

    public String getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(String v) {
        this.modifiedDate = v;
    }

    public Boolean getProcessingStatus() {
        return this.processingStatus;
    }

    public void setProcessingStatus(Boolean v) {
        this.processingStatus = v;
    }

    public Boolean getProcessedStatus() {
        return this.processedStatus;
    }

    public void setProcessedStatus(Boolean v) {
        this.processedStatus = v;
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
        this.uploadId = null;
        this.uploadFileName = null;
        this.uploadFileNameFilterType = Constants.FILTER_TYPE.START_WITH;
        this.processingStatus = true;
        this.processedStatus = false;
    }

}
