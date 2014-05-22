package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.EmailTemplateListView;

import java.util.List;

public class EmailTemplateFilterResultForm extends AbstractFilterResult<EmailTemplateListView> {

    private List<EmailTemplateListView> templates;

    public List<EmailTemplateListView> getTemplates() {
        return templates;
    }

    public void setTemplates(List<EmailTemplateListView> templates) {
        this.templates = templates;
    }

    @Override
    public List<EmailTemplateListView> getResult() {
        return templates;
    }

    @Override
    public void reset() {
        super.reset();
        templates = null;
    }
}
