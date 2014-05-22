package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.TemplatePropertyData;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class TemplatePropertyDAOImpl extends  DAOImpl implements TemplatePropertyDAO{

    private static final Logger logger = Logger.getLogger(TemplatePropertyDAOImpl.class);

    public TemplatePropertyDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<TemplatePropertyData> findTemplateProperties(Long templateId) {

        Query q = em.createQuery("Select templateProperty from TemplatePropertyData templateProperty where templateProperty.templateId = (:templateId)");
        q.setParameter("templateId", templateId);

        return q.getResultList();
    }

    @Override
    public List<TemplatePropertyData> updateTemplateProperties(Long templateId, List<TemplatePropertyData> properties) {

        logger.debug("updateTemplateProperties()=> BEGIN");

        if (Utility.isSet(properties) && Utility.longNN(templateId) > 0) {

            for (int index = 0; index < properties.size(); index++) {

                TemplatePropertyData p = properties.get(index);

                logger.debug("updateTemplateProperties()=> property[" + index + "] - " + p);

                if (p != null) {

                    p.setTemplateId(templateId);

                    logger.debug("updateTemplateProperties()=> property[" + index + "] templatePropertyId: " + p.getTemplatePropertyId());

                    p = p.getTemplatePropertyId() == null
                            ? super.create(p)
                            : super.update(p);

                    properties.set(index, p);

                }
            }

        }

        logger.debug("updateTemplateProperties()=> END.");

        return properties;
    }
}
