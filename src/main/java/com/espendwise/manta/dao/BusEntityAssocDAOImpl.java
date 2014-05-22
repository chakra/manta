package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class BusEntityAssocDAOImpl extends DAOImpl implements BusEntityAssocDAO {

    private static final Logger logger = Logger.getLogger(BusEntityAssocDAOImpl.class);

    public BusEntityAssocDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public BusEntityAssocData createAssoc(Long busEntity1Id, Long busEntity2Id, String busEntityAssocCd) {

        logger.info("createAssoc()=> BEGIN" +
                ",\n busEntity1Id  =  " + busEntity1Id +
                ",\n busEntity2Id =  " + busEntity2Id  +
                ",\n busEntityAssocCd: " + busEntityAssocCd
        );

        BusEntityAssocData assocData = new BusEntityAssocData();


        assocData.setBusEntity1Id(busEntity1Id);
        assocData.setBusEntity2Id(busEntity2Id);
        assocData.setBusEntityAssocCd(busEntityAssocCd);

        BusEntityAssocData assoc = super.create(assocData);

        logger.info("removeAssoc()=> END.");

        return assoc;

    }

    @Override
    public void removeAssoc(List<BusEntityAssocData> assoc) {
        for(BusEntityAssocData e:Utility.listNN(assoc)) {
            remove(e);
        }
    }

    @Override
    public List<BusEntityAssocData> findAssocs(List<Long> busEntity1Ids, Long busEntity2Id, String assocCd) {

        logger.info("findAssocs()=> BEGIN" +
                ", busEntity1Ids: " + busEntity1Ids +
                ", busEntity2Id: " + busEntity2Id +
                ", assocCd: " + assocCd);


        Query q = em.createQuery("Select assoc from BusEntityAssocData assoc " +
                " where assoc.busEntityAssocCd = (:busEntityAssocCd) " +
                (Utility.isSet(busEntity1Ids) ? " and assoc.busEntity1Id in (:busEntity1Id)" : "") +
                (Utility.isSet(busEntity2Id) ? " and assoc.busEntity2Id = (:busEntity2Id)" : "")
        );

        q.setParameter(BusEntityAssocData.BUS_ENTITY_ASSOC_CD, assocCd);

        if (Utility.isSet(busEntity1Ids)) {
            q.setParameter(BusEntityAssocData.BUS_ENTITY1_ID, busEntity1Ids);
        }

        if (Utility.isSet(busEntity2Id)) {
            q.setParameter(BusEntityAssocData.BUS_ENTITY2_ID, busEntity2Id);

        }

        List<BusEntityAssocData> x = result(q, BusEntityAssocData.class);

        logger.info("findAssocs()=> END, fetched " + x.size() + " rows");
        return x;

    }

    @Override
    public List<BusEntityAssocData> findAssocs(Long busEntity1Id, List<Long> busEntity2Ids, String assocCd) {

        logger.info("findAssocs()=> BEGIN" +
                ", busEntity2Id: " + busEntity1Id+
                ", busEntity2Ids: " + busEntity2Ids +
                ", assocCd: " + assocCd);

        Query q = em.createQuery("Select assoc from BusEntityAssocData assoc " +
                " where assoc.busEntityAssocCd = (:busEntityAssocCd) " +
                (Utility.isSet(busEntity1Id) ? " and assoc.busEntity1Id in (:busEntity1Id)" : "") +
                (Utility.isSet(busEntity2Ids) ? " and assoc.busEntity2Id = (:busEntity2Id)" : "")
        );

        q.setParameter(BusEntityAssocData.BUS_ENTITY_ASSOC_CD, assocCd);

        if (Utility.isSet(busEntity1Id)) {
            q.setParameter(BusEntityAssocData.BUS_ENTITY1_ID, busEntity1Id);
        }

        if (Utility.isSet(busEntity2Ids)) {
            q.setParameter(BusEntityAssocData.BUS_ENTITY2_ID, busEntity2Ids);

        }

        List<BusEntityAssocData> x = result(q, BusEntityAssocData.class);

        logger.info("findAssocs()=> END, fetched " + x.size() + " rows");
        return x;
    }


    @Override
    public void removeAssoc(Long busEntity1Id, Long busEntity2Id, String assocCode) {

        logger.info("removeAssoc()=> BEGIN" +
                ", busEntity1Id: "+busEntity1Id+
                ", busEntity2Id: "+busEntity2Id+
                ", assocCode: "+assocCode);

        BusEntityAssocData assoc = findAssoc(busEntity1Id, busEntity2Id, assocCode);

        logger.info("removeAssoc()=> assoc to remove: "+assoc);

        if (assoc != null) {
            remove(assoc);
            logger.info("removeAssoc()=> assochas been removed");
        }

        logger.info("removeAssoc()=> END.");
    }


    @Override
    public void deleteAssoc(List<Long> busEntity1Id, Long busEntity2Id) {
        deleteAssoc(
                busEntity1Id,
                busEntity2Id,
                null
        );
    }

    @Override
    public void deleteAssoc(List<Long> busEntity1Id, Long busEntity2Id, String assocCd) {

        logger.info("deleteAssoc()=> BEGIN" +
                ", busEntity1Id.size: " + Utility.listNN(busEntity1Id).size() +
                ", busEntity2Id: " + busEntity2Id  +
                ", assocCd: " + assocCd
        );

        if (Utility.isSet(busEntity1Id) && busEntity2Id!=null) {

            for (List<Long> ids : Utility.createPackages(busEntity1Id, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {

                Query q = em.createQuery("Delete from BusEntityAssocData where " +
                        "busEntity1Id in (:busEntity1Id)" +
                        " and busEntity2Id = (:busEntity2Id)" +
                        (Utility.isSet(assocCd)?" and busEntityAssocCd = (:busEntityAssocCd)":"")
                );

                q.setParameter(BusEntityAssocData.BUS_ENTITY1_ID, ids);
                q.setParameter(BusEntityAssocData.BUS_ENTITY2_ID, busEntity2Id);

                if(Utility.isSet(assocCd)){
                    q.setParameter(BusEntityAssocData.BUS_ENTITY_ASSOC_CD, assocCd);
                }

                q.executeUpdate();
            }

        }

        logger.info("deleteAssoc()=> END.");
    }

    @Override
    public BusEntityAssocData saveAssoc(Long busEntity1Id, Long busEntity2Id, String busEntityAssocCd) {

        BusEntityAssocData assoc = findAssoc(busEntity1Id, busEntity2Id, busEntityAssocCd);
        if (assoc == null) {
            assoc = createAssoc(busEntity1Id, busEntity2Id, busEntityAssocCd);
        }

        return assoc;

    }

    public BusEntityAssocData findAssoc(Long busEntity1Id, Long busEntity2Id, String busEntityAssocCd) {

        if(!Utility.isSet(busEntity1Id) || !Utility.isSet(busEntity2Id) || !Utility.isSet(busEntityAssocCd)){
            return null;
        }

        List<BusEntityAssocData> x = findAssocs(busEntity1Id, busEntity2Id, busEntityAssocCd);
        return !x.isEmpty() ? x.get(0) : null;

    }

    @Override
    public List<BusEntityAssocData> findAssocs(Long busEntity1Id, Long busEntity2Id, String busEntityAssocCd) {

        Query q = em.createQuery("Select assoc from BusEntityAssocData assoc " +
                " where assoc.busEntityAssocCd = (:busEntityAssocCd) " +
                (Utility.isSet(busEntity1Id) ? " and assoc.busEntity1Id = (:busEntity1Id)" : "") +
                (Utility.isSet(busEntity2Id) ? " and assoc.busEntity2Id = (:busEntity2Id)" : "")
        );

        q.setParameter("busEntityAssocCd", busEntityAssocCd);

        if (Utility.isSet(busEntity1Id)) {
            q.setParameter("busEntity1Id", busEntity1Id);
        }

        if (Utility.isSet(busEntity2Id)) {
            q.setParameter("busEntity2Id", busEntity2Id);

        }

        return result(q, BusEntityAssocData.class);

    }

    }
