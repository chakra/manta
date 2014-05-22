package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.web.validator.ItemLoaderFormValidator;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.UploadData;
import com.espendwise.manta.model.data.UploadSkuData;
import com.espendwise.manta.model.view.UploadSkuView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.spi.Initializable;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.espendwise.manta.util.*;

import java.util.List;
import java.util.ArrayList;

@Validation(ItemLoaderFormValidator.class)
public class ItemLoaderForm extends AbstractFilterResult<SelectableObjects.SelectableObject<UploadSkuView>> implements Initializable {

    public static interface ACTION {

        public static String SAVE = "/save";
        public static String MATCH = "/match";
        public static String SHOW_MATCHED = "/showMatched";
        public static String ASSIGN_SKUS = "/assignSkus";
        public static String REMOVE_ASSIGNMENT = "/removeAssign";
        public static String CREATE_SKUS = "/createSkus";
        public static String UPDATE_SKUS = "/updateSkus";
        public static String RELOAD = "/reload";

    }

    private boolean initialize;

    private UploadData uploadData;

    private CommonsMultipartFile uploadXlsFile;
    private String step = "createNew";

    // filter
    private String category;
    private String skuName;
    private String manufName;
    private String distName;
    private String skuNumber;

    private String skuNumberTypeFilterType;
    private String matchedFilterType;

    private String columnToUpdate;
    private String columnToUpdateFilterValue;

    // source table
    private SelectableObjects<String[]>  sourceTable;
    private String[] columnTypes;

    // store properties
    private boolean autoSkuFlag;
    private boolean allowMixedCategoryAndItemUnderSameParent;

    // items
    private SelectableObjects<UploadSkuView> UploadSkuViewList;

    private SelectableObjects<UploadSkuView> itemsToMatch;

    private List<Pair<String, String>> itemPropertiesAll = RefCodeNamesKeys.getRefCodeValues(Constants.ITEM_LOADER_PROPERTY.class, false);
    private List<Pair<String, String>> itemProperties ;
    private int[] mItemPropertyMap = new int[itemPropertiesAll.size()];
    

    private List<BusEntityData> itemCertifiedCompanies;
    private List<BusEntityData> allManufacturerList;
    private List<ItemView> allCategories;


    public ItemLoaderForm() {
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public UploadData getUploadData() {
        return this.uploadData;
    }

    public void setUploadData(UploadData uploadData) {
        this.uploadData = uploadData;
    }
    

    public  CommonsMultipartFile getUploadXlsFile() {
        return this.uploadXlsFile;
    }

    public void setUploadXlsFile(CommonsMultipartFile file) {
        this.uploadXlsFile = file;
    }

    public String getStep() {
        return this.step;
    }

    public void setStep(String step) {
        this.step = step;    
    }


    public SelectableObjects<String[]> getSourceTable() {
        return this.sourceTable;
    }

    public void setSourceTable(SelectableObjects<String[]> sourceTable) {
        this.sourceTable = sourceTable;
    }

    public List<SelectableObjects.SelectableObject<String[]>> getSourceTableList() {
        return sourceTable != null ? sourceTable.getSelectableObjects() : null;
    }

    public String[] getColumnTypes() {
        return this.columnTypes;
    }

    public void setColumnTypes(String[] s) {
        this.columnTypes = s;
    }



    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory(){
        return this.category;
    }


    public String getManufName() {
        return manufName;
    }

    public void setManufName(String manufName) {
        this.manufName = manufName;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getSkuNumberTypeFilterType() {
        return skuNumberTypeFilterType;
    }
    public void setSkuNumberTypeFilterType(String skuNumberTypeFilterType) {
        this.skuNumberTypeFilterType = skuNumberTypeFilterType;
    }

    public String getMatchedFilterType() {
        return matchedFilterType;
    }
    public void setMatchedFilterType(String matchedFilterType) {
        this.matchedFilterType = matchedFilterType;
    }

    public String getColumnToUpdate() {
        return columnToUpdate;
    }

    public void setColumnToUpdate(String columnToUpdate) {
        this.columnToUpdate = columnToUpdate;
    }

    public String getColumnToUpdateFilterValue() {
        return this.columnToUpdateFilterValue;
    };
    public void setColumnToUpdateFilterValue(String columnToUpdateFilterValue) {
        this.columnToUpdateFilterValue = columnToUpdateFilterValue;
    }

    public List  getItemCertifiedCompanies() {
        return this.itemCertifiedCompanies;
    }
    public void setItemCertifiedCompanies(List v) {
        this.itemCertifiedCompanies = v;
    }

    public List getAllManufacturerList() {
        return this.allManufacturerList;
    }
    public void setAllManufacturerList(List v) {
        this.allManufacturerList = v;
    }

    public List getAllCategories() {
        return this.allCategories;
    }

    public void setAllCategories(List v) {
        this.allCategories = v;
    }

    public boolean getAutoSkuFlag() {
        return this.autoSkuFlag;
    }

    public void setAutoSkuFlag(boolean autoSkuFlag) {
        this.autoSkuFlag = autoSkuFlag;
    }

    public void setAllowMixedCategoryAndItemUnderSameParent(boolean pAllowMixedCategoryAndItemUnderSameParent) {
        this.allowMixedCategoryAndItemUnderSameParent = pAllowMixedCategoryAndItemUnderSameParent;
    }

    public boolean getAllowMixedCategoryAndItemUnderSameParent() {
        return allowMixedCategoryAndItemUnderSameParent;
    }

    public SelectableObjects<UploadSkuView> getUploadSkuViewList() {
        return this.UploadSkuViewList;
    }
    public void setUploadSkuViewList(SelectableObjects<UploadSkuView> v) {
        this.UploadSkuViewList = v;
    }


    public SelectableObjects<UploadSkuView> getItemsToMatch() {
        return this.itemsToMatch;
    }
    public void setItemsToMatch(SelectableObjects<UploadSkuView> v) {
        this.itemsToMatch = v;
    }
    public List<SelectableObjects.SelectableObject<UploadSkuView>> getItemsToMatchList() {
        return itemsToMatch != null ? itemsToMatch.getSelectableObjects() : null;
    }


    @Override
    public List<SelectableObjects.SelectableObject<UploadSkuView>> getResult() {
        return UploadSkuViewList != null ? UploadSkuViewList.getSelectableObjects() : null;
    }


    @Override
    public void initialize() {
        initialize = true;
    }

    @Override
    public boolean isInitialized() {
        return  initialize;
    }

    @Override
    public void reset() {
        //tableId = new Long(0);
        //tableStatus = null;
        step = "createNew";

        category = null;
        skuName = null;
        manufName = null;
        distName = null;
        skuNumber = null;

        skuNumberTypeFilterType = RefCodeNames.SKU_TYPE_CD.STORE;
        matchedFilterType = RefCodeNames.ITEM_LOADER_MATCH_FILTER_CD.ALL;

        columnToUpdate = null;
        columnToUpdateFilterValue  = null;

        itemCertifiedCompanies = null;
    }

    public List<Pair<String, String>> getItemProperties() {
        return itemProperties;
    }

    public void setItemProperties(List<Pair<String, String>> itemProperties) {
        this.itemProperties = itemProperties;
    }

    public List<Pair<String, String>> getItemPropertiesAll() {
        return itemPropertiesAll;
    }

    public void setItemPropertiesAll(List<Pair<String, String>> itemProperties) {
        this.itemPropertiesAll = itemProperties;
    }

    public int[] getItemPropertyMap() { return mItemPropertyMap;}
    public void setItemPropertyMap(int[] pItemPropertyMap) {mItemPropertyMap = pItemPropertyMap;}

    public boolean getIsNew() {
        return isNew();
    }

    public boolean isNew() {
      return isInitialized() &&
             (uploadData == null || uploadData.getUploadId() == null || uploadData.getUploadId() == 0);
    }


  static public String getSkuProperty(SelectableObjects.SelectableObject<UploadSkuView> usD, String property) {
    return getSkuProperty(usD.getValue(), property);  
  }

  static public String getSkuProperty(UploadSkuView usV, String property) {
    UploadSkuData usD = usV.getUploadSkuData();
    if (usD == null) {
        return null;
    }
    String valS = null;
    if (Constants.ITEM_LOADER_PROPERTY.SKU_NUM.equals(property))
      return usD.getSkuNum();

    if (Constants.ITEM_LOADER_PROPERTY.SHORT_DESC.equals(property)) {
      valS = usD.getShortDesc();
    }
    if (Constants.ITEM_LOADER_PROPERTY.LONG_DESC.equals(property)) {
      valS = usD.getLongDesc();
    }
    if (Constants.ITEM_LOADER_PROPERTY.OTHER_DESC.equals(property)) {
      valS = usD.getOtherDesc();
    }
    if (Constants.ITEM_LOADER_PROPERTY.UNSPSC.equals(property)) {
      valS = usD.getUnspscCode();
    }
    if (Constants.ITEM_LOADER_PROPERTY.NSN.equals(property)) {
      valS = usD.getNsn();
    }
    if (Constants.ITEM_LOADER_PROPERTY.PSN.equals(property)) {
      valS = usD.getPsn();
    }
    if (Constants.ITEM_LOADER_PROPERTY.COLOR.equals(property)) {
      valS = usD.getSkuColor();
    }
    if (Constants.ITEM_LOADER_PROPERTY.PACK.equals(property)) {
      valS = usD.getSkuPack();
    }
    if (Constants.ITEM_LOADER_PROPERTY.SIZE.equals(property)) {
      valS = usD.getSkuSize();
    }
    if (Constants.ITEM_LOADER_PROPERTY.UOM.equals(property)) {
      valS = usD.getSkuUom();
    }
    if (Constants.ITEM_LOADER_PROPERTY.CATEGORY.equals(property)) {
        //String categoryName = usD.getCategory();
        //String adminCategoryName = usD.getAdminCategory();
        //valS = CategoryUtil.encodeCategoryNames(categoryName, adminCategoryName);
        valS = usD.getCategory();
    }
    if (Constants.ITEM_LOADER_PROPERTY.MANUFACTURER.equals(property)) {
      valS = usD.getManufName();
    }
    if (Constants.ITEM_LOADER_PROPERTY.MANUF_SKU.equals(property)) {
      valS = usD.getManufSku();
    }
    if (Constants.ITEM_LOADER_PROPERTY.MANUF_PACK.equals(property)) {
      valS = usD.getManufPack();
    }
    if (Constants.ITEM_LOADER_PROPERTY.MANUF_UOM.equals(property)) {
      valS = usD.getManufUom();
    }

    if (Constants.ITEM_LOADER_PROPERTY.DISTRIBUTOR.equals(property)) {
      valS = usD.getDistName();
    }
    if (Constants.ITEM_LOADER_PROPERTY.DIST_SKU.equals(property)) {
      valS = usD.getDistSku();
    }
    if (Constants.ITEM_LOADER_PROPERTY.DIST_PACK.equals(property)) {
      valS = usD.getDistPack();
    }
   if (Constants.ITEM_LOADER_PROPERTY.DIST_UOM.equals(property)) {
      valS = usD.getDistUom();
    }
    if (Constants.ITEM_LOADER_PROPERTY.GEN_MANUFACTURER.equals(property)) {
      valS = usD.getGenManufName();
    }
    if (Constants.ITEM_LOADER_PROPERTY.GEN_MANUF_SKU.equals(property)) {
      valS = usD.getGenManufSku();
    }

   if (Constants.ITEM_LOADER_PROPERTY.LIST_PRICE.equals(property)) {
      valS = usD.getListPrice();
    }
    if (Constants.ITEM_LOADER_PROPERTY.DIST_COST.equals(property)) {
      valS = usD.getDistCost();
    }
    if (Constants.ITEM_LOADER_PROPERTY.MFCP.equals(property)) {
      valS = usD.getMfcp();
    }
    if (Constants.ITEM_LOADER_PROPERTY.BASE_COST.equals(property)) {
      valS = usD.getBaseCost();
    }
    if (Constants.ITEM_LOADER_PROPERTY.DIST_UOM_MULTI.equals(property)) {
      valS = (usD.getDistUomMult());
    }
    if (Constants.ITEM_LOADER_PROPERTY.SPL.equals(property)) {
      valS = usD.getSpl();
    }
    if (Constants.ITEM_LOADER_PROPERTY.TAX_EXEMPT.equals(property)) {
      valS = usD.getTaxExempt();
    }
    if (Constants.ITEM_LOADER_PROPERTY.CATALOG_PRICE.equals(property)) {
      valS = usD.getCatalogPrice();
    }
    if (Constants.ITEM_LOADER_PROPERTY.IMAGE_URL.equals(property)) {
      valS = usD.getImageUrl();
    }
   if (Constants.ITEM_LOADER_PROPERTY.MSDS_URL.equals(property)) {
      valS = usD.getMsdsUrl();
    }
    if (Constants.ITEM_LOADER_PROPERTY.DED_URL.equals(property)) {
      valS = usD.getDedUrl();
    }
    if (Constants.ITEM_LOADER_PROPERTY.PROD_SPEC_URL.equals(property)) {
      valS = usD.getProdSpecUrl();
    }
    if (Constants.ITEM_LOADER_PROPERTY.GREEN_CERTIFIED.equals(property)) {
      valS = usD.getGreenCertified();
    }
    if (Constants.ITEM_LOADER_PROPERTY.CUSTOMER_SKU.equals(property)) {
        valS = usD.getCustomerSkuNum();
    }
    if (Constants.ITEM_LOADER_PROPERTY.SHIPPING_WEIGHT.equals(property)) {
        valS = usD.getShipWeight();
    }
    if (Constants.ITEM_LOADER_PROPERTY.WEIGHT_UNIT.equals(property)) {
        valS = usD.getWeightUnit();
    }
    if (Constants.ITEM_LOADER_PROPERTY.CUSTOMER_DESC.equals(property)) {
        valS = usD.getCustomerDesc();
    }
    if (Constants.ITEM_LOADER_PROPERTY.THUMBNAIL_URL.equals(property)) {
        valS = usD.getThumbnailUrl();
    }
    if (Constants.ITEM_LOADER_PROPERTY.SERVICE_FEE_CODE.equals(property)) {
        valS = usD.getServiceFeeCode();
    }
    return valS;
  }


  static public void setSkuProperty(UploadSkuView usV, String property, String valS) {
    UploadSkuData usD = usV.getUploadSkuData();
    if (usD == null) {
        usD = new UploadSkuData();
        usV.setUploadSkuData(usD);
    }
    if (Constants.ITEM_LOADER_PROPERTY.SKU_NUM.equals(property))
      usD.setSkuNum(valS);

    if (Constants.ITEM_LOADER_PROPERTY.SHORT_DESC.equals(property)) {
      usD.setShortDesc(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.LONG_DESC.equals(property)) {
      usD.setLongDesc(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.OTHER_DESC.equals(property)) {
      usD.setOtherDesc(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.UNSPSC.equals(property)) {
      usD.setUnspscCode(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.NSN.equals(property)) {
      usD.setNsn(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.PSN.equals(property)) {
      usD.setPsn(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.COLOR.equals(property)) {
      usD.setSkuColor(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.PACK.equals(property)) {
      usD.setSkuPack(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.SIZE.equals(property)) {
      usD.setSkuSize(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.UOM.equals(property)) {
      usD.setSkuUom(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.CATEGORY.equals(property)) {
        //String categoryName = usD.getCategory();
        //String adminCategoryName = usD.getAdminCategory();
        //valS = CategoryUtil.encodeCategoryNames(categoryName, adminCategoryName);
        usD.setCategory(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.MANUFACTURER.equals(property)) {
      usD.setManufName(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.MANUF_SKU.equals(property)) {
      usD.setManufSku(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.MANUF_PACK.equals(property)) {
      usD.setManufPack(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.MANUF_UOM.equals(property)) {
      usD.setManufUom(valS);
    }

    if (Constants.ITEM_LOADER_PROPERTY.DISTRIBUTOR.equals(property)) {
      usD.setDistName(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.DIST_SKU.equals(property)) {
      usD.setDistSku(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.DIST_PACK.equals(property)) {
      usD.setDistPack(valS);
    }
   if (Constants.ITEM_LOADER_PROPERTY.DIST_UOM.equals(property)) {
      usD.setDistUom(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.GEN_MANUFACTURER.equals(property)) {
      usD.setGenManufName(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.GEN_MANUF_SKU.equals(property)) {
      usD.setGenManufSku(valS);
    }

   if (Constants.ITEM_LOADER_PROPERTY.LIST_PRICE.equals(property)) {
      usD.setListPrice(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.DIST_COST.equals(property)) {
      usD.setDistCost(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.MFCP.equals(property)) {
      usD.setMfcp(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.BASE_COST.equals(property)) {
      usD.setBaseCost(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.DIST_UOM_MULTI.equals(property)) {
      usD.setDistUomMult(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.SPL.equals(property)) {
      usD.setSpl(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.TAX_EXEMPT.equals(property)) {
      usD.setTaxExempt(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.CATALOG_PRICE.equals(property)) {
      usD.setCatalogPrice(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.IMAGE_URL.equals(property)) {
      usD.setImageUrl(valS);
    }
   if (Constants.ITEM_LOADER_PROPERTY.MSDS_URL.equals(property)) {
      usD.setMsdsUrl(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.DED_URL.equals(property)) {
      usD.setDedUrl(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.PROD_SPEC_URL.equals(property)) {
      usD.setProdSpecUrl(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.GREEN_CERTIFIED.equals(property)) {
      usD.setGreenCertified(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.CUSTOMER_SKU.equals(property)) {
        usD.setCustomerSkuNum(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.SHIPPING_WEIGHT.equals(property)) {
        usD.setShipWeight(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.WEIGHT_UNIT.equals(property)) {
        usD.setWeightUnit(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.CUSTOMER_DESC.equals(property)) {
        usD.setCustomerDesc(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.THUMBNAIL_URL.equals(property)) {
        usD.setThumbnailUrl(valS);
    }
    if (Constants.ITEM_LOADER_PROPERTY.SERVICE_FEE_CODE.equals(property)) {
        usD.setServiceFeeCode(valS);
    }

  }

  public List<Long> getUploadSkuIds() {
    if (UploadSkuViewList == null ) {
      return null;
    }
    List<Long> uploadSkuIdV = new ArrayList<Long>();
    for (UploadSkuView skuV : UploadSkuViewList.getValues()) {
      UploadSkuData usD = skuV.getUploadSkuData();
      if (usD != null && usD.getItemId() != null && usD.getItemId() > 0) {
        uploadSkuIdV.add(usD.getItemId());
      }
    }
    return uploadSkuIdV;
  }


    @Override
    public String toString() {
        return "ItemLoaderForm{" +
                "initialize=" + initialize +
                ", uploadData=" + uploadData +
                ", category=" + category +
                ", skuName=" + skuName +
                ", manufName=" + manufName +
                ", distName=" + distName +
                ", skuNumber=" + skuNumber +
                ", skuNumberTypeFilterType=" + skuNumberTypeFilterType +
                ", matchedFilterType=" + matchedFilterType +
                ", columnToUpdate=" + columnToUpdate +
                ", columnToUpdateFilterValue=" + columnToUpdateFilterValue +

                '}';
    }
}
