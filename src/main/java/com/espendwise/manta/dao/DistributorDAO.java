package com.espendwise.manta.dao;

import java.util.List;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.DistributorConfigurationView;
import com.espendwise.manta.model.view.DistributorIdentView;
import com.espendwise.manta.model.view.DistributorInfoView;
import com.espendwise.manta.model.view.DistributorListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.util.criteria.DistributorListViewCriteria;
import com.espendwise.manta.util.criteria.StoreDistributorCriteria;

public interface DistributorDAO {

    public List<DistributorListView> findDistributorsByCriteria(DistributorListViewCriteria criteria);

    public List<BusEntityData> findDistributors(StoreDistributorCriteria criteria);

    public EntityHeaderView findDistributorHeader(Long distributorId);

    public DistributorIdentView findDistributorToEdit(Long storeId,  Long distributorId);

    public DistributorIdentView saveDistributor(Long storeId, DistributorIdentView distributorView) throws DatabaseUpdateException;

    public DistributorConfigurationView findDistributorConfigurationInformation(Long distributorId);

    public DistributorConfigurationView saveDistributorConfigurationInformation(Long distributorId, DistributorConfigurationView configuration);
    
    public DistributorInfoView findDistributorInfo(Long distributorId);
}
