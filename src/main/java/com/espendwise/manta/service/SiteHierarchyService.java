package com.espendwise.manta.service;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.SiteHierarchyTotalReportView;
import com.espendwise.manta.model.view.SiteHierarchyView;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.criteria.SiteHierarchySearchCriteria;

import java.util.List;

public interface SiteHierarchyService {

    public List<BusEntityData> findSiteHierarchyLevelsOfAccount(Long accountId);

    public void configureSiteHierarchy(Long storeId,
                                       Long parentLevelId,
                                       Long endLevelId,
                                       String levelAssocCd,
                                       String elementAssocCode,
                                       UpdateRequest<BusEntityData> updateRequest);

    public BusEntityAssocData findSiteHierarchyLevelAssocData(Long busEntityId);

    public List<BusEntityData> findSiteHierarchyChildItems(Long accountId, String pItemTypeCd, String pItemAssocCd);

    public List<BusEntityData> findSiteHierarchyChildItems(Long manageLevel, String siteHierarchyLevelElement, String siteHierarchyElementOfLevel, SiteHierarchySearchCriteria criteria);

    public BusEntityData findSiteHierarchyData(Long level1Id, String siteHierarchyLevelElement);

    public void configureSiteHierarchyWithSites(Long storeId, Long accountId, Long layerId, UpdateRequest<Long> longUpdateRequest);

    public List<BusEntityAssocData> findConfigurationFor(Long levelId, List<Long> longs, String siteHierarchySiteOfElement);

    public List<Long> findSiteConfiguration(Long siteId, int levelSize);

    public List<SiteHierarchyView> findSiteConfiguration(List<Long> configurationValueIds, BusEntityData topLevelData);

    public void createOrReplaceSiteHierarchySiteAssoc(Long levelId, Long locationId);

    public List<SiteHierarchyTotalReportView> findSiteHierarchyTotalReport(Long accountId, int siteHierarchyLevelCount);

    public List<BusEntityAssocData> findAssoc(Long element1Id, Long element2Id, String assocCd);

    public List<BusEntityData> findSiteHierarchyChildItems(Long levelId, String typeCd, List<String> namesL);

    public List<Long> findConfiguredHierarchy(List<Long> ids, boolean isTopLevel);

    public List<Long> findConfiguredHierarchyHigh(Long levelId);

    public List<BusEntityAssocData> findConfiguredSitesNotFor(Long hierarchyId, List<Long> sitesIds);
}
