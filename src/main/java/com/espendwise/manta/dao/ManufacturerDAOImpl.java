package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.binder.PropertyBinder;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.criteria.ManufacturerListViewCriteria;
import com.espendwise.manta.util.criteria.StoreManufacturerCriteria;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.ArrayList;

public class ManufacturerDAOImpl extends DAOImpl implements ManufacturerDAO{

    private static final Logger logger = Logger.getLogger(ManufacturerDAOImpl.class);

    public ManufacturerDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<ManufacturerListView> findManufacturersByCriteria(ManufacturerListViewCriteria criteria) {

        logger.info("findManufacturersByCriteria()=> BEGIN, criteria: "+criteria);

        StringBuilder baseQuery = new StringBuilder("Select " +
                "new com.espendwise.manta.model.view.ManufacturerListView(" +
                "   manufacturer.busEntityId," +
                "   manufacturer.shortDesc, " +
                "   manufacturer.busEntityStatusCd" +
                ") " +
                " from " +
                "com.espendwise.manta.model.fullentity.BusEntityFullEntity manufacturer " +
                " inner join manufacturer.busEntityAssocsForBusEntity1Id storeManufacturer " +
                " where storeManufacturer.busEntity2Id.busEntityId = (:storeId) and storeManufacturer.busEntityAssocCd = (:storeManufacturerAssocCd)"
        );

        if (Utility.isSet(criteria.getManufacturerId())) {
            baseQuery.append(" and manufacturer.busEntityId = ").append(Parse.parseLong(criteria.getManufacturerId()));
        }

        if (Utility.isSet(criteria.getManufacturerName())) {

        	if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getManufacturerNameFilterType())) {

                baseQuery.append(" and UPPER(manufacturer.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getManufacturerName().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getManufacturerNameFilterType())) {

                baseQuery.append(" and UPPER(manufacturer.shortDesc) like '")
                        .append(QueryHelp.contains(criteria.getManufacturerName().toUpperCase()))
                        .append("'");

            }
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and manufacturer.busEntityStatusCd <> ")
                    .append(QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE));
        }



        Query q = em.createQuery(baseQuery.toString());

        q.setParameter("storeId", criteria.getStoreId());
        q.setParameter("storeManufacturerAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.MANUFACTURER_OF_STORE);

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        List<ManufacturerListView> r = q.getResultList();

        logger.info("findSitesByCriteria)=> END, fetched : " + r.size() + " rows");

        return r;

    }

     @Override
    public ManufacturerIdentView findManufacturerToEdit(Long storeId, Long manufacturerId) {

        StoreManufacturerCriteria criteria = new StoreManufacturerCriteria();
        criteria.setStoreId(storeId);
        criteria.setName(Long.toString(manufacturerId));
        criteria.setFilterType(Constants.FILTER_TYPE.ID);

        List<BusEntityData> manufacturers = findManufacturers(criteria);

        if(!manufacturers.isEmpty())  {
            return  pickupManufacturerIdentView(manufacturers.get(0));
        } else {
           return null;
        }

    }

     @Override
    public List<BusEntityData> findManufacturers(StoreManufacturerCriteria criteria) {

        logger.info("findManufacturers()=> BEGIN, criteria "+criteria);

        StringBuilder baseQuery = new StringBuilder("Select manufacturer from BusEntityData manufacturer");


        if (criteria.getStoreId() != null) {

            baseQuery
                    .append(",BusEntityAssocData manufacturerStore")
                    .append(" where manufacturer.busEntityId = manufacturerStore.busEntity1Id")
                    .append(" and manufacturerStore.busEntity2Id = (:storeId)")
                    .append(" and manufacturerStore.busEntityAssocCd = (:manufacturerStoreCd)") ;
        }

        if (Utility.isSet(criteria.getName())) {

            if (Constants.FILTER_TYPE.ID.equals(criteria.getFilterType())) {

                baseQuery.append(" and manufacturer.busEntityId = ").append(Parse.parseLong(criteria.getName()));

            } else if (Constants.FILTER_TYPE.START_WITH.equals(criteria.getFilterType())) {

                baseQuery.append(" and UPPER(manufacturer.shortDesc) like '")
                        .append(QueryHelp.startWith(criteria.getName().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.CONTAINS.equals(criteria.getFilterType())) {

                baseQuery.append(" and UPPER(manufacturer.shortDesc) like '")
                        .append(QueryHelp.contains(criteria.getName().toUpperCase()))
                        .append("'");

            } else if (Constants.FILTER_TYPE.EXACT_MATCH.equals(criteria.getFilterType())) {

                baseQuery.append(" and manufacturer.shortDesc = '")
                        .append(QueryHelp.escapeQuotes(criteria.getName()))
                        .append("'");
            }
        }

        if (!Utility.isSet(criteria.getName()) || !Constants.FILTER_TYPE.ID.equals(criteria.getFilterType())) {
            if (criteria.getManufacturerId() != null && criteria.getManufacturerId() > 0) {
                baseQuery.append(" and manufacturer.busEntityId = ").append(criteria.getManufacturerId());
            }
        }

        if (Utility.isTrue(criteria.getActiveOnly())) {
            baseQuery.append(" and manufacturer.busEntityStatusCd <> ").
                    append(QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_STATUS_CD.INACTIVE));
        }

        Query q = em.createQuery(baseQuery.toString());

        if (criteria.getStoreId() != null) {

            q.setParameter("storeId", criteria.getStoreId());
            q.setParameter("manufacturerStoreCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.MANUFACTURER_OF_STORE);

        }

        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        return (List<BusEntityData>) q.getResultList();
    }

    @Override
    public ManufacturerIdentView saveManufacturer(Long storeId, ManufacturerIdentView manufacturerView) throws DatabaseUpdateException {

        logger.info("saveManufacturer()=> BEGIN, ManufacturerIdentView "+manufacturerView);

        BusEntityData be = manufacturerView.getBusEntityData();
        ManufacturerPropertiesView manufacturerProperties = manufacturerView.getProperties();

        if (be.getBusEntityId() == null) {

            be = saveManufacturerBusEntity(be);

            if (Utility.longNN(be.getBusEntityId()) > 0) {

                if (createManufacturerOfStoreAssoc(storeId, be.getBusEntityId())) {

                    manufacturerProperties = createOrUpdateManufacturerProperties(be.getBusEntityId(),
                            manufacturerProperties
                    );

                } else {

                    throw new DatabaseUpdateException();

                }

            } else {

                throw new DatabaseUpdateException();

            }

        } else {

            be = saveManufacturerBusEntity(be);
            manufacturerProperties = createOrUpdateManufacturerProperties(be.getBusEntityId(), manufacturerProperties);

        }


        manufacturerView.setBusEntityData(be);
        manufacturerView.setProperties(manufacturerProperties);

        logger.info("saveManufacturer()=> END");
        return manufacturerView;
    }


    private boolean createManufacturerOfStoreAssoc(Long storeId, Long busEntityId) {

        BusEntityAssocDAO busEntityAssocDao = new BusEntityAssocDAOImpl(em);

        BusEntityAssocData assoc = busEntityAssocDao.createAssoc(busEntityId,
                storeId,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.MANUFACTURER_OF_STORE
        );

        return assoc != null && Utility.longNN(assoc.getBusEntityAssocId()) > 0;
    }



    private ManufacturerPropertiesView createOrUpdateManufacturerProperties(
        Long busEntityId, ManufacturerPropertiesView manufacturerProperty) {

        List<PropertyData> properties = new ArrayList<PropertyData>();

        properties.add(manufacturerProperty.getMsdsPlugin());

        PropertyDAO propertyDAO = new PropertyDAOImpl(em);
        properties = propertyDAO.updateEntityProperties(busEntityId, properties);

        manufacturerProperty.setManufacturerId(busEntityId);
        manufacturerProperty.setMsdsPlugin(properties.get(0));

        return manufacturerProperty;
    }

    private BusEntityData saveManufacturerBusEntity(BusEntityData be) {
        if (be.getBusEntityId() == null) {
          be = super.create(be);
        } else {
            be = super.update(be);
        }
        return be;
    }


   @Override
    public EntityHeaderView findManufacturerHeader(Long manufacturerId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.EntityHeaderView(manufacturer.busEntityId, manufacturer.shortDesc)" +
                " from  BusEntityData manufacturer where manufacturer.busEntityId = (:manufacturerId) "
        );

        q.setParameter("manufacturerId", manufacturerId);

        List x = q.getResultList();

        return !x.isEmpty() ? (EntityHeaderView) x.get(0) : null;
    }


  private void removeEntity(BusEntityData busEntityData) {
        if (busEntityData != null) {
            em.remove(busEntityData);
        }
    }


    private ManufacturerIdentView pickupManufacturerIdentView(BusEntityData busEntityData) {

        if (busEntityData == null || !(Utility.longNN(busEntityData.getBusEntityId()) > 0)) {
            return null;
        }

        ManufacturerIdentView ManufacturerIdentView = new ManufacturerIdentView();

        ManufacturerPropertiesView propertiesView = findManufacturerProperty(busEntityData.getBusEntityId());
        ManufacturerIdentView.setBusEntityData(busEntityData);
        ManufacturerIdentView.setProperties(propertiesView);

        return ManufacturerIdentView;

    }


        private ManufacturerPropertiesView findManufacturerProperty(Long busEntityId) {
        PropertyDAO propertyDao = new PropertyDAOImpl(em);
        List<PropertyData> properties =
            propertyDao.findEntityNamedActiveProperties(busEntityId,
                                        Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.MSDS_PLUGIN));
        return PropertyBinder.bindManufacturerProperties(new ManufacturerPropertiesView(),
                busEntityId,
                properties
        );
    }



}
