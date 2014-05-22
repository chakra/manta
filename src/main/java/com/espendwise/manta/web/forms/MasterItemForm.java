package com.espendwise.manta.web.forms;

import com.espendwise.manta.util.validation.Validation;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.web.validator.MasterItemFormValidator;
import com.espendwise.manta.spi.Initializable;
import com.espendwise.manta.model.view.ManufacturerListView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.model.data.BusEntityData;
import java.util.ArrayList;
import java.util.List;
import com.espendwise.manta.util.SelectableObjects;
import org.springframework.web.multipart.commons.*;

@Validation(MasterItemFormValidator.class)
public class MasterItemForm extends AbstractFilterResult<SelectableObjects.SelectableObject<BusEntityData>> implements Initializable {

    public static interface ACTION {
        public static String CREATE = "/create";
        public static String DELETE = "/delete";
        public static String CLONE = "/clone";
        public static String GET_QRCODE = "/getQRCode";
    }

    private boolean initialize;

    private Long itemId;
    private Long cloneItemId;

    private Long catalogId;
    private boolean autoSkuFlag;
    private String itemSku;
    private String itemName;
    private String status;
    private String longDesc;
    private String uomCd;
    private String uomValue;
    private String pack;
    private String size;
    private boolean uomAndPackVaryByGeography;

    private String manufacturerSku;
    private String manufacturerName;
    private Long manufacturerId;

    private Long itemCategoryId;

    // attachments
    private String imgFileName;
    private String thumbnailFileName;
    private String MSDSFileName;
    private String DEDFileName;
    private String specsFileName;

    private CommonsMultipartFile imgFileData;
    private CommonsMultipartFile thumbnailFileData;
    private CommonsMultipartFile MSDSFileData;
    private CommonsMultipartFile DEDFileData;
    private CommonsMultipartFile specsFileData;

    // other properties
    private String productUPC;
    private String packUPC;
    private String UNSPSCCode;
    private String shippingCubicSize;
    private String shippingWeight;
    private boolean hazmat;
    private String color;
    private String scent;
    private String listPriceMSRP;
    private String NSN;
    private String weightUnit;

    private List<BusEntityData> itemCertifiedCompanies;

    private SelectableObjects<BusEntityData> allCertifiedCompanyList;

    private List<BusEntityData> allManufacturerList;

    private List<ItemView> allCategories;

    public MasterItemForm() {
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCloneItemId() {
        return cloneItemId;
    }

    public void setCloneItemId(Long cloneItemId) {
        this.cloneItemId = cloneItemId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public boolean getAutoSkuFlag() {
        return this.autoSkuFlag;
    }

    public void setAutoSkuFlag(boolean v) {
        this.autoSkuFlag = v;
    }
    
    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getManufacturerSku() {
        return manufacturerSku;
    }

    public void setManufacturerSku(String manufacturerSku) {
        this.manufacturerSku = manufacturerSku;
    }

    public Long getItemCategoryId() {
        return this.itemCategoryId;
    }

    public void setItemCategoryId(Long v) {
        this.itemCategoryId = v;
    }


    public String getUomCd() {
        return uomCd;
    }
    public void setUomCd(String uomCd) {
        this.uomCd = uomCd;
    }

    public String getUomValue() {
        return uomValue;
    }
    public void setUomValue(String uomV) {
        this.uomValue = uomV;
    }

    public String getPack() {
        return pack;
    }
    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public boolean getUomAndPackVaryByGeography() {
        return uomAndPackVaryByGeography;
    }
    public void setUomAndPackVaryByGeography(boolean v) {
        this.uomAndPackVaryByGeography = v;
    }
    
    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Long getManufacturerId() {
        return this.manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getImgFileName() {
        return this.imgFileName;
    };
    public void setImgFileName(String v) {
        this.imgFileName = v;
    }

    public String getThumbnailFileName() {
        return this.thumbnailFileName;
    };
    public void setThumbnailFileName(String v) {
        this.thumbnailFileName = v;
    }

    public String getMSDSFileName() {
        return this.MSDSFileName;
    };
    public void setMSDSFileName(String v) {
        this.MSDSFileName = v;
    }

    public String getSpecsFileName() {
        return this.specsFileName;
    };
    public void setSpecsFileName(String v) {
        this.specsFileName = v;
    }

    public String getDEDFileName() {
        return this.DEDFileName;
    }
    public void setDEDFileName(String v) {
        this.DEDFileName = v;
    }

    public CommonsMultipartFile getImgFileData() {
        return imgFileData;
    }

    public void setImgFileData(CommonsMultipartFile f) {
        imgFileData = f;
    }

    public CommonsMultipartFile getThumbnailFileData() {
        return thumbnailFileData;
    }

    public void setThumbnailFileData(CommonsMultipartFile f) {
        thumbnailFileData = f;
    }

    public CommonsMultipartFile getMSDSFileData() {
        return MSDSFileData;
    }

    public void setMSDSFileData(CommonsMultipartFile f) {
        MSDSFileData = f;
    }

    public CommonsMultipartFile getDEDFileData() {
        return DEDFileData;
    }

    public void setDEDFileData(CommonsMultipartFile f) {
        DEDFileData = f;
    }

    public CommonsMultipartFile getSpecsFileData() {
        return specsFileData;
    }

    public void setSpecsFileData(CommonsMultipartFile f) {
        specsFileData = f;
    }



    public String getProductUPC() {
        return this.productUPC;
    }
    public void setProductUPC(String v) {
        this.productUPC = v;
    }

    public String getPackUPC() {
        return this.packUPC;
    }
    public void setPackUPC(String v) {
        this.packUPC = v;
    }


    public String getUNSPSCCode() {
        return this.UNSPSCCode;
    }
    public void setUNSPSCCode(String v) {
        this.UNSPSCCode = v;
    }

    public String getShippingCubicSize() {
        return this.shippingCubicSize;
    }
    public void setShippingCubicSize(String v) {
        this.shippingCubicSize = v;
    }

    public String getShippingWeight() {
        return this.shippingWeight;
    }
    public void setShippingWeight(String v) {
        this.shippingWeight = v;
    }

    public boolean getHazmat() {
        return this.hazmat;
    }
    public void setHazmat(boolean v) {
        this.hazmat = v;
    }

    public String getColor() {
        return this.color;
    }
    public void setColor(String v) {
        this.color = v;
    }

    public String getScent() {
        return this.scent;
    }
    public void setScent(String v) {
        this.scent = v;
    }

    public String getListPriceMSRP() {
        return this.listPriceMSRP;
    }
    public void setListPriceMSRP(String v) {
        this.listPriceMSRP = v;
    }

    public String getNSN() {
        return this.NSN;
    }
    public void setNSN(String v) {
        this.NSN = v;
    }

    public String getWeightUnit() {
        return this.weightUnit;
    }
    public void setWeightUnit(String v) {
        this.weightUnit = v;
    }
   

    public String getStatus() {
        return status;
    }

    public void setStatus(String itemStatus) {
        this.status = itemStatus;
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

    public SelectableObjects<BusEntityData> getAllCertifiedCompanyList() {
        return this.allCertifiedCompanyList;
    }
    public void setAllCertifiedCompanyList(SelectableObjects<BusEntityData> v) {
        this.allCertifiedCompanyList = v;
    }
    @Override
    public List<SelectableObjects.SelectableObject<BusEntityData>> getResult() {
        return allCertifiedCompanyList != null ? allCertifiedCompanyList.getSelectableObjects() : null;
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
        itemId = new Long("0");
        
        itemSku = null;
        itemName = null;
        status = null;
        longDesc = null;
        uomCd = null;
        uomValue = null;
        pack = null;
        size = null;
        uomAndPackVaryByGeography = false;

        manufacturerSku = null;
        manufacturerName = null;
        manufacturerId = new Long("0");

        itemCategoryId = new Long("0");
        
        imgFileName = null;
        thumbnailFileName = null;
        MSDSFileName = null;
        DEDFileName = null;
        specsFileName = null;


        productUPC = null;
        packUPC = null;
        UNSPSCCode = null;
        shippingCubicSize = null;
        shippingWeight = null;
        hazmat = false;
        color = null;
        scent = null;
        listPriceMSRP = null;
        NSN = null;
        weightUnit = null;

        itemCertifiedCompanies = null;
    }

    public boolean getIsNew() {
        return isNew();
    }

    public boolean isNew() {
      return isInitialized() && (itemId  == null || itemId == 0);
    }



    @Override
    public String toString() {
        return "MasterItemForm{" +
                "initialize=" + initialize +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemStatus='" + status + '\'' +
                ", sku='" + itemSku + '\'' +
                '}';
    }
}
