package com.espendwise.manta.dao;

import com.espendwise.manta.model.data.ItemMappingData;
import com.espendwise.manta.model.data.ItemMetaData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.model.view.ItemListView;
import com.espendwise.manta.model.view.EntityHeaderView;
import com.espendwise.manta.util.criteria.ItemListViewCriteria;
import com.espendwise.manta.model.view.ItemIdentView;

import java.util.List;

public interface ItemDAO {

    public List<ItemListView> findItemsByCriteria(ItemListViewCriteria criteria);

    public CatalogStructureData getItemCatalogStructure(ItemListViewCriteria criteria);

    EntityHeaderView findItemHeader(Long itemId);

    public ItemIdentView findItemToEdit(Long storeId, Long catalogId, Long itemId);

    public ItemIdentView saveItem(Long storeId, ItemIdentView itemIdentView);
    public List<ItemMetaData> updateItemMeta(List<ItemMetaData> metaList, Long itemId);

    public List<ItemMappingData> getDistItemMapping(Long shoppingCatalogId, String sku, String uom);

	CatalogStructureData getCatalogItemByStoreSku(Long catalogId, Long storeSkuNum);
}
