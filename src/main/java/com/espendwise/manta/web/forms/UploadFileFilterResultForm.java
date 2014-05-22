package com.espendwise.manta.web.forms;

import com.espendwise.manta.model.view.UploadFileListView;


import java.util.List;

public class UploadFileFilterResultForm extends AbstractFilterResult<UploadFileListView> {

    private List<UploadFileListView> uploadFiles;

    public UploadFileFilterResultForm() {
    }

    public List<UploadFileListView> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(List<UploadFileListView> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    @Override
    public List<UploadFileListView> getResult() {
        return uploadFiles;
    }

    @Override
    public void reset() {
        super.reset();
        this.uploadFiles = null;
    }


}
