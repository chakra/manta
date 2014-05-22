package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ContractData;
import com.espendwise.manta.model.data.UserData;
import com.espendwise.manta.model.view.SiteAccountView;
import com.espendwise.manta.model.view.SiteHeaderView;
import com.espendwise.manta.model.view.SiteIdentView;
import com.espendwise.manta.model.view.SiteListView;
import com.espendwise.manta.model.view.UserListView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.util.criteria.SiteListViewCriteria;
import com.espendwise.manta.util.criteria.StoreSiteCriteria;

import java.util.List;

public interface SiteDAO {

    public List<SiteListView> findSitesByCriteria(SiteListViewCriteria criteria);

    public SiteHeaderView findSiteHeader(Long storeId, Long siteId);

    public SiteIdentView findSiteToEdit(Long storeId,  Long siteId);

    public SiteIdentView saveSiteIdent(Long storeId, SiteIdentView siteView) throws DatabaseUpdateException;

    public void removeSiteIdent(SiteIdentView site);

    public void configureSiteWithUsers(Long siteId, List<UserData> users);

    public void configureSiteWithUsersFromAnotherSite(Long srcSiteId, Long destSiteId, Long storeId, Long userId);

    public List<SiteAccountView> findSites(StoreSiteCriteria criteria);

    public void configureCatalogFromAnotherSite(Long createAssocFromSiteId, BusEntityData busEntity) throws IllegalDataStateException;
    
    public void configureSiteUsersList(Long siteId, Long storeId, List<UserListView> selected, List<UserListView> deselected);
    
    public void configureSiteCatalog(Long siteId, Long catalogId);

    public BusEntityAssocData findSiteAccountAssoc(Long siteId);
    
    public List<ContractData> findSiteContract(Long siteId);
    
    public List<BusEntityData> findSiteAccount(Long siteId);

	List<Long> findSiteByDistRefNumberAndStoreId(String distSiteRefNum, Long storeId);

	List<Long> findSiteByAccountIdsAndStoreId(List<Long> accountIds, Long storeId);
}
