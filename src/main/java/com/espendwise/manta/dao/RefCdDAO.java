package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.RefCdData;
import com.espendwise.manta.model.data.WoRefCdData;

import java.util.List;

public interface RefCdDAO {


    public List<RefCdData> getRefCodes(String refCd, int orderBy);


    public List<WoRefCdData> getWoRefCodes(String refCd, int orderBy);

}
