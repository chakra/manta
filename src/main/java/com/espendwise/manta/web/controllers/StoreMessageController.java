package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.LanguageData;
import com.espendwise.manta.model.data.StoreMessageData;
import com.espendwise.manta.model.data.StoreMessageDetailData;
import com.espendwise.manta.model.entity.StoreMessageEntity;
import com.espendwise.manta.model.view.CountryView;
import com.espendwise.manta.service.StoreMessageService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.StoreMessageUpdateRequest;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.StoreMessageDetailForm;
import com.espendwise.manta.web.forms.StoreMessageForm;
import com.espendwise.manta.web.resolver.StoreMessageWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(UrlPathKey.STORE_MESSAGE.IDENTIFICATION)
public class StoreMessageController extends BaseController {

    private static final Logger logger = Logger.getLogger(StoreMessageController.class);

    private StoreMessageService storeMessageService;

    @Autowired
    public StoreMessageController(StoreMessageService storeMessageService) {
        this.storeMessageService = storeMessageService;
    }

    @InitBinder
    public void initBinder(DataBinder binder) {
        logger.info("initBinder()=> init binder: " +binder);
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        logger.info("handleValidationException()=> Yeah, I caught the error: " +ex);

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new StoreMessageWebUpdateExceptionResolver());

        return "storeMessage/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.STORE_MESSAGE_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.STORE_MESSAGE) StoreMessageForm storeMessageForm, BindingResult bindingResult, Model model) throws Exception {

        WebErrors webErrors = new BindingWebErrors(bindingResult);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(storeMessageForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.STORE_MESSAGE, storeMessageForm);
            return "storeMessage/edit";
        }


        List locales = new ArrayList();
        int size = storeMessageForm.getDetail().size();
        Object[] duplicateLocaleList = null;
        
        for(int i=0; i<size; i++){
        	StoreMessageDetailForm detailForm = (StoreMessageDetailForm) storeMessageForm.getDetail().get(i);
        	String langCd = detailForm.getLanguageCd();
        	String countryCd = detailForm.getCountry();
        	
        	if(Utility.isSet(langCd)){
	        	
	        	String localeCd = langCd+"_"+countryCd;
	        	if(!locales.contains(localeCd)){
	        		locales.add(localeCd);
	        	}else{
	        		LanguageData language = AppResourceHolder.getAppResource().getDbConstantsResource().getLanguageByCd(langCd);
	        		if(Utility.isSet(countryCd)){
	        			CountryView country = AppResourceHolder.getAppResource().getDbConstantsResource().getCountryByCd(countryCd);
	        			duplicateLocaleList = new Object[]{language.getUiName(), country.getUiName()};
	        			webErrors.putError("exception.web.error.duplicateMessagesFoundC", Args.typed(duplicateLocaleList));
	        		}else{
	        			duplicateLocaleList = new Object[]{language.getUiName()};
	        			webErrors.putError("exception.web.error.duplicateMessagesFound", Args.typed(duplicateLocaleList));
	        		}
	        	}
        	}
        }
        
        if(duplicateLocaleList!=null && duplicateLocaleList.length>0){
        	
            model.addAttribute(SessionKey.STORE_MESSAGE, storeMessageForm);
            return "storeMessage/edit";
        }
        
        StoreMessageEntity storeMessage;
        try {

            storeMessage = save(storeMessageForm, false);

        } catch (ValidationException e) {

            return  handleValidationException(e, request);

        }

        logger.info("redirect(()=> redirect to: " + storeMessage.getStoreMessageId());

        return redirect(String.valueOf(storeMessage.getMessage().getStoreMessageId()));
    }



    public StoreMessageEntity save(StoreMessageForm storeMessageForm, Boolean publish) throws Exception {

        logger.info("save()=> BEGIN, StoreID: " + getStoreId() + ", ModelAttribute: " + storeMessageForm);

        AppLocale locale = getAppLocale();

        StoreMessageUpdateRequest updateRequest = new StoreMessageUpdateRequest();

        StoreMessageEntity storeMessageTest = new StoreMessageEntity();
        
        StoreMessageEntity storeMessage = new StoreMessageEntity(null, 
        		new ArrayList<StoreMessageDetailData>(), new StoreMessageData()) ;
        
        if (!storeMessageForm.getIsNew()) {
            storeMessage = storeMessageService.findStoreMessages( storeMessageForm.getStoreMessageId());
        }
        
        if(storeMessageForm.getMessageType().equals(RefCodeNames.MESSAGE_TYPE_CD.ACKNOWLEDGEMENT_REQUIRED)){
        	storeMessageForm.setForcedReadCount("1");
        }

        boolean clearViewHistory = false;

        StoreMessageData messageData = storeMessage.getMessage();
        
        
        messageData.setStoreMessageId(!storeMessageForm.getIsNew() ? storeMessageForm.getStoreMessageId() : null);
        messageData.setShortDesc(storeMessageForm.getName());
        messageData.setCountry(storeMessageForm.getCountry());
        messageData.setEndDate(parseDateNN(locale, storeMessageForm.getEndDate()));
        messageData.setDisplayOrder(parseNumberNN(storeMessageForm.getDisplayOrder()));
        messageData.setMessageType(storeMessageForm.getMessageType());
        messageData.setForcedRead(String.valueOf(storeMessageForm.getForcedRead()));
        messageData.setForcedReadCount(parseNumberNN(storeMessageForm.getForcedReadCount()));
        messageData.setLanguageCd(storeMessageForm.getLanguageCd());
        messageData.setCountry(storeMessageForm.getCountry());
        messageData.setMessageAbstract(storeMessageForm.getMessageAbstract());
        messageData.setMessageAuthor(storeMessageForm.getMessageAuthor());
        messageData.setMessageBody(storeMessageForm.getMessageBody());
        messageData.setMessageTitle(storeMessageForm.getMessageTitle());
        messageData.setPostedDate(parseDateNN(locale, storeMessageForm.getPostedDate()));
        messageData.setPublished(publish ? String.valueOf(true) : String.valueOf(storeMessageForm.getPublished()));
        List<StoreMessageDetailData> detailDataList = new ArrayList<StoreMessageDetailData>();
        for(StoreMessageDetailForm detailForm : storeMessageForm.getDetail()){
        	StoreMessageDetailData detailDataToEdit = new StoreMessageDetailData();
        	for(StoreMessageDetailData detailData : storeMessage.getDetails()){
        		if(detailData.getStoreMessageDetailId().equals(detailForm.getStoreMessageDetailId())){
        			detailDataToEdit = detailData;
        			break;
        		}
        	}
        	detailDataToEdit.setMessageDetailTypeCd(detailForm.getMessageDetailTypeCd());
        	detailDataToEdit.setCountry(detailForm.getCountry());
        	detailDataToEdit.setLanguageCd(detailForm.getLanguageCd());
        	detailDataToEdit.setMessageAbstract(detailForm.getMessageAbstract());
        	detailDataToEdit.setMessageAuthor(detailForm.getMessageAuthor());
        	detailDataToEdit.setMessageBody(detailForm.getMessageBody());
        	detailDataToEdit.setMessageTitle(detailForm.getMessageTitle());
        	
        	detailDataList.add(detailDataToEdit);
            	
        }
        storeMessage.setDetails(detailDataList);
            

        if ((storeMessageForm.getMessageType().equals(RefCodeNames.MESSAGE_TYPE_CD.ACKNOWLEDGEMENT_REQUIRED)
        		&& (storeMessageForm.getPublished() || publish))) {

        	boolean forcedRead = messageData.getMessageType().equals(RefCodeNames.MESSAGE_TYPE_CD.FORCE_READ) || 
        			messageData.getMessageType().equals(RefCodeNames.MESSAGE_TYPE_CD.ACKNOWLEDGEMENT_REQUIRED);
        	
        	boolean forcedReadForm = storeMessageForm.getMessageType().equals(RefCodeNames.MESSAGE_TYPE_CD.FORCE_READ) || 
        			storeMessageForm.getMessageType().equals(RefCodeNames.MESSAGE_TYPE_CD.ACKNOWLEDGEMENT_REQUIRED);
        	
        	Long forcedReadCount = (Long) parseNumberNN(forcedRead ? storeMessageForm.getForcedReadCount() : null);
        	
        	
            
            if (Utility.isTrue(forcedRead) != forcedReadForm || 
            		Utility.longNN(messageData.getForcedReadCount()) != Utility.longNN(forcedReadCount)) {
                clearViewHistory = true;
            }
          
            messageData.setForcedReadCount(forcedReadCount);
 
        }

        messageData.setStoreMessageStatusCd(storeMessageForm.getStoreMessageStatusCd());

        updateRequest.setStoreId(getStoreId());
        updateRequest.setStoreMessage(storeMessage);
        updateRequest.setApplyValidationRuie(true);
        updateRequest.setClearViewHistory(clearViewHistory);


        storeMessage = storeMessageService.saveMessageData(updateRequest);
        logger.info("save()=> END.");

        return storeMessage;

    }

    @SuccessMessage
    @Clean(SessionKey.STORE_MESSAGE_HEADER)
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public String publish(WebRequest request, @ModelAttribute(SessionKey.STORE_MESSAGE) StoreMessageForm storeMessageForm, BindingResult bindingResult, Model model) throws Exception {

        logger.info("publish()=> BEGIN");

        WebErrors webErrors = new BindingWebErrors(bindingResult);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(storeMessageForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.STORE_MESSAGE, storeMessageForm);
            return "storeMessage/edit";
        }

        StoreMessageEntity storeMessage;
        try {

            storeMessage = save(storeMessageForm, true);

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }

        logger.info("publish()=> END.");

        return redirect("../" + String.valueOf(storeMessage.getStoreMessageId()));

    }

    @RequestMapping(value = "/clone", method = RequestMethod.POST)
    public String clone(@ModelAttribute(SessionKey.STORE_MESSAGE) StoreMessageForm form, Model model) throws Exception {

        logger.info("clone()=> BEGIN");

        String title = AppI18nUtil.getMessage("admin.global.text.cloned", new String[]{form.getMessageTitle()});

        form.setStoreMessageId(null);
        form.setPublished(false);
        form.setMessageTitle(title);

        model.addAttribute(SessionKey.STORE_MESSAGE, form);

        logger.info("clone()=> END.");

        return "storeMessage/edit";

    }

    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.STORE_MESSAGE) StoreMessageForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new StoreMessageForm(null);
        StoreMessageDetailForm detailForm = new StoreMessageDetailForm();
        detailForm.setMessageDetailTypeCd(RefCodeNames.MESSAGE_DETAIL_TYPE_CD.DEFAULT);
        form.getDetail().add(detailForm);
        
        form.initialize();

        model.addAttribute(SessionKey.STORE_MESSAGE, form);

        logger.info("create()=> END.");

        return "storeMessage/edit";

    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(HttpServletRequest request, @ModelAttribute(SessionKey.STORE_MESSAGE) StoreMessageForm form, @PathVariable("storeMessageId") Long storeMessageId, Model model) {

        logger.info("show()=> BEGIN");

        if(storeMessageId > 0){
	        StoreMessageData message = storeMessageService.findStoreMessage(getStoreId(), storeMessageId);
	        
	        StoreMessageEntity messageEntity = storeMessageService.findStoreMessages(storeMessageId);
	        
	        //List<StoreMessageEntity> findStoreMessages(Long storeMessageId);
	
	        if (message != null) {
	
	            AppLocale locale = getAppLocale();
	
	            form.setPostedDate(formatDate(locale, message.getPostedDate()));
	            form.setEndDate(formatDate(locale, message.getEndDate()));
				form.setDisplayOrder(message.getDisplayOrder() != null
						? String.valueOf(message.getDisplayOrder()) : Constants.EMPTY);
				form.setForcedReadCount(message.getForcedReadCount() != null
						&& message.getForcedReadCount() > 0 ? String
						.valueOf(message.getForcedReadCount()) : Constants.EMPTY);
				//form.setCountry(message.getCountry());
	            //form.setLanguageCd(message.getLanguageCd());
	            //form.setMessageAbstract(message.getMessageAbstract());
	            //form.setMessageAuthor(message.getMessageAuthor());
	            //form.setMessageBody(message.getMessageBody());
	            //form.setMessageTitle(message.getMessageTitle());
	            form.setModBy(message.getModBy());
	            form.setPublished(Utility.isTrue(message.getPublished()));
	            form.setStoreMessageStatusCd(message.getStoreMessageStatusCd());
	            form.setStoreMessageId(message.getStoreMessageId());
	            form.setMessageType(message.getMessageType());
	            form.setName(message.getShortDesc());
	            List<StoreMessageDetailForm> detailFormList = new ArrayList<StoreMessageDetailForm>();
	            if(messageEntity != null){
	            	
	            	//sort by messageDetailTypeCd
	            	Collections.sort((List) messageEntity.getDetails(),AppComparator.STORE_MESSAGE_DETAIL_DATA_COMPARATOR);
	            	
		            for(StoreMessageDetailData detail : messageEntity.getDetails()){
		            	StoreMessageDetailForm detailForm = new StoreMessageDetailForm();
		            	
		            	detailForm.setStoreMessageDetailId(detail.getStoreMessageDetailId());
		            	detailForm.setMessageDetailTypeCd( detail.getMessageDetailTypeCd());
		                //detailForm.setEndDate(formatDate(locale, detail.getEndDate()));
		                //detailForm.setForcedRead(Utility.isTrue(detail.getForcedRead(), false));
		               // detailForm.setForcedReadCount(detail.getForcedReadCount() != null && detail.getForcedReadCount() > 0 ? String.valueOf(detail.getForcedReadCount()) : Constants.EMPTY);
		                detailForm.setCountry(detail.getCountry());
		                detailForm.setLanguageCd(detail.getLanguageCd());
		                detailForm.setMessageAbstract(detail.getMessageAbstract());
		                detailForm.setMessageAuthor(detail.getMessageAuthor());
		                detailForm.setMessageBody(detail.getMessageBody());
		                detailForm.setMessageTitle(detail.getMessageTitle());
		                detailForm.setStoreMessageId(detail.getStoreMessageId());
		                
		                detailFormList.add(detailForm);
		                
		                
		            }
	            }
	            form.setDetail(detailFormList);
	            
	
	        }
        }else{
        
            StoreMessageDetailForm detailForm = new StoreMessageDetailForm();
            detailForm.setMessageDetailTypeCd(RefCodeNames.MESSAGE_DETAIL_TYPE_CD.DEFAULT);
            form.getDetail().add(detailForm);
            
            form.initialize();

            model.addAttribute(SessionKey.STORE_MESSAGE, form);
        }

        model.addAttribute(SessionKey.STORE_MESSAGE, form);

        logger.info("show()=> END.");

        return "storeMessage/edit";

    }
   
	@RequestMapping(value = "/addTranslation", method = RequestMethod.POST)
	public String addTranslation(
			@ModelAttribute(SessionKey.STORE_MESSAGE) StoreMessageForm form,
			Model model) throws Exception {

		logger.info("addTranslation()=> BEGIN");
		StoreMessageDetailForm detailForm = new StoreMessageDetailForm();
		detailForm.setMessageDetailTypeCd(null);
		form.getDetail().add(detailForm);
		model.addAttribute(SessionKey.STORE_MESSAGE, form);

		logger.info("addTranslation()=> END.");

		return "storeMessage/edit";

	}
	
	@SuccessMessage
	@RequestMapping(value = "/deleteTranslation/{index}", method = RequestMethod.POST)
	public String deleteTranslation(WebRequest request, 
			@ModelAttribute(SessionKey.STORE_MESSAGE) StoreMessageForm form,
			@PathVariable("index") Long index,
			Model model) throws Exception {

		logger.info("deleteTranslation()=> BEGIN : index : " + index);
		List detailsList = form.getDetail();
		StoreMessageDetailForm detail = (StoreMessageDetailForm)detailsList.get(index.intValue());
		
		storeMessageService.deleteTranslation(form.getStoreMessageId(), detail.getStoreMessageDetailId());
        
        logger.info("deleteTranslation()=> END.index : ");
        
        return redirect("../../" + String.valueOf(form.getStoreMessageId()));
	}

    @ModelAttribute(SessionKey.STORE_MESSAGE)
    public StoreMessageForm initModel() {

        StoreMessageForm form = new StoreMessageForm(null);
        form.initialize();

        return form;

    }


}
