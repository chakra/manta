package com.espendwise.manta.service;


import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.criteria.ManufacturerListViewCriteria;
import com.espendwise.manta.util.criteria.StoreManufacturerCriteria;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface ManufacturerService {

    public List<ManufacturerListView> findManufacturersByCriteria(ManufacturerListViewCriteria criteria);

    public EntityHeaderView findManufacturerHeader(Long manufacturerId);

    public ManufacturerIdentView findManufacturerToEdit(Long storeId, Long manufacturerId);

    public ManufacturerIdentView saveManufacturer(Long storeId, Long userId, ManufacturerIdentView ManufacturerIdentView) throws DatabaseUpdateException, IllegalDataStateException;

   // public ManufacturerIdentView saveManufacturer(Long storeId, Long userId, ManufacturerIdentView ManufacturerIdentView) throws ValidationException, DatabaseUpdateException, IllegalDataStateException;

    //public void deleteManufacturer(Long storeId, Long manufacturerId) throws DatabaseUpdateException;

    //public void deleteManufacturerIdent(Long storeId, Long manufacturerId);

    public List<BusEntityData> findManufacturers(StoreManufacturerCriteria criteria);


}
