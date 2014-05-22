package com.espendwise.manta.service;


import com.espendwise.manta.dao.SiteHierarchyDAO;
import com.espendwise.manta.dao.SiteHierarchyDAOImpl;
import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.SiteHierarchyTotalReportView;
import com.espendwise.manta.model.view.SiteHierarchyView;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.criteria.SiteHierarchySearchCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.rules.AccountSiteHierarchyUpdateConstraint;
import com.espendwise.manta.util.validation.rules.SiteHierarchyConfigurationDuplicateConstraint;
import com.espendwise.manta.util.validation.rules.SiteHierarchySiteOverlapConstraint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class SiteHierarchyServiceImpl extends DataAccessService implements SiteHierarchyService {

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findSiteHierarchyLevelsOfAccount(Long accountId) {
        return new SiteHierarchyDAOImpl(getEntityManager())
                .findSiteHierarchyLevelsOfAccount(accountId);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureSiteHierarchy(Long storeId,
                                                   Long parentLevelId,
                                                   Long endLevelId,
                                                   String levelAssocCd,
                                                   String elementAssocCode,
                                                   UpdateRequest<BusEntityData> updateRequest) {


        ServiceLayerValidation validation = new ServiceLayerValidation();

        validation.addRule(
                new AccountSiteHierarchyUpdateConstraint(
                        storeId,
                        parentLevelId,
                        endLevelId,
                        levelAssocCd,
                        elementAssocCode,
                        updateRequest
                )
        );

        validation.validate();

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        siteHierarchyDao.configureSiteHierarchy(
                parentLevelId,
                endLevelId,
                levelAssocCd,
                elementAssocCode,
                updateRequest
        );

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public BusEntityAssocData findSiteHierarchyLevelAssocData(Long levelId) {

        EntityManager entityManager = getEntityManager();

        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);
        return siteHierarchyDao.findSiteHierarchyLevelAssocData(levelId);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findSiteHierarchyChildItems(Long levelId, String itemTypeCd, String itemAssocCd) {

        EntityManager entityManager = getEntityManager();

        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);
        return siteHierarchyDao.findSiteHierarchyChildItems(levelId, itemTypeCd, itemAssocCd);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findSiteHierarchyChildItems(Long manageLevel,
                                                           String siteHierarchyLevelElement,
                                                           String siteHierarchyElementOfLevel,
                                                           SiteHierarchySearchCriteria criteria) {
        EntityManager entityManager = getEntityManager();

        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);
        return siteHierarchyDao.findSiteHierarchyChildItems(
                manageLevel,
                siteHierarchyLevelElement,
                siteHierarchyElementOfLevel,
                criteria
        );
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public BusEntityData findSiteHierarchyData(Long level1Id, String siteHierarchyLevelElement) {

        EntityManager entityManager = getEntityManager();

        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);
        return siteHierarchyDao.findSiteHierarchyData(level1Id, siteHierarchyLevelElement);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void configureSiteHierarchyWithSites(Long storeId, Long accountId, Long layerId, UpdateRequest<Long> longUpdateRequest) {

        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new SiteHierarchySiteOverlapConstraint(layerId, longUpdateRequest));

        validation.validate();

        EntityManager entityManager = getEntityManager();

        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);
        siteHierarchyDao.configureSiteHierarchyWithSites(
                layerId,
                longUpdateRequest
        );
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BusEntityAssocData> findConfigurationFor(Long levelId, List<Long> longs, String siteHierarchySiteOfElement) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findConfigurationFor(levelId, longs, siteHierarchySiteOfElement);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Long> findSiteConfiguration(Long siteId, int levelSize) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findSiteConfiguration(siteId, levelSize);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<SiteHierarchyView> findSiteConfiguration(List<Long> configurationValueIds, BusEntityData topLevelData) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findSiteHierarchyView(configurationValueIds, topLevelData);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createOrReplaceSiteHierarchySiteAssoc(Long levelId, Long locationId) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        ServiceLayerValidation validation = new ServiceLayerValidation();
        validation.addRule(new SiteHierarchyConfigurationDuplicateConstraint(levelId, locationId));

        validation.validate();

        siteHierarchyDao.createOrReplaceSiteHierarchySiteAssoc(levelId, locationId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<SiteHierarchyTotalReportView> findSiteHierarchyTotalReport(Long accountId, int siteHierarchyLevelCount) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findSiteHierarchyTotalReport(accountId, siteHierarchyLevelCount);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public List<BusEntityAssocData> findAssoc(Long element1Id, Long element2Id, String assocCd){

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findAssoc(element1Id, element2Id,assocCd);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<BusEntityData> findSiteHierarchyChildItems(Long levelId, String typeCd, List<String> namesL) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findSiteHierarchyChildItems(levelId, typeCd, namesL);
    }


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Long> findConfiguredHierarchy(List<Long> ids, boolean isTopLevel) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findConfiguredHierarchy(ids,isTopLevel);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<Long> findConfiguredHierarchyHigh(Long levelId) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findConfiguredHierarchyHigh(levelId);
    }

    @Override
    public List<BusEntityAssocData> findConfiguredSitesNotFor(Long hierarchyId, List<Long> sitesIds) {

        EntityManager entityManager = getEntityManager();
        SiteHierarchyDAO siteHierarchyDao = new SiteHierarchyDAOImpl(entityManager);

        return siteHierarchyDao.findConfiguredSitesNotFor(hierarchyId, sitesIds);

    }


}
