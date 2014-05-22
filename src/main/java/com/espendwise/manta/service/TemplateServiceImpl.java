package com.espendwise.manta.service;


import com.espendwise.manta.dao.*;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.model.view.EmailTemplateListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.StoreTemplateCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.EmailTemplateChangeNameConstraint;
import com.espendwise.manta.util.validation.rules.EmailTemplateUniqueConstraint;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class TemplateServiceImpl extends DataAccessService implements TemplateService {

    private static final Logger logger = Logger.getLogger(TemplateServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<EmailTemplateListView> findEmailTemplatesByCriteria(StoreTemplateCriteria criteria) {
        TemplateDAOImpl templateDao = new TemplateDAOImpl(getEntityManager());
        return templateDao.findEmailTemplates(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EntityHeaderView findTemplateHeader(Long templateId) {
        TemplateDAO templateDao = new TemplateDAOImpl(getEntityManager());
        return templateDao.findTemplateHeader(templateId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EmailTemplateIdentView findEmailTemplateIdent(Long storeId, Long emailTemplateId) {
        TemplateDAO templateDao = new TemplateDAOImpl(getEntityManager());
        return templateDao.findEmailTemplateIdent(storeId, emailTemplateId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EmailTemplateIdentView saveEmailTemplateIdent(Long storeId, EmailTemplateIdentView emailTemplateView) {

        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new EmailTemplateUniqueConstraint(storeId, emailTemplateView));
        validation.addRule(new EmailTemplateChangeNameConstraint(storeId, emailTemplateView));

        validation.validate();

        TemplateDAO templateDao = new TemplateDAOImpl(getEntityManager());
        return templateDao.saveEmailTemplateIdent(storeId, emailTemplateView);

    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public boolean isTemplateUsedBySystem(Long storeId, Long templateId) {

        if (Utility.longNN(templateId) <= 0 || Utility.longNN(storeId) <= 0 ) {
            return false;
        }

        boolean returnValue;

        EntityManager entityManager = getEntityManager();

        //next, see if this template is the only one with the original name
        List<EmailTemplateListView> existingTemplates = findEmailTemplatesByCriteria(new StoreTemplateCriteria(storeId));

        String originalTemplateName = null;

        boolean isOnlyInstance = false;
        boolean isUsedByStore = false;
        boolean isUsedByAccount = false;

        if (existingTemplates != null) {

            Map<String, Integer> nameToCountMap = new HashMap<String, Integer>();

            for (EmailTemplateListView existingTemplate : existingTemplates) {

                String templateName = existingTemplate.getName();
                if (existingTemplate.getTemplateId() == templateId.longValue()) {
                    originalTemplateName = templateName;
                }

                Integer count = nameToCountMap.get(templateName);
                if (count == null) {
                    nameToCountMap.put(templateName, 1);
                } else {
                    nameToCountMap.put(templateName, count + 1);
                }
            }

            if (Utility.isSet(originalTemplateName)) {
                isOnlyInstance = nameToCountMap.get(originalTemplateName) == 1;
            }
        }


        if (isOnlyInstance) {

            PropertyDAO propertyDao = new PropertyDAOImpl(entityManager);

            List<PropertyData> storeProperties = propertyDao.findEntityProperties(storeId, null);

            String storeOrderConfirmationEmailTemplate = null;
            String storeShippingNotificationEmailTemplate = null;
            String storePendingApprovalEmailTemplate = null;
            String storeRejectedOrderEmailTemplate = null;
            String storeModifiedOrderEmailTemplate = null;

            for (PropertyData pD : storeProperties) {

                String propType = pD.getPropertyTypeCd();

                if (RefCodeNames.PROPERTY_TYPE_CD.ORDER_CONFIRMATION_EMAIL_TEMPLATE.equals(propType)) {
                    storeOrderConfirmationEmailTemplate = pD.getValue();
                } else if (RefCodeNames.PROPERTY_TYPE_CD.SHIPPING_NOTIFICATION_EMAIL_TEMPLATE.equals(propType)) {
                    storeShippingNotificationEmailTemplate = pD.getValue();
                } else if (RefCodeNames.PROPERTY_TYPE_CD.PENDING_APPROVAL_EMAIL_TEMPLATE.equals(propType)) {
                    storePendingApprovalEmailTemplate = pD.getValue();
                } else if (RefCodeNames.PROPERTY_TYPE_CD.REJECTED_ORDER_EMAIL_TEMPLATE.equals(propType)) {
                    storeRejectedOrderEmailTemplate = pD.getValue();
                } else if (RefCodeNames.PROPERTY_TYPE_CD.MODIFIED_ORDER_EMAIL_TEMPLATE.equals(propType)) {
                    storeModifiedOrderEmailTemplate = pD.getValue();
                }
            }

            isUsedByStore = (Utility.isSet(storeOrderConfirmationEmailTemplate) && storeOrderConfirmationEmailTemplate.equalsIgnoreCase(originalTemplateName))
                    || (Utility.isSet(storeShippingNotificationEmailTemplate) && storeShippingNotificationEmailTemplate.equalsIgnoreCase(originalTemplateName))
                    || (Utility.isSet(storePendingApprovalEmailTemplate) && storePendingApprovalEmailTemplate.equalsIgnoreCase(originalTemplateName))
                    || (Utility.isSet(storeRejectedOrderEmailTemplate) && storeRejectedOrderEmailTemplate.equalsIgnoreCase(originalTemplateName))
                    || (Utility.isSet(storeModifiedOrderEmailTemplate) && storeModifiedOrderEmailTemplate.equalsIgnoreCase(originalTemplateName));


            if (!isUsedByStore) {

                AccountDAO accountDao = new AccountDAOImpl(entityManager);

                List<Long> accountIds = (accountDao.findAccountIdsByStore(storeId));

                List<String> propertyTypes = Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.EXTRA);

                List<String> shortDescriptions = Utility.toList(
                        RefCodeNames.PROPERTY_TYPE_CD.ORDER_CONFIRMATION_EMAIL_TEMPLATE,
                        RefCodeNames.PROPERTY_TYPE_CD.SHIPPING_NOTIFICATION_EMAIL_TEMPLATE,
                        RefCodeNames.PROPERTY_TYPE_CD.PENDING_APPROVAL_EMAIL_TEMPLATE,
                        RefCodeNames.PROPERTY_TYPE_CD.REJECTED_ORDER_EMAIL_TEMPLATE,
                        RefCodeNames.PROPERTY_TYPE_CD.MODIFIED_ORDER_EMAIL_TEMPLATE
                );

                List<PropertyData> accountProperties = propertyDao.findEntityProperties(accountIds, shortDescriptions, propertyTypes, true);

                Map<Long, List<PropertyData>> accountPropertiesMap = PropertyUtil.toBusEntityPropMap(accountProperties);

                Iterator<Long> accountIterator = accountPropertiesMap.keySet().iterator();
                while (accountIterator.hasNext() && !isUsedByAccount) {
                    Long accountId = accountIterator.next();
                    List<PropertyData> properties = accountPropertiesMap.get(accountId);
                    if (properties != null) {
                        Iterator<PropertyData> propertyIterator = properties.iterator();
                        while (propertyIterator.hasNext() && !isUsedByAccount) {
                            PropertyData property = propertyIterator.next();
                            isUsedByAccount = originalTemplateName.equalsIgnoreCase(property.getValue());
                        }
                    }
                }
            }
        }

        //this template can be removed if it isn't the only instance or it isn't used by either the store or an account
        returnValue = !isOnlyInstance || (!isUsedByStore && !isUsedByAccount);

        return !returnValue;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean deleteEmailTemplate(Long storeId, Long templateId) {
        if (isTemplateUsedBySystem(storeId, templateId)) {
            return false;
        } else {
            TemplateDAO templateDao = new TemplateDAOImpl(getEntityManager());
            return  templateDao.deleteTemplate(storeId, templateId);
        }
    }
}
    
    
