package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.SiteHierarchyTotalReportView;
import com.espendwise.manta.model.view.SiteHierarchyView;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.criteria.SiteHierarchySearchCriteria;

import java.util.List;

public interface SiteHierarchyDAO {

    public void configureSiteHierarchy(Long accountIdId, Long endLevelId,String levelAssocCd,  String itemAssocCode, UpdateRequest<BusEntityData> updateRequest);

    public List<BusEntityData> findSiteHierarchyLevelsOfAccount(Long accountId);

    public BusEntityAssocData findSiteHierarchyLevelAssocData(Long busEntityId);

    public List<BusEntityData> findSiteHierarchyChildItems(Long levelId, String itemTypeCd, String itemAssocCd);

    public BusEntityData findSiteHierarchyData(Long level1Id, String siteHierarchyLevelElement);

    public List<BusEntityData> findSiteHierarchyChildItems(Long manageLevel, String siteHierarchyLevelElement, String siteHierarchyElementOfLevel, SiteHierarchySearchCriteria criteria);

    public void configureSiteHierarchyWithSites(Long layerId, UpdateRequest<Long> updateRequest);

    public List<BusEntityAssocData> findConfigurationFor(Long levelId, List<Long> assocs, String assocCd);

    public List<Long> findSiteConfiguration(Long siteId, int siteHierarchyLevelCount);

    public List<SiteHierarchyView> findSiteHierarchyView(List<Long> configurationValueIds, BusEntityData topLevelData);

    public void createOrReplaceSiteHierarchySiteAssoc(Long parentId, Long siteId);

    public List<SiteHierarchyTotalReportView> findSiteHierarchyTotalReport(Long accountId, int siteHierarchyLevelCount);

    public List<BusEntityAssocData> findAssoc(Long element1Id, Long element2Id, String assocCd);

    public List<BusEntityData> findSiteHierarchyChildItems(Long levelId, String typeCd, List<String> namesL);

    public List<Long> findConfiguredHierarchy(List<Long> ids, boolean isTopLevel);

    public List<Long> findConfiguredHierarchyHigh(Long levelId);

    public  List<BusEntityAssocData> findConfiguredSitesNotFor(Long hierarchyId, List<Long> sitesIds);
}
