package com.espendwise.manta.dao;


import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.model.view.EmailTemplateListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.util.criteria.StoreTemplateCriteria;

import java.util.List;

public interface TemplateDAO {

    public List<EmailTemplateListView> findEmailTemplates(StoreTemplateCriteria criteria);

    public EntityHeaderView findTemplateHeader(Long templateId);

    public EmailTemplateIdentView findEmailTemplateIdent(Long storeId, Long emailTemplateId);

    public EmailTemplateIdentView saveEmailTemplateIdent(Long storeId, EmailTemplateIdentView emailTemplateView);

    public boolean deleteTemplate(Long storeId, Long templateId);


}
