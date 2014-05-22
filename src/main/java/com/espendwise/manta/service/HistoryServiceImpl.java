package com.espendwise.manta.service;


import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.espendwise.manta.dao.HistoryDAO;
import com.espendwise.manta.model.data.HistoryData;
import com.espendwise.manta.model.data.HistoryObjectData;
import com.espendwise.manta.model.data.HistorySecurityData;
import com.espendwise.manta.util.criteria.HistoryCriteria;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
public class HistoryServiceImpl extends DataAccessService implements HistoryService {

    private static final Logger logger = Logger.getLogger(HistoryServiceImpl.class);
	
    @Autowired
	private HistoryDAO historyDao;

    public HistoryServiceImpl() {
    }
    
    public HistoryServiceImpl(HistoryDAO historyDao) {
        this.historyDao = historyDao;
    }

    @Override
    //use Propagation.REQUIRES_NEW as the recording of history should be completely independent of
    //any business transaction.  We do not want any failure to record history to prevent any business
    //functionality from completing successfully
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void createHistoryRecord(HistoryData historyData, Set<HistoryObjectData> historyObjectDatas, Set<HistorySecurityData> historySecurityDatas) {
    	EntityManager entityManager = getEntityManager();
    	historyDao.setEntityManager(entityManager);
    	historyDao.createHistoryRecord(historyData, historyObjectDatas, historySecurityDatas);
    }
	
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<HistoryData> findHistoryRecordsByCriteria(HistoryCriteria criteria) {
    	EntityManager entityManager = getEntityManager();
    	historyDao.setEntityManager(entityManager);
		List<HistoryData> result = historyDao.findHistoryRecords(criteria);
	    return result;
	}
  
}
