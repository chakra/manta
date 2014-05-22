package com.espendwise.manta.service;


import com.espendwise.manta.dao.StoreMessageDAO;
import com.espendwise.manta.dao.StoreMessageDAOImpl;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.model.entity.StoreMessageEntity;
import com.espendwise.manta.model.entity.StoreMessageListEntity;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.StoreMessageListView;
import com.espendwise.manta.util.StoreMessageUpdateRequest;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.criteria.StoreMessageDataCriteria;
import com.espendwise.manta.util.criteria.StoreMsgAccountConfigCriteria;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.rules.StoreMessageDateRangeRule;
import com.espendwise.manta.util.validation.rules.StoreMessageUniqueConstraint;
import com.espendwise.manta.util.validation.rules.StoreMessageUpdateConstraint;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class StoreMessageServiceImpl extends DataAccessService implements StoreMessageService {


    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public StoreMessageEntity saveMessageData(StoreMessageUpdateRequest updateRequest) throws ValidationException {

        ServiceLayerValidation validation = new ServiceLayerValidation();

        validation.addRule(new StoreMessageUniqueConstraint(updateRequest.getStoreId(), updateRequest.getStoreMessage().getMessage()));
        validation.addRule(new StoreMessageUpdateConstraint(updateRequest.getStoreId(), updateRequest.getStoreMessage().getMessage()));
        if (updateRequest.isApplyValidationRuie()) {
            validation.addRule(new StoreMessageDateRangeRule(updateRequest.getStoreMessage().getMessage()));
        }

        validation.validate();

        EntityManager entityManager = getEntityManager();
        StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);

        StoreMessageEntity storeMessage = storeMessageDao.saveMessage(updateRequest.getStoreId(), updateRequest.getStoreMessage());
        if (updateRequest.isClearViewHistory()) {
            storeMessageDao.clearViewHistory(updateRequest.getStoreId(), storeMessage.getStoreMessageId());
        }

        return storeMessage;

    }
    
    
    @Override
    @Transactional(readOnly = true)
    public StoreMessageEntity findStoreMessages(Long storeMessageId) {
    	
        EntityManager entityManager = getEntityManager();
        StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);

        return storeMessageDao.findStoreMessages( storeMessageId);
    }

    
    

    @Override
    @Transactional(readOnly = true)
    public StoreMessageData findStoreMessage(Long storeId, Long storeMessageId) {

        EntityManager entityManager = getEntityManager();
        StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);

        return storeMessageDao.findStoreMessage(storeId, storeMessageId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreMessageListView> findMessagesByCriteria(StoreMessageDataCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);

        return storeMessageDao.findMessagesByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusEntityData> findConfiguratedAccounts(StoreMsgAccountConfigCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);

        return storeMessageDao.findConfiguratedAccounts(criteria);
    }

    @Override
    @Transactional(readOnly = false)
    public void configureAllAccounts(Long storeId, Long storeMessageId, Boolean activeOnly) {

        EntityManager entityManager = getEntityManager();
        StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);

        storeMessageDao.configureAllAccounts(storeId, storeMessageId, activeOnly);
    }

    @Override
    @Transactional(readOnly = false)
    public void configureAccounts(Long storeId, Long storeMessageId, UpdateRequest<Long> configurationRequest) {

        EntityManager entityManager = getEntityManager();
        StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);

        storeMessageDao.configureAccounts(storeId, storeMessageId, configurationRequest);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public EntityHeaderView findStoreMessageHeader(Long storeMessageId) {

        EntityManager entityManager = getEntityManager();
        StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);

        return storeMessageDao.findStoreMessageHeader(storeMessageId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteTranslation(Long storeMessageId, Long storeMessageDetailId) {
        
    	EntityManager entityManager = getEntityManager();
    	StoreMessageDAO storeMessageDao = new StoreMessageDAOImpl(entityManager);
    	storeMessageDao.deleteTranslation(storeMessageId, storeMessageDetailId);
        
    }
  
}
