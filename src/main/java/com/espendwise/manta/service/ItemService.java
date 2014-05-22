package com.espendwise.manta.service;

import com.espendwise.manta.model.view.ItemListView;
import com.espendwise.manta.model.view.EntityHeaderView;
//import com.espendwise.manta.model.view.ItemIdentView;
import com.espendwise.manta.util.criteria.ItemListViewCriteria;
import java.util.List;
import com.espendwise.manta.model.view.ItemIdentView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.ItemMetaData;
import com.espendwise.manta.model.data.CatalogStructureData;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface ItemService {

    public List<ItemListView> findItemsByCriteria(ItemListViewCriteria criteria);

    public EntityHeaderView findMasterItemHeader(Long itemId);

    public ItemIdentView findItemToEdit(Long storeId, Long catalogId, Long itemId);

     public CatalogStructureData getItemCatalogStructure(ItemListViewCriteria criteria);

    public ItemIdentView saveItemIdent(Long storeId, Long userId, ItemIdentView itemIdentView) throws DatabaseUpdateException, IllegalDataStateException;

    public List<ItemMetaData> saveItemMeta(List<ItemMetaData> metaList, Long itemId) throws DatabaseUpdateException, IllegalDataStateException;

    public void saveItemImage(String path, String imageType, CommonsMultipartFile imageFile)  throws DatabaseUpdateException, IllegalDataStateException;

    public void saveItemImage(String path, String imageType, byte[] imageFile)  throws DatabaseUpdateException, IllegalDataStateException;

    public void copyItemImage(String oldPath, String newPath, String imageType)  throws DatabaseUpdateException, IllegalDataStateException;

    public void removeItemImage(String path)  throws DatabaseUpdateException, IllegalDataStateException;

    public List<BusEntityData> getCertiriedCompanies(List<Long> certifiedCompanyIds);

    public List<ItemView> getCategories(Long catalogId);

    public byte[] getImageBinaryData(String path, String contentType, String contentUsage);
}
