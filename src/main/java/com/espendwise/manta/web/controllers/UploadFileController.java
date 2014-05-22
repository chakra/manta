package com.espendwise.manta.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.apache.log4j.Logger;
import com.espendwise.manta.service.*;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.*;
import com.espendwise.manta.util.criteria.StoreManufacturerCriteria;
import com.espendwise.manta.util.criteria.StoreDistributorCriteria;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.spi.Clean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.espendwise.manta.web.forms.ItemLoaderForm;
import com.espendwise.manta.model.data.*;
import com.espendwise.manta.model.view.*;

import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.espendwise.manta.util.arguments.Args;

import org.springframework.web.bind.annotation.*;

import org.apache.poi.hssf.usermodel.*;

import java.io.*;
import java.math.BigDecimal;


@Controller
@SessionAttributes(SessionKey.UPLOAD_FILE)
@RequestMapping(UrlPathKey.UPLOAD_FILE.IDENTIFICATION)

public class UploadFileController extends BaseController {

    private static final Logger logger = Logger.getLogger(UploadFileController.class);

    private UploadFileService uploadFileService;
    private ItemService itemService;
    private ManufacturerService manufacturerService;
    private DistributorService distributorService;
     private StoreService storeService;

    @Autowired
    public UploadFileController(UploadFileService uploadFileService,
                                ItemService itemService,
                                ManufacturerService manufacturerService,
                                DistributorService distributorService,
                                StoreService storeService) {
        this.uploadFileService = uploadFileService;
        this.itemService = itemService;
        this.manufacturerService = manufacturerService;
        this.distributorService = distributorService;
        this.storeService = storeService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        //webErrors.putErrors(ex, new UploadFileWebUpdateExceptionResolver());

        return "uploadFile/edit";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "uploadFile/edit";
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(HttpServletRequest request,
            @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
            @PathVariable("uploadId") Long uploadId,
            Model model) {

        logger.info("show()=> BEGIN, uploadId=" + String.valueOf(uploadId));

        List<UploadSkuView> uploadSkuList = uploadFileService.findUploadSkuViewList(uploadId);
        UploadData uploadFile =  uploadFileService.findUploadData(uploadId);

        if (uploadSkuList != null) {
            form.reset();
            SelectableObjects<UploadSkuView> selectableObj = new SelectableObjects<UploadSkuView>(
                    uploadSkuList,
                    null,
                    AppComparator.UPLOAD_SKU_VIEW_SKU_DATA_COMPARATOR
            );
            form.setUploadSkuViewList(selectableObj);
        }
        form.setUploadData(uploadFile);

        // set columns
        List<Pair<String, String>> itemPropertiesAll = form.getItemPropertiesAll();
        List<Pair<String, String>> itemProperties = new ArrayList<Pair<String, String>>();
        int[] itemPropertyMap = new int[itemPropertiesAll.size()];
        for (int ii = 0; ii < itemPropertyMap.length; ii++) {
          itemPropertyMap[ii] = 0;
        }

        for (UploadSkuView uploadSku : uploadSkuList) {
            for (int ii = 0; ii < itemPropertyMap.length; ii++) {
                if (itemPropertyMap[ii] != 0) continue;
                Pair<String, String> prop =  itemPropertiesAll.get(ii);
                String valS = form.getSkuProperty(uploadSku, prop.getObject2());
                if (Utility.isSet(valS)) {
                    itemPropertyMap[ii] = 1;
                    itemProperties.add(prop);
                }
            }
        }
        form.setItemPropertyMap(itemPropertyMap);
        form.setItemProperties(itemProperties);
        form.setStep("edit");

        model.addAttribute(SessionKey.UPLOAD_FILE, form);

        logger.info("show()=> END.");

        return "uploadFile/edit";

    }

    @RequestMapping(value = "/getSubtable", method = RequestMethod.POST)
    public String getSubtable(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("getSubtable()=> BEGIN, uploadData=" + form.getUploadData());
        WebErrors webErrors = new WebErrors(request);

        if (form.getSourceTable() != null) {
            List<Pair<String, String>> itemProperties = form.getItemProperties();

            List<String[]> selectedRows = form.getSourceTable().getSelected();

            List<UploadSkuView> uploadSkuList = new ArrayList<UploadSkuView>();

            int j = 0;
            for (String[] row : selectedRows) {
                UploadSkuView usV = new UploadSkuView();
                UploadSkuData uploadSkuD = new UploadSkuData();
                uploadSkuD.setRowNum(new Long(j));
                usV.setUploadSkuData(uploadSkuD);
                j++;
                for (int i = 0; i < row.length; i++) {
                    String prop = itemProperties.get(i).getObject2();
                    form.setSkuProperty(usV, prop, row[i]);
                }
                uploadSkuList.add(usV);
            }

            SelectableObjects<UploadSkuView> selectableObj = new SelectableObjects<UploadSkuView>(
                        uploadSkuList,
                        null,
                        AppComparator.UPLOAD_SKU_VIEW_SKU_DATA_COMPARATOR
            );

            form.setUploadSkuViewList(selectableObj);
            form.setSourceTable(null);
            form.getUploadData().setUploadStatusCd(RefCodeNames.UPLOAD_STATUS_CD.PROCESSING);
            form.setStep("edit");
        }
        model.addAttribute(SessionKey.UPLOAD_FILE, form);


        logger.info("getSubtable()=> END.");

        return "uploadFile/edit";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @Clean({SessionKey.UPLOAD_FILE_HEADER})
    public String upload(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("upload()=> BEGIN, uploadFile=" + (form.getUploadXlsFile()!=null?form.getUploadXlsFile().getOriginalFilename():""));
        WebErrors webErrors = new WebErrors(request);

        CommonsMultipartFile xlsFile = form.getUploadXlsFile();
        if (xlsFile != null && xlsFile.getSize() > 0) {

            String fileName = xlsFile.getOriginalFilename();
            if (!Utility.isSet(fileName)) {
                webErrors.putError("exception.web.error.uploadFile.emptyFileName");
                //return redirect(request, UrlPathKey.UPLOAD_FILE.FILTER);
                return "uploadFile/edit";
            }
            // validate extention
            String fileExt = "";
            int i = fileName.lastIndexOf(".");
            if (i >= 0) {
                fileExt = fileName.substring(i+1);
            }
            if (!Utility.isSet(fileExt) || !fileExt.equalsIgnoreCase("xls")) {
                webErrors.putError("validation.web.error.wrongFileFormat", Args.typed(fileName));
                return "uploadFile/edit";
            }
            // try to get content
            UploadData uploadData = new UploadData();
            form.setUploadData(uploadData);

            uploadData.setFileName(fileName);
            InputStream stream = null;
            try {
                stream = form.getUploadXlsFile().getInputStream();
                HSSFWorkbook workbook = new HSSFWorkbook(stream);
                HSSFSheet sheet = workbook.getSheetAt(0);
                HSSFRow headerRow = sheet.getRow(0);
                int colQty = headerRow.getPhysicalNumberOfCells() ;
                boolean[] emptyColumnFl = new boolean[colQty];
                for (int ii = 0; ii < colQty; ii++) {
                    emptyColumnFl[ii] = true;
                }
                int rowQty = sheet.getPhysicalNumberOfRows();
                if (rowQty < 1) {
                    webErrors.putError("validation.web.error.wrongFileFormat", Args.typed(fileName));
                    return "uploadFile/edit";
                }
                List<String[]> sourceTable = new ArrayList<String[]>();
                for (int jj = 0; jj < rowQty; jj++) {
                HSSFRow tRow = sheet.getRow(jj);
                String[] row = new String[colQty];
                boolean emptyFl = true;
                for (int ii = 0; ii < colQty; ii++) {
                      row[ii] = null;
                      HSSFCell cell = tRow.getCell(ii);
                      if (cell == null) {
                        continue;
                      }
                      int ll = 0;
                      String valS = getCellValue(cell);
                      if (!Utility.isSet(valS)) {
                        continue;
                      }
                      emptyColumnFl[ii] = false;
                      emptyFl = false;
                      if (valS != null) {
                          valS = valS.trim();
                      }
                      row[ii] = valS;
                    }
                    if (!emptyFl) {
                      sourceTable.add(row);
                    }
                }

              List<Pair<String, String>> itemPropertiesAll = form.getItemPropertiesAll();
              List<Pair<String, String>> itemProperties = new ArrayList<Pair<String, String>>();

              char[][] itemPropertiesChar = new char[itemPropertiesAll.size()][];
              for (int ii = 0; ii < itemPropertiesAll.size(); ii++) {
                String prop = itemPropertiesAll.get(ii).getObject2().toUpperCase();
                itemPropertiesChar[ii] = prop.toCharArray();
              }

              // get header
              String[] columnTypes = new String[colQty];
              String[] row0 = sourceTable.get(0);
              for (int ii = 0; ii < row0.length; ii++) {
                  int propInd = bestMatch(itemPropertiesChar, Utility.strNN(row0[ii]));
                  String columnCd = "";
                  if (propInd < 0) {
                    columnTypes[ii] = "";
                  } else {
                    columnTypes[ii] = itemPropertiesAll.get(propInd).getObject2();
                    columnCd = itemPropertiesAll.get(propInd).getObject1();
                  }
                  itemProperties.add(new Pair(columnCd, columnTypes[ii]));

              }

              form.setItemProperties(itemProperties);

              form.setColumnTypes(columnTypes);
              SelectableObjects<String[]> selectableObj = new SelectableObjects<String[]>(
                            sourceTable, null, null
                    );
              form.setSourceTable(selectableObj);
              form.setUploadXlsFile(null);

          } catch (Exception exc) {
                webErrors.putError(exc.getMessage());
                return "uploadFile/edit";
          }

        }
        form.setStep("createNew");
        
        model.addAttribute(SessionKey.UPLOAD_FILE, form);

        logger.info("upload()=> END");

        return "uploadFile/edit";
    }

    private String getCellValue(HSSFCell cell) {
        String value = "";
        if(cell != null) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    value = String.valueOf(cell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
            }
        }
        return value;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("save()=> BEGIN, itemLoaderForm: "+form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        if (form.getUploadSkuViewList() != null) {
            List<UploadSkuView> skuDataToSaveList = form.getUploadSkuViewList().getSelected();
            if (skuDataToSaveList != null && skuDataToSaveList.size() > 0) {
                UploadData uploadData = form.getUploadData();
                if (uploadData == null) {
                    uploadData = new UploadData();
                    form.setUploadData(uploadData);
                }
                Long tableId = uploadData.getUploadId();
                if (tableId == null) {
                    uploadData.setFileType("??");
                    uploadData.setUploadStatusCd(RefCodeNames.UPLOAD_STATUS_CD.PROCESSING);
                    uploadData.setCoulumnQty(form.getItemProperties().size());
                }

                uploadData.setRowQty(skuDataToSaveList.size());

                uploadData = uploadFileService.saveUploadData(getStoreId(), uploadData);

                skuDataToSaveList = uploadFileService.saveUploadSkuViewList(tableId, skuDataToSaveList);
            }
        }

        return "uploadFile/edit";
    }

    @RequestMapping(value = "/exportTemplate", method = RequestMethod.GET)
    public String exportTemplate(WebRequest request, HttpServletResponse response) throws Exception {
        logger.info("exportTemplate()=> BEGIN");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

	    try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            List<Pair<String, String>> headers = RefCodeNamesKeys.getRefCodeValues(Constants.ITEM_LOADER_PROPERTY.class, false);
            HSSFSheet sheet = workbook.createSheet("Item Loader Template");
            HSSFRow row = sheet.createRow(0);
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFFont font = workbook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
            for(int i=0; i<headers.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                Pair<String, String> h = headers.get(i);
                cell.setCellValue(h.getObject2());
            }
	    	workbook.write(out);

	    } catch (Exception e) {
	    	throw new RuntimeException(e);
	    }

        try {
            ReportingUtil.initializeResponseForExcelExport(request, response, "itemLoaderTemplate.xls");
	        response.setContentLength(out.size());
	        out.writeTo(response.getOutputStream());
	        response.flushBuffer();
	        response.getOutputStream().close();
        }
        catch(IOException e){
        	response.getOutputStream().close();
        }
        logger.info("exportTemplate()=> END");
        return null;
    }


    @RequestMapping(value = "/match", method = RequestMethod.POST)
    public String match(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("match()=> BEGIN, itemLoaderForm: "+form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        if (form.getUploadSkuViewList() == null) {
            return "uploadFile/edit";
        }

        List<UploadSkuView> itemsToMatchDB
            = uploadFileService.matchItems( getStoreId(),
                                            form.getUploadData().getUploadId(),
                                            form.getUploadSkuViewList().getValues());


        SelectableObjects<UploadSkuView> itemsToMatch = null;
        if (itemsToMatchDB != null) {
            itemsToMatch = new SelectableObjects<UploadSkuView>(
                        itemsToMatchDB,
                        null,
                        AppComparator.UPLOAD_SKU_VIEW_SKU_DATA_COMPARATOR
            );
        }
        if (itemsToMatch == null) itemsToMatch = new SelectableObjects<UploadSkuView>();
        Iterator iter = itemsToMatch.getIterator();
        SelectableObjects.SelectableObject<UploadSkuView> usMatchVw = null;

        SelectableObjects<UploadSkuView> uploadSkusVw = form.getUploadSkuViewList();

        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            UploadSkuData usD = usVw.getUploadSkuData();
            Long rowNum = usD.getRowNum();
            String skuNum = usVw.getSkuNum();
            boolean newSkuFl = true;
            boolean createFl = Utility.isSet(skuNum) ? false : true;
            uploadSkusVw.setSelected(ii, true);

          while (usMatchVw != null || iter.hasNext()) {
            if (usMatchVw == null) {
              usMatchVw = (SelectableObjects.SelectableObject)iter.next();
              usMatchVw.setSelected(false);
            }
            UploadSkuData usMatchD = usMatchVw.getValue().getUploadSkuData();
            Long rowNumMatch = usMatchD.getRowNum();
            if (rowNumMatch < rowNum) {
              usMatchVw = null;
              continue;
            }
            if (rowNumMatch > rowNum) {
              break;
            }
            if (newSkuFl && createFl) {
              usMatchVw.setSelected(true);
              uploadSkusVw.setSelected(ii, false);
            }
            newSkuFl = false;
            usMatchVw = null;
          }
        }

        form.setItemsToMatch(itemsToMatch);
        form.setUploadSkuViewList(uploadSkusVw);

        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("match()=> END");
        return "uploadFile/edit";
    }
   
    @RequestMapping(value = "/showMatched", method = RequestMethod.POST)
    public String showMatched(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("showMatched()=> BEGIN, itemLoaderForm: "+form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        List<Long> uploadSkuIds = form.getUploadSkuIds();

        List<UploadSkuView> itemsToMatch =
            uploadFileService.getMatchedItems(getStoreId(), form.getUploadData().getUploadId(), uploadSkuIds);

        if (itemsToMatch == null) itemsToMatch = new ArrayList<UploadSkuView>();
        Iterator iter = itemsToMatch.iterator();
        UploadSkuView usMatchVw = null;

        List<UploadSkuView> uploadSkusVw = form.getUploadSkuViewList().getValues();
        List<UploadSkuView> selectedUploadSkusVw = new ArrayList<UploadSkuView>(); 

        for (UploadSkuView usVw : uploadSkusVw) {
          UploadSkuData usD = usVw.getUploadSkuData();
          Long rowNum = usD.getRowNum();
          String skuNum = usVw.getSkuNum();
          boolean newSkuFl = true;
          boolean createFl = Utility.isSet(skuNum) ? false : true;

          if (createFl) selectedUploadSkusVw.add(usVw);

          while (usMatchVw != null || iter.hasNext()) {
            if (usMatchVw == null) {
              usMatchVw = (UploadSkuView) iter.next();
              selectedUploadSkusVw.remove(usMatchVw);
            }
            UploadSkuData usMatchD = usMatchVw.getUploadSkuData();
            Long rowNumMatch = usMatchD.getRowNum();
            if (rowNumMatch < rowNum) {
              usMatchVw = null;
              continue;
            }
            if (rowNumMatch > rowNum) {
              break;
            }
            if (newSkuFl && createFl) {
                selectedUploadSkusVw.add(usMatchVw);
                selectedUploadSkusVw.remove(usVw);
            }
            newSkuFl = false;
            usMatchVw = null;
          }
        }

         if (itemsToMatch.size() > 0) {
            SelectableObjects<UploadSkuView> selectableObj = new SelectableObjects<UploadSkuView>(
                        itemsToMatch,
                        selectedUploadSkusVw,
                        AppComparator.UPLOAD_SKU_VIEW_SKU_DATA_COMPARATOR
            );
            form.setUploadSkuViewList(selectableObj);

         }

         form.setItemsToMatch(null);

        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("showMatched()=> END");
        return "uploadFile/edit";
    }


    @RequestMapping(value = "/assignSkus", method = RequestMethod.POST)
    public String assignSkus(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("assignSkus()=> BEGIN, itemLoaderForm: "+form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        SelectableObjects<UploadSkuView> itemsToMatch =
            form.getItemsToMatch() != null ? form.getItemsToMatch() : new SelectableObjects<UploadSkuView>();

        int matchedQty = 0;

        Iterator iter = itemsToMatch.getIterator();

        SelectableObjects.SelectableObject usMatchVw = null;

        List<UploadSkuView> uploadSkusVw = form.getUploadSkuViewList().getValues();

        for (int ii = 0; ii < uploadSkusVw.size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.get(ii);
            UploadSkuData usD = usVw.getUploadSkuData();
            Long rowNum = usD.getRowNum();
            boolean createFl = form.getUploadSkuViewList().getSelected(ii);
            boolean setFl = false;

            while (usMatchVw != null || iter.hasNext()) {
                if (usMatchVw == null) {
                    usMatchVw = (SelectableObjects.SelectableObject) iter.next();
                }
                UploadSkuData usMatchD = ((UploadSkuView)usMatchVw.getValue()).getUploadSkuData();
                Long rowNumMatch = usMatchD.getRowNum();
                if (rowNumMatch < rowNum) {
                    usMatchVw = null;
                    continue;
                }
                if (rowNumMatch > rowNum) {
                    break;
                }
                boolean assignFl = usMatchVw.isSelected();
                if (assignFl) {
                    if (createFl) {
                        webErrors.putError("exception.web.error.uploadFile.skuSelectedToCreate", Args.typed(rowNum));
                        return "uploadFile/edit";
                    } else if (setFl) {
                        webErrors.putError("exception.web.error.uploadFile.multipleSkuSelected", Args.typed(rowNum));
                        return "uploadFile/edit";
                    } else {
                        setFl = true;
                        matchedQty++;
                        UploadSkuData v = ((UploadSkuView)usMatchVw.getValue()).getUploadSkuData();
                        usD.setItemId(v.getItemId());
                        usVw.setSkuNum(v.getSkuNum());
                    }
                }
                usMatchVw = null;
            }
        }
        if (matchedQty == 0) {
            webErrors.putError("exception.web.error.uploadFile.noSkuToAssignSelected");
            return "uploadFile/edit";
        }

         if (uploadSkusVw.size() > 0) {
            uploadFileService.saveUploadSkuViewList(form.getUploadData().getUploadId(), uploadSkusVw);

            SelectableObjects<UploadSkuView> selectableObj = new SelectableObjects<UploadSkuView>(
                        uploadSkusVw,
                        null,
                        AppComparator.UPLOAD_SKU_VIEW_SKU_DATA_COMPARATOR
            );
            form.setUploadSkuViewList(selectableObj);
         }

        form.setItemsToMatch(null);


        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("assignSkus()=> END");
        return "uploadFile/edit";
    }


    @RequestMapping(value = "/removeAssignment", method = RequestMethod.POST)
    public String removeAssignment(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("removeAssignment()=> BEGIN, itemLoaderForm: "+form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        SelectableObjects<UploadSkuView> itemsToMatch =
            form.getItemsToMatch() != null ? form.getItemsToMatch() : new SelectableObjects<UploadSkuView>();

        SelectableObjects<UploadSkuView> uploadSkusVw = form.getUploadSkuViewList();
        int qty = 0;
        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            UploadSkuData usD = usVw.getUploadSkuData();
            if (uploadSkusVw.getSelected(ii)) {
                usD.setItemId(new Long(0));
                usVw.setSkuNum(null);
                qty++;
            }
        }
        if (qty == 0) {
            webErrors.putError("exception.web.error.uploadFile.noSkuSelected");
            return "uploadFile/edit";
        }

         if (uploadSkusVw.getValues().size() > 0) {
            uploadFileService.saveUploadSkuViewList(form.getUploadData().getUploadId(), uploadSkusVw.getValues());
            form.setUploadSkuViewList(uploadSkusVw);
         }

        form.setItemsToMatch(null);

        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("removeAssignment()=> END");
        return "uploadFile/edit";
    }


    @RequestMapping(value = "/createSkus", method = RequestMethod.POST)
    public String createSkus(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("createSkus()=> BEGIN, itemLoaderForm: "+form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }
        Long catalogId = uploadFileService.getStoreCatalogId(getStoreId());
        HashMap<Pair<String, String>, ItemView> categoryHM = getCategoriesHM(catalogId);

        HashMap<String, BusEntityData> manufHM = getManufacturersHM();
        HashMap<String, BusEntityData> distHM = getDistributorsHM();
        checkItemsForCreate(form, categoryHM, manufHM, distHM, catalogId, webErrors);
        if (webErrors.size() > 0 ) {
            return "uploadFile/edit";
        }
        SelectableObjects<UploadSkuView> itemsToMatch = form.getItemsToMatch();

   
        //Check errors
        if (itemsToMatch == null) itemsToMatch = new SelectableObjects<UploadSkuView>();
        int qty = 0;

        Iterator iter = itemsToMatch.getIterator();
        SelectableObjects.SelectableObject usMatchVw = null;

        SelectableObjects<UploadSkuView> uploadSkusVw = form.getUploadSkuViewList();
        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            UploadSkuData usD = usVw.getUploadSkuData();
            Long rowNum = usD.getRowNum();
            String skuNum = usVw.getSkuNum();
            boolean createFl = uploadSkusVw.getSelected(ii);
            if (createFl) qty++;
            if (Utility.isSet(skuNum) && createFl) {
                webErrors.putError("exception.web.error.uploadFile.skuAlreadyAssigned", Args.typed(rowNum));
                return "uploadFile/edit";
            }
            while (usMatchVw != null || iter.hasNext()) {
                if (usMatchVw == null) {
                    usMatchVw = (SelectableObjects.SelectableObject) iter.next();
                }
                UploadSkuData usMatchD = ((UploadSkuView)usMatchVw.getValue()).getUploadSkuData();

                Long rowNumMatch = usMatchD.getRowNum();
                if (rowNumMatch < rowNum) {
                    usMatchVw = null;
                    continue;
                }
                if (rowNumMatch > rowNum) {
                    break;
                }
                boolean assignFl = usMatchVw.getSelected();
                if (assignFl) {
                    if (createFl) {
                        webErrors.putError("exception.web.error.uploadFile.cantCreateSku", Args.typed(rowNum));
                        return "uploadFile/edit";
                    }
                }
                usMatchVw = null;
            }
        }
        if (qty == 0) {
            webErrors.putError("exception.web.error.uploadFile.noSkuSelected");
            return "uploadFile/edit";
        }

        //Create items
        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            UploadSkuData usD = usVw.getUploadSkuData();
            Long rowNum = usD.getRowNum();
            boolean createFl = uploadSkusVw.getSelected(ii);
            if (!createFl) continue;

            ItemIdentView productD = new ItemIdentView();
            WebError err = generateProductData(productD, usVw, manufHM, distHM, categoryHM,
                               catalogId, form.getAllowMixedCategoryAndItemUnderSameParent());
            if (err != null) {
                webErrors.putError(err);
                return "uploadFile/edit";
            }

            Map<String, byte[]> externalData = loadExternalData(usD, webErrors, rowNum);
            //trySearchAndStoreGreenCertified(productD, usD, ae, factory.getProductInformationAPI());
            if (webErrors.size() > 0) {
                return "uploadFile/edit";
            }

            productD = itemService.saveItemIdent(getStoreId(), getUserId(), productD);

            if (externalData != null && externalData.isEmpty() == false) {
                storeExternalData(productD, usD, externalData, webErrors);
                productD = itemService.saveItemIdent(getStoreId(), getUserId(), productD);
            }

            usD.setItemId(productD.getItemData().getItemId());
            usVw.setSkuNum(productD.getCatalogStructureData().getCustomerSkuNum());

            usD = uploadFileService.saveUploadSkuData(form.getUploadData().getUploadId(), usD);

        }
        form.setUploadSkuViewList(uploadSkusVw);
        form.setItemsToMatch(null);

        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("createSkus()=> END");
        return "uploadFile/edit";
    }


    @RequestMapping(value = "/updateSkus", method = RequestMethod.POST)
    public String updateSkus(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  Model model) throws Exception {

        logger.info("updateSkus()=> BEGIN, itemLoaderForm: "+form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        List<Long> uploadSkuIds = form.getUploadSkuIds();

        List<UploadSkuView> itemsToMatch =
            uploadFileService.getMatchedItems(getStoreId(), form.getUploadData().getUploadId(), uploadSkuIds);

        if (itemsToMatch == null) itemsToMatch = new ArrayList<UploadSkuView>();

        SelectableObjects<UploadSkuView> uploadSkusVw = form.getUploadSkuViewList();

        int qty = 0;
        Set<Integer> notAssigned = new TreeSet<Integer>();
        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            UploadSkuData usD = usVw.getUploadSkuData();
            Long rowNum = usD.getRowNum();
            String skuNum = usVw.getSkuNum();
            boolean updateFl = uploadSkusVw.getSelected(ii);
            if (!updateFl) continue;
            if (updateFl) qty++;
            if (!Utility.isSet(skuNum)) {
                webErrors.putError("exception.web.error.uploadFile.skuNotYetAssigned", Args.typed(rowNum));
                notAssigned.add(ii);
            }
        }
        if (qty == 0) {
            webErrors.putError("exception.web.error.uploadFile.noSkuSelectedForUpdate");
            return "uploadFile/edit";
        }


        Long storeCatalogId = uploadFileService.getStoreCatalogId(getStoreId());
        HashMap<Pair<String, String>, ItemView> categoryHM = getCategoriesHM(storeCatalogId);
        HashMap<String, BusEntityData> manufHM = getManufacturersHM();
        HashMap<String, BusEntityData> distHM = getDistributorsHM();

        WebErrors err = checkItemsForUpdate(form, categoryHM, manufHM, distHM, storeCatalogId, notAssigned);
        if (err != null && err.size() > 0) {
            return "uploadFile/edit";
        }

        //update items
        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            boolean updateFl = uploadSkusVw.getSelected(ii);
            if (!updateFl) continue;
            UploadSkuData usD = usVw.getUploadSkuData();
            Long itemId = usD.getItemId();
            ItemIdentView productD = itemService.findItemToEdit(getStoreId(), storeCatalogId, itemId);

            WebError er = generateProductData(productD, usVw, manufHM, distHM, categoryHM,
    		  storeCatalogId, form.getAllowMixedCategoryAndItemUnderSameParent());
            if (err != null) {
                webErrors.putError(er);
                return "uploadFile/edit";
            }

            Map<String, byte[]> externalData = loadExternalData(usD, webErrors, usD.getRowNum());
            if (webErrors.size() > 0) {
                return "uploadFile/edit";
            }
            if (externalData != null && externalData.isEmpty() == false) {
                storeExternalData(productD, usD, externalData, webErrors);
            }
            productD = itemService.saveItemIdent(getStoreId(), getUserId(), productD);

            usD.setItemId(productD.getItemData().getItemId());
            usVw.setSkuNum(productD.getCatalogStructureData().getCustomerSkuNum());

            usD = uploadFileService.saveUploadSkuData(form.getUploadData().getUploadId(), usD);
        }
        form.setUploadSkuViewList(uploadSkusVw);
        form.setItemsToMatch(null);


        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("updateSkus()=> END");
        return "uploadFile/edit";
    }


    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public String select(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  @PathVariable("uploadId") Long uploadId,
                  Model model) throws Exception {

        logger.info("select()=> BEGIN, itemLoaderForm: " + form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        UploadData uploadFile =  uploadFileService.findUploadData(uploadId);
        List<UploadSkuView> uploadSkus = uploadFileService.findUploadSkuViewList(uploadId);

        
        boolean matchedOnlyFl = RefCodeNames.ITEM_LOADER_MATCH_FILTER_CD.MATCHED.equals(form.getMatchedFilterType());
        boolean unMatchedOnlyFl = RefCodeNames.ITEM_LOADER_MATCH_FILTER_CD.UNMATCHED.equals(form.getMatchedFilterType());
        boolean storeSkuFilterFl = false;
        boolean distSkuFilterFl = false;
        boolean manufSkuFilterFl = false;

        String skuFilter = form.getSkuNumber();
        if (Utility.isSet(skuFilter)) {
          if (RefCodeNames.SKU_TYPE_CD.DISTRIBUTOR.equals(form.getSkuNumberTypeFilterType())) distSkuFilterFl = true;
          else if (RefCodeNames.SKU_TYPE_CD.MANUFACTURER.equals(form.getSkuNumberTypeFilterType())) manufSkuFilterFl = true;
          else if (RefCodeNames.SKU_TYPE_CD.STORE.equals(form.getSkuNumberTypeFilterType())) storeSkuFilterFl = true;
          skuFilter = skuFilter.trim().toUpperCase();
        }
        String manufFilter = form.getManufName();
        boolean manufFilterFl = false;
        if (Utility.isSet(manufFilter)) {
          manufFilterFl = true;
          manufFilter = manufFilter.trim().toUpperCase();
        }
        String distFilter = form.getDistName();
        boolean distFilterFl = false;
        if (Utility.isSet(distFilter)) {
          distFilterFl = true;
          distFilter = distFilter.trim().toUpperCase();
        }
        String nameFilter = form.getSkuName();
        boolean nameFilterFl = false;
        if (Utility.isSet(nameFilter)) {
          nameFilterFl = true;
          nameFilter = nameFilter.trim().toUpperCase();
        }
        String categFilter = form.getCategory();
        boolean categFilterFl = false;
        if (Utility.isSet(categFilter)) {
          categFilterFl = true;
          categFilter = categFilter.trim().toUpperCase();
        }

        List<UploadSkuView> uploadSkusFiltered = new ArrayList<UploadSkuView>();

        if (matchedOnlyFl || unMatchedOnlyFl ||
            storeSkuFilterFl || manufSkuFilterFl || distSkuFilterFl ||
            manufFilterFl || distFilterFl ||
            nameFilterFl || categFilterFl) {
            for (UploadSkuView usV : uploadSkus) {
                UploadSkuData usD = usV.getUploadSkuData();
                if (matchedOnlyFl) {
                  if (usD.getItemId() <= 0) continue;
                }
                if (unMatchedOnlyFl) {
                  if (usD.getItemId() > 0) continue;
                }
                if (storeSkuFilterFl) {
                  String skuNum = usD.getSkuNum();
                  String matchedSkuNum = usV.getSkuNum();
                  if ((!Utility.isSet(skuNum) ||
                       !skuNum.equalsIgnoreCase(skuFilter)) &&
                      (!Utility.isSet(matchedSkuNum) ||
                       !matchedSkuNum.equalsIgnoreCase(skuFilter))) {
                    continue;
                  }
                }
                if (manufSkuFilterFl) {
                  String manufSku = usD.getManufSku();
                  if (!Utility.isSet(manufSku) ||
                      manufSku.toUpperCase().indexOf(skuFilter) < 0) {
                    continue;
                  }
                }
                if (distSkuFilterFl) {
                  String distSku = usD.getDistSku();
                  if (!Utility.isSet(distSku) ||
                      distSku.toUpperCase().indexOf(skuFilter) < 0) {
                    continue;
                  }
                }
                if (manufFilterFl) {
                  String manufName = usD.getManufName();
                  if (!Utility.isSet(manufName) ||
                      manufName.toUpperCase().indexOf(manufFilter) < 0) {
                    continue;
                  }
                }
                if (distFilterFl) {
                  String distName = usD.getDistName();
                  if (!Utility.isSet(distName) ||
                      distName.toUpperCase().indexOf(distFilter) < 0) {
                    continue;
                  }
                }
                if (nameFilterFl) {
                  String skuName = usD.getShortDesc();
                  if (!Utility.isSet(skuName) ||
                      skuName.toUpperCase().indexOf(nameFilter) < 0) {
                    continue;
                  }
                }
                if (categFilterFl) {
                  String categ = usD.getCategory();
                  if (!Utility.isSet(categ) ||
                      categ.toUpperCase().indexOf(categFilter) < 0) {
                    continue;
                  }
                }
                uploadSkusFiltered.add(usV);
            }

        } else {
            uploadSkusFiltered = uploadSkus;
        }

        if (uploadSkusFiltered != null) {
            SelectableObjects<UploadSkuView> selectableObj = new SelectableObjects<UploadSkuView>(
                    uploadSkusFiltered,
                    null,
                    AppComparator.UPLOAD_SKU_VIEW_SKU_DATA_COMPARATOR
            );
            form.setUploadSkuViewList(selectableObj);
        }
        form.setUploadData(uploadFile);
        form.setStep("edit");
        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("select()=> END");
        return "uploadFile/edit";
    }

    @RequestMapping(value = "/selectUpdate", method = RequestMethod.POST)
    public String selectUpdate(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  @PathVariable("uploadId") Long uploadId,
                  Model model) throws Exception {

        logger.info("selectUpdate()=> BEGIN, itemLoaderForm: " + form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        UploadData uploadFile =  uploadFileService.findUploadData(uploadId);
        List<UploadSkuView> uploadSkus = uploadFileService.findUploadSkuViewList(uploadId);

        form.setStep("edit");
        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("selectUpdate()=> END");
        return "uploadFile/edit";
     }


    @RequestMapping(value = "/reload", method = RequestMethod.POST)
    public String reload(WebRequest request,
                  @ModelAttribute(SessionKey.UPLOAD_FILE) ItemLoaderForm form,
                  @PathVariable("uploadId") Long uploadId,
                  Model model) throws Exception {

        logger.info("reload()=> BEGIN, itemLoaderForm: " + form);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.UPLOAD_FILE, form);
            return "uploadFile/edit";
        }

        UploadData uploadFile =  uploadFileService.findUploadData(uploadId);
        List<UploadSkuView> uploadSkus = uploadFileService.findUploadSkuViewList(uploadId);

        form.setStep("edit");
        model.addAttribute(SessionKey.UPLOAD_FILE, form);
        logger.info("reload()=> END");
        return "uploadFile/edit";
     }


    @ModelAttribute(SessionKey.UPLOAD_FILE)
    public ItemLoaderForm initModel() {
    	ItemLoaderForm form = new ItemLoaderForm();
        form.initialize();

        List<String> propNames = new ArrayList<String>();
        propNames.add(RefCodeNames.PROPERTY_TYPE_CD.AUTO_SKU_ASSIGN);
        propNames.add(RefCodeNames.PROPERTY_TYPE_CD.ALLOW_MIXED_CATEGORY_AND_ITEM);


        // Auto Sku flag
        List<PropertyData> storeProperties = storeService.findStoreProperties(getStoreId(),
                                             propNames);

        if (Utility.isSet(storeProperties)) {
            for (PropertyData prop : storeProperties) {
                if (prop.getShortDesc().equals(RefCodeNames.PROPERTY_TYPE_CD.AUTO_SKU_ASSIGN)) {
                    form.setAutoSkuFlag(Utility.isTrue(prop.getValue()));
                }
                if (prop.getShortDesc().equals(RefCodeNames.PROPERTY_TYPE_CD.ALLOW_MIXED_CATEGORY_AND_ITEM)) {
                    form.setAllowMixedCategoryAndItemUnderSameParent(Utility.isTrue(prop.getValue()));
                }
            }
        }

        return form;

    }

    private List<BusEntityData> getStoreManufacturers() {
        StoreManufacturerCriteria criteria = new StoreManufacturerCriteria();
        criteria.setStoreId(getStoreId());
        return  manufacturerService.findManufacturers(criteria);
    }


    private List<BusEntityData> getStoreDistributors() {
        StoreDistributorCriteria criteria = new StoreDistributorCriteria();
        criteria.setStoreId(getStoreId());
        return  distributorService.findDistributors(criteria);
    }


  private static int bestMatch(char[][] pItemPropertiesChar,
                               String pColumnTitle) {
    int bestMatch = -1;
    int bestMatchNum = 0;
    char[] colTitleCh = pColumnTitle.toUpperCase().toCharArray();
    for (int ii = 0; ii < pItemPropertiesChar.length; ii++) {
      int jj = 0, kk = 0, ww = 0;
      for (; jj < colTitleCh.length && kk < pItemPropertiesChar[ii].length; ) {
        char cc1 = colTitleCh[jj];
        char cc2 = pItemPropertiesChar[ii][kk];
        if (cc1 == ' ' && cc2 == ' ') {
          jj++;
          kk++;
          continue;
        }
        if (cc1 == cc2) {
          ww++;
          jj++;
          kk++;
          continue;
        }
        if (cc1 == ' ') {
          kk++;
          continue;
        }
        if (cc2 == ' ') {
          jj++;
          continue;
        }
        jj++;
        kk++;
      }
      if (ww > 2 && bestMatchNum < ww) {
        bestMatchNum = ww;
        bestMatch = ii;
      }
    }
    return bestMatch;
  }

   private void checkItemsForCreate(ItemLoaderForm pForm,
            HashMap categoryHM, HashMap manufHM, HashMap distHM,
            Long storeCatalogId, WebErrors err) {
        SelectableObjects<UploadSkuView> itemsToMatch = pForm.getItemsToMatch();
        // Check errors
        if (itemsToMatch == null)
            itemsToMatch = new SelectableObjects<UploadSkuView>();
        int qty = 0;

        Iterator iter = itemsToMatch.getIterator();
        SelectableObjects.SelectableObject<UploadSkuView> usMatchVw = null;
        SelectableObjects<UploadSkuView> uploadSkusVw = pForm.getUploadSkuViewList();
        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            UploadSkuData usD = usVw.getUploadSkuData();
            Long rowNum = usD.getRowNum();
            String skuNum = usVw.getSkuNum();
            boolean createFl = uploadSkusVw.getSelected(ii);
            if (createFl)
                qty++;
            if (Utility.isSet(skuNum) && createFl) {
                err.putError("exception.web.error.uploadFile.skuAlreadyAssigned", Args.typed(rowNum));
                return;
            }
            while (usMatchVw != null || iter.hasNext()) {
                if (usMatchVw == null) {
                    usMatchVw = (SelectableObjects.SelectableObject) iter.next();
                }
                UploadSkuData usMatchD = usMatchVw.getValue().getUploadSkuData();
                Long rowNumMatch = usMatchD.getRowNum();
                if (rowNumMatch < rowNum) {
                    usMatchVw = null;
                    continue;
                }
                if (rowNumMatch > rowNum) {
                    break;
                }
                boolean assignFl = usMatchVw.isSelected();
                if (assignFl) {
                    if (createFl) {
                       err.putError("exception.web.error.uploadFile.cantCreateSku", Args.typed(rowNum));
                       return;
                    }
                }
                usMatchVw = null;
            }
        }
        if (qty == 0) {
            err.putError("exception.web.error.uploadFile.noSkuSelected");
            return;
        }
        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            UploadSkuData usD = usVw.getUploadSkuData();
            Long rowNum = usD.getRowNum();
            boolean createFl = uploadSkusVw.getSelected(ii);
            if (!createFl) continue;

            ItemIdentView productD = new ItemIdentView();
            try {
                WebError error = generateProductData(productD, usVw, manufHM, distHM, categoryHM,
                		storeCatalogId, pForm.getAllowMixedCategoryAndItemUnderSameParent());
                if (error != null) {
                    err.putError(error);
                }
            } catch (Exception e) {

            }
            Map<String, byte[]> externalData = loadExternalData(usD, err, rowNum);

        }
    }

    private WebErrors checkItemsForUpdate(ItemLoaderForm pForm,
            HashMap categoryHM, HashMap manufHM, HashMap distHM,
            Long catalogId, Set<Integer> notAssigned)
            throws Exception {
        Long storeId = getStoreId();
        WebErrors webErrors = new WebErrors();
        SelectableObjects<UploadSkuView> uploadSkusVw = pForm.getUploadSkuViewList();
        //PropertyDataVector propertyDV = storeD.getMiscProperties();
        for (int ii = 0; ii < uploadSkusVw.getValues().size(); ii++) {
            UploadSkuView usVw = uploadSkusVw.getValue(ii);
            boolean updateFl = uploadSkusVw.getSelected(ii);
            UploadSkuData usD = usVw.getUploadSkuData();

            Map<String, byte[]> externalData = loadExternalData(usD, webErrors, usD.getRowNum());

            if (updateFl == false || notAssigned.contains(ii) == true) {
                continue;
            }

            Long itemId = usD.getItemId();
            ItemIdentView productD = null;
            productD = itemService.findItemToEdit(storeId, catalogId, itemId);
            WebError err =  generateProductData(productD, usVw, manufHM, distHM, categoryHM,
                		catalogId, pForm.getAllowMixedCategoryAndItemUnderSameParent());
            if (err != null) {
                webErrors.add(err);
            }
        }
        return webErrors;
    }


  private WebError generateProductData(ItemIdentView productD,
                                       UploadSkuView uploadSkuVw,
                                       HashMap manufHM,
                                       HashMap distHM,
                                       HashMap categoryHM,
                                       Long storeCatalogId,
                                       boolean allowMixedCategoryAndItemUnderSameParent)  {
    UploadSkuData usD = uploadSkuVw.getUploadSkuData();
    String shortDesc = usD.getShortDesc();
    Long rowNum = usD.getRowNum();

    ItemData iD = productD.getItemData();
    if (iD == null) {
        iD = new ItemData();
        iD.setItemTypeCd(RefCodeNames.ITEM_TYPE_CD.PRODUCT);
        iD.setItemStatusCd(RefCodeNames.ITEM_STATUS_CD.ACTIVE);
        productD.setItemData(iD);
    }
    Long itemId = iD.getItemId();

    CatalogStructureData csD = productD.getCatalogStructureData();
    if (csD == null) {
        csD = new CatalogStructureData();
        csD.setCatalogStructureCd(RefCodeNames.CATALOG_STRUCTURE_CD.CATALOG_PRODUCT);
    }
    csD.setCatalogId(storeCatalogId);
    productD.setCatalogStructureData(csD);

    csD.setCustomerSkuNum(usD.getCustomerSkuNum());
    iD.setSkuNum(new Long(0));

    if (!Utility.isSet(shortDesc)) {
        return new WebError("exception.web.error.uploadFile.noShortDescFound", Args.typed(rowNum));
    }
    iD.setShortDesc(shortDesc);

    String longDesc = usD.getLongDesc();
    if (!Utility.isSet(longDesc)) longDesc = shortDesc;
    iD.setLongDesc(longDesc);

    // item meta
    List<ItemMetaData> itemMetaList = productD.getItemMeta();
    if (itemMetaList == null) {
        itemMetaList = new ArrayList<ItemMetaData>();
    }

    //set size
    String size = usD.getSkuSize();
    if (!Utility.isSet(size)) {
      return new WebError("exception.web.error.uploadFile.noSkuSizeFound", Args.typed(rowNum));
    }
    ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.SIZE, size, itemId, itemMetaList);

    //set pack
    String pack = usD.getDistPack();
    if (!Utility.isSet(pack)) {
      pack = usD.getSkuPack();
    }
    if (!Utility.isSet(pack)) {
      pack = usD.getManufPack();
    }
    if (!Utility.isSet(pack)) {
        return new WebError("exception.web.error.uploadFile.noSkuPackFound", Args.typed(rowNum));
    }
    ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.PACK, pack, itemId, itemMetaList);

    //set uom
    String uom = usD.getDistUom();
    if (!Utility.isSet(uom)) {
      uom = usD.getSkuUom();
    }
    if (!Utility.isSet(uom)) {
      uom = usD.getManufUom();
    }
    if (!Utility.isSet(uom)) {
        return new WebError("exception.web.error.uploadFile.noSkuUOMFound", Args.typed(rowNum));
    }
    ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.UOM, uom, itemId, itemMetaList);

    //set color
    String color = usD.getSkuColor();
    if (Utility.isSet(color)) {
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.COLOR, color, itemId, itemMetaList);
    }

    //set unspsc
    String unspscCode = usD.getUnspscCode();
    if (Utility.isSet(unspscCode)) {
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.UNSPSC_CD, unspscCode, itemId, itemMetaList);
    }

    //set nsn
    String nsn = usD.getNsn();
    if (Utility.isSet(nsn)) {
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.NSN, nsn, itemId, itemMetaList);
    }

    //set psn
    String psn = usD.getPsn();
    if (Utility.isSet(psn)) {
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.PSN, psn, itemId, itemMetaList);
    }

    // set shipping weight and weight unit
    String shipWeight = usD.getShipWeight();
    if (Utility.isSet(shipWeight)) {
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.SHIP_WEIGHT, shipWeight, itemId, itemMetaList);
    }
    String weightUnit = usD.getWeightUnit();
    if (Utility.isSet(weightUnit)) {
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.WEIGHT_UNIT, weightUnit, itemId, itemMetaList);
    }

    //Set list price
    String listPriceS = usD.getListPrice();
    if (Utility.isSet(listPriceS)) {
      try {
        double listPriceDb = Double.parseDouble(listPriceS);
      } catch (Exception exc) {
        return new WebError("exception.web.error.uploadFile.invalidListPrice", Args.typed(listPriceS,rowNum));
      }
      ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.LIST_PRICE, listPriceS, itemId, itemMetaList);
   }

    //Set mfcp (cost price)
    String mfcpS = usD.getMfcp();
    if (Utility.isSet(mfcpS)) {
      try {
        double mfcpDb = Double.parseDouble(mfcpS);
      } catch (Exception exc) {
        return new WebError("exception.web.error.uploadFile.invalidCostPrice", Args.typed(mfcpS,rowNum));
      }
      ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.COST_PRICE, mfcpS, itemId, itemMetaList);
    }

    //set other desc
    String otherDesc = usD.getOtherDesc();
    if (Utility.isSet(otherDesc)) {
        ItemUtil.updateMeta(RefCodeNames.ITEM_META_CD.OTHER_DESC, otherDesc, itemId, itemMetaList);
    }
    productD.setItemMeta(itemMetaList);

    // item mapping

    //set manuf
    String manufName = usD.getManufName();
    BusEntityData manufD = (BusEntityData) manufHM.get(manufName);
    if (manufD == null) {
        return new WebError("exception.web.error.uploadFile.noMatchedSystemManufFound", Args.typed(rowNum));
    }

    String manufSku = usD.getManufSku();
    if (!Utility.isSet(manufSku)) {
        return new WebError("exception.web.error.uploadFile.noManufSkuFound", Args.typed(rowNum));
    }

    List<ItemMappingData> itemMappingList = productD.getItemMapping();
    productD.setItemMapping(itemMappingList);

    if (manufD != null && Utility.isSet(manufSku)) {
        ItemUtil.updateManufactureMapping(itemId, manufSku, manufD.getBusEntityId(), itemMappingList);
    }

    //set distributor
    String distName = usD.getDistName();
    if (Utility.isSet(distName)) {
      distName = distName.trim();
      BusEntityData distD = (BusEntityData)distHM.get(distName);
      if (distD == null) {
        return new WebError("exception.web.error.uploadFile.noMatchedSystemDistFound", Args.typed(distName,rowNum));
      } else {
        Long distId = distD.getBusEntityId();
        String distSku = usD.getDistSku();
        String distPack = usD.getDistPack();
        String distUom = usD.getDistUom();
        List<ItemMappingData> distMappingList = new ArrayList<ItemMappingData>();

        if (Utility.isSet(distSku)) {
            // add/update dist mapping
            String distUomMultS = usD.getDistUomMult();
            if (distUomMultS == null) distUomMultS = "1";
            ItemMappingData distItemMapD = new ItemMappingData();
            distItemMapD.setBusEntityId(distId);
            distItemMapD.setItemId(productD.getItemData().getItemId());
            distItemMapD.setItemMappingCd(RefCodeNames.ITEM_MAPPING_CD.ITEM_DISTRIBUTOR);
            distItemMapD.setItemNum(distSku);
            distItemMapD.setItemPack(distPack);
            distItemMapD.setItemUom(distUom);
            distItemMapD.setStatusCd(RefCodeNames.ITEM_STATUS_CD.ACTIVE);
            distItemMapD.setUomConvMultiplier(new BigDecimal(distUomMultS));
            distItemMapD.setStandardProductList(usD.getSpl());

            distMappingList.add(distItemMapD);
            ItemUtil.updateItemDistributorsMapping(productD, distMappingList, distUomMultS, usD.getSpl());
         }

      }
    }

    //set category
    List<ItemAssocData> assocs = new ArrayList<ItemAssocData>();
    productD.setItemAssocs(assocs);

    String skuCateg = usD.getCategory();
    String skuAdminCateg = usD.getAdminCategory();
    if (!Utility.isSet(skuCateg)) {
        return new WebError("exception.web.error.uploadFile.noItemCategoryFound", Args.typed(rowNum));
    } else {
	  skuCateg = skuCateg.trim();
	  if(skuAdminCateg != null){
	    skuAdminCateg=skuAdminCateg.trim();
	  }
      ItemView categoryD = (ItemView) categoryHM.get(new Pair<String,String>(skuCateg, skuAdminCateg));
      if (categoryD == null) {
        return new WebError("exception.web.error.uploadFile.noStoreCategoryFound", Args.typed(skuCateg,skuAdminCateg,rowNum));
      } else {
          ItemAssocData categD = new ItemAssocData();
          categD.setItemAssocCd(RefCodeNames.ITEM_ASSOC_CD.PRODUCT_PARENT_CATEGORY);
          categD.setCatalogId(storeCatalogId);
          assocs.add(categD);
      }
    }

    //Set effective and expiration dates
    iD.setEffDate(new Date());
    iD.setExpDate(null);

    return null;
  }

    private Map<String, byte[]> loadExternalData(
            UploadSkuData pSkuData, WebErrors pErrors,  Long rowNum) {
        Map<String, byte[]> result = new TreeMap<String, byte[]>();
        loadData(result, pSkuData.getImageUrl(), RefCodeNames.ITEM_META_CD.IMAGE, pErrors, rowNum);
        loadData(result, pSkuData.getThumbnailUrl(), RefCodeNames.ITEM_META_CD.THUMBNAIL, pErrors, rowNum);
        loadData(result, pSkuData.getMsdsUrl(), RefCodeNames.ITEM_META_CD.MSDS, pErrors, rowNum);
        loadData(result, pSkuData.getDedUrl(), RefCodeNames.ITEM_META_CD.DED, pErrors, rowNum);
        loadData(result, pSkuData.getProdSpecUrl(), RefCodeNames.ITEM_META_CD.SPEC, pErrors, rowNum);
        return result;
    }

    private final static void loadData(final Map<String, byte[]> pExternalDataMap,
            final String pUrl, final String pFileType, WebErrors pErrors, Long rowNum) {
        String requiredMimeType = null;
        if (RefCodeNames.ITEM_META_CD.IMAGE.equals(pFileType) ||
            RefCodeNames.ITEM_META_CD.THUMBNAIL.equals(pFileType)) {
            requiredMimeType = "image";
        }
        try {
            byte[] data = loadData(pUrl, requiredMimeType);
            if (data != null) {
                pExternalDataMap.put(pFileType, data);
            }
        } catch (Exception e) {
            pErrors.putError("exception.web.error.uploadFile.cannotLoadDataFromUrl", Args.typed(pUrl, rowNum));
        }
    }

    private final static byte[] loadData(final String pUrl, String pRequiredMimeType) throws Exception {
		if (!Utility.isSet(pUrl)) {
			return null;
		}
		try {
			return HttpClientUtil.load(pUrl, pRequiredMimeType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Cann't load data from '" + pUrl + "'!");
		}
	}

    private void storeExternalData(ItemIdentView pProductD,
            UploadSkuData pSkuData,
            Map<String, byte[]> pExternalDataMap,
            WebErrors pErrors) throws Exception {
        if (pExternalDataMap == null || pExternalDataMap.size() == 0) {
            return;
        }
        if (pExternalDataMap.get(RefCodeNames.ITEM_META_CD.IMAGE) != null) {
            storeExternalData(pProductD, RefCodeNames.ITEM_META_CD.IMAGE, pSkuData.getImageUrl(),
                    pExternalDataMap.get(RefCodeNames.ITEM_META_CD.IMAGE), pErrors);
        }
        if (pExternalDataMap.get(RefCodeNames.ITEM_META_CD.THUMBNAIL) != null) {
            storeExternalData(pProductD, RefCodeNames.ITEM_META_CD.THUMBNAIL, pSkuData.getThumbnailUrl(),
                    pExternalDataMap.get(RefCodeNames.ITEM_META_CD.THUMBNAIL), pErrors);
        }
        if (pExternalDataMap.get(RefCodeNames.ITEM_META_CD.MSDS) != null) {
            storeExternalData(pProductD, RefCodeNames.ITEM_META_CD.MSDS, pSkuData.getMsdsUrl(),
                    pExternalDataMap.get(RefCodeNames.ITEM_META_CD.MSDS), pErrors);
        }
        if (pExternalDataMap.get(RefCodeNames.ITEM_META_CD.DED) != null) {
            storeExternalData(pProductD, RefCodeNames.ITEM_META_CD.DED, pSkuData.getDedUrl(),
                    pExternalDataMap.get(RefCodeNames.ITEM_META_CD.DED), pErrors);
        }
        if (pExternalDataMap.get(RefCodeNames.ITEM_META_CD.SPEC) != null) {
            storeExternalData(pProductD, RefCodeNames.ITEM_META_CD.SPEC, pSkuData.getProdSpecUrl(),
                    pExternalDataMap.get(RefCodeNames.ITEM_META_CD.SPEC), pErrors);
        }
    }

    private void storeExternalData(ItemIdentView item,
            String type, String url,
            byte[] pExternalData, WebErrors pErrors) throws Exception {
        if (pExternalData == null || pExternalData.length == 0) {
            return;
        }

        List<ItemMetaData> metaList = item.getItemMeta();
        Long itemId = item.getItemData().getItemId();
        String fileName = null;
        if (type.equals(RefCodeNames.ITEM_META_CD.THUMBNAIL)) {
                fileName = ItemUtil.fillOutAttachment(url, Constants.ITEM_ATTACHMENT_TYPE.THUMBNAIL, itemId);
                ItemUtil.updateMeta(type, fileName, itemId, metaList);
        } else if (type.equals(RefCodeNames.ITEM_META_CD.IMAGE)) {
                fileName = ItemUtil.fillOutAttachment(url, Constants.ITEM_ATTACHMENT_TYPE.IMAGE, itemId);
                ItemUtil.updateMeta(type, fileName, itemId, metaList);
        } else if (type.equals(RefCodeNames.ITEM_META_CD.DED)) {
                fileName = ItemUtil.fillOutAttachment(url, Constants.ITEM_ATTACHMENT_TYPE.DED, itemId);
                ItemUtil.updateMeta(type, fileName, itemId, metaList);
        } else if (type.equals(RefCodeNames.ITEM_META_CD.MSDS)) {
                fileName = ItemUtil.fillOutAttachment(url, Constants.ITEM_ATTACHMENT_TYPE.MSDS, itemId);
                ItemUtil.updateMeta(type, fileName, itemId, metaList);
        } else if (type.equals(RefCodeNames.ITEM_META_CD.SPEC)) {
                fileName = ItemUtil.fillOutAttachment(url, Constants.ITEM_ATTACHMENT_TYPE.SPEC, itemId);
                ItemUtil.updateMeta(type, fileName, itemId, metaList);
        }
        if (Utility.isSet(fileName)) {
            itemService.saveItemImage(fileName, "ItemImage", pExternalData);
        }

    }



    private HashMap<Pair<String, String>, ItemView> getCategoriesHM(Long catalogId) {
        List<ItemView> categories = itemService.getCategories(catalogId);
        HashMap<Pair<String, String>, ItemView> categoryHM = new HashMap<Pair<String, String>, ItemView>();
        for (ItemView ccD : categories ) {
            categoryHM.put(new Pair(ccD.getShortDesc(), ccD.getLongDesc()), ccD);
            if(ccD.getLongDesc() != null){
                categoryHM.put(new Pair(ccD.getShortDesc() + "("+ccD.getLongDesc()+")", null), ccD);
                categoryHM.put(new Pair(ccD.getShortDesc() + " ("+ccD.getLongDesc()+")", null), ccD);
                categoryHM.put(new Pair(ccD.getShortDesc() + "-"+ccD.getLongDesc(), null), ccD);
            }
        }
        return categoryHM;
    }

    private HashMap<String, BusEntityData> getManufacturersHM() {

            List<BusEntityData> manufDV = getStoreManufacturers();

        HashMap<String, BusEntityData> manufHM = new HashMap<String, BusEntityData> ();
        for (BusEntityData mD : manufDV ) {
          manufHM.put(mD.getShortDesc(), mD);
        }
        return manufHM;
    }

    private HashMap<String, BusEntityData> getDistributorsHM() {

        List<BusEntityData> distDV = getStoreDistributors();
        HashMap<String, BusEntityData>  distHM = new HashMap<String, BusEntityData> ();
        for (BusEntityData dD : distDV ) {
          distHM.put(dD.getShortDesc(), dD);
        }
        return distHM;
    }

}
