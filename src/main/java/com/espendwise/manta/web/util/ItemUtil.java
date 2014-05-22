package com.espendwise.manta.web.util;

import com.espendwise.manta.model.data.ItemMetaData;
import com.espendwise.manta.model.data.ItemMappingData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.view.ItemIdentView;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.RefCodeNames;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;


/**
 * Created by IntelliJ IDEA.
 * User: Vedena
 * Date: Aug 1, 2013
 * Time: 2:34:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemUtil {

    public static void updateMeta(String nameValue, String value, Long itemId, List<ItemMetaData> metaList) {
        ItemMetaData m = null;
         if (metaList!=null) {
            for (ItemMetaData mD : metaList) {
                if (mD.getNameValue().equals(nameValue)) {
                    m = mD;
                    break;
                }
            }
        }
        if (m == null){
            if (Utility.isSet(value)) {
                ItemMetaData newM = new ItemMetaData();
                newM.setNameValue(nameValue);
                newM.setItemId(itemId);
                newM.setValue(value);
                metaList.add(newM);
            }
        } else {
            if (Utility.isSet(value)) {
               m.setValue(value);
            } else {
                metaList.remove(m);
            }
        }
    }

    public static void  updateManufactureMapping(Long itemId, String manufSku, Long manufId, List<ItemMappingData> mappingList) {

        if (mappingList == null) {
            mappingList = new ArrayList<ItemMappingData>();
        }

        boolean found = false;
        for (ItemMappingData m : mappingList) {
            if (m.getItemMappingCd().equals(RefCodeNames.ITEM_MAPPING_CD.ITEM_MANUFACTURER)) {
                found = true;
                m.setItemNum(manufSku);
                m.setBusEntityId(manufId);
                break;
            }
        }
        if (!found) {
            ItemMappingData m = new ItemMappingData();
            m.setItemMappingCd(RefCodeNames.ITEM_MAPPING_CD.ITEM_MANUFACTURER);
            m.setItemNum(manufSku);
            m.setBusEntityId(manufId);
            m.setItemId(itemId);
            mappingList.add(m);
        }

    }

    public static List<ItemMappingData> getItemDistributorsMapping(ItemIdentView item) {
        List<ItemMappingData> result = new ArrayList<ItemMappingData>();
        if (item.getItemMapping() == null) {
            return result;
        }
        for (ItemMappingData m : item.getItemMapping()) {
            if (m.getItemMappingCd().equals(RefCodeNames.ITEM_MAPPING_CD.ITEM_DISTRIBUTOR)) {
                result.add(m);
            }
        }
        return result;
    }

    public static void updateItemDistributorsMapping(ItemIdentView item, 
                    List<ItemMappingData> distMappingList,
                    String distUomMultS, String spl) {
        if (distMappingList == null) {
            return;
        }

        List<ItemMappingData> itemMapping =  item.getItemMapping();
        if (item.getItemMapping() == null) {
            new ArrayList<ItemMappingData>();
        }
        ItemData itemD = item.getItemData();
        if (itemD == null) {
            itemD = new ItemData();
            item.setItemData(itemD);
        }
        Long itemId = item.getItemData().getItemId();
        BigDecimal distUomMils = new BigDecimal(distUomMultS);

        for (ItemMappingData distM : distMappingList) {
            boolean found = false;
            for (ItemMappingData m : itemMapping) {
                if (m.getItemMappingCd().equals(RefCodeNames.ITEM_MAPPING_CD.ITEM_DISTRIBUTOR) &&
                    distM.getBusEntityId().equals(m.getBusEntityId())) {
                        m.setItemNum(distM.getItemNum());
                        m.setItemPack(distM.getItemPack());
                        m.setItemUom(distM.getItemUom());
                        if (Utility.isSet(distUomMultS) ) {
                            m.setUomConvMultiplier(distUomMils);
                        }
                        if (Utility.isSet(spl)) {
                            m.setStandardProductList(spl);
                        }
                        found = true;
                        break;
                }
            }
            if (!found) {
                ItemMappingData distItemMapD = new ItemMappingData();
                 distItemMapD.setBusEntityId(distM.getBusEntityId());
                 distItemMapD.setItemId(itemId);
                 distItemMapD.setItemMappingCd(RefCodeNames.ITEM_MAPPING_CD.ITEM_DISTRIBUTOR);
                 distItemMapD.setItemNum(distM.getItemNum());
                 distItemMapD.setItemPack(distM.getItemPack());
                 distItemMapD.setItemUom(distM.getItemUom());
                 distItemMapD.setStatusCd(RefCodeNames.ITEM_STATUS_CD.ACTIVE);
                 distItemMapD.setUomConvMultiplier(distUomMils);
                 distItemMapD.setStandardProductList(spl);

                 itemMapping.add(distItemMapD);
            }
        }
        item.setItemMapping(itemMapping);
    }

    public static byte[] loadFileFromUrl(String pUrl, String pRequiredMimeType) throws Exception {
		if (pUrl == null || pUrl.length() == 0) {
			return null;
		}
		try {
			return HttpClientUtil.load(pUrl, pRequiredMimeType);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Cann't load data from '" + pUrl + "'!");
		}
	}

    public static String fillOutAttachment(String fileName, String type, Long itemId) {
        if (!Utility.isSet(fileName)) {
            return null;
        }
        String fileExt = "";
            int i = fileName.lastIndexOf(".");
            if (i >= 0) {
                fileExt = fileName.substring(i);
            }
        String basepath =
	          "/en/products/" + type + "/"
	            + String.valueOf(itemId)
	            + fileExt;
	    return basepath;
    }





}
