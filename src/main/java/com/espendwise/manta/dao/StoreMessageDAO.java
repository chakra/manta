package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.model.entity.StoreMessageEntity;
import com.espendwise.manta.model.entity.StoreMessageListEntity;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.StoreMessageListView;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.criteria.StoreMessageDataCriteria;
import com.espendwise.manta.util.criteria.StoreMsgAccountConfigCriteria;

import java.util.List;

public interface StoreMessageDAO {

    public StoreMessageData saveMessageData(Long storeId, StoreMessageData storeMessageData);

    public StoreMessageData findStoreMessage(Long storeId, Long storeMessageId);

    public List<StoreMessageListView> findMessagesByCriteria(StoreMessageDataCriteria criteria);

    public List<BusEntityData> findConfiguratedAccounts(StoreMsgAccountConfigCriteria criteria);

    public void configureAllAccounts(Long storeId, Long storeMessageId, Boolean activeOnly);

    public void configureAccounts(Long storeId, Long storeMessageId, UpdateRequest<Long> configurationRequest);

    public EntityHeaderView findStoreMessageHeader(Long storeMessageId);

    public void clearViewHistory(Long storeId, Long storeMessageId);
    
    public StoreMessageEntity findStoreMessages(Long storeMessageId);
    
    public StoreMessageEntity saveMessage(Long storeId, StoreMessageEntity storeMessage);
    
    public void deleteTranslation(Long storeMessageId, Long storeMessageDetailId) ;
}
