package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.ContactView;
import com.espendwise.manta.model.view.SiteIdentPropertiesView;
import com.espendwise.manta.model.view.SiteIdentView;
import com.espendwise.manta.service.DatabaseUpdateException;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.Clean;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.spi.AutoClean;
import com.espendwise.manta.util.PropertyUtil;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.binder.PropertyBinder;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.SiteForm;
import com.espendwise.manta.web.resolver.DatabaseWebUpdateExceptionResolver;
import com.espendwise.manta.web.resolver.SiteWebUpdateExceptionResolver;
import com.espendwise.manta.web.util.*;
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
import net.glxn.qrgen.*;
import net.glxn.qrgen.image.*;


import java.util.List;


@Controller
@RequestMapping(UrlPathKey.SITE.IDENTIFICATION)
@AutoClean(SessionKey.SITE_USER_FILTER_RESULT)
public class SiteController extends BaseController {

    private static final Logger logger = Logger.getLogger(SiteController.class);

    private SiteService siteService;

    @Autowired
    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }


    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new SiteWebUpdateExceptionResolver());

        return "site/edit";
    }

    public String handleDatabaseUpdateException(DatabaseUpdateException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new DatabaseWebUpdateExceptionResolver());

        return "site/edit";
    }

    @SuccessMessage
    @Clean(SessionKey.SITE_HEADER)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String save(WebRequest request, @ModelAttribute(SessionKey.SITE) SiteForm siteForm, Model model) throws Exception {

        logger.info("save()=> BEGIN, siteForm: "+siteForm);

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(siteForm);

        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            model.addAttribute(SessionKey.SITE, siteForm);
            return "site/edit";
        }

        SiteIdentView siteView = new SiteIdentView();

        if (!siteForm.getIsNew()) {
            siteView = siteService.findSiteToEdit(getStoreId(),
                    siteForm.getSiteId()
            );
        }

        siteView.setAccountId(Parse.parseLong(siteForm.getAccountId()));
        siteView.setAccountName(siteForm.getAccountName());
        siteView.setBusEntityData(WebFormUtil.createBusEntityData(getAppLocale(), siteView.getBusEntityData(), siteForm));

        siteView.setProperties(WebFormUtil.createSiteIdentProperties(siteView.getProperties(), siteForm));
        siteView.setContact(WebFormUtil.createContact(siteView.getContact(), siteForm));

        try {

            siteView = siteService.saveSiteIdent(
                    getStoreId(),
                    getUserId(),
                    siteView,
                    siteForm.isClonedWithAssoc() ? (Long) parseNumber(siteForm.getCloneId()) : null
            );

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        } catch (DatabaseUpdateException e) {

            return handleDatabaseUpdateException(e, request);

        }

        logger.info("redirect(()=> redirect to: " + siteView.getBusEntityData().getBusEntityId());
        logger.info("save()=> END.");

        return redirect(String.valueOf(siteView.getBusEntityData().getBusEntityId()));
    }



    @Clean(SessionKey.SITE_HEADER)
    @RequestMapping(value = SiteForm.ACTION.CLONE, method = RequestMethod.POST)
    public String clone(@ModelAttribute(SessionKey.SITE) SiteForm form, Model model) throws Exception {

        logger.info("clone()=> BEGIN");

        clone(form, model, SiteForm.ACTION.CLONE);

        logger.info("clone()=> END.");

        return "site/edit";

    }

    private void clone(SiteForm form, Model model, String cloneCode) {

        String title = AppI18nUtil.getMessage("admin.global.text.cloned", new String[]{form.getSiteName()});

        form.setCloneId(String.valueOf(form.getSiteId()));
        form.setSiteId(null);
        form.setSiteName(title);
        form.setLocationBudgetRefNum(null);
        form.setLocationDistrRefNum(null);
        form.setCloneCode(cloneCode);

        model.addAttribute(SessionKey.SITE, form);
    }

    @Clean(SessionKey.SITE_HEADER)
    @RequestMapping(value = SiteForm.ACTION.CLONE_WITH_ASSOC, method = RequestMethod.POST)
    public String cloneWithAssoc(@ModelAttribute(SessionKey.SITE) SiteForm form, Model model) throws Exception {

        logger.info("cloneWithAssoc()=> BEGIN");

        clone(form, model, SiteForm.ACTION.CLONE_WITH_ASSOC);
        logger.info("cloneWithAssoc()=> END.");

        return "site/edit";

    }

    @SuccessMessage
    @Clean(SessionKey.SITE_HEADER)
    @RequestMapping(value = SiteForm.ACTION.DELETE, method = RequestMethod.POST)
    public String delete(WebRequest request, @ModelAttribute(SessionKey.SITE) SiteForm siteForm) throws Exception {

        logger.info("delete()=> BEGIN, siteForm: " + siteForm);

        if (!siteForm.getIsNew()) {

            try {

                siteService.deleteSite(getStoreId(), siteForm.getSiteId());

            } catch (DatabaseUpdateException e) {

                return handleDatabaseUpdateException(e, request);

            }

            WebFormUtil.removeObjectFromFilterResult(
                    request,
                    SessionKey.SITE_FILTER_RESULT,
                    siteForm.getSiteId()
            );

            logger.info("delete()=> END. redirect to filter");
            return redirect("../");

        }

        logger.info("delete()=> END.");
        return "emailTemplate/edit";

    }

    @Clean(SessionKey.SITE_HEADER)
    @RequestMapping(value = SiteForm.ACTION.CREATE)
    public String create(@ModelAttribute(SessionKey.SITE) SiteForm form, Model model) throws Exception {

        logger.info("create()=> BEGIN");

        form = new SiteForm();
        form.initialize();

        model.addAttribute(SessionKey.SITE, form);

        logger.info("create()=> END.");

        return "site/edit";

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.SITE) SiteForm form, @PathVariable(IdPathKey.LOCATION_ID) Long locationId, Model model) {

        logger.info("show()=> BEGIN");

        SiteIdentView site = siteService.findSiteToEdit(getStoreId(),
                locationId
        );

        logger.info("show()=> site: "+site);

        if (site   != null) {

            BusEntityData entity = site.getBusEntityData();
            ContactView contact = site.getContact();
            SiteIdentPropertiesView properties = PropertyBinder.bindSiteIdentProperties(new SiteIdentPropertiesView(),
                    entity.getBusEntityId(),
                    site.getProperties()
            );

            form.setSiteId(site.getBusEntityData().getBusEntityId());
            form.setSiteName(site.getBusEntityData().getShortDesc());
            form.setAccountId(String.valueOf(site.getAccountId()));
            form.setAccountName(site.getAccountName());

            form.setStatus(site.getBusEntityData().getBusEntityStatusCd());
            form.setEffDate(formatDate(getAppLocale(), site.getBusEntityData().getEffDate()));
            form.setExpDate(formatDate(getAppLocale(), site.getBusEntityData().getExpDate()));

            form.setLocationBudgetRefNum(PropertyUtil.toValueNN(properties.getSiteReferenceNumber()));
            form.setLocationDistrRefNum(PropertyUtil.toValueNN(properties.getDistrSiteRefNumber()));
            form.setProductBundle(PropertyUtil.toValueNN(properties.getProductBundle()));
            form.setTargetFicilityRank(PropertyUtil.toValueNN(properties.getTargetFicilityRank()));
            form.setLocationLineLevelCode(PropertyUtil.toValueNN(properties.getLocationLineLevelCode()));
            form.setLocationComments(PropertyUtil.toValueNN(properties.getLocationComments()));
            form.setLocationShipMsg(PropertyUtil.toValueNN(properties.getLocationShipMsg()));

            form.setContact(WebFormUtil.createContactForm(contact));
            form.setOptions(WebFormUtil.createSiteOptions(properties));
            form.setCorporateSchedule(WebFormUtil.createDeliveryScheduleForm(getAppLocale(), site.getCorporateScheduleCorporate()));
        }

        model.addAttribute(SessionKey.SITE, form);
        
        logger.info("show()=> END.");

        return "site/edit";

    }


    @ModelAttribute(SessionKey.SITE)
    public SiteForm initModel() {

        SiteForm form = new SiteForm();
        form.initialize();

        return form;

    }
    @ResponseBody
    @RequestMapping(value = SiteForm.ACTION.GET_QRCODE)
    public byte[] getQRCode( @PathVariable(IdPathKey.LOCATION_ID) Long locationId,
    						 @RequestParam("desc") String siteName) throws Exception {

        logger.info("getQRCode()=> BEGIN storeId = " + getStoreId() + ", locationId = " + locationId+ ", siteName=" + siteName);

        byte[] stream = ((QRCode.from("https://LO/" + getStoreId() +"/"+ locationId + "/" + siteName).to(ImageType.GIF).withSize(175, 175).stream()).toByteArray() );
 
        logger.info("getQRCode()=> END. stream.length = " + (stream!= null ? stream.length : 0));

        return stream;

    }


}
