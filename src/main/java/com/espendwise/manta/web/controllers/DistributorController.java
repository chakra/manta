package com.espendwise.manta.web.controllers;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.data.CurrencyData;
import com.espendwise.manta.model.view.CatalogStructureListView;
import com.espendwise.manta.model.view.ContactView;
import com.espendwise.manta.model.view.DistributorIdentPropertiesView;
import com.espendwise.manta.model.view.DistributorIdentView;
import com.espendwise.manta.model.view.TradingPartnerListView;
import com.espendwise.manta.service.CatalogService;
import com.espendwise.manta.service.CurrencyService;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.DistributorService;
import com.espendwise.manta.service.TradingPartnerService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Pair;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.binder.PropertyBinder;
import com.espendwise.manta.util.criteria.CatalogStructureListViewCriteria;
import com.espendwise.manta.util.criteria.CurrencyCriteria;
import com.espendwise.manta.util.criteria.TradingPartnerListViewCriteria;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.DistributorForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.resolver.DistributorWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;


@Controller
@RequestMapping(UrlPathKey.DISTRIBUTOR.IDENTIFICATION)
public class DistributorController extends BaseController {

    private static final Logger logger = Logger.getLogger(DistributorController.class);

    private DistributorService distributorService;
    private CurrencyService currencyService;
    private TradingPartnerService tradingPartnerService;

    @Autowired
    public DistributorController(DistributorService distributorService, CurrencyService currencyService, TradingPartnerService tradingPartnerService) {
        this.distributorService = distributorService;
        this.currencyService = currencyService;
        this.tradingPartnerService = tradingPartnerService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DistributorWebUpdateExceptionResolver());

        return "distributor/edit";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "distributor/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.DISTRIBUTOR_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.DISTRIBUTOR) DistributorForm distributorForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, distributorForm: "+distributorForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(distributorForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            populateFormReferenceData(distributorForm);
            model.addAttribute(SessionKey.DISTRIBUTOR, distributorForm);
            return "distributor/edit";
        }
        
        //St.John disallowed setting the status to inactive if there are existing items in shopping
        //catalogs so enforce that logic here.  See StoreDistMgrLogic.updateDistributor(), lines 801-814
        String status = distributorForm.getDistributorStatus();
        if (RefCodeNames.DISTRIBUTOR_STATUS_CD.INACTIVE.equalsIgnoreCase(status)) {
        	Long distributorId = distributorForm.getDistributorId();
        	if (distributorId != null) {
	        	CatalogService catalogService = ServiceLocator.getCatalogService();
	        	CatalogStructureListViewCriteria searchCriteria = new CatalogStructureListViewCriteria(distributorId, Constants.FILTER_RESULT_LIMIT.CATALOG);
	        	searchCriteria.setCatalogType(RefCodeNames.CATALOG_TYPE_CD.SHOPPING);
	        	List<CatalogStructureListView> catalogStructures = catalogService.findCatalogStructuresByCriteria(searchCriteria);
	        	if (catalogStructures != null && !catalogStructures.isEmpty()) {
	            	webErrors.putError("exception.web.error.invalidDistributorStatusShoppingCatalogItems", (TypedArgument[])null);
	                populateFormReferenceData(distributorForm);
	                model.addAttribute(SessionKey.DISTRIBUTOR, distributorForm);
	                return "distributor/edit";
	        	}
        	}
        }

        DistributorIdentView distributorIdentView = new DistributorIdentView();

        if (!distributorForm.getIsNew()) {
        	distributorIdentView = distributorService.findDistributorToEdit(getStoreId(),
        			distributorForm.getDistributorId()
            );
        } else {

        }

        distributorIdentView.setBusEntityData(WebFormUtil.createBusEntityData(distributorIdentView.getBusEntityData(), distributorForm));
        distributorIdentView.setContact(WebFormUtil.createContact(distributorIdentView.getContact(), distributorForm));
        distributorIdentView.setProperties(WebFormUtil.createDistributorProperties(distributorIdentView.getProperties(), distributorForm));

        try {

        	distributorIdentView = distributorService.saveDistributor(
                    getStoreId(),
                    getUserId(),
                    distributorIdentView
            );

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        } catch (DatabaseUpdateException e) {

            return handleDatabaseUpdateException(e, request);

        }

        logger.info("redirect(()=> redirect to: " + distributorIdentView.getBusEntityData().getBusEntityId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(distributorIdentView.getBusEntityData().getBusEntityId()));
    }



    @RequestMapping(value = "/create")
    public String create(@ModelAttribute(SessionKey.DISTRIBUTOR)DistributorForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new DistributorForm();
        form.initialize();
        populateFormReferenceData(form);

        model.addAttribute(SessionKey.DISTRIBUTOR, form);

        logger.info("create()=> END.");

        return "distributor/edit";

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.DISTRIBUTOR) DistributorForm form, @PathVariable(IdPathKey.DISTRIBUTOR_ID) Long distributorId, Model model) {

        logger.info("show()=> BEGIN");

        DistributorIdentView distributor = distributorService.findDistributorToEdit(getStoreId(),
        		distributorId
        );

        logger.info("show()=> distributor: "+distributor);

        if (distributor != null) {
            BusEntityData entity = distributor.getBusEntityData();
            ContactView contact = distributor.getContact();
            DistributorIdentPropertiesView properties = PropertyBinder.bindDistributorProperties(new DistributorIdentPropertiesView(),
                    entity.getBusEntityId(),
                    distributor.getProperties()
            );
            
            form.setDistributorId(distributor.getBusEntityData().getBusEntityId());
            form.setDistributorName(distributor.getBusEntityData().getShortDesc());
            form.setDistributorStatus(distributor.getBusEntityData().getBusEntityStatusCd());
            form.setDistributorLocale(distributor.getBusEntityData().getLocaleCd());
            form.setDistributorDisplayName(PropertyUtil.toValueNN(properties.getDisplayName()));
            form.setDistributorType(PropertyUtil.toValueNN(properties.getType()));
            form.setDistributorCallCenterHours(PropertyUtil.toValueNN(properties.getCallCenterHours()));
            form.setDistributorCompanyCode(PropertyUtil.toValueNN(properties.getCompanyCode()));
            form.setDistributorCustomerReferenceCode(PropertyUtil.toValueNN(properties.getCustomerReferenceCode()));
            form.setContact(WebFormUtil.createContactForm(contact));
            List<TradingPartnerListView> tradingPartners = null;
            if (getAppUser().isAdmin() || getAppUser().isSystemAdmin()) {
            	tradingPartners = tradingPartnerService.findTradingPartnersByCriteria(new TradingPartnerListViewCriteria(distributorId, null));
            }
            else {
        		tradingPartners = new ArrayList<TradingPartnerListView>();
        	}
            form.setTradingPartners(tradingPartners);
        }
        
        populateFormReferenceData(form);

        model.addAttribute(SessionKey.DISTRIBUTOR, form);

        logger.info("show()=> END.");
         
        return "distributor/edit";

    }
    
    @RequestMapping(value = "/reset")
    public String reset(@ModelAttribute(SessionKey.DISTRIBUTOR) DistributorForm form, @PathVariable(IdPathKey.DISTRIBUTOR_ID) Long distributorId, Model model) {
    	return show(form, distributorId, model);
    }

    private void populateFormReferenceData(DistributorForm form) {
        //populate the form with reference information (currently only locale choices)
        List<Pair<String, String>> locales = new ArrayList<Pair<String, String>>();
        List<CurrencyData> currencyList = currencyService.findCurrenciesByCriteria(new CurrencyCriteria());
        for (CurrencyData currency : currencyList) {
        	String locale = currency.getLocale();
        	if (locale.length() > 2) {
        		locales.add(new Pair<String, String>(locale, locale));
        	}
        }
        //include pig-latin as a choice
        locales.add(new Pair<String, String>(Constants.LOCALE_PIG_LATIN,Constants.LOCALE_PIG_LATIN));
        form.setLocaleChoices(locales);
    }

    @ModelAttribute(SessionKey.DISTRIBUTOR)
    public DistributorForm initModel() {

    	DistributorForm form = new DistributorForm();
        form.initialize();

        return form;

    }
    @ResponseBody
  //  @RequestMapping(value = DistributorForm.ACTION.GET_QRCODE)
    public byte[] getQRCode( @PathVariable(IdPathKey.LOCATION_ID) Long locationId) throws Exception {

        logger.info("getQRCode()=> BEGIN storeId = " + getStoreId() + ", locationId = " + locationId);

        byte[] stream = ((QRCode.from("https://LO/" + getStoreId() +"/"+ locationId).to(ImageType.GIF).withSize(175, 175).stream()).toByteArray() );
 
        logger.info("getQRCode()=> END. stream.length = " + (stream!= null ? stream.length : 0));

        return stream;

    }


}
