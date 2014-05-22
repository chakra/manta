package com.espendwise.manta.util.criteria;

import java.io.Serializable;
import java.util.Date;

public class UploadFileListViewCriteria implements Serializable {

    private Long    storeId;
    private Long  uploadId;
    private String  uploadFileName;
    private String  uploadFileNameFilterType;

    private Date    addDate;
    private Date    modifiedDate;

    private Boolean processing;
    private Boolean processed;
    
    private Integer limit;

    public UploadFileListViewCriteria(Long storeId, Integer limit) {
        this.storeId = storeId;
        this.limit = limit;
    }

    public UploadFileListViewCriteria(Integer limit) {
        this.limit = limit;
    }
    public UploadFileListViewCriteria() {

    }

	public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }



	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadFileNameFilterType() {
		return uploadFileNameFilterType;
	}

	public void setUploadFileNameFilterType(String uploadFileNameFilterType) {
		this.uploadFileNameFilterType = uploadFileNameFilterType;
	}

    public Date getAddDate() {
        return this.addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getProcessing() {
        return this.processing;
    }

    public void setProcessing(Boolean v) {
        this.processing = v;
    }

    public Boolean getProcessed() {
        return this.processed;
    }

    public void setProcessed(Boolean v) {
        this.processed = v;
    }


	@Override
    public String toString() {
        return "ProductListViewCriteria{" +
                "storeId=" + storeId +
                ", uploadId='" + uploadId + '\'' +
                ", uploadFileName='" + uploadFileName + '\'' +
                ", uploadFileNameFilterType='" + uploadFileNameFilterType + '\'' +
                ", addDate='" + addDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", processing='" + processing + '\'' +
                ", processed='" + processed + '\'' +
                ", limit=" + limit +
                '}';
    }
}
