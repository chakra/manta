package com.espendwise.manta.dao;


import com.espendwise.manta.model.data.BusEntityAssocData;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.SiteHierarchyTotalReportView;
import com.espendwise.manta.model.view.SiteHierarchyView;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.criteria.SiteHierarchySearchCriteria;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class SiteHierarchyDAOImpl extends DAOImpl implements SiteHierarchyDAO {

    private static final Logger logger = Logger.getLogger(SiteHierarchyDAOImpl.class);

    public SiteHierarchyDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }


    /**
     *   Returns BusEntityData objects for the top levels of Site Hierarchy for account
     */
    public List<BusEntityData> findSiteHierarchyLevelsOfAccount(Long accountId) {

        logger.info("findSiteHierarchyLevelsOfAccount()=> BEGIN, accountID = " + accountId);

        List<BusEntityData> beList = findSiteHierarchyChildItems(
                accountId,
                RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_ACCOUNT,
                null
        );


        beList = Utility.listNN(beList);

        logger.info("findSiteHierarchyLevelsOfAccount()=> END, fetched " + beList.size() +" rows");
        return beList;

    }

    /**
     *   Returns count of the top levels of Site Hierarchy for account
     */
    public int getMaxSiteHierarchyLevels(Long accountId) {

        logger.info("getMaxSiteHierarchyLevels()=> BEGIN, accountID = " + accountId);

        List<BusEntityData> beList = findSiteHierarchyLevelsOfAccount(accountId);

        logger.info("getMaxSiteHierarchyLevels()=> END, max levels = " + beList.size());

        return beList.size();

    }


    /*
    //  Returns BusEntityData objects for elements (child items) of the Site Hierarchy level.
    //  parameters : pParentId - parent level Id ( for the highest level) or parent element of level id( for lower level)
    //               pItemTypeCd - bus_entity_type_cd of element
    //               pItemAssocCd - bus_entity_assoc_cd of a level to element
    //               pFilter - search filter
    */
    @Override
    public List<BusEntityData> findSiteHierarchyChildItems(Long parentId,
                                                           String itemTypeCd,
                                                           String itemAssocCd,
                                                           SiteHierarchySearchCriteria filter) {


        logger.info("findSiteHierarchyChildItems()=> BEGIN" +
                ".\n     parentId = " + parentId +
                ".\n   itemTypeCd = " + itemTypeCd +
                ".\n  itemAssocCd = " + itemAssocCd +
                ".\n       filter = " + filter
        );

        Query query = em.createQuery("select be from BusEntityData be, BusEntityAssocData bea " +
                " where be.busEntityId = bea.busEntity1Id" +
                " and be.busEntityTypeCd = (:busEntityTypeCd)" +
                " and bea.busEntity2Id = (:busEntity2Id)" +
                " and bea.busEntityAssocCd = (:busEntityAssocCd)" +
                (Utility.isSet(filter) && Utility.isSet(filter.getName()) ? " and be.shortDesc like :shortDesc" : "") +
                (Utility.isSet(filter) && Utility.isSet(filter.getId()) ? " and be.busEntityId  = (:busEntityId)" : "")
        );

        query.setParameter(BusEntityData.BUS_ENTITY_TYPE_CD, itemTypeCd);
        query.setParameter(BusEntityAssocData.BUS_ENTITY_ASSOC_CD, itemAssocCd);
        query.setParameter(BusEntityAssocData.BUS_ENTITY2_ID, parentId);

        if (Utility.isSet(filter)) {

            if (Utility.isSet(filter.getId())) {
                query.setParameter(BusEntityData.BUS_ENTITY_ID, filter.getId());
            }

            if (Utility.isSet(filter.getName())) {
                query.setParameter(BusEntityData.SHORT_DESC, QueryHelp.toFilterValue(filter.getName()));
            }

        }

        List<BusEntityData> x = result(query, BusEntityData.class);

        logger.info("findSiteHierarchyChildItems()=> END, fetched " + x.size() + " rows");

        return x;
    }


    /**
     * Returns a list of ids for items belonging to a given level (ie. common object)
     */
    public List<Long> findSiteHierarchyLevelItems(int levelId) {

        logger.info("findSiteHierarchyLevelItems()=> BEGIN, levelId =" + levelId);

        Query query = em.createQuery("select bea1.busEntity1Id from BusEntityAssocData bea1, BusEntityAssocData bea2 " +
                " where bea1.busEntity2Id = bea2.busEntity2Id" +
                " and bea1.busEntity1Id = (:levelId)" +
                " and bea1.busEntityAssocCd = (:levelAssocCd)" +
                " and bea2.busEntityAssocCd = (:elementLevelAssocCd)"
        );

        query.setParameter("levelId", levelId);
        query.setParameter("levelAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_LEVEL);
        query.setParameter("elementLevelAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL);

        List<Long> items = result(query, Long.class);

        logger.info("findSiteHierarchyLevelItems()=> END, items = " + items);


        return items;
    }



    /**
     *   Returns Association of the hierarchy level (ie. common object) to the higher level element .
     *   parameters :  int pLevelId - higher level element id
     *    ( higher level element may have only 1 association of type 'SITE_HIER_LEVEL_OF_LEVEL'
     *     with common sublevel object )
     */
    public BusEntityAssocData findSiteHierarchyLevelAssocData(Long levelId) {

            logger.info("findSiteHierarchyLevelAssocData()=> BEGIN, levelId = " + levelId);

            BusEntityAssocDAO beAssocDao = new BusEntityAssocDAOImpl(em);

            List<BusEntityAssocData> beaList = beAssocDao.findAssocs(
                    (Long) null,
                    levelId,
                    RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_LEVEL
            );

            BusEntityAssocData result = null;
            if (beaList != null && beaList.size() > 0) {
                if (beaList.size() == 1) {
                    result = beaList.get(0);
                } else {
                    throw SystemError.multiplyAssociationsWithLevelObject(levelId);
                }
            }

        logger.info(
                "findSiteHierarchyLevelAssocData()=> END," +
                        "" + (result != null
                        ? ("Object is found, ID: " + result.getBusEntityAssocId()) : " object not found")
        );

        return result;

    }

    @Override
    public List<BusEntityData> findSiteHierarchyChildItems(Long levelId, String itemTypeCd, String itemAssocCd) {
        return findSiteHierarchyChildItems(levelId,
                itemTypeCd,
                itemAssocCd,
                null
        );
    }


    @Override
    public void configureSiteHierarchy(Long parentLevelId, Long endLevelId, String levelAssocCd, String itemAssocCode, UpdateRequest<BusEntityData> updateRequest) {
        updateSiteHierarchy(
                parentLevelId,
                endLevelId,
                levelAssocCd,
                itemAssocCode,
                updateRequest.getToDelete(),
                updateRequest.getToCreate(),
                updateRequest.getToUpdate()
        );
    }

    public void updateSiteHierarchy(Long parentId,
                                    Long levelId,
                                    String levelAssocCd,
                                    String itemAssocCd,
                                    List<BusEntityData> levelItemsToDelete,
                                    List<BusEntityData> levelItemsToCreate,
                                    List<BusEntityData> levelItemsToUpdate) {

        boolean isTopLevelFl = RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_ACCOUNT.equals(itemAssocCd);

        BusEntityDAO busEntityDao = new BusEntityDAOImpl(em);
        BusEntityAssocDAO busEntityAssocDAO = new BusEntityAssocDAOImpl(em);

        logger.info("updateSiteHierarchy()=> BEGIN" +
                ",\n     parentId = " + parentId +
                ",\n      levelId = " + levelId +
                ",\n levelAssocCd = " + levelAssocCd +
                ",\n  itemAssocCd = " + itemAssocCd
        );

        //QWERTSiteHierarchyValidator.checkDuplicated(parentId, levelItemsToCreate, levelItemsToUpdate, getSession());
        //QWERTSiteHierarchyValidator.checkLowerLevelConfig(levelItemsToDelete, getSession(), isTopLevelFl);

        if (isTopLevelFl && levelItemsToCreate != null && levelItemsToCreate.size() > 0) {
            //QWERT SiteHierarchyValidator.checkHigherLevelConfig(levelId, getSession());
        }


        // Create elements of current level and set associations to element of parent level
        if (levelItemsToCreate != null) {
            logger.info("updateSiteHierarchy()=> levelItemsToCreare = " + levelItemsToCreate);
            for (BusEntityData levelItem : levelItemsToCreate) {
                levelItem = busEntityDao.create(levelItem);
                busEntityAssocDAO.saveAssoc(levelItem.getBusEntityId(), parentId, itemAssocCd);
                logger.info("updateSiteHierarchy()=> levelItem = " + levelItem + " created");
            }
        }

        // Delete elements of current level
        if (levelItemsToDelete != null) {
            logger.info("updateSiteHierarchy()=> levelItemsToDelete =" + levelItemsToDelete);
            busEntityAssocDAO.deleteAssoc(Utility.toIds(levelItemsToDelete), parentId);
            busEntityDao.delete(Utility.toIds(levelItemsToDelete));
        }

        // Update elements of current level
        if (levelItemsToUpdate != null) {
            logger.info("updateSiteHierarchy()=> levelItemsToUpdate = " + levelItemsToUpdate);
            for (BusEntityData levelItem : levelItemsToUpdate) {
                busEntityDao.createOrUpdate(levelItem);
            }
        }
        // Set current level's association to element of parent level if one or more elements exist on this (current) level
        List<BusEntityAssocData> beaList = busEntityAssocDAO.findAssocs((Long) null, parentId, itemAssocCd);

        if (Utility.isSet(levelAssocCd) && beaList != null && !beaList.isEmpty()) {
            busEntityAssocDAO.saveAssoc(levelId, parentId, levelAssocCd);
        } else if (beaList == null || beaList.isEmpty()) {
            busEntityAssocDAO.removeAssoc(levelId, parentId, levelAssocCd);
        }

        logger.info("updateSiteHierarchy()=> END.");


    }

    @Override
    public void configureSiteHierarchyWithSites(Long layerId, UpdateRequest<Long> updateRequest) {
        updateSiteHierarchySiteAssoc(layerId,
                updateRequest.getToCreate(),
                updateRequest.getToDelete()
        );
    }


    public void updateSiteHierarchySiteAssoc(Long parentId, List<Long> selected, List<Long> deselected) {


        String assocCd = RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT;

        logger.info("updateSiteHierarchySiteAssoc()==> BEGIN" +
                ",\n parentId = " + parentId +
                ",\n selected = " + ((selected != null) ? selected.toString() : "NULL") +
                ",\n deselected =" + ((deselected != null) ? deselected.toString() : "NULL"));

        BusEntityAssocDAO beaDAO = new BusEntityAssocDAOImpl(em);

        if (deselected != null && deselected.size() > 0) {
            beaDAO.deleteAssoc(deselected, parentId, assocCd);
        }

        if (selected != null && selected.size() > 0) {
            //QWERT SiteHierarchyValidator.checkOverlapingSiteConfig(parentId, selected, getSession());
        }

        for (Long siteId : Utility.listNN(selected)) {
            beaDAO.saveAssoc(siteId, parentId, assocCd);
        }

        logger.info("updateSiteHierarchySiteAssoc()=> END.");

    }


    public void createOrReplaceSiteHierarchySiteAssoc(Long parentId, Long siteId) {

        String assocCd = RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT;

        logger.info("replaceSiteHierarchySiteAssoc()=> BEGIN," +
                " parentLevelId = " + parentId +
                ", siteId = " + siteId);

        BusEntityAssocDAO beaDAO = new BusEntityAssocDAOImpl(em);
        List<BusEntityAssocData> beaList = beaDAO.findAssocs(
                Utility.longNN(siteId),
                (Long) null,
                assocCd
        );

        if (beaList == null || beaList.size() == 0) {

            beaDAO.createAssoc(siteId, parentId, assocCd);

        } else if (beaList.size() > 1) {

            throw SystemError.siteSiteHierarchyMultipleCnfiguration(parentId, siteId);

        } else {

            BusEntityAssocData beaData = beaList.get(0);
            beaData.setBusEntity2Id(parentId);

            super.update(beaData);
        }

        logger.info("replaceSiteHierarchySiteAssoc()=> END.");

    }

    public List<BusEntityAssocData> findBusEntityAssocCollection(Long secondaryId, String assocCd) {

        logger.info("findBusEntityAssocCollection()=> BEGIN,  secondaryId = " + secondaryId + ", pAssocCd = " + assocCd);

        BusEntityAssocDAO dao = new BusEntityAssocDAOImpl(em);
        List<BusEntityAssocData> x = dao.findAssocs((Long) null, secondaryId, assocCd);

        logger.info("getBusEntityAssocCollection()=> END, fetched " + x.size() + " rows");

       return x;
    }

    /**
     * Returns BusEntityData object data for Site Hierarchy Level or Element
     * parameters :
     * Integer pBusEntityId - level's or element's id
     * String pBusEntityTypeCd - {'SITE_HIERARCHY_LEVEL', 'SITE_HIERARCHY_ELEMENT'}
     */
    public BusEntityData findSiteHierarchyData(Long busEntityId, String busEntityTypeCd) {
        BusEntityDAO dao = new BusEntityDAOImpl(em);
        List<BusEntityData> x = dao.find(Utility.toList(busEntityId), busEntityTypeCd);
        return x.isEmpty() ? null : x.get(0);
    }

    /**
     * Prepare Site Hierarchy configured for site
     */
    public List<SiteHierarchyView> findSiteHierarchyView(List<Long> configurationValueIds, BusEntityData topLevelData) {

        logger.info("findSiteHierarchyView()=> BEGIN, configurationValueIds = " + configurationValueIds+",  topLevelData: "+topLevelData.getBusEntityId());

        List<SiteHierarchyView> x = new ArrayList<SiteHierarchyView>();
        List<SiteHierarchyView> y = new ArrayList<SiteHierarchyView>();

        for (List<Long> pack : Utility.createPackages(configurationValueIds, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {

            Query q = em.createQuery("Select " +
                    "new com.espendwise.manta.model.view.SiteHierarchyView(assoc.busEntity2Id, '', '', entity.busEntityId, entity.shortDesc) " +
                    " from BusEntityData entity, BusEntityAssocData assoc where  assoc.busEntity1Id = entity.busEntityId " +
                    " and entity.busEntityId in (:busEntityId)" +
                    " and entity.busEntityTypeCd = (:busEntityTypeCd) " +
                    " and assoc.busEntityAssocCd = (:busEntityAssocCd)"
            );

            q.setParameter(BusEntityData.BUS_ENTITY_ID, configurationValueIds);
            q.setParameter(BusEntityData.BUS_ENTITY_TYPE_CD, RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL_ELEMENT);
            q.setParameter(BusEntityAssocData.BUS_ENTITY_ASSOC_CD, RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL);

            x.addAll(result(q, SiteHierarchyView.class));

        }


        logger.info("findSiteHierarchyView)=> x.size = " + x.size());

        for (List<Long> pack : Utility.createPackages(configurationValueIds, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {

            Query q = em.createQuery("Select " +
                    "new com.espendwise.manta.model.view.SiteHierarchyView(entity.busEntityId, entity.longDesc, entity.shortDesc, assoc.busEntity2Id, '') " +
                    " from BusEntityData entity, BusEntityAssocData assoc where  assoc.busEntity1Id = entity.busEntityId " +
                    " and assoc.busEntity2Id in (:busEntity2Id)" +
                    " and entity.busEntityTypeCd = (:busEntityTypeCd) " +
                    " and assoc.busEntityAssocCd = (:busEntityAssocCd)"
            );

            q.setParameter(BusEntityAssocData.BUS_ENTITY2_ID, configurationValueIds);
            q.setParameter(BusEntityData.BUS_ENTITY_TYPE_CD, RefCodeNames.BUS_ENTITY_TYPE_CD.SITE_HIERARCHY_LEVEL);
            q.setParameter(BusEntityAssocData.BUS_ENTITY_ASSOC_CD, RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_LEVEL);

            y.addAll(result(q, SiteHierarchyView.class));

        }

        logger.info("findSiteHierarchyView()=> y.size = " + y.size());

        for (SiteHierarchyView valueView : x) {
            for (SiteHierarchyView view : y) {
                if (view.getValueId().intValue() == valueView.getSiteHierarchyId()) {
                    valueView.setSiteHierarchyName(view.getSiteHierarchyName());
                    valueView.setSiteHierarchyId(view.getSiteHierarchyId());
                    valueView.setSiteHierarchyNum(view.getSiteHierarchyNum());
                    break;
                } else if (valueView.getSiteHierarchyId().intValue() ==  topLevelData.getBusEntityId()) {
                    valueView.setSiteHierarchyName(topLevelData.getShortDesc());
                    valueView.setSiteHierarchyId(topLevelData.getBusEntityId());
                    valueView.setSiteHierarchyNum(topLevelData.getLongDesc());
                    break;
                }
            }
        }

        logger.info("getSiteHierarchyView()=> END, fetched " + x.size() + " rows");

        return x;

    }

    public List<Long> findSiteConfiguration(Long siteId, int siteHierarchyLevelCount) {

        logger.info("findSiteconfiguration()=> BEGIN, siteId =" + siteId);

        List<Long> x = new ArrayList<Long>();

        String[] alias = new String[siteHierarchyLevelCount];
        String[] aliasTables = new String[siteHierarchyLevelCount];
        for (int i = 0; i < siteHierarchyLevelCount; i++) {
            alias[i] = (" L" + i);
            aliasTables[i] = BusEntityAssocData.class.getSimpleName() +  alias[i];
        }

        List<String> criteria = new ArrayList<String>();
        List<String> rfields = new ArrayList<String>();

        for (int i = 0; i < siteHierarchyLevelCount; i++) {

            criteria.add(
                    QueryHelp.conditionString(
                            alias[i],
                            BusEntityAssocData.BUS_ENTITY_ASSOC_CD,
                            QueryHelp.EQUAL,
                            RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL
                    )
            );

            if (i == siteHierarchyLevelCount -1) {

                criteria.add(
                        QueryHelp.conditionString(
                                alias[i],
                                BusEntityAssocData.BUS_ENTITY1_ID,
                                QueryHelp.EQUAL,
                                Constants.ROOT,
                                BusEntityAssocData.BUS_ENTITY2_ID
                        )
                );

            } else {
                criteria.add(
                        QueryHelp.conditionString(
                                alias[i],
                                BusEntityAssocData.BUS_ENTITY1_ID,
                                QueryHelp.EQUAL,
                                alias[i + 1],
                                BusEntityAssocData.BUS_ENTITY2_ID
                        )
                );
            }

            rfields.add(QueryHelp.field(alias[i], BusEntityAssocData.BUS_ENTITY1_ID));
        }


        String q = "Select "+Utility.toCommaString(rfields)+" from BusEntityAssocData  " + Constants.ROOT + ("," + Utility.toCommaString(aliasTables)) +
                " where " + Constants.ROOT + ".busEntityAssocCd = (:siteOfElement)" +
                " and " + Constants.ROOT + ".busEntity1Id = (:siteId) and "+QueryHelp.fetchAndCriteria(criteria)+"" +
                "";



        Query query = em.createQuery(q);

        query.setParameter("siteOfElement", RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT);
        query.setParameter("siteId", siteId);

       // throw new ServerMessageException("admin.siteHierarchy.error.site.multipleConfiguration",
       //         new Object[] { });

        if (rfields.size() == 1) {

            x = result(query, Long.class);

        } else {

            List<? extends Object[]> objArr = result(query, Object[].class);
            for (Object[] item : objArr) {
                for (int i = 0; i < item.length; i++) {
                    if (i < siteHierarchyLevelCount) {
                        x.add((Long) item[i]);
                    }
                }
            }

        }

        logger.info("findSiteconfiguration()=> END, configuration = " + x);

        return x;
    }

    public List<SiteHierarchyTotalReportView> findSiteHierarchyTotalReport(Long accountId, int siteHierarchyLevelCount) {

        List<SiteHierarchyTotalReportView> x = new ArrayList<SiteHierarchyTotalReportView>();
        if (siteHierarchyLevelCount == 0) {
            return x;
        }

        logger.info("findSiteHierarchyTotalReport()=> accountId =" + accountId +", siteHierarchyLevelCount: "+siteHierarchyLevelCount);

        String[] alias = new String[siteHierarchyLevelCount];
        String rootAlias = "L" + siteHierarchyLevelCount;

        StringBuilder joinTableStr = new StringBuilder();

        for (int i = 0; i < siteHierarchyLevelCount; i++) {
            alias[i] = "L" + (siteHierarchyLevelCount - 1 - i);
        }

        joinTableStr
                .append("  ")
                .append(BusEntityAssocData.class.getSimpleName())
                .append(" ")
                .append(rootAlias);


        List<String> conditions = new ArrayList<String>();
        List<String> equalConditions = new ArrayList<String>();
        List<String> groupBy = new ArrayList<String>();
        List<String> resultFields = new ArrayList<String>();

        for (int i = 0; i < siteHierarchyLevelCount; i++) {

            joinTableStr.append(",   ")
                    .append(BusEntityAssocData.class.getSimpleName())
                    .append(" ")
                    .append(alias[i]);

            if (i == siteHierarchyLevelCount - 1) {

                conditions.add(
                        QueryHelp.conditionString(
                                rootAlias,
                                BusEntityAssocData.BUS_ENTITY1_ID,
                                QueryHelp.EQUAL,
                                alias[0],
                                BusEntityAssocData.BUS_ENTITY2_ID
                        )
                );

                equalConditions.add(
                        QueryHelp.conditionString(rootAlias,
                                BusEntityAssocData.BUS_ENTITY_ASSOC_CD,
                                QueryHelp.EQUAL,
                                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL
                        )
                );

            } else if (siteHierarchyLevelCount > 1) {

                conditions.add(
                        QueryHelp.conditionString(
                                alias[i],
                                BusEntityAssocData.BUS_ENTITY1_ID,
                                QueryHelp.EQUAL,
                                alias[i + 1],
                                BusEntityAssocData.BUS_ENTITY2_ID
                        )
                );

                equalConditions.add(
                        QueryHelp.conditionString(
                                alias[i],
                                BusEntityAssocData.BUS_ENTITY_ASSOC_CD,
                                QueryHelp.EQUAL,
                                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL
                        )
                );

            }

            if (i == 0) {

                resultFields.add(QueryHelp.field(rootAlias, BusEntityAssocData.BUS_ENTITY1_ID));
                groupBy.add(QueryHelp.field(rootAlias, BusEntityAssocData.BUS_ENTITY1_ID));

            } else if (siteHierarchyLevelCount > 1) {

                resultFields.add(QueryHelp.field(alias[i - 1], BusEntityAssocData.BUS_ENTITY1_ID));

                groupBy.add(QueryHelp.field(alias[i - 1], BusEntityAssocData.BUS_ENTITY1_ID));

            }
        }

        Query q = em.createQuery("Select " + Utility.toCommaString(resultFields) + ", count(site.busEntityId)" +
                " from " + joinTableStr + ", BusEntityAssocData  siteAssoc, BusEntityData  site " +
                " where " + QueryHelp.conditionString(alias[siteHierarchyLevelCount - 1], BusEntityAssocData.BUS_ENTITY_ASSOC_CD, QueryHelp.EQUAL, RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT) +
                " and " + QueryHelp.fetchAndCriteria(conditions) + " and "+QueryHelp.fetchAndCriteria(equalConditions) +
                " and " + QueryHelp.conditionString(alias[siteHierarchyLevelCount - 1], BusEntityAssocData.BUS_ENTITY1_ID, QueryHelp.EQUAL, "siteAssoc", BusEntityAssocData.BUS_ENTITY1_ID) +
                " and siteAssoc.busEntity2Id = (:busEntity2Id)" +
                " and siteAssoc.busEntityAssocCd = (:busEntityAssocCd)" +
                " and site.busEntityId =siteAssoc.busEntity1Id " +
                " group by " + Utility.toCommaString(groupBy)
        );

        q.setParameter(BusEntityAssocData.BUS_ENTITY2_ID, accountId);
        q.setParameter(BusEntityAssocData.BUS_ENTITY_ASSOC_CD, RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_OF_ACCOUNT);


        List<Object[]> objArray = result(q, Object[].class);

        for (Object[] obj : objArray) {

            SiteHierarchyTotalReportView view = new SiteHierarchyTotalReportView();
            view.setTotalOfSites((Long) obj[obj.length - 1]);

            switch (obj.length - 2) {
                case 3:
                    view.setSiteHierarchyLevel1Id((Long) obj[0]);
                    view.setSiteHierarchyLevel2Id((Long) obj[1]);
                    view.setSiteHierarchyLevel3Id((Long) obj[2]);
                    view.setSiteHierarchyLevel4Id((Long) obj[3]);
                    break;
                case 2:
                    view.setSiteHierarchyLevel1Id((Long) obj[0]);
                    view.setSiteHierarchyLevel2Id((Long) obj[1]);
                    view.setSiteHierarchyLevel3Id((Long) obj[2]);
                    break;
                case 1:
                    view.setSiteHierarchyLevel1Id((Long) obj[0]);
                    view.setSiteHierarchyLevel2Id((Long) obj[1]);
                    break;
                case 0:
                    view.setSiteHierarchyLevel1Id((Long) obj[0]);
                    break;
            }
            x.add(view);
        }

        logger.info("findSiteHierarchyTotalReport()=> x: " + x);

        if ( !x.isEmpty()) {

            HashSet<Long> levelIds = new HashSet<Long>();

            for (SiteHierarchyTotalReportView aResult : x) {

                if (Utility.longNN(aResult.getSiteHierarchyLevel1Id()) > 0) {
                    levelIds.add(aResult.getSiteHierarchyLevel1Id());
                }

                if (Utility.longNN(aResult.getSiteHierarchyLevel2Id()) > 0) {
                    levelIds.add(aResult.getSiteHierarchyLevel2Id());
                }

                if (Utility.longNN(aResult.getSiteHierarchyLevel3Id()) > 0) {
                    levelIds.add(aResult.getSiteHierarchyLevel3Id());
                }

                if (Utility.longNN(aResult.getSiteHierarchyLevel4Id()) > 0) {
                    levelIds.add(aResult.getSiteHierarchyLevel4Id());
                }
            }

            Map<Long, String> levelIdToNameMap = findLevelIdToNameMap(new ArrayList<Long>(levelIds));
            for (SiteHierarchyTotalReportView item : x) {
                item.setSiteHierarchyLevel1Name(levelIdToNameMap.get(item.getSiteHierarchyLevel1Id()));
                item.setSiteHierarchyLevel2Name(levelIdToNameMap.get(item.getSiteHierarchyLevel2Id()));
                item.setSiteHierarchyLevel3Name(levelIdToNameMap.get(item.getSiteHierarchyLevel3Id()));
                item.setSiteHierarchyLevel4Name(levelIdToNameMap.get(item.getSiteHierarchyLevel4Id()));
            }
        }


        logger.info("findSiteHierarchyTotalReport()=> fetched " + x.size() + " rows");

        return x;
    }

    @Override
    public List<BusEntityAssocData> findAssoc(Long element1Id, Long element2Id, String assocCd) {
        BusEntityAssocDAO beAssocDao = new BusEntityAssocDAOImpl(em);
        return beAssocDao.findAssocs(
                element1Id,
                element2Id,
                assocCd
        );
    }

    @Override
    public List<BusEntityData> findSiteHierarchyChildItems(Long levelId, String typeCd, List<String> names) {

        Query q = em.createQuery("Select entity from BusEntityData entity, BusEntityAssocData assoc " +
                " where entity.busEntityId = assoc.busEntity1Id " +
                " and assoc.busEntity2Id =  (:busEntity2Id)" +
                " and entity.busEntityTypeCd = (:busEntityTypeCd)" +
                " and entity.shortDesc in (:shortDesc)"
        );

        q.setParameter(BusEntityAssocData.BUS_ENTITY2_ID, levelId);
        q.setParameter(BusEntityData.BUS_ENTITY_TYPE_CD, typeCd);
        q.setParameter(BusEntityData.SHORT_DESC, names);


        return result(q, BusEntityData.class);
    }


    public List<Long> findConfiguredHierarchy(List<Long> idList, boolean isTopLevel) {

        logger.info("findConfiguredHierarchy()=> BEGIN, idList= "+idList+", isTopLevel: "+isTopLevel);

        List<Long> x = new ArrayList<Long>();

        for (List<Long> ids : Utility.createPackages(idList, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {

            Query q = em.createQuery("Select distinct(assoc.busEntity2Id) from  BusEntityAssocData  assoc" +
                    " where assoc.busEntityAssocCd in (:assocCds)" +
                    (isTopLevel ? " and assoc.busEntity1Id in (:ids)" : " and  assoc.busEntity2Id in (:ids)")
            );

            List<String> assocCds = new ArrayList<String>();
            if (isTopLevel) {
                assocCds.add(RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_LEVEL);
            } else {
                assocCds.add(RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL);
                assocCds.add(RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT);
            }

            q.setParameter("ids", ids);
            q.setParameter("assocCds", assocCds);

            x.addAll(result(q, Long.class));

        }

        logger.info("findConfiguredHierarchy()=> fetched "+x.size()+" rows");

        return x;
    }

    @Override
    public List<Long> findConfiguredHierarchyHigh(Long levelId) {

        Query q = em.createQuery(
                "Select  assoc.busEntity2Id from BusEntityAssocData assoc" +
                        " where assoc.busEntity2Id in ( Select assoc2.busEntity1Id from BusEntityAssocData assoc1, BusEntityAssocData assoc2 " +
                        " where assoc1.busEntity2Id = assoc2.busEntity2Id " +
                        " and assoc1.busEntity1Id =  (:busEntity1Id)" +
                        " and assoc1.busEntityAssocCd = " + QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_LEVEL) +
                        " and assoc2.busEntityAssocCd = " + QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL) +
                        ") and assoc.busEntityAssocCd = " + QueryHelp.toQuoted(RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT)

        );

        q.setParameter(BusEntityAssocData.BUS_ENTITY1_ID, levelId);

        return result(q, Long.class);

    }

    @Override
    public List<BusEntityAssocData> findConfiguredSitesNotFor(Long hierarchyId, List<Long> sitesIds) {

        List<BusEntityAssocData> x = new ArrayList<BusEntityAssocData>();

        for (List<Long> ids : Utility.createPackages(sitesIds, Constants.DEFAULT_SQL_PACKAGE_SIZE)) {

            Query q = em.createQuery("Select assoc from  BusEntityAssocData  assoc" +
                    " where assoc.busEntityAssocCd = (:busEntityAssocCd)" +
                    " and assoc.busEntity1Id in (:busEntity1Id)" +
                    " and  assoc.busEntity2Id != (:busEntity2Id)"
            );

            q.setParameter(BusEntityAssocData.BUS_ENTITY1_ID, ids);
            q.setParameter(BusEntityAssocData.BUS_ENTITY2_ID, hierarchyId);
            q.setParameter(BusEntityAssocData.BUS_ENTITY_ASSOC_CD, RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_SITE_OF_ELEMENT);

            x.addAll(result(q, BusEntityAssocData.class));

        }


        return x;


    }


    public Map<Long, String> findLevelIdToNameMap(List<Long> levelIds) {

        logger.info("findLevelIdToNameMap()=> BEGIN. levelIds.size = " + ((levelIds != null) ? levelIds.size() : "NULL"));

        Map<Long, String> map = new HashMap<Long, String>();

        BusEntityDAO dao = new BusEntityDAOImpl(em);

        if (levelIds != null && !levelIds.isEmpty()) {
            List<BusEntityData> results = dao.find(levelIds);
            for (BusEntityData res : results) {
                map.put(res.getBusEntityId(), res.getShortDesc());
            }
        }


        logger.info("findLevelIdToNameMap()=> END. map.size = " + (map));

        return map;
    }

    /**
     * Returns a list of ids for items belonging to a given level (ie. common object)
     */
    public Long findSiteHierarchyParentLevelId(Long levelId) {

        logger.info("getSiteHierarchyParentLevelId()=> BEGIN, levelId = " + levelId);

        Long parentLevelId = null;

        BusEntityAssocDAO beaDao = new BusEntityAssocDAOImpl(em);
        List<BusEntityAssocData> beList = beaDao.findAssocs(
                levelId,
                (Long) null,
                RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_ELEMENT_OF_LEVEL);

        if (beList != null && !beList.isEmpty()) {
            if (beList.size() == 1) {
                parentLevelId = beList.get(0).getBusEntity2Id();
            } else {
                throw SystemError.moreThanOneSiteHierarchyParentLevelFound(levelId);
            }
        }

        logger.info("findSiteHierarchyParentLevelId()=> END. parentLevelId = " + parentLevelId);

        return parentLevelId;

    }

    @Override
    public List<BusEntityAssocData> findConfigurationFor(Long levelId, List<Long> assocs, String assocCd) {

        BusEntityAssocDAO dao = new BusEntityAssocDAOImpl(em);
        return dao.findAssocs(assocs, levelId, assocCd);

    }
}
