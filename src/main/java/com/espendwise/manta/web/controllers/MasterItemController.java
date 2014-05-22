package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CatalogStructureData;
import com.espendwise.manta.model.data.ItemAssocData;
import com.espendwise.manta.model.data.ItemData;
import com.espendwise.manta.model.data.ItemMappingData;
import com.espendwise.manta.model.data.ItemMetaData;
import com.espendwise.manta.model.data.PropertyData;
import com.espendwise.manta.model.view.CatalogListView;
import com.espendwise.manta.model.view.CatalogStructureListView;
import com.espendwise.manta.model.view.ItemIdentView;
import com.espendwise.manta.model.view.ItemView;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.service.ItemService;
import com.espendwise.manta.service.ManufacturerService;
import com.espendwise.manta.service.StoreService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.criteria.CatalogListViewCriteria;
import com.espendwise.manta.util.criteria.CatalogStructureListViewCriteria;
import com.espendwise.manta.util.criteria.ProductListViewCriteria;
import com.espendwise.manta.util.criteria.StoreManufacturerCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.MasterItemForm;
import com.espendwise.manta.web.resolver.MasterItemWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.ItemUtil;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;
import com.espendwise.manta.web.util.WebSort;

@Controller
@RequestMapping(UrlPathKey.MASTER_ITEM.IDENTIFICATION)

public class MasterItemController extends BaseController {

    private static final Logger logger = Logger.getLogger(MasterItemController.class);

    private ItemService itemService;
    private ManufacturerService manufacturerService;
    private CatalogService catalogService;
    private StoreService storeService;
    private Long itemClonedID;


    @Autowired
    public MasterItemController(ItemService itemService, ManufacturerService manufacturerService,
                    CatalogService catalogService, StoreService storeService) {
        this.itemService = itemService;
        this.manufacturerService = manufacturerService;
        this.catalogService = catalogService;
        this.storeService = storeService;

    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new MasterItemWebUpdateExceptionResolver());

        return "masterItem/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.MASTER_ITEM_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
   
    public String save( WebRequest request,
                        @ModelAttribute(SessionKey.MASTER_ITEM)MasterItemForm masterItemForm,
                        Model model) throws Exception {

        logger.info("save()=> BEGIN, masterItemForm: "+masterItemForm);

        WebErrors webErrors = new WebErrors(request);
      
        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(masterItemForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.MASTER_ITEM, masterItemForm);
            return "masterItem/edit";
        }
        
        

        /////////////////////////////////////////////////////////////
        //Set up Item status code
        /////////////////////////////////////////////////////////////
      
             if(RefCodeNames.ITEM_STATUS_CD.INACTIVE.equals(masterItemForm.getStatus()))
             {
                CatalogStructureListViewCriteria searchCriteria = new CatalogStructureListViewCriteria(0);
                searchCriteria.setCatalogStructureCd(RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);
 	        	searchCriteria.setItemId(masterItemForm.getItemId());
 	        	
 	        	List<CatalogStructureData> catalogStructureList = catalogService.findCatalogStructuresUsingCriteria(searchCriteria);
                 if(catalogStructureList!=null &&catalogStructureList.size()>0)
                 {
                     int size=catalogStructureList.size();
                    
                     String errMessage="";

                     for(int i=0;i<(size>10?10:size);i++)
                     {
                         errMessage=errMessage.concat((i>0?", ":"")+String.valueOf(catalogStructureList.get(i).getCatalogId()));
                     }
                     
                     logger.info("testt ::: "+errMessage);
                     if(size>10) errMessage+=" ...";
                     masterItemForm.setStatus(RefCodeNames.ITEM_STATUS_CD.ACTIVE);
                     model.addAttribute(SessionKey.MASTER_ITEM, errMessage);
                     model.addAttribute("catalogCount", size);
                     model.addAttribute("catalogIds", errMessage);
                 }
                 
                 
             }
      

        
        
        ItemIdentView itemView = new ItemIdentView();
        boolean isNew = masterItemForm.getIsNew();

        if (!isNew) {
            itemView = itemService.findItemToEdit(getStoreId(),
                                        masterItemForm.getCatalogId(),
                                        masterItemForm.getItemId()
            );
        }


        

        itemView = fillOutItemIdentView(itemView, masterItemForm);

        try {

            itemView = itemService.saveItemIdent(getStoreId(),getUserId(),itemView);


            if (isNew) {
                updateAttachments(masterItemForm, itemView);
                itemService.saveItemMeta(itemView.getItemMeta(), itemView.getItemData().getItemId());
            }

            CommonsMultipartFile thFile = masterItemForm.getThumbnailFileData();
            if (thFile != null && thFile.getSize() > 0) {
                itemService.saveItemImage(masterItemForm.getThumbnailFileName(), "ItemImage", thFile);
            } else {
                copyAttachment(masterItemForm.getThumbnailFileName(), masterItemForm.getCloneItemId(), itemView.getItemData().getItemId());
            }
            CommonsMultipartFile imageFile = masterItemForm.getImgFileData();
            if (imageFile != null && imageFile.getSize() > 0) {
                itemService.saveItemImage(masterItemForm.getImgFileName(), "ItemImage", imageFile);
            } else {
                copyAttachment(masterItemForm.getImgFileName(), masterItemForm.getCloneItemId(), itemView.getItemData().getItemId());
            }
            CommonsMultipartFile dedFile = masterItemForm.getDEDFileData();
            if (dedFile != null && dedFile.getSize() > 0) {
                itemService.saveItemImage(masterItemForm.getDEDFileName(), "ItemImage", dedFile);
            }else {
                copyAttachment(masterItemForm.getDEDFileName(), masterItemForm.getCloneItemId(), itemView.getItemData().getItemId());
            }
            CommonsMultipartFile msdsFile = masterItemForm.getMSDSFileData();
            if (msdsFile != null && msdsFile.getSize() > 0) {
                itemService.saveItemImage(masterItemForm.getMSDSFileName(), "ItemImage", msdsFile);
            } else {
                copyAttachment(masterItemForm.getMSDSFileName(), masterItemForm.getCloneItemId(), itemView.getItemData().getItemId());
            }
            CommonsMultipartFile specFile = masterItemForm.getSpecsFileData();
            if (specFile != null && specFile.getSize() > 0) {
                itemService.saveItemImage(masterItemForm.getSpecsFileName(), "ItemImage", specFile);
            }  else {
                copyAttachment(masterItemForm.getSpecsFileName(), masterItemForm.getCloneItemId(), itemView.getItemData().getItemId());
            }


        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }

        
        this.itemClonedID = null;
        masterItemForm.setCloneItemId(null);


        logger.info("redirect(()=> redirect to: " + String.valueOf(itemView.getItemData().getItemId()));
        logger.info("save()=> END.");

        return redirect(String.valueOf(itemView.getItemData().getItemId()));
    }



    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    public String clone(WebRequest request,
                  @ModelAttribute(SessionKey.MASTER_ITEM) MasterItemForm form,
                  Model model) throws Exception {

        logger.info("clone()=> BEGIN");
        WebErrors webErrors = new WebErrors(request);

        Long itemId = form.getItemId();
        Long cloneItemId = form.getCloneItemId();

        if (itemId == null || itemId.longValue() == 0) {
            if (cloneItemId == null || cloneItemId.longValue() == 0) {
                webErrors.putError("exception.web.error.masterItem.emptyCloneItem");
                return redirect(request, UrlPathKey.MASTER_ITEM.FILTER);
            }
            itemId = cloneItemId;
            ItemIdentView item = itemService.findItemToEdit(getStoreId(),
                    form.getCatalogId(),
                    itemId
            );

            if (item != null) {
                form.reset();
                fillOutItemForm(form, item);
            }

        }
        this.itemClonedID = form.getItemId();

            logger.info("this.itemClonedID="+String.valueOf(this.itemClonedID));
            logger.info("form.getItemId()="+String.valueOf(form.getItemId()));
        String title = AppI18nUtil.getMessage("admin.global.text.cloned", new String[]{form.getItemName()});
        form.setItemId(null);
        form.setCloneItemId(itemClonedID);
        form.setItemName(title);

        model.addAttribute(SessionKey.MASTER_ITEM, form);

        logger.info("clone()=> END.");

        return "masterItem/edit";

    }

    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.MASTER_ITEM) MasterItemForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new MasterItemForm();
        form.initialize();
        initFormLists(form);
       // form.setCloneMasterItemID(null);

        model.addAttribute(SessionKey.MASTER_ITEM, form);

        logger.info("create()=> END.");

        return "masterItem/edit";

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(HttpServletRequest request,
            @ModelAttribute(SessionKey.MASTER_ITEM) MasterItemForm form,
            @PathVariable("itemId") Long itemId,
            Model model) {

        logger.info("show()=> BEGIN, itemId=" + String.valueOf(itemId));

        ItemIdentView item = itemService.findItemToEdit(getStoreId(),
                form.getCatalogId(),
                itemId
        );

        if (item != null) {
            form.reset();
            fillOutItemForm(form, item);
        }

        model.addAttribute(SessionKey.MASTER_ITEM, form);

        logger.info("show()=> END.");

        return "masterItem/edit";

    }

    private void fillOutItemForm(MasterItemForm form, ItemIdentView item) {
        if (item!=null) {
            // item data
            form.setItemId(item.getItemData().getItemId());
            form.setItemName(item.getItemData().getShortDesc());
            form.setStatus(item.getItemData().getItemStatusCd());
            form.setLongDesc(item.getItemData().getLongDesc());
            // catalog structure
            CatalogStructureData catalogStructureD = item.getCatalogStructureData();
            if (catalogStructureD != null) {
                form.setItemSku(Utility.isSet(catalogStructureD.getCustomerSkuNum()) ?
                    catalogStructureD.getCustomerSkuNum() :
                    ""+item.getItemData().getSkuNum());
            } else {
                form.setItemSku(""+item.getItemData().getSkuNum());
            }
            // assocs
            List<ItemAssocData> assocs = item.getItemAssocs();

            if (assocs != null) {
                for (ItemAssocData a : assocs) {
                    if (a.getItemAssocCd().equals(RefCodeNames.ITEM_ASSOC_CD.PRODUCT_PARENT_CATEGORY)) {
                        form.setItemCategoryId(a.getItem2Id());
                        break;
                    }
                }
            }
            // item meta
            List<ItemMetaData> metaList = item.getItemMeta();
            if (metaList!=null) {
                for (ItemMetaData m : metaList) {
                    if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.COLOR)) {
                        form.setColor(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.HAZMAT)) {
                        form.setHazmat(Utility.isTrue(m.getValue()));
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.PACK)) {
                        form.setPack(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.UPC_NUM)) {
                        form.setProductUPC(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.PKG_UPC_NUM)) {
                        form.setPackUPC(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.SCENT)) {
                        form.setScent(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.SHIP_WEIGHT)) {
                        form.setShippingWeight(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.CUBE_SIZE)) {
                        form.setShippingCubicSize(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.LIST_PRICE)) {
                        form.setListPriceMSRP(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.NSN)) {
                        form.setNSN(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.SIZE)) {
                        form.setSize(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.UNSPSC_CD)) {
                        form.setUNSPSCCode(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.UOM)) {
                        form.setUomCd(WebFormUtil.fillItemUomCd(m.getValue()));
                        form.setUomValue(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.PACK_PROBLEM_SKU)) {
                        form.setUomAndPackVaryByGeography(Utility.isTrue(m.getValue()));
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.WEIGHT_UNIT)) {
                        form.setWeightUnit(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.IMAGE)) {
                        form.setImgFileName(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.THUMBNAIL)) {
                        form.setThumbnailFileName(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.DED)) {
                        form.setDEDFileName(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.MSDS)) {
                        form.setMSDSFileName(m.getValue());
                    } else if (m.getNameValue().equals(RefCodeNames.ITEM_META_CD.SPEC)) {
                        form.setSpecsFileName(m.getValue());
                    }
                }
            }
            // mappings
            List<ItemMappingData> mappingList = item.getItemMapping();
            List<Long> itemCertifiedCompanyIds = new ArrayList<Long>();
            if (mappingList != null) {
                for (ItemMappingData m : mappingList) {
                    if (m.getItemMappingCd().equals(RefCodeNames.ITEM_MAPPING_CD.ITEM_CERTIFIED_COMPANY)) {
                        itemCertifiedCompanyIds.add(m.getBusEntityId());
                    } else if (m.getItemMappingCd().equals(RefCodeNames.ITEM_MAPPING_CD.ITEM_MANUFACTURER)) {
                        form.setManufacturerSku(m.getItemNum());
                        form.setManufacturerId(m.getBusEntityId());
                    }
                }
            }
            // certified
            List<BusEntityData> certifiedCompanies = itemService.getCertiriedCompanies(null);
            List<BusEntityData> itemCertifiedCompanies =
                itemCertifiedCompanyIds.size() > 0 ?
                itemService.getCertiriedCompanies(itemCertifiedCompanyIds) :
                new ArrayList<BusEntityData>();
            SelectableObjects<BusEntityData> selectableObj = new SelectableObjects<BusEntityData>(
                    certifiedCompanies,
                    itemCertifiedCompanies,
                    AppComparator.BUS_ENTITY_ID_COMPARATOR
            );
            form.setAllCertifiedCompanyList(selectableObj);
            form.setItemCertifiedCompanies(itemCertifiedCompanies);

        }

    }

    private ItemIdentView fillOutItemIdentView(ItemIdentView item, MasterItemForm form) {
        if (item == null) {
            item = new ItemIdentView();
        }
        // item data
        ItemData itemData = item.getItemData();
        if (item.getItemData() == null) {
            itemData = new ItemData();
            itemData.setItemTypeCd(RefCodeNames.ITEM_TYPE_CD.PRODUCT);
            itemData.setSkuNum(new Long(-1));
        }
        itemData.setShortDesc(form.getItemName());
        itemData.setItemStatusCd(form.getStatus());
        itemData.setLongDesc(form.getLongDesc());

        item.setItemData(itemData);

        Long itemId = item.getItemData().getItemId();

        // catalog structure
        CatalogStructureData catalogStructureD = item.getCatalogStructureData();
        if (catalogStructureD == null) {
            catalogStructureD = new CatalogStructureData();
            catalogStructureD.setCatalogStructureCd(RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);
            catalogStructureD.setCatalogId(form.getCatalogId());
            catalogStructureD.setStatusCd(RefCodeNames.CATALOG_STATUS_CD.ACTIVE);
            catalogStructureD.setItemId(itemId);
        }
        if (!form.getAutoSkuFlag()) {
            catalogStructureD.setCustomerSkuNum(form.getItemSku());
        }
        item.setCatalogStructureData(catalogStructureD);

        // assocs
        List<ItemAssocData> assocs = item.getItemAssocs();
        if (assocs == null) {
            assocs = new ArrayList<ItemAssocData>();
            ItemAssocData categD = new ItemAssocData();
            categD.setItemAssocCd(RefCodeNames.ITEM_ASSOC_CD.PRODUCT_PARENT_CATEGORY);
            categD.setItem1Id(itemId);
            categD.setCatalogId(form.getCatalogId());
            assocs.add(categD);
        }
        for (ItemAssocData a : assocs) {
            if (a.getItemAssocCd().equals(RefCodeNames.ITEM_ASSOC_CD.PRODUCT_PARENT_CATEGORY)) {
                a.setItem2Id(form.getItemCategoryId());
                break;
            }
        }
        item.setItemAssocs(assocs);

        // item meta
        List<ItemMetaData> metaList = item.getItemMeta();
        if (metaList == null) {
            metaList = new ArrayList<ItemMetaData>();
        }

        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.COLOR, form.getColor(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.PACK, form.getPack(), itemId, metaList);
        ItemUtil.updateMeta( RefCodeNames.ITEM_META_CD.HAZMAT, Utility.isTrueStrOf(form.getHazmat()), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.UPC_NUM, form.getProductUPC(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.PKG_UPC_NUM, form.getPackUPC(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.SCENT, form.getScent(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.SHIP_WEIGHT, form.getShippingWeight(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.CUBE_SIZE, form.getShippingCubicSize(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.LIST_PRICE, form.getListPriceMSRP(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.WEIGHT_UNIT, form.getWeightUnit(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.NSN, form.getNSN(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.SIZE, form.getSize(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.UNSPSC_CD, form.getUNSPSCCode(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.UOM, form.getUomValue(), itemId, metaList);
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.PACK_PROBLEM_SKU, Utility.isTrueStrOf(form.getUomAndPackVaryByGeography()), itemId, metaList);

        // attachments  (if item is not new)
        if (itemId != null && itemId > 0) {
            updateAttachments(form, item);
        }

        item.setItemMeta(metaList);



        // mappings
        List<ItemMappingData> mappingList = item.getItemMapping();
        if (mappingList == null) {
            mappingList = new ArrayList<ItemMappingData>();
        }

        ItemUtil.updateManufactureMapping(itemId, form.getManufacturerSku(), form.getManufacturerId(), mappingList);

        List<BusEntityData> certSelected = form.getAllCertifiedCompanyList().getSelected();
        List<BusEntityData> certDeselected = form.getAllCertifiedCompanyList().getDeselected();

        for (BusEntityData certDesel : certDeselected) {
            for (ItemMappingData mm : mappingList) {
                if (mm.getItemMappingCd().equals(RefCodeNames.ITEM_MAPPING_CD.ITEM_CERTIFIED_COMPANY)) {
                    if (mm.getBusEntityId().equals(certDesel.getBusEntityId())) {
                        mappingList.remove(mm);
                        break;
                    }
                }
            }
        }
        for (BusEntityData certSel : certSelected) {
            boolean f = false;
            for (ItemMappingData m : mappingList) {
                if (m.getItemMappingCd().equals(RefCodeNames.ITEM_MAPPING_CD.ITEM_CERTIFIED_COMPANY)) {
                    if (m.getBusEntityId().equals(certSel.getBusEntityId())) {
                        f = true;
                        break;
                    }
                }
            }
            if (!f) {
                ItemMappingData m = new ItemMappingData();
                m.setItemId(itemId);
                m.setItemMappingCd(RefCodeNames.ITEM_MAPPING_CD.ITEM_CERTIFIED_COMPANY);
                m.setBusEntityId(certSel.getBusEntityId());
                mappingList.add(m);
            }
        }
        item.setItemMapping(mappingList);


        return item;
    }

    private void updateAttachments(MasterItemForm form, ItemIdentView item ) {
        updateAttachment(form.getThumbnailFileData(), form, item, RefCodeNames.ITEM_META_CD.THUMBNAIL );
        updateAttachment(form.getImgFileData(), form, item, RefCodeNames.ITEM_META_CD.IMAGE );
        updateAttachment(form.getDEDFileData(), form, item, RefCodeNames.ITEM_META_CD.DED );
        updateAttachment(form.getMSDSFileData(), form, item, RefCodeNames.ITEM_META_CD.MSDS );
        updateAttachment(form.getSpecsFileData(), form, item, RefCodeNames.ITEM_META_CD.SPEC );
    }

    private void updateAttachment(CommonsMultipartFile file, MasterItemForm form, ItemIdentView item, String type) {
        List<ItemMetaData> metaList = item.getItemMeta();
        Long itemId = item.getItemData().getItemId();
        boolean isItemCloned = form.getCloneItemId() != null && form.getCloneItemId() >= 0;
        if (type.equals(RefCodeNames.ITEM_META_CD.THUMBNAIL)) {
             if (file != null && file.getSize() > 0) {
                form.setThumbnailFileName(ItemUtil.fillOutAttachment(file.getOriginalFilename(), Constants.ITEM_ATTACHMENT_TYPE.THUMBNAIL, itemId));
                ItemUtil.updateMeta(type, form.getThumbnailFileName(), itemId, metaList);
            } else if (isItemCloned) {
                form.setThumbnailFileName(ItemUtil.fillOutAttachment(form.getThumbnailFileName(), Constants.ITEM_ATTACHMENT_TYPE.THUMBNAIL, itemId));
                ItemUtil.updateMeta(type, form.getThumbnailFileName(), itemId, metaList);
            }
        } else if (type.equals(RefCodeNames.ITEM_META_CD.IMAGE)) {
             if (file != null && file.getSize() > 0) {
                form.setImgFileName(ItemUtil.fillOutAttachment(file.getOriginalFilename(), Constants.ITEM_ATTACHMENT_TYPE.IMAGE, itemId));
                ItemUtil.updateMeta(type, form.getImgFileName(), itemId, metaList);
            } else if (isItemCloned) {
                form.setImgFileName(ItemUtil.fillOutAttachment(form.getImgFileName(), Constants.ITEM_ATTACHMENT_TYPE.IMAGE, itemId));
                ItemUtil.updateMeta(type, form.getImgFileName(), itemId, metaList);
            }
        } else if (type.equals(RefCodeNames.ITEM_META_CD.DED)) {
             if (file != null && file.getSize() > 0) {
                form.setDEDFileName(ItemUtil.fillOutAttachment(file.getOriginalFilename(), Constants.ITEM_ATTACHMENT_TYPE.DED, itemId));
                ItemUtil.updateMeta(type, form.getDEDFileName(), itemId, metaList);
            } else if (isItemCloned) {
                form.setDEDFileName(ItemUtil.fillOutAttachment(form.getDEDFileName(), Constants.ITEM_ATTACHMENT_TYPE.DED, itemId));
                ItemUtil.updateMeta(type, form.getDEDFileName(), itemId, metaList);
            }
        } else if (type.equals(RefCodeNames.ITEM_META_CD.MSDS)) {
             if (file != null && file.getSize() > 0) {
                form.setMSDSFileName(ItemUtil.fillOutAttachment(file.getOriginalFilename(), Constants.ITEM_ATTACHMENT_TYPE.MSDS, itemId));
                ItemUtil.updateMeta(type, form.getMSDSFileName(), itemId, metaList);
            } else if (isItemCloned) {
                form.setMSDSFileName(ItemUtil.fillOutAttachment(form.getMSDSFileName(), Constants.ITEM_ATTACHMENT_TYPE.MSDS, itemId));
                ItemUtil.updateMeta(type, form.getMSDSFileName(), itemId, metaList);
            }
        } else if (type.equals(RefCodeNames.ITEM_META_CD.SPEC)) {
             if (file != null && file.getSize() > 0) {
                form.setSpecsFileName(ItemUtil.fillOutAttachment(file.getOriginalFilename(), Constants.ITEM_ATTACHMENT_TYPE.SPEC, itemId));
                ItemUtil.updateMeta(type, form.getSpecsFileName(), itemId, metaList);
            } else if (isItemCloned) {
                form.setSpecsFileName(ItemUtil.fillOutAttachment(form.getSpecsFileName(), Constants.ITEM_ATTACHMENT_TYPE.SPEC, itemId));
                ItemUtil.updateMeta(type, form.getSpecsFileName(), itemId, metaList);
            }
        }

    }

    @ModelAttribute(SessionKey.MASTER_ITEM)
    public MasterItemForm initModel() {
        logger.info("initModel => BEGIN");
        MasterItemForm form = new MasterItemForm();
        form.initialize();
        initFormLists(form);
        logger.info("initModel => END");
        return form;

    }

    private void initFormLists(MasterItemForm form) {
        logger.info("initFormLists=> BEGIN");
        List<BusEntityData> storeManufacturers = getStoreManufacturers();
        WebSort.sort(storeManufacturers, BusEntityData.SHORT_DESC);
        form.setAllManufacturerList(storeManufacturers);
        Long catalogId =  getStoreCatalogId(getStoreId());
        form.setCatalogId(catalogId);
        List<ItemView> categories = itemService.getCategories(catalogId);
        form.setAllCategories(categories);

        List<BusEntityData> certifiedCompanies = itemService.getCertiriedCompanies(null);
        List<BusEntityData> itemCertifiedCompanies = new ArrayList<BusEntityData>();
        SelectableObjects<BusEntityData> selectableObj = new SelectableObjects<BusEntityData>(
                    certifiedCompanies,
                    itemCertifiedCompanies,
                    AppComparator.BUS_ENTITY_ID_COMPARATOR
        );
        form.setAllCertifiedCompanyList(selectableObj);
        form.setItemCertifiedCompanies(itemCertifiedCompanies);

        // Auto Sku flag
        List<PropertyData> storeProperties = storeService.findStoreProperties(getStoreId(),
                                             Utility.toList(RefCodeNames.PROPERTY_TYPE_CD.AUTO_SKU_ASSIGN));

        if (Utility.isSet(storeProperties)) {
            form.setAutoSkuFlag(Utility.isTrue(storeProperties.get(0).getValue()));
        }
        

        logger.info("initFormLists=> END.");
    }

    private List<BusEntityData> getStoreManufacturers() {
        StoreManufacturerCriteria criteria = new StoreManufacturerCriteria();
        criteria.setStoreId(getStoreId());
        return  manufacturerService.findManufacturers(criteria);
    }

    private Long getStoreCatalogId(Long storeId) {
        CatalogListViewCriteria catalogCriteria = new CatalogListViewCriteria(storeId, Constants.FILTER_RESULT_LIMIT.CATALOG);
        catalogCriteria.setCatalogType(RefCodeNames.CATALOG_TYPE_CD.STORE);
        catalogCriteria.setActiveOnly(true);
        List<CatalogListView> catalogs = catalogService.findCatalogsByCriteria(catalogCriteria);

        if (catalogs != null) {
            return catalogs.get(0).getCatalogId();
        } else {
            return new Long(0);
        }
    }



    @ResponseBody
    @RequestMapping(value = MasterItemForm.ACTION.GET_QRCODE)
    public byte[] getQRCode( @PathVariable(IdPathKey.ITEM_ID) Long itemId,
    						 @RequestParam("desc") String itemName) throws Exception {
        if (itemId == null) return null;
        byte[] stream = ((QRCode.from("https://IT/" + getStoreId() +"/"+ itemId + "/" + itemName).to(ImageType.GIF).withSize(175, 175).stream()).toByteArray() );
        return stream;
    }

    @ResponseBody
    @RequestMapping(value = "/getThumbnail", method = RequestMethod.GET)
    public byte[] getThumbnail( @PathVariable(IdPathKey.ITEM_ID) Long itemId,
                                @RequestParam("path") String path) throws Exception  {

        if (Utility.isSet(path)) {
            byte[] stream =  itemService.getImageBinaryData(path, null, null);
            return stream;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getItemImage", method = RequestMethod.GET)
    public byte[] getItemImage( @PathVariable(IdPathKey.ITEM_ID) Long itemId,
                                @RequestParam("path") String path) throws Exception  {
        if (Utility.isSet(path)) {
            byte[] stream =  itemService.getImageBinaryData(path, null, null);
            return stream;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getItemAttachment", method = RequestMethod.GET, produces="application/pdf")
    public byte[] getItemAttachment( @PathVariable(IdPathKey.ITEM_ID) Long itemId,
                          @RequestParam("path") String path) throws Exception  {
        if (Utility.isSet(path)) {
            byte[] stream =  itemService.getImageBinaryData(path, null, null);
            return stream;
        }
        return null;
    }

   @RequestMapping(value = "/deleteImage")
   public String deleteImage(@ModelAttribute(SessionKey.MASTER_ITEM) MasterItemForm form, Model model) throws Exception {
        return deleteFile(RefCodeNames.ITEM_META_CD.IMAGE, form, model);
   }

   @RequestMapping(value = "/deleteThumbnail")
   public String deleteThumbnail(@ModelAttribute(SessionKey.MASTER_ITEM) MasterItemForm form, Model model) throws Exception {
        return deleteFile(RefCodeNames.ITEM_META_CD.THUMBNAIL, form, model);
   }

   @RequestMapping(value = "/deleteMSDS")
   public String deleteMSDS(@ModelAttribute(SessionKey.MASTER_ITEM) MasterItemForm form, Model model) throws Exception {
        return deleteFile(RefCodeNames.ITEM_META_CD.MSDS, form, model);
   }

   @RequestMapping(value = "/deleteDED")
   public String deleteDED(@ModelAttribute(SessionKey.MASTER_ITEM) MasterItemForm form, Model model) throws Exception {
        return deleteFile(RefCodeNames.ITEM_META_CD.DED, form, model);
   }

   @RequestMapping(value = "/deleteSpecs")
   public String deleteSpecs(@ModelAttribute(SessionKey.MASTER_ITEM) MasterItemForm form, Model model) throws Exception {
        return deleteFile(RefCodeNames.ITEM_META_CD.SPEC, form, model);
   }

   private String deleteFile(String fileType, MasterItemForm form, Model model)  throws Exception  {
        logger.info("deleteFile()=> BEGIN, fileType=" + fileType);
        if (!Utility.isSet(fileType)) {
            return "masterItem/edit";
        }

        String filePath = "";
        if (fileType.equals(RefCodeNames.ITEM_META_CD.IMAGE)) {
            filePath =  form.getImgFileName();
            form.setImgFileName(null);
        } else if (fileType.equals(RefCodeNames.ITEM_META_CD.THUMBNAIL)) {
            filePath =  form.getThumbnailFileName();
            form.setThumbnailFileName(null);
        } else if (fileType.equals(RefCodeNames.ITEM_META_CD.MSDS)) {
            filePath =  form.getMSDSFileName();
            form.setMSDSFileName(null);
        } else if (fileType.equals(RefCodeNames.ITEM_META_CD.DED)) {
            filePath =  form.getDEDFileName();
            form.setDEDFileName(null);
        } else if (fileType.equals(RefCodeNames.ITEM_META_CD.SPEC)) {
            filePath =  form.getSpecsFileName();
            form.setSpecsFileName(null);
        }

        if (Utility.isSet(filePath)) {
            // remove file from meta
            Long itemId = form.getItemId();

            ItemIdentView item = itemService.findItemToEdit(getStoreId(),
                form.getCatalogId(),
                itemId
            );

            List<ItemMetaData> metaList = item.getItemMeta();
            if (metaList == null) {
                metaList = new ArrayList<ItemMetaData>();
            }
            ItemUtil.updateMeta(fileType, null, itemId, metaList);

            // save item
            itemService.saveItemIdent(getStoreId(),
                    getUserId(),
                    item
            );


            // remove file from content
            itemService.removeItemImage(filePath);

        }

        model.addAttribute(SessionKey.MASTER_ITEM, form);
        logger.info("deleteFile()=> END.");
        return "masterItem/edit";

   }

    private void copyAttachment(String path, Long oldItemId, Long newItemId) throws Exception {
        if (oldItemId == null || oldItemId <= 0) {
           return;
        }
        if (!Utility.isSet(path)) {
           return;
        }
        String newPath = new String(path);
        String oldPath = new String(path.replaceAll(String.valueOf(newItemId), String.valueOf(oldItemId)));
        itemService.copyItemImage(oldPath, newPath, "ItemImage");

   }

}
