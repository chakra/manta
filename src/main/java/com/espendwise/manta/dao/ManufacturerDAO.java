package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.view.ManufacturerIdentView;
import com.espendwise.manta.model.view.ManufacturerListView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.util.criteria.ManufacturerListViewCriteria;
import com.espendwise.manta.util.criteria.StoreManufacturerCriteria;

import java.util.List;

public interface ManufacturerDAO {

    public List<ManufacturerListView> findManufacturersByCriteria(ManufacturerListViewCriteria criteria);

    public List<BusEntityData> findManufacturers(StoreManufacturerCriteria criteria);

    public EntityHeaderView findManufacturerHeader(Long manufacturerId);

    public ManufacturerIdentView findManufacturerToEdit(Long storeId,  Long manufacturerId);

    public ManufacturerIdentView saveManufacturer(Long storeId, ManufacturerIdentView manufacturerView) throws DatabaseUpdateException;




}
