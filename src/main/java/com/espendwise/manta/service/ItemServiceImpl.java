package com.espendwise.manta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.apache.log4j.Logger;
import com.espendwise.manta.model.view.ItemListView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ItemMetaData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.util.criteria.ItemListViewCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.validation.ServiceLayerValidation;
import com.espendwise.manta.dao.ItemDAO;
import com.espendwise.manta.dao.ItemDAOImpl;
import com.espendwise.manta.model.view.ItemIdentView;
import com.espendwise.manta.dao.BusEntityDAO;
import com.espendwise.manta.dao.BusEntityDAOImpl;
import com.espendwise.manta.dao.CatalogDAO;
import com.espendwise.manta.dao.CatalogDAOImpl;

import com.espendwise.manta.dao.ContentDAO;
import com.espendwise.manta.dao.ContentDAOImpl;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.ArrayList;
import com.espendwise.manta.util.RefCodeNames;

import com.espendwise.manta.util.validation.rules.MasterItemUpdateConstraint;

@Service
public class ItemServiceImpl extends DataAccessService implements ItemService {

    private static final Logger logger = Logger.getLogger(ItemServiceImpl.class);

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemListView> findItemsByCriteria(ItemListViewCriteria criteria) {

        EntityManager entityManager = getEntityManager();
        ItemDAO itemDao = new ItemDAOImpl(entityManager);

        return itemDao.findItemsByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public EntityHeaderView findMasterItemHeader(Long itemId) {

        EntityManager entityManager = getEntityManager();
        ItemDAO itemDao = new ItemDAOImpl(entityManager);

        return itemDao.findItemHeader(itemId);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ItemIdentView findItemToEdit(Long storeId, Long catalogId, Long itemId) {

        EntityManager entityManager = getEntityManager();
        ItemDAO itemDao = new ItemDAOImpl(entityManager);

        return itemDao.findItemToEdit(storeId, catalogId, itemId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
     public CatalogStructureData getItemCatalogStructure(ItemListViewCriteria criteria) {
        EntityManager entityManager = getEntityManager();
        ItemDAO itemDao = new ItemDAOImpl(entityManager);

        return itemDao.getItemCatalogStructure(criteria);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ItemIdentView saveItemIdent(Long storeId,
                                       Long userId,
                                       ItemIdentView itemIdentView) throws ValidationException, DatabaseUpdateException, IllegalDataStateException {

        ServiceLayerValidation validation = new ServiceLayerValidation();

        validation.addRule(new MasterItemUpdateConstraint(storeId, itemIdentView));


        validation.validate();

        EntityManager entityManager = getEntityManager();

        ItemDAO itemDao = new ItemDAOImpl(entityManager);

        ItemIdentView item = itemDao.saveItem(storeId, itemIdentView);


        return item;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<ItemMetaData> saveItemMeta(List<ItemMetaData> metaList, Long itemId) 
        throws DatabaseUpdateException, IllegalDataStateException {

        EntityManager entityManager = getEntityManager();

        ItemDAO itemDao = new ItemDAOImpl(entityManager);

        List<ItemMetaData> list = itemDao.updateItemMeta(metaList, itemId);
        return list;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveItemImage(String path, String imageType, CommonsMultipartFile imageFile)  
                        throws DatabaseUpdateException, IllegalDataStateException {
        EntityManager entityManager = getEntityManager();
        ContentDAO contentDao = new ContentDAOImpl(entityManager);

        contentDao.addContentSaveImage(path, imageType, imageFile);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveItemImage(String path, String imageType, byte[] imageFile)
                        throws DatabaseUpdateException, IllegalDataStateException {
        EntityManager entityManager = getEntityManager();
        ContentDAO contentDao = new ContentDAOImpl(entityManager);

        contentDao.addContentSaveImage(path, imageType, imageFile);

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void copyItemImage(String oldPath, String newPath, String imageType)
        throws DatabaseUpdateException, IllegalDataStateException {
        EntityManager entityManager = getEntityManager();
        ContentDAO contentDao = new ContentDAOImpl(entityManager);

        contentDao.copyContentImage(oldPath, newPath, imageType);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeItemImage(String path)
        throws DatabaseUpdateException, IllegalDataStateException {

        EntityManager entityManager = getEntityManager();
        ContentDAO contentDao = new ContentDAOImpl(entityManager);

        contentDao.removeContent(path);
    }

    @Override
    public List<BusEntityData> getCertiriedCompanies(List<Long> certifiedCompanyIds) {
        EntityManager entityManager = getEntityManager();
        BusEntityDAO bDao = new BusEntityDAOImpl(entityManager);
        return bDao.find(certifiedCompanyIds, RefCodeNames.BUS_ENTITY_TYPE_CD.CERTIFIED_COMPANY);
    }

    @Override
    public List<ItemView> getCategories(Long catalogId) {
        EntityManager entityManager = getEntityManager();
        CatalogDAO catDao = new CatalogDAOImpl(entityManager);
        List<Long> catalogIds = new ArrayList<Long>();
        catalogIds.add(catalogId);
        return catDao.findCatalogCategories(catalogIds);
    }

    @Override
    public byte[] getImageBinaryData(String path, String contentType, String contentUsage) {
        EntityManager entityManager = getEntityManager();
        ContentDAO contentDao = new ContentDAOImpl(entityManager);
        byte[] image = contentDao.getImage(path, contentType, contentUsage);
        return image == null ? new byte[0] : image;
    }
}
