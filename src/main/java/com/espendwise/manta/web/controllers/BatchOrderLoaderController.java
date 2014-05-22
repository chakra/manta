package com.espendwise.manta.web.controllers;

import com.espendwise.manta.dao.EventDAOImpl;
import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.view.BatchOrderView;
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
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.web.forms.BatchOrderLoaderForm;
import com.espendwise.manta.web.forms.BatchOrderLoaderResultForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;
import com.espendwise.ocean.common.webaccess.BasicResponseValue;
import com.espendwise.ocean.common.webaccess.LoginData;
import com.espendwise.ocean.common.webaccess.ObjectTokenType;
import com.espendwise.ocean.common.webaccess.ResponseError;
import com.espendwise.ocean.common.webaccess.RestRequest;
import com.espendwise.ocean.common.webaccess.WebAccessException;
import com.espendwise.webservice.restful.value.BatchOrderValidationRequestData;

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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javassist.bytecode.Descriptor.Iterator;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(UrlPathKey.BATCH_ORDER.LOADER)
@SessionAttributes({SessionKey.BATCH_ORDER_LOADER, SessionKey.BATCH_ORDER_LOADER_RESULT})
@AutoClean(SessionKey.BATCH_ORDER_LOADER_RESULT)
public class BatchOrderLoaderController extends BaseController {

    private static final Logger logger = Logger.getLogger(BatchOrderLoaderController.class);
    public static String asSoonAsPossible = new MessageI18nArgument("batchOrder.loader.processWhen.option.asSoonAsPossible").resolve();
    public static String moring = new MessageI18nArgument("batchOrder.loader.processWhen.option.moring").resolve();
    public static String afternoon = new MessageI18nArgument("batchOrder.loader.processWhen.option.afternoon").resolve();
    public static String evening = new MessageI18nArgument("batchOrder.loader.processWhen.option.evening").resolve();
    
    
    private EventService eventService;

    @Autowired
    public BatchOrderLoaderController(EventService eventService) {
        this.eventService = eventService;
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "batchOrder/loader";
    }

    @SuccessMessage
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.BATCH_ORDER_LOADER) BatchOrderLoaderForm loaderForm, 
    		@ModelAttribute(SessionKey.BATCH_ORDER_LOADER_RESULT) BatchOrderLoaderResultForm resultForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, batchOrderLoaderForm: "+loaderForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(loaderForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.BATCH_ORDER_LOADER, loaderForm);
            return "batchOrder/loader";
        }
        
        // validate file contents with stjohn service
        BatchOrderValidationRequestData requestData = new BatchOrderValidationRequestData();
        requestData.setStoreId(new Integer(getStoreId().intValue()));
        requestData.setFileName(loaderForm.getUploadedFile().getOriginalFilename());
        requestData.setDataContents(EventDAOImpl.objectToBytes(loaderForm.getUploadedFile().getBytes()));
        Map returnValues = (Map) requestStjohnService(requestData, "/service/validateBatchOrder", webErrors);
        
        if (webErrors.size() > 0){
        	return "batchOrder/loader";
        }
        
        Integer orderCount = new Integer(returnValues.get("orderCount").toString());
        
		List<BatchOrderView> batchOrders = new ArrayList<BatchOrderView>();
		BatchOrderView batchOrder = new BatchOrderView();
		batchOrder.setEventId(new Long(0));
		batchOrder.setStoreId(getStoreId());
		batchOrder.setStoreName(getStoreName());
		batchOrder.setFileName(loaderForm.getUploadedFile().getOriginalFilename());
		String applyToBudget = loaderForm.isApplyToBudget() ? Constants.YES : Constants.NO;
		batchOrder.setApplyToBudget(applyToBudget);
		String sendConfirmation = loaderForm.isSendConfirmation() ? Constants.YES : Constants.NO;
		batchOrder.setSendConfirmation(sendConfirmation);
		batchOrder.setOrderCount(orderCount);
		batchOrder.setProcessOn(loaderForm.getProcessOn());
		batchOrder.setDateFormat(I18nUtil.getDatePattern());
		batchOrder.setProcessWhen(loaderForm.getProcessWhen());
		batchOrder.setFileBinaryData(loaderForm.getUploadedFile().getBytes());
		batchOrders.add(batchOrder);
		try {
			batchOrder = eventService.saveBatchOrder(batchOrder, getAppUser().getLocale());
        } catch (DatabaseUpdateException e) {
        	e.printStackTrace();
            return handleDatabaseUpdateException(e, request);
        }		
		
    		SelectableObjects<BatchOrderView> selectableObj = new SelectableObjects<BatchOrderView>(
    				batchOrders,
    				new ArrayList<BatchOrderView>(),
                    AppComparator.BATCH_ORDER_VIEW_COMPARATOR);
    		resultForm.setBatchOrders(selectableObj);
        

        logger.info("save()=> END.");

        return "batchOrder/loader";
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.BATCH_ORDER_LOADER_RESULT) BatchOrderLoaderResultForm resultForm,
    		@ModelAttribute(SessionKey.BATCH_ORDER_LOADER) BatchOrderLoaderForm loaderForm, Model model) {
    	logger.info("show()=> BEGIN");        
    	resultForm.reset();
    	loaderForm.reset();
        model.addAttribute(SessionKey.BATCH_ORDER_LOADER, loaderForm);

        logger.info("show()=> END.");
        return "batchOrder/loader";

    }
    
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel(@ModelAttribute(SessionKey.BATCH_ORDER_LOADER_RESULT) BatchOrderLoaderResultForm resultForm,  WebRequest request) throws Exception {

        logger.info("cancel()=> BEGIN");

        List<Long> selected = new ArrayList<Long>();
        List<Long> deSelected = new ArrayList<Long>();        
        
    	for (BatchOrderView config : resultForm.getBatchOrders().getNewlySelected()){
    		selected.add(config.getEventId());
    	}    	
        
        if (!selected.isEmpty() || !deSelected.isEmpty()){
        	eventService.cancellEvent(selected);
        }

        disploayAll(resultForm);
        WebAction.success(request, new SuccessActionMessage("batchOrder.loader.cancelledMessage"), WebRequest.SCOPE_REQUEST);

        logger.info("cancel()=> END.");
        
        return "batchOrder/loader";

    }
    
    @RequestMapping(value = "/displayAll", method = RequestMethod.GET)
    public String disploayAll(@ModelAttribute(SessionKey.BATCH_ORDER_LOADER_RESULT) BatchOrderLoaderResultForm resultForm) throws Exception {
    	logger.info("disploayAll()=> BEGIN");
    	List<BatchOrderView> batchOrders = eventService.getBatchOrders(getStoreId(), RefCodeNames.EVENT_STATUS_CD.STATUS_HOLD);
    	SelectableObjects<BatchOrderView> selectableObj = new SelectableObjects<BatchOrderView>(
				batchOrders,
				new ArrayList<BatchOrderView>(),
                AppComparator.BATCH_ORDER_VIEW_COMPARATOR);
		resultForm.setBatchOrders(selectableObj);
		WebSort.sort(resultForm, "eventId");
    	logger.info("displayAll()=> END.");
    	return "batchOrder/loader";
    }

    
    @RequestMapping(value = "/sortby/{field}", method = RequestMethod.GET)
    public String sort(@ModelAttribute(SessionKey.BATCH_ORDER_LOADER_RESULT) BatchOrderLoaderResultForm resultForm, @PathVariable String field) throws Exception {
    	WebSort.sort(resultForm, field);
    	return "batchOrder/loader";
    }
    
    @ModelAttribute(SessionKey.BATCH_ORDER_LOADER)
    public BatchOrderLoaderForm initModel() {

    	BatchOrderLoaderForm form = new BatchOrderLoaderForm();
        form.initialize();
        populateProcessTimeChoices(form);
        return form;
    }
    
    @ModelAttribute(SessionKey.BATCH_ORDER_LOADER_RESULT)
    public BatchOrderLoaderResultForm init(HttpSession session) {

    	BatchOrderLoaderResultForm batchOrderResult = (BatchOrderLoaderResultForm) session.getAttribute(SessionKey.BATCH_ORDER_LOADER_RESULT);

        if (batchOrderResult == null) {
        	batchOrderResult = new BatchOrderLoaderResultForm();
        }

        return batchOrderResult;

    }
    
    private void populateProcessTimeChoices(BatchOrderLoaderForm form) {
        List<Pair<String, String>> processWhenChoices = new ArrayList<Pair<String, String>>();
        processWhenChoices.add(new Pair<String, String>(asSoonAsPossible, asSoonAsPossible));
        processWhenChoices.add(new Pair<String, String>(moring, moring));
        processWhenChoices.add(new Pair<String, String>(afternoon, afternoon));
        processWhenChoices.add(new Pair<String, String>(evening, evening));
        form.setProcessWhenChoices(processWhenChoices);
    }
}
