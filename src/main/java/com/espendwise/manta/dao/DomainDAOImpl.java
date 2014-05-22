package com.espendwise.manta.dao;

import com.espendwise.manta.model.view.DomainView;
import com.espendwise.manta.util.RefCodeNames;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


public class DomainDAOImpl extends DAOImpl implements DomainDAO {

    private static final Logger logger = Logger.getLogger(DomainDAOImpl.class);

    public DomainDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }


    public DomainView getDomainView(Long domainId) {

        logger.info("getDomainView()=> BEGIN");

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.DomainView(domain.busEntityId, store.busEntityId, domain.shortDesc)  " +
                "from BusEntityData domain, BusEntityAssocData assoc, BusEntityData store "+
                " where domain.busEntityId = (:domainId)"+
                " and store.busEntityTypeCd = (:storeTypeCd)" +
                " and store.busEntityStatusCd = (:storeStatusCd)" +
                " and store.busEntityId = assoc.busEntity1Id" +
                " and domain.busEntityId = assoc.busEntity2Id" +
                " and assoc.busEntityAssocCd = (:assocCd)");

        q.setParameter("domainId", domainId);
        q.setParameter("storeTypeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.STORE);
        q.setParameter("storeStatusCd", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);
        q.setParameter("assocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.DEFAULT_STORE_OF_DOMAIN);

        List<DomainView> r = (List<DomainView>) q.getResultList();

        logger.info("getDomainView()=> END.");

        return r.isEmpty() ? null : r.get(0);
    }


    @Override
    public Long getDomainId(String domainName) {

        logger.info("getDomainId()=> BEGIN");

        Query q = em.createQuery("Select domain.busEntityId from BusEntityData domain " +
                " where domain.busEntityStatusCd = (:status)" +
                " and domain.busEntityTypeCd = (:busEntityTypeCd)" +
                " and domain.shortDesc like :domainName");

        q.setParameter("status", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);
        q.setParameter("busEntityTypeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.DOMAIN_NAME);
        q.setParameter("domainName", domainName);

        List<Long> r = (List<Long>) q.getResultList();

        logger.info("getDomainId()=> END.");

        return r.isEmpty() ? null : r.get(0);
    }

    @Override
    public Long getDefaultDomainId() {

        logger.info("getDefaultDomainId()=> BEGIN");

        Query q = em.createQuery("Select domain.busEntityId from BusEntityData domain,PropertyData props " +
                " where domain.busEntityStatusCd = (:status)" +
                " and domain.busEntityId = props.busEntityId" +
                " and domain.busEntityTypeCd =(:busEntityTypeCd)" +
                " and props.value = 'true'" +
                " and props.propertyTypeCd = (:defProp)");


        q.setParameter("status", RefCodeNames.BUS_ENTITY_STATUS_CD.ACTIVE);
        q.setParameter("busEntityTypeCd", RefCodeNames.BUS_ENTITY_TYPE_CD.DOMAIN_NAME);
        q.setParameter("defProp", RefCodeNames.PROPERTY_TYPE_CD.DEFAULT);


        List<Long> r = (List<Long>) q.getResultList();

        logger.info("getDefaultDomainId()=> END.");

        return r.isEmpty() ? null : r.get(0);

    }

    @Override
    public Long findDomainStore(String domainName) {

        DomainView domain = null;

        Long domainId = getDomainId(domainName);

        if (domainId == null) {
            domainId = getDefaultDomainId();
        }

        if (domainId != null) {
            domain = getDomainView(domainId);
        }

        if (domain != null) {
            return domain.getStoreId();
        }

        return null;

    }

}
