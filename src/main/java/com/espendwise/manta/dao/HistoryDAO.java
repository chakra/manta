package com.espendwise.manta.dao;

import java.util.List;
import java.util.Set;

import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.util.criteria.HistoryCriteria;

public interface HistoryDAO extends DAO {
	
    public void createHistoryRecord(HistoryData historyData, Set<HistoryObjectData> historyObjectDatas, Set<HistorySecurityData> historySecurityDatas);

    public List<HistoryData> findHistoryRecords(HistoryCriteria criteria);
}
