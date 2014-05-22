package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.CostCenterAssocData;
import java.util.List;

import java.util.List;

public interface CostCenterAssocDAO {

    public List<CostCenterAssocData> readCostCenterAssocs(Long costCenterId, String assocType);

}
