package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.CostCenterData;
import com.espendwise.manta.model.view.CostCenterListView;
import com.espendwise.manta.model.view.CostCenterView;
import com.espendwise.manta.model.view.CostCenterHeaderView;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.util.criteria.CostCenterListViewCriteria;

import java.util.List;

public interface CostCenterDAO {

    public List<CostCenterData> findCatalogCostCenters(Long catalogId, String assocTypeCode);

    public List<CostCenterListView> findCostCentersByCriteria(CostCenterListViewCriteria criteria);

    public CostCenterView findCostCenterToEdit(Long storeId, Long costCenterId);

    public CostCenterHeaderView findCostCenterHeader(Long costCenterId);

    public CostCenterView saveCostCenter(Long storeId, CostCenterView costCenter);

    public void configureCostCenterCatalogs(Long costCenterId, Long storeId, List<CatalogListView> selected,List<CatalogListView> fullList);

}
