package com.espendwise.manta.service;


import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.EmailTemplateListView;
import com.espendwise.manta.util.criteria.StoreTemplateCriteria;

import java.util.List;

public interface TemplateService {

    public List<EmailTemplateListView> findEmailTemplatesByCriteria(StoreTemplateCriteria criteria);

    public EntityHeaderView findTemplateHeader(Long templateId);

    public EmailTemplateIdentView findEmailTemplateIdent(Long storeId, Long emailTemplateId);

    public EmailTemplateIdentView saveEmailTemplateIdent(Long storeId, EmailTemplateIdentView emailTemplateView);

    public boolean isTemplateUsedBySystem(Long storeId, Long templateId);

    public boolean deleteEmailTemplate(Long storeId,  Long templateId);
    
}
