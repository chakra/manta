package com.espendwise.manta.web.controllers;

import com.espendwise.manta.dao.EventDAOImpl;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.view.CatalogManagerView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.EventService;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.SelectableObjects;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.IOUtility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.CatalogLoaderForm;
import com.espendwise.manta.web.forms.CatalogLoaderResultForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.resolver.LoaderWebExceptionResolver;
import com.espendwise.manta.web.util.*;
import com.espendwise.ocean.common.webaccess.BasicResponseValue;
import com.espendwise.ocean.common.webaccess.LoginData;
import com.espendwise.ocean.common.webaccess.ObjectTokenType;
import com.espendwise.ocean.common.webaccess.ResponseError;
import com.espendwise.ocean.common.webaccess.RestRequest;
import com.espendwise.ocean.common.webaccess.WebAccessException;
import com.espendwise.manta.model.view.CatalogManagerView;
import com.espendwise.manta.model.data.EventPropertyData;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.web.util.WebError;
//import com.espendwise.webservice.restful.value.CatalogValidationRequestData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;



import javax.servlet.http.HttpServletResponse;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
//import java.util.LinkedHashMap;
import java.util.List;
//import java.util.Map;
import java.io.InputStream;

import javassist.bytecode.Descriptor.Iterator;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(UrlPathKey.CATALOG_MANAGER.LOADER)
@SessionAttributes({SessionKey.CATALOG_LOADER, SessionKey.CATALOG_LOADER_RESULT})
@AutoClean(SessionKey.CATALOG_LOADER_RESULT)
public class CatalogLoaderController extends BaseController {

    private static final Logger logger = Logger.getLogger(CatalogLoaderController.class);
     
    
    private EventService eventService;
    private CatalogService catalogService;

    @Autowired
    public CatalogLoaderController(EventService eventService, CatalogService catalogService ) {
        this.eventService = eventService;
        this.catalogService = catalogService;
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "catalogManager/loader";
    }
    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new LoaderWebExceptionResolver());
        
        return "catalogManager/loader";
    }  
    
    @SuccessMessage
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.CATALOG_LOADER) CatalogLoaderForm loaderForm, 
    		@ModelAttribute(SessionKey.CATALOG_LOADER_RESULT) CatalogLoaderResultForm resultForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, catalogLoaderForm: "+loaderForm);
        
        loaderForm.setProcessNow(false);
        String mapping = save(request, loaderForm, resultForm, model, RefCodeNames.EVENT_STATUS_CD.STATUS_HOLD);

 
        logger.info("save()=> END.");

        return mapping;
    }
  
    @SuccessMessage
    @RequestMapping(value = "/processNow", method = RequestMethod.POST)
    public String processNow(WebRequest request, @ModelAttribute(SessionKey.CATALOG_LOADER) CatalogLoaderForm loaderForm, 
    		@ModelAttribute(SessionKey.CATALOG_LOADER_RESULT) CatalogLoaderResultForm resultForm, Model model) throws Exception {

        logger.info("processNow()=> BEGIN, catalogLoaderForm: "+loaderForm);

        loaderForm.setProcessNow(true);
        String mapping = save(request, loaderForm, resultForm, model, RefCodeNames.EVENT_STATUS_CD.STATUS_READY);
        
        logger.info("processNow()=> END.");

        return mapping;
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.CATALOG_LOADER_RESULT) CatalogLoaderResultForm resultForm,
    		@ModelAttribute(SessionKey.CATALOG_LOADER) CatalogLoaderForm loaderForm, Model model) {
    	logger.info("show()=> BEGIN");        
    	resultForm.reset();
    	loaderForm.reset();
        model.addAttribute(SessionKey.CATALOG_LOADER, loaderForm);

        logger.info("show()=> END.");
        return "catalogManager/loader";

    }
    
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel(@ModelAttribute(SessionKey.CATALOG_LOADER_RESULT) CatalogLoaderResultForm resultForm,  WebRequest request, HttpSession session) throws Exception {
    	logger.info("cancel()=> BEGIN");
    	WebErrors webErrors = new WebErrors(request);
        List<Long> selected = new ArrayList<Long>();
        List<Long> deSelected = new ArrayList<Long>();        
        
        logger.info("cancel()=>  Newly selected:"+ resultForm.getCatalogs().getNewlySelected());
        logger.info("cancel()=>  selected:"+ resultForm.getCatalogs().getSelected());
    	
        for (CatalogManagerView config : resultForm.getCatalogs().getSelected()){
    		selected.add(config.getEventId());
    	}    	
    	if (selected.isEmpty()) {
    		webErrors.putError(new WebError("catalogManager.loader.noEventSelectedMessage"));
    	}
        
        if (!selected.isEmpty() || !deSelected.isEmpty()){
        	eventService.cancellEvent(selected);
            displayAll(resultForm);
            WebAction.success(request, new SuccessActionMessage("catalogManager.loader.cancelledMessage"), WebRequest.SCOPE_REQUEST);
        }


        logger.info("cancel()=> END.");
        
        return "catalogManager/loader";

    }
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String download(@ModelAttribute(SessionKey.CATALOG_LOADER_RESULT) CatalogLoaderResultForm resultForm,  WebRequest request, HttpSession session, HttpServletResponse response) throws Exception {

        logger.info("download()=> BEGIN");
        final String FILE_NAME_PROP = "fileName";
        final String DATA_CONTENT_PROP = "dataContents";
        WebErrors webErrors = new WebErrors(request);

        List<Long> selected = new ArrayList<Long>();
        List<Long> deSelected = new ArrayList<Long>();        
        
        logger.info("download()=>  Newly Selected:"+ resultForm.getCatalogs().getNewlySelected());
        logger.info("download()=>  selected:"+ resultForm.getCatalogs().getSelected());
    	
        for (CatalogManagerView config : resultForm.getCatalogs().getSelected()){
    		selected.add(config.getEventId());
    	}    	

    	List<String> propNames = new ArrayList<String>();
        propNames.add(FILE_NAME_PROP);
        propNames.add(DATA_CONTENT_PROP);
        
        logger.info("download()=>  selected.size():"+ selected.size());
    	
    	if (selected.isEmpty()) {
    		webErrors.putError(new WebError("catalogManager.loader.noEventSelectedMessage"));

    	} else if (selected.size() > 1){
    		webErrors.putError(new WebError("catalogManager.loader.multipleEventSelectedMessage"));
    	
    	}  else {
            logger.info("download()=> to download!");
    		
        	List<EventPropertyData> eventProps = eventService.getEventProperties(selected, propNames);
        	String fileName = null;
        	byte[] dataContents = null;
        	if (eventProps != null ){
        		for (EventPropertyData eventPropD : eventProps){
        			if (eventPropD != null && FILE_NAME_PROP.equals(eventPropD.getShortDesc())){
        				fileName = eventPropD.getStringVal();
        				continue;
        			}
        			if (eventPropD != null && DATA_CONTENT_PROP.equals(eventPropD.getShortDesc())){
        				dataContents = eventPropD.getBlobValue();
        				continue;
        			}
        		}
        	}
            
        	Object obj = null;
            if (dataContents != null) {
                obj = IOUtility.bytesToObject(dataContents);
	        	byte transferData[] = (byte[]) obj;
	            if (transferData != null && fileName != null) {
	                response.setContentType("application/octet-stream");
	                response.setHeader("Cache-Control", "no-cache");
	                response.setHeader("Content-disposition", "attachment; filename=\"download_"+ fileName + "\"");
	                response.getOutputStream().write(transferData);
	                response.flushBuffer();
	            }
            }
        	
        }

        logger.info("download()=> END.");
        
        return "catalogManager/loader";

    }
    
    @RequestMapping(value = "/displayAll", method = RequestMethod.GET)
    public String displayAll(@ModelAttribute(SessionKey.CATALOG_LOADER_RESULT) CatalogLoaderResultForm resultForm) throws Exception {
    	logger.info("displayAll()=> BEGIN");
    	List<CatalogManagerView> catalogs = eventService.getCatalogs(getStoreId(), RefCodeNames.EVENT_STATUS_CD.STATUS_HOLD);
    	//===== date convertation:
    	for (CatalogManagerView catalog : catalogs) {
    		String effectiveDate = catalog.getEffectiveDate();
    		String format = (catalog.getDateFormat()!= null) ? catalog.getDateFormat() :I18nUtil.getDatePattern();
    		
    		SimpleDateFormat sdf = new SimpleDateFormat(format);
    		logger.info("displayAll()=> format="+ format);
    		Date effDateD = sdf.parse(effectiveDate);
    		
    		catalog.setEffDateD(effDateD);
    	}
    	//==========
		WebSort.sort(catalogs, "eventId", new Boolean(true));
    	SelectableObjects<CatalogManagerView> selectableObj = new SelectableObjects<CatalogManagerView>(
				catalogs,
				new ArrayList<CatalogManagerView>(),
                AppComparator.CATALOG_VIEW_COMPARATOR);
		resultForm.setCatalogs(selectableObj);
    	logger.info("displayAll()=> END.");
    	return "catalogManager/loader";
    }

    
    @RequestMapping(value = "/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.CATALOG_LOADER_RESULT) CatalogLoaderResultForm resultForm, @PathVariable String field) throws Exception {
    	WebSort.sort(resultForm, field);
    	return "catalogManager/loader";
    }
    
    @ModelAttribute(SessionKey.CATALOG_LOADER)
    public CatalogLoaderForm initModel() {

    	CatalogLoaderForm form = new CatalogLoaderForm();
        form.initialize();
        return form;
    }
    
    @ModelAttribute(SessionKey.CATALOG_LOADER_RESULT)
    public CatalogLoaderResultForm init(HttpSession session) {

    	CatalogLoaderResultForm catalogResult = (CatalogLoaderResultForm) session.getAttribute(SessionKey.CATALOG_LOADER_RESULT);

        if (catalogResult == null) {
        	catalogResult = new CatalogLoaderResultForm();
        }

        return catalogResult;

    }
    
    private String save (WebRequest request, CatalogLoaderForm loaderForm, CatalogLoaderResultForm resultForm, Model model, String eventStatus ) throws Exception {
        WebErrors webErrors = new WebErrors(request);
        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(loaderForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.CATALOG_LOADER, loaderForm);
            return "catalogManager/loader";
        }
        //file validation
        String streamType = loaderForm.getUploadedFile().getOriginalFilename().endsWith(".xls") ? "xls" : 
        	loaderForm.getUploadedFile().getOriginalFilename().endsWith(".xlsx") ? "xlsx" :
        	loaderForm.getUploadedFile().getOriginalFilename().endsWith(".csv") ? "csv" : null;
        boolean validationCompleted= false;
        int count = 0;
        try{        	
        	count = catalogService.processCatalogUpload(getAppUser().getLocale(), getStoreId(), loaderForm.getUploadedFile().getInputStream(), streamType);
        	//SuccessActionMessage message = catalogService.processCatalogUpload(getAppUser().getLocale(), getStoreId(), loaderForm.getUploadedFile().getInputStream(), streamType);
        	//WebAction.success(request, message);
        	validationCompleted = true;
        }catch (ValidationException e) {
        	logger.info("save() =======> handleValidationException ");
            return handleValidationException(e, request);
            
        }catch(Exception e){
        	webErrors.putError(new WebError("dummyMessageKey", null, e.getMessage()));
            return "catalogManager/loader";
        }
        
        // validate file contents with stjohn service
        //CatalogValidationRequestData requestData = new CatalogValidationRequestData();
        //requestData.setStoreId(new Integer(getStoreId().intValue()));
        //requestData.setFileName(loaderForm.getUploadedFile().getOriginalFilename());
        //requestData.setDataContents(EventDAOImpl.objectToBytes(loaderForm.getUploadedFile().getBytes()));
        //requestStjohnService(requestData, "/service/validateCatalog", webErrors);
        
        //TODO validate file contents !!!!
        
        if (!validationCompleted){
        	return "catalogManager/loader";
        }
        
    	logger.info("save() =======> Creating Event ==> Begin. ");
		List<CatalogManagerView> catalogs = new ArrayList<CatalogManagerView>();
		CatalogManagerView catalog = new CatalogManagerView();
		catalog.setEventId(new Long(0));
		catalog.setStoreId(getStoreId());
		catalog.setStoreName(getStoreName());
		catalog.setFileName(loaderForm.getUploadedFile().getOriginalFilename());
		catalog.setCatalogCount(count);
		catalog.setEffectiveDate(loaderForm.getEffectiveDate());
		catalog.setDateFormat(I18nUtil.getDatePattern());
		catalog.setTimeZone(loaderForm.getTimeZone());
		
		SimpleDateFormat sdf = new SimpleDateFormat(catalog.getDateFormat());
		Date effDateD = sdf.parse(catalog.getEffectiveDate());
		catalog.setEffDateD(effDateD);
		
		catalog.setFileBinaryData(loaderForm.getUploadedFile().getBytes());
		catalogs.add(catalog);
		try {
			catalog = eventService.saveCatalog(catalog, getAppUser().getLocale(), eventStatus);
	    	logger.info("save() =======> Creating Event : eventId="+catalog.getEventId() );
			
		} catch (DatabaseUpdateException e) {
        	e.printStackTrace();
            return handleDatabaseUpdateException(e, request);
        }		
		
    		SelectableObjects<CatalogManagerView> selectableObj = new SelectableObjects<CatalogManagerView>(
    				catalogs,
    				new ArrayList<CatalogManagerView>(),
                    AppComparator.CATALOG_VIEW_COMPARATOR);
    		resultForm.setCatalogs(selectableObj);
        
    		return "catalogManager/loader";
    }
 }
