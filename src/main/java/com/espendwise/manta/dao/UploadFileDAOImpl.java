package com.espendwise.manta.dao;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.espendwise.manta.model.view.*;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.fullentity.BusEntityFullEntity;
import com.espendwise.manta.util.criteria.UploadFileListViewCriteria;
import com.espendwise.manta.util.criteria.ItemListViewCriteria;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.QueryHelp;
import com.espendwise.manta.util.Constants;

import com.espendwise.manta.dao.ItemDAO;
import com.espendwise.manta.dao.ItemDAOImpl;


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class UploadFileDAOImpl extends DAOImpl implements UploadFileDAO{

    private static final Logger logger = Logger.getLogger(UploadFileDAOImpl.class);

    public UploadFileDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<UploadFileListView> findUploadFilesByCriteria(UploadFileListViewCriteria criteria) {

        logger.info("findUploadFilesByCriteria()=> BEGIN, criteria: "+criteria);
        List statuses = new ArrayList();
        if (Utility.isTrue(criteria.getProcessing())) {
            statuses.add(RefCodeNames.UPLOAD_STATUS_CD.PROCESSING);
        }
        if (Utility.isTrue(criteria.getProcessed())) {
            statuses.add(RefCodeNames.UPLOAD_STATUS_CD.PROCESSED);
        }

        StringBuilder baseQuery = new StringBuilder("Select distinct " +
                "new com.espendwise.manta.model.view.UploadFileListView(" +
                "    uploadFile.uploadId," +
                "    uploadFile.fileName," +
                "    uploadFile.addDate, " +
                "    uploadFile.modDate, " +
                "    uploadFile.uploadStatusCd) " +
                " from UploadData uploadFile " +
                "    where uploadFile.storeId = (:storeId) ");
        if (Utility.isSet(criteria.getUploadId()))  {
            baseQuery.append(" and uploadFile.uploadId = (:uploadId) ");
        }
        if (Utility.isSet(criteria.getUploadFileName())) {
            baseQuery.append(" and upper(uploadFile.fileName) like (:uploadFileName) ");
        }
        if (Utility.isSet(criteria.getAddDate())) {
            baseQuery.append(" and uploadFile.addDate >= (:addDate) ");
        }
        if (Utility.isSet(criteria.getModifiedDate())) {
            baseQuery.append(" and uploadFile.modDate >= (:modifiedDate) ");
        }
        if (!statuses.isEmpty()) {
            baseQuery.append(" and uploadFile.uploadStatusCd in (:statuses) ");
        }

        Query q = em.createQuery(baseQuery.toString());
        q.setParameter("storeId", criteria.getStoreId());

        if (Utility.isSet(criteria.getUploadId()))  {
            q.setParameter("uploadId", criteria.getUploadId());
        }
        if (Utility.isSet(criteria.getUploadFileName())) {
            q.setParameter("uploadFileName",
                    QueryHelp.toFilterValue(
                            criteria.getUploadFileName().toUpperCase(),
                            criteria.getUploadFileNameFilterType())
            );
        }
        if (Utility.isSet(criteria.getAddDate())) {
            q.setParameter("addDate", criteria.getAddDate());
        }
        if (Utility.isSet(criteria.getModifiedDate())) {
            q.setParameter("modifiedDate", criteria.getModifiedDate());
        }
         if (!statuses.isEmpty()) {
             q.setParameter("statuses", statuses);
         }


        if (criteria.getLimit() != null) {
            q.setMaxResults(criteria.getLimit());
        }

        List<UploadFileListView> r = q.getResultList();

        logger.info("findUploadFilesByCriteria => END, fetched : " + r.size() + " rows");

        return r;
    }

    @Override
    public UploadData findUploadData(Long uploadId) {
        logger.info("findUploadData()=> BEGIN, uploadId: " + uploadId);

        String baseQuery = "select uploadData from UploadData uploadData where uploadId = (:uploadId) ";

        Query q = em.createQuery(baseQuery);
        q.setParameter("uploadId", uploadId);

        List<UploadData> r = q.getResultList();

        logger.info("findUploadData => END, fetched : " + r.size() + " rows");

        return !r.isEmpty() ? r.get(0) : null;

    }

    @Override
    public List<UploadSkuData> findUploadSkuDataList(Long tableId) {
        logger.info("findUploadSkuDataList()=> BEGIN, tableId: "+tableId);

        String baseQuery = "select skuData from UploadSkuData skuData where uploadId = (:tableId) order by skuData.rowNum";

        Query q = em.createQuery(baseQuery);
        q.setParameter("tableId", tableId);


        List<UploadSkuData> r = q.getResultList();

        logger.info("findUploadSkuDataList => END, fetched : " + r.size() + " rows");

        return r;
    }

    @Override
    public List<UploadSkuView> findUploadSkuViewList(Long tableId) {
        List<UploadSkuData> skuDList = findUploadSkuDataList(tableId);

        List<UploadSkuView> skuVList = new ArrayList<UploadSkuView>();

        if (skuDList != null) {
            for (UploadSkuData skuD : skuDList) {
                UploadSkuView skuV = new UploadSkuView();
                skuV.setUploadSkuData(skuD);
                skuVList.add(skuV);
            }
        }

        return skuVList;

    }


    @Override
    public EntityHeaderView findUploadFileHeader(Long uploadId) {

        Query q = em.createQuery("Select new com.espendwise.manta.model.view.EntityHeaderView(uploadFile.uploadId, uploadFile.fileName)" +
                " from  UploadData uploadFile where uploadFile.uploadId = (:uploadId) "
        );

        q.setParameter("uploadId", uploadId);

        List x = q.getResultList();

        return !x.isEmpty() ? (EntityHeaderView) x.get(0) : null;
    }

    @Override
    public UploadData saveUploadData(Long storeId, UploadData uploadData) {
        logger.info("saveUploadData()=> BEGIN, storeId=" + storeId + ", uploadData=" + uploadData);
        if (storeId != null && uploadData != null) {
            uploadData.setStoreId(storeId);
            if (uploadData.getUploadId() == null) {
                uploadData = super.create(uploadData);
            } else {
                uploadData = super.update(uploadData);
            }

        }
        logger.info("saveUploadData()=> END");
        return uploadData;
    }

    @Override
    public UploadSkuData saveUploadSkuData(Long uploadId, UploadSkuData uploadSkuData) {
        logger.info("saveUploadSkuData()=> BEGIN, uploadId=" + uploadId + ", uploadSkuData=" + uploadSkuData);
        if (uploadId != null && uploadSkuData != null) {
            uploadSkuData.setUploadId(uploadId);
            if (uploadSkuData.getUploadSkuId() == null) {
                uploadSkuData = super.create(uploadSkuData);
            } else {
                uploadSkuData = super.update(uploadSkuData);
            }
        }
        logger.info("saveUploadSkuData()=> END");
        return uploadSkuData;
    }



    @Override
    public List<UploadSkuData> saveUploadSkuDataList(Long uploadId, List<UploadSkuData> skuDataList) {
        logger.info("saveUploadSkuDataList()=> BEGIN, uploadId=" + uploadId);
        if (uploadId != null && skuDataList != null) {
            for (UploadSkuData skuData : skuDataList) {
                skuData = saveUploadSkuData(uploadId, skuData);
            }
        }
        logger.info("saveUploadSkuDataList()=> END");
        return skuDataList;
    }

    @Override
    public List<UploadSkuView> saveUploadSkuViewList(Long uploadId, List<UploadSkuView> skuViewList) {
        logger.info("saveUploadSkuViewList()=> BEGIN, uploadId=" + uploadId);
        if (uploadId != null && skuViewList != null) {
            for (UploadSkuView skuView : skuViewList) {
                UploadSkuData skuData = skuView.getUploadSkuData();
                skuData = saveUploadSkuData(uploadId, skuData);
            }
        }
        logger.info("saveUploadSkuViewList()=> END");
        return skuViewList;

    }

    @Override
    public List<UploadSkuView> matchItems(Long storeId, Long uploadId, List<UploadSkuView> itemsToMatch) {
        logger.info("matchItems()=> BEGIN, uploadId=" + uploadId);
        if (itemsToMatch == null || itemsToMatch.size() == 0) {
            return null;
        }
        List<UploadSkuView> matchedItems = new ArrayList<UploadSkuView>();
        Long catalogId = getStoreCatalogId(storeId);

        // all active items for store catalog
        Query q = em.createQuery(
            "select distinct catalogStructure from CatalogStructureData catalogStructure " +
                    "where " +
                    "   catalogStructure.catalogId.catalogId = (:catalogId) " +
                    "   and catalogStructure.catalogStructureCd = (:catalogStructureCd)"
        );

        q.setParameter("catalogId", catalogId);
        q.setParameter("catalogStructureCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);

        List<CatalogStructureData> storeItems = q.getResultList();

        // items by dist mappings
        q = em.createQuery(
            "select itemMapping " +
                    " from ItemMappingData itemMapping, CatalogStructureData catalogItem, ItemData item " +
                    " where itemMapping.itemId = catalogItem.itemId " +
                  //  " and itemMapping.busEntityId = catalogItem.busEntityId " +
                    " and catalogItem.catalogId = (:catalogId) " +
                    " and itemMapping.itemMappingCd = (:itemMappingCd) " +
                    " and catalogItem.catalogStructureCd = (:catalogStructureCd) " +
                    " and itemMapping.itemId = item.itemId " +
                    " and item.itemStatusCd = (:statusCd)" +
                 " order by itemMapping.busEntityId "
        );
        q.setParameter("catalogId", catalogId);
        q.setParameter("itemMappingCd", RefCodeNames.ITEM_MAPPING_CD.ITEM_DISTRIBUTOR);
        q.setParameter("statusCd", RefCodeNames.ITEM_STATUS_CD.ACTIVE);
        q.setParameter("catalogStructureCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);

        List<ItemMappingData> itemDistMappings = q.getResultList();

         // items by manuf mapping
         q = em.createQuery(
            "select itemMapping " +
                    " from ItemMappingData itemMapping, CatalogStructureData catalogItem, ItemData item " +
                    " where itemMapping.itemId = catalogItem.itemId " +
                    " and itemMapping.busEntityId is not null " +
                    " and catalogItem.catalogId = (:catalogId) " +
                    " and itemMapping.itemMappingCd = (:itemMappingCd) " +
                    " and catalogItem.catalogStructureCd = (:catalogStructureCd) " +
                    " and itemMapping.itemId = item.itemId " +
                    " and item.itemStatusCd = (:statusCd)" +
                 " order by itemMapping.busEntityId "
        );
        q.setParameter("catalogId", catalogId);
        q.setParameter("itemMappingCd", RefCodeNames.ITEM_MAPPING_CD.ITEM_MANUFACTURER);
        q.setParameter("statusCd", RefCodeNames.ITEM_STATUS_CD.ACTIVE);
        q.setParameter("catalogStructureCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);

        List<ItemMappingData> itemManufMappings = q.getResultList();

        List manufIdV = new ArrayList<Long>();
        Long prevManufId = new Long(0);
        for (ItemMappingData iMapping : itemManufMappings) {
            Long manufId = iMapping.getBusEntityId();
            if (manufId != null && !prevManufId.equals(manufId)) {
                prevManufId = manufId;
                manufIdV.add(manufId);
            }
        }

        // manuf
        List<BusEntityData> manufDV = new ArrayList<BusEntityData>();
        if (manufIdV.size() > 0) {
            q = em.createQuery(" select busEntity from BusEntityData busEntity where busEntity.busEntityId in (:ids) ");
            q.setParameter("ids", manufIdV);
            manufDV = q.getResultList();
        }

         HashMap manufSkuHM = new HashMap();
         BusEntityData wrkManufD = null;
         for(Iterator iter=itemManufMappings.iterator(),iter1=manufDV.iterator(); iter.hasNext();) {
            ItemMappingData imD = (ItemMappingData) iter.next();
            Long manufId = imD.getBusEntityId();
            //if (manufId )
            while(wrkManufD!=null || iter1.hasNext()) {
              if(wrkManufD==null) wrkManufD = (BusEntityData) iter1.next();
              Long mId = wrkManufD.getBusEntityId();
              if(manufId>mId) {
                wrkManufD = null;
                continue;
              }
              if(manufId<mId) {
                break;
              }
              String key = wrkManufD.getShortDesc()+":"+imD.getItemNum();
              LinkedList imDLL = (LinkedList) manufSkuHM.get(key);
              if(imDLL==null) {
                imDLL = new LinkedList();
                imDLL.add(imD);
                manufSkuHM.put(key,imDLL);
              } else {
                imDLL.add(imD);
              }
              break;
            }
         }

          for(UploadSkuView usV : itemsToMatch) {
            UploadSkuData usD = usV.getUploadSkuData();

            List<UploadSkuView> musVwV = new ArrayList<UploadSkuView>();
            String skuNum = usD.getSkuNum();
            if(Utility.isSet(skuNum)) {
              for(CatalogStructureData csD : storeItems) {
                if(skuNum.equals(csD.getCustomerSkuNum())) {
                  UploadSkuView usVw = new UploadSkuView();
                  UploadSkuData matchUsD = new UploadSkuData();
                  usVw.setUploadSkuData(matchUsD);
                  matchUsD.setItemId(csD.getItemId());
                  matchUsD.setUploadId(usD.getUploadId());
                  matchUsD.setRowNum(usD.getRowNum());
                  matchUsD.setSkuNum(skuNum);
                  musVwV.add(usVw);
                  break;
                }
              }
            }
            boolean cwSkuMatchFl = (musVwV.size()>0)?true:false;
            if(!cwSkuMatchFl) { //Do not need match distributor if sku matched
              String distSku = usD.getDistSku();
              if(distSku!=null && !"N/A".equalsIgnoreCase(distSku)) {
                distSku = distSku.trim();
                if(distSku.startsWith("0")) {
                  distSku = cutLeadingZero(distSku);
                }
                for(ItemMappingData imD : itemDistMappings) {

                  String distSku1 = imD.getItemNum();
                  if(distSku1==null) continue;
                  distSku1 = distSku1.trim();
                  if(distSku1.startsWith("0")) {
                    distSku1 = cutLeadingZero(distSku1);
                  }
                  if(distSku1.equalsIgnoreCase(distSku)) {
                    UploadSkuView usVw = new UploadSkuView();
                    UploadSkuData matchUsD = new UploadSkuData();
                    usVw.setUploadSkuData(matchUsD);
                    matchUsD.setItemId(imD.getItemId());
                    matchUsD.setUploadId(usD.getUploadId());
                    matchUsD.setRowNum(usD.getRowNum());
                    matchUsD.setDistId(imD.getBusEntityId());
                    matchUsD.setDistPack(imD.getItemPack());
                    matchUsD.setDistSku(imD.getItemNum());
                    matchUsD.setDistUom(imD.getItemUom());
                    matchUsD.setSpl(imD.getStandardProductList());
                    musVwV.add(usVw);
                  }
                }
              }
            }
            String manufName = usD.getManufName();
            String manufSku = usD.getManufSku();
            if(!cwSkuMatchFl) {
              if(manufSku!=null && !"N/A".equalsIgnoreCase(manufSku)) {
                manufSku = manufSku.trim();
                LinkedList imDLL = (LinkedList) manufSkuHM.get(manufName+":"+manufSku);
                if(imDLL!=null) {
                  for(Iterator manufMapIter=imDLL.iterator(); manufMapIter.hasNext();) {
                    ItemMappingData imD = (ItemMappingData) manufMapIter.next();
                    if(imD!=null) {
                      UploadSkuView usVw = null;
                      UploadSkuData matchUsD = null;
                      for(Iterator iter1=musVwV.iterator(); iter1.hasNext();) {
                        UploadSkuData mUsVw = (UploadSkuData) iter1.next();
                        if(imD.getItemId()==mUsVw.getItemId()) {
                          matchUsD = mUsVw;
                          break;
                        }
                      }
                      if(matchUsD==null) {
                        usVw = new UploadSkuView();
                        matchUsD = new UploadSkuData();
                        usVw.setUploadSkuData(matchUsD);
                        musVwV.add(usVw);
                      }
                      matchUsD.setItemId(imD.getItemId());
                      matchUsD.setUploadId(usD.getUploadId());
                      matchUsD.setRowNum(usD.getRowNum());
                      matchUsD.setManufId(imD.getBusEntityId());
                      matchUsD.setManufPack(imD.getItemPack());
                      matchUsD.setManufSku(imD.getItemNum());
                      matchUsD.setManufUom(imD.getItemUom());
                    }
                  }
                }
                if(manufSku.startsWith("0")) {
                  manufSku = cutLeadingZero(manufSku);
                  imDLL = (LinkedList) manufSkuHM.get(manufName+":"+manufSku);
                  if(imDLL!=null) {
                    for(Iterator manufMapIter=imDLL.iterator(); manufMapIter.hasNext();) {
                      ItemMappingData imD = (ItemMappingData) manufMapIter.next();
                      if(imD!=null) {
                        UploadSkuView usVw = null;
                        UploadSkuData matchUsD = null;
                        for(Iterator iter1=musVwV.iterator(); iter1.hasNext();) {
                          UploadSkuView mUsVw = (UploadSkuView) iter1.next();
                          if(imD.getItemId()==mUsVw.getUploadSkuData().getItemId()) {
                            usVw = mUsVw;
                            matchUsD = mUsVw.getUploadSkuData();
                            break;
                          }
                        }
                        if(matchUsD==null) {
                          usVw = new UploadSkuView();
                          matchUsD = new UploadSkuData();
                          usVw.setUploadSkuData(matchUsD);
                          musVwV.add(usVw);
                        }
                        matchUsD.setItemId(imD.getItemId());
                        matchUsD.setUploadId(usD.getUploadId());
                        matchUsD.setRowNum(usD.getRowNum());
                        matchUsD.setManufId(imD.getBusEntityId());
                        matchUsD.setManufPack(imD.getItemPack());
                        matchUsD.setManufSku(imD.getItemNum());
                        matchUsD.setManufUom(imD.getItemUom());
                      }
                    }
                  }
                }
              }
            } else { //  cw sku matches
              UploadSkuView mUsVw = musVwV.get(0);
              UploadSkuData matchUsD = mUsVw.getUploadSkuData();
              Long itemId = matchUsD.getItemId();
              for (ItemMappingData imD : itemManufMappings) {
                if(imD.getItemId()==itemId) {
                  matchUsD.setManufPack(imD.getItemPack());
                  matchUsD.setManufSku(imD.getItemNum());
                  matchUsD.setManufUom(imD.getItemUom());
                  break;
                }
              }
            }

            if(musVwV.size()>0) {
              matchedItems.addAll(musVwV);
            }
          }

        matchedItems = populateItemInfo(matchedItems, catalogId);
        logger.info("matchItems()=> END");
        return matchedItems;
    }

    @Override
    public List<UploadSkuView> getMatchedItems(Long storeId, Long uploadId, List<Long> pUploadSkuIds) {
          List<UploadSkuData> uploadSkuDV = findUploadSkuDataList(uploadId);

          List<Long> matchedIdV = new ArrayList<Long>();
          HashSet distNameHS = new HashSet();
          for(Iterator iter=uploadSkuDV.iterator(); iter.hasNext();) {
            UploadSkuData usD = (UploadSkuData) iter.next();
            Long itemId = usD.getItemId();
            if(itemId > 0) {
              matchedIdV.add(itemId);
            }
            String distName = usD.getDistName();
            if(Utility.isSet(distName)) {
              if(!distNameHS.contains(distName)) {
                distNameHS.add(distName);
              }
            }
          }
          List<String> distNames = new ArrayList<String>();
          for(Iterator iter=distNameHS.iterator(); iter.hasNext();) {
            String distName = (String) iter.next();
            distNames.add(distName);
          }
  logger.info("VVV: distNames.size="+distNames.size());
          Long catalogId = getStoreCatalogId(storeId);
          HashMap distItemHM = new HashMap();
          
          if (distNames.size() > 0) {
              Query q = em.createQuery(
                      "select distinct distributor " +
                      " from com.espendwise.manta.model.fullentity.BusEntityFullEntity distributor " +
                      " inner join distributor.busEntityAssocsForBusEntity1Id storeDistributor " +
                      " where storeDistributor.busEntity2Id.busEntityId = (:storeId) " +
                              "and storeDistributor.busEntityAssocCd = (:storeDistributorAssocCd) " +
                              "and distributor.shortDesc in (:distNames) " +
                              "and distributor.busEntityTypeCd = (:busEntityType)");
              q.setParameter("storeId", storeId);
              q.setParameter("storeDistributorAssocCd", RefCodeNames.BUS_ENTITY_ASSOC_CD.DISTRIBUTOR_OF_STORE);
              q.setParameter("distNames", distNames);
              q.setParameter("busEntityType", RefCodeNames.BUS_ENTITY_TYPE_CD.DISTRIBUTOR);

              List<BusEntityFullEntity> distDV = q.getResultList();
    logger.info("VVV: distDV="+distDV.size());

              List<Long> distIdV = new ArrayList<Long>();
              for(Iterator iter=distDV.iterator(); iter.hasNext();) {
                BusEntityFullEntity beD = (BusEntityFullEntity) iter.next();
                distIdV.add(beD.getBusEntityId());
              }
        logger.info("VVV: distIdV.size="+distIdV.size());


            // items by dist mappings
            q = em.createQuery(
                "select itemMapping " +
                        " from ItemMappingData itemMapping, CatalogStructureData catalogItem " +
                        " where itemMapping.itemId = catalogItem.itemId " +
                        " and itemMapping.busEntityId in (:distIds) " +
                        " and catalogItem.catalogId = (:catalogId) " +
                        " and catalogItem.itemId in (:itemIds) " +
                        " and itemMapping.itemMappingCd = (:itemMappingCd) " +
                        " and catalogItem.catalogStructureCd = (:catalogStructureCd) " +
                     " order by itemMapping.busEntityId "
            );
            q.setParameter("catalogId", catalogId);
            q.setParameter("distIds", distIdV);
            q.setParameter("itemMappingCd", RefCodeNames.ITEM_MAPPING_CD.ITEM_DISTRIBUTOR);
            q.setParameter("itemIds", matchedIdV);
            q.setParameter("catalogStructureCd", RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);

            List<ItemMappingData> distItemMappingDV = q.getResultList();

       logger.info("VVV: distItemMappingDV.size="+distItemMappingDV.size());


              for(Iterator iter=distItemMappingDV.iterator(); iter.hasNext();) {
                ItemMappingData imD = (ItemMappingData) iter.next();
                Long beId = imD.getBusEntityId();
                for(Iterator iter1=distDV.iterator(); iter1.hasNext();) {
                  BusEntityData beD = (BusEntityData) iter1.next();
                  if(beId==beD.getBusEntityId()) {
                    distItemHM.put(beD.getShortDesc()+":"+imD.getItemId(),  imD);
                    break;
                  }
                }
              }
          }
          List<UploadSkuView> matchedItems = new ArrayList<UploadSkuView>();


          for(Iterator iter = uploadSkuDV.iterator(); iter.hasNext();) {
            UploadSkuData usD = (UploadSkuData) iter.next();
            Long itemId = usD.getItemId();
            if(itemId<=0) {
              continue;
            }
            UploadSkuView usVw = new UploadSkuView();
            UploadSkuData matchUsD = new UploadSkuData();
            usVw.setUploadSkuData(matchUsD);
            matchUsD.setItemId(itemId);
            matchUsD.setUploadId(usD.getUploadId());
            matchUsD.setRowNum(usD.getRowNum());
            ItemMappingData imD = (ItemMappingData) distItemHM.get(usD.getDistName()+":"+itemId);
            if(imD!=null) {
               matchUsD.setDistId(imD.getBusEntityId());
               matchUsD.setDistPack(imD.getItemPack());
               matchUsD.setDistSku(imD.getItemNum());
               matchUsD.setDistUom(imD.getItemUom());
               matchUsD.setSpl(imD.getStandardProductList());
            }
            matchedItems.add(usVw);
          }

          matchedItems = populateItemInfo(matchedItems, catalogId);
          return matchedItems;

    }

    private List<UploadSkuView> populateItemInfo(List<UploadSkuView> items, Long catalogId) {
        ItemDAO itemDao = new ItemDAOImpl(em);
        ItemListViewCriteria criteria = new ItemListViewCriteria();
        criteria.setItemIds(Utility.toIds(items));
        criteria.setCatalogId(catalogId);
        criteria.setShowMfgInfo(true);
        criteria.setShowDistInfo(true);
        List<ItemListView> itemListV = itemDao.findItemsByCriteria(criteria);

        for (ItemListView itemV : itemListV) {
            for (UploadSkuView uV : items) {
                UploadSkuData uD = uV.getUploadSkuData();
                if (uD.getItemId().equals(itemV.getItemId())) {
                    uD.setShortDesc(itemV.getItemName());
                    uD.setSkuPack(itemV.getPack());
                    uD.setSkuSize(itemV.getSize());
                    uD.setSkuColor(itemV.getColor());
                    uD.setSkuUom(itemV.getUom());
                    uD.setManufName(itemV.getManufacturer());
                    uD.setManufSku(itemV.getManufacturerSku());
                    break;
                }
            }
        }
        return items;
    }


    private String cutLeadingZero(String pVal) {
      char[] bbb = pVal.toCharArray();
      int pos = 0;
      for(; pos<bbb.length; pos++) {
        if(bbb[pos]!='0') break;
      }
      String retVal = new String(bbb,pos,bbb.length-pos);
      return retVal;

    }



    @Override
    public Long getStoreCatalogId(Long storeId) {
        Query q = em.createQuery(
            "select catalog.catalogId " +
                " from CatalogFullEntity catalog " +
            " inner join catalog.catalogAssocs catalogStore " +
            " inner join catalogStore.busEntityId store" +
               " where catalog.catalogTypeCd = (:catalogTypeCd) " +
                " and store.busEntityId.busEntityId = (:storeId)" +
                " and catalog.catalogStatusCd <> (:inactiveStatusCd)");
         q.setParameter("catalogTypeCd", RefCodeNames.CATALOG_TYPE_CD.STORE);
         q.setParameter("storeId", storeId);
         q.setParameter("inactiveStatusCd", RefCodeNames.CATALOG_STATUS_CD.INACTIVE);

         List x = q.getResultList();

         return !x.isEmpty() ? (Long) x.get(0) : null;
    }
    
}
