package com.espendwise.manta.service;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.CostCenterListViewCriteria;
//import com.espendwise.manta.util.criteria.StoreCostCenterCriteria;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface CostCenterService {

    public List<CostCenterListView> findCostCentersByCriteria(CostCenterListViewCriteria criteria);

    public CostCenterHeaderView findCostCenterHeader(Long costCenterId);

    public CostCenterView findCostCenterToEdit(Long storeId, Long costCenterId);

    public CostCenterView saveCostCenter(Long storeId, CostCenterView costCenterView) throws DatabaseUpdateException, IllegalDataStateException;

    public void configureCostCenterCatalogs(Long costCenterId, Long storeId, 
                List<CatalogListView> selected, List<CatalogListView> fullList);
    
}
