package com.espendwise.manta.service;

import java.util.List;
import java.util.Set;

import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.util.criteria.HistoryCriteria;

public interface HistoryService {
	
    public void createHistoryRecord(HistoryData historyData, Set<HistoryObjectData> historyObjectDatas, Set<HistorySecurityData> historySecurityDatas);

    public List<HistoryData> findHistoryRecordsByCriteria(HistoryCriteria criteria);
}