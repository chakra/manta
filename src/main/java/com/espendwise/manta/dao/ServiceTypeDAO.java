package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.WoServiceTypeData;
import com.espendwise.manta.model.data.WoServiceTypeAssocData;
import com.espendwise.manta.model.data.WoServiceTypeCategoryData;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.IllegalDataStateException;
//import com.espendwise.manta.util.criteria.StoreAccountCriteria;

import java.util.List;

public interface ServiceTypeDAO {

    public WoServiceTypeData findServiceTypeById(Long serviceTypeId) ;

	public List<WoServiceTypeData> findServiceTypeCollection(List<Long> serviceTypeIds);

}
