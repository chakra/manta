package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.CatalogAssocData;
import java.util.List;

import java.util.List;

public interface CatalogAssocDAO {

    public List<CatalogAssocData> readCatalogAssoc(Long siteId, String assocType);

}
