package com.espendwise.manta.service;


import com.espendwise.manta.model.data.WoServiceTypeData;
import com.espendwise.manta.model.data.WoServiceTypeAssocData;
import com.espendwise.manta.model.data.WoServiceTypeCategoryData;

import java.util.List;

public interface ServiceTypeService {


	    public WoServiceTypeData findServiceType(Long serviceTypeId) ;

	    public List<WoServiceTypeData> findServiceTypeCollection(List<Long> pserviceTypeIds);

}