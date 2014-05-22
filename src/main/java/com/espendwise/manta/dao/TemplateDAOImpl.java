package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.TemplateData;
import com.espendwise.manta.model.data.TemplatePropertyData;
import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.EmailTemplateListView;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.StoreTemplateCriteria;
import com.espendwise.manta.util.parser.Parse;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class TemplateDAOImpl extends DAOImpl implements TemplateDAO {

    public TemplateDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<EmailTemplateListView> findEmailTemplates(StoreTemplateCriteria criteria) {

        StringBuilder query = new StringBuilder("Select new com.espendwise.manta.model.view.EmailTemplateListView(" +
                " template.templateId, " +
                " template.busEntityId.busEntityId, " +
                " template.name," +
                " emailTypeCd.value, " +
                " emailTypeCd.value, " +
                " localeTypeCd.value)" +
                " from TemplateFullEntity template" +
                " left join template.templateProperties localeTypeCd with localeTypeCd.templatePropertyCd = (:templatePropertyLocaleTypeCd)" +
                " left join template.templateProperties emailTypeCd with emailTypeCd.templatePropertyCd = (:templatePropertyEmailTypeCd)" +
                " where template.busEntityId.busEntityId = (:busEntityId) and template.type = (:emailType)");

        if (Utility.isSet(criteria.getTemplateId())) {
            query.append(" and template.templateId = ").append(Parse.parseLong(criteria.getTemplateId()));
        }	
        if (Utility.isSet(criteria.getFilterValue())) {

        	if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getFilterType())) {

                query.append(" and UPPER(template.name) like '")
                        .append(QueryHelp.startWith(criteria.getFilterValue().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getFilterType())) {

                query.append(" and UPPER(template.name) like '")
                        .append(QueryHelp.contains(criteria.getFilterValue().toUpperCase()))
                        .append("'");


            }
        }

        Query q = em.createQuery(query.toString());

        q.setParameter("busEntityId", criteria.getStoreId());
        q.setParameter("templatePropertyLocaleTypeCd", RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.LOCALE);
        q.setParameter("templatePropertyEmailTypeCd", RefCodeNames.TEMPLATE_PROPERTY_TYPE_CD.EMAIL_TYPE);
        q.setParameter("emailType", RefCodeNames.TEMPLATE_TYPE_CD.EMAIL);

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        return (List<EmailTemplateListView>) q.getResultList();
    }

    public EntityHeaderView findTemplateHeader(Long templateId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.EntityHeaderView(template.templateId, template.name)" +
                " from TemplateData template where template.templateId = (:templateId) "
        );

        q.setParameter("templateId", templateId);

        List x = q.getResultList();

        return !x.isEmpty() ? (EntityHeaderView) x.get(0) : null;
    }

    public EmailTemplateIdentView findEmailTemplateIdent(Long storeId, Long emailTemplateId) {

        Query q = em.createQuery("Select template from TemplateData template" +
                " where template.templateId = (:templateId)  and template.busEntityId = (:storeId)"
        );

        q.setParameter("templateId", emailTemplateId);
        q.setParameter("storeId", storeId);

        List templates = q.getResultList();

        TemplateData templateData = !templates.isEmpty() ? (TemplateData) templates.get(0) : null;
        if (templateData != null) {

            TemplatePropertyDAO propertyDAO = new TemplatePropertyDAOImpl(em);
            List<TemplatePropertyData> properties = propertyDAO.findTemplateProperties(templateData.getTemplateId());

            return new EmailTemplateIdentView(templateData, properties);
        }

        return null;

    }

    @Override
    public EmailTemplateIdentView saveEmailTemplateIdent(Long storeId, EmailTemplateIdentView emailTemplateView) {

        if(emailTemplateView == null) {
            return emailTemplateView;
        }

        TemplateData templateData = emailTemplateView.getTemplateData();
        List<TemplatePropertyData> properties = emailTemplateView.getProperties();

        templateData = templateData.getTemplateId() == null
                ? super.create(templateData)
                : super.update(templateData);

       if(templateData!=null && Utility.longNN(templateData.getTemplateId())>0 && Utility.isSet(properties)){
           TemplatePropertyDAO propertyDAO = new TemplatePropertyDAOImpl(em);
           properties = propertyDAO.updateTemplateProperties(templateData.getTemplateId(), properties);
       }
        
       return new EmailTemplateIdentView(templateData,  properties);
    
    }

    @Override
    public boolean deleteTemplate(Long storeId, Long templateId) {

        Query q = em.createQuery("delete from TemplatePropertyData templateProperty where templateProperty.templateId = (:templateId)");
        q.setParameter("templateId", templateId);
        q.executeUpdate();

        q = em.createQuery("delete  from TemplateData template where template.templateId = (:templateId)");
        q.setParameter("templateId", templateId);
        q.executeUpdate();

        return true;

    }


}
