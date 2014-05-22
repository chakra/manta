package com.espendwise.manta.web.controllers;

import com.espendwise.manta.auth.AppUser;
import com.espendwise.manta.auth.ctx.AppCtx;
import com.espendwise.manta.auth.ctx.AppStoreContext;
import com.espendwise.manta.model.data.BusEntityData;
import com.espendwise.manta.model.view.SiteHierarchyTotalReportView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.SiteHierarchyService;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.util.AppComparator;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.UpdateRequest;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.AccountSiteHierarchyForm;
import com.espendwise.manta.web.forms.SiteHierarchyLevelForm;
import com.espendwise.manta.web.forms.SiteHierarchyTotalReportForm;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.validator.SiteHierarchyWebUpdateExceptionResolver;
import com.espendwise.ocean.common.webaccess.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.SITE_HIERARCHY)
public class AccountSiteHierarchyController extends SiteHierarchySupportController  {

    private static final Logger logger = Logger.getLogger(AccountSiteHierarchyController.class);
    private static final String ORCA_GENERATE_SITE_HIERARCHY_REPORT_PATH_TEMPLATE =  "/instance/%s/%s/account/%s/generateSiteHierarchyReport";

    @Autowired
    public AccountSiteHierarchyController(AccountService accountService, SiteHierarchyService siteHierarchyService, SiteService siteService) {
        super(accountService, siteHierarchyService, siteService);
    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(@ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY) AccountSiteHierarchyForm form, @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId) {

        logger.info("show()=> BEGIN, AccountID: " + accountId);

        BusEntityData account = findAccount(accountId);

        List<BusEntityData> siteHierLevels = siteHierarchyService.findSiteHierarchyLevelsOfAccount(account.getBusEntityId());

        logger.info("show()=> database sh levels: " + siteHierLevels);

        List<SiteHierarchyLevelForm> levels = SiteHierarchyLevelFormBinder.obj2Form(0,
                siteHierLevels,
                Utility.emptyList(SiteHierarchyLevelForm.class)
        );

        WebSort.sort(levels, SiteHierarchyLevelForm.LONG_DESC);
        WebSort.sort(siteHierLevels, BusEntityData.LONG_DESC);

        List<SiteHierarchyTotalReportView> hierReport = siteHierarchyService.findSiteHierarchyTotalReport(account.getBusEntityId(), siteHierLevels.size());
        Collections.sort(hierReport, AppComparator.SITE_HIERARCHY_COMPARE);

        SiteHierarchyTotalReportForm totalReportMap = new SiteHierarchyTotalReportForm();
        if (hierReport != null && !hierReport.isEmpty() ){
            for (SiteHierarchyTotalReportView totalReport : hierReport) {
                String key = totalReport.getSiteHierarchyLevel1Name();
                List<SiteHierarchyTotalReportView> items = totalReportMap.containsKey(key) ? totalReportMap.get(key) : new ArrayList<SiteHierarchyTotalReportView>();
                items.add(totalReport);
                totalReportMap.put(key, items);
            }
        }
        
        form.setAccountId(accountId);
        form.setLevelCollection(levels);
        form.setLevels(Utility.toIdNamePairs(siteHierLevels));
        form.setHierarchyReport(totalReportMap);
        form.setAllLevelsEmpty(checkAllLevelsEmpty(levels));
        
        logger.info("show()=> form has been populated, form " + form);

        logger.info("show()=> END.");

        return "account/locationHierarchy";

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String saveHierarchy(WebRequest request, @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY) AccountSiteHierarchyForm form) throws Exception {

        logger.info("saveHierarchy()=> BEGIN, form: " + form);

        BusEntityData account = findAccount(form.getAccountId());

        WebErrors errors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {
            errors.putErrors(validationErrors);
            return "account/locationHierarchy";
        }

        List<BusEntityData> dbSiteHierLevels = siteHierarchyService.findSiteHierarchyLevelsOfAccount(account.getBusEntityId());

        UpdateRequest<BusEntityData> updateRequest = SiteHierarchyLevelFormBinder.createUpdateObject(
                true,
                form.getLevelCollection(),
                dbSiteHierLevels
        );

        logger.info("saveHierarchy()=> updateRequest: " + updateRequest);


        try {

            siteHierarchyService.configureSiteHierarchy(
                    getStoreId(),
                    account.getBusEntityId(),
                    SiteHierarchyLevelFormBinder.findEndSiteHierarchyLevelId(form.getLevelCollection()),
                    null,
                    RefCodeNames.BUS_ENTITY_ASSOC_CD.SITE_HIERARCHY_LEVEL_OF_ACCOUNT,
                    updateRequest
            );

            if (!form.isAllLevelsEmpty() || !checkAllLevelsEmpty(form.getLevelCollection())) {
                WebAction.success(request, new SuccessActionMessage(), WebRequest.SCOPE_SESSION);
            }

        } catch (ValidationException e) {

            return handleValidationException(request, e);

        }

        return redirect(UrlPathAssistent.createPath(request, UrlPathKey.ACCOUNT.SITE_HIERARCHY));

    }


    @RequestMapping(value = "/report")
    public String report(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY) AccountSiteHierarchyForm form) throws Exception {

        logger.info("report()=> BEGIN");

        WebErrors errors = new WebErrors(request);

        try {

            LoginCredential orcaLoginCredential = getAppUser().getContext(AppCtx.ORCA_INSTANCE).getLoginCredential();
            if (orcaLoginCredential == null) {
                getUserManager().logonToOrca(getAuthUser());
            }

            byte[] values = generateSiteHierarchyReport(form.getAccountId());

            if (values != null) {

                response.setContentType("application/x-excel");
                response.setHeader("Content-disposition", "attachment; filename=Location-Hierarchy-Report.xls");
                response.getOutputStream().write(values);
                response.flushBuffer();

            }

        } catch (WebAccessException e) {

            handleWebAccessException(e, errors);

        } catch (WebAccessResponseException e) {

            handleWebAccessResponseException(e, errors);
        }


        logger.info("report()=> END.");

        return "account/locationHierarchy";

    }

    private WebErrors handleWebAccessException(WebAccessException e, WebErrors errors) {

        if (e != null) {


            if (e.getStatus() == BasicResponseValue.STATUS.ACCESS_DENIED) {

                errors.putError("admin.account.siteHierarchy.error.downloadReport.accessDenied");

            } else if (Utility.isSet(e.getErrors())) {

                for (ResponseError err : e.getErrors()) {
                    errors.putError(new WebError(err.getMessage(), null, err.getMessage()));
                }

            } else {

                errors.putError(new WebError(e.getMessage(), null, e.getMessage()));

            }

            return errors;
        }

        return null;
    }

    public byte[] generateSiteHierarchyReport(Long accountId) throws Exception {

        logger.info("generateSiteHierarchyReport()=> BEGIN");

        byte[] value = null;

        AppUser user = getAppUser();

        LoginCredential loginCredential = user.getContext(AppCtx.ORCA_INSTANCE).getLoginCredential();
        logger.info("generateSiteHierarchyReport()=> loginCredential: " + loginCredential);


        if (loginCredential != null) {

            AppStoreContext storeContext = user.getContext(AppCtx.STORE);

            String dsUnit = storeContext.getDataSource().getDataSourceIdent().getDataSourceName();
            Long storeId = storeContext.getStoreId();

            String requestPath = String.format(ORCA_GENERATE_SITE_HIERARCHY_REPORT_PATH_TEMPLATE,
                    dsUnit,
                    storeId.toString(),
                    String.valueOf(accountId)
            );

            logger.info("generateSiteHierarchyReport()=> hostAddress:  " + loginCredential.getHostAddress());
            logger.info("generateSiteHierarchyReport()=> requestPath:  " + requestPath);
            logger.info("generateSiteHierarchyReport()=> accessToken:  " + loginCredential.getAccessToken());

            RestRequest request = new RestRequest(loginCredential.getHostAddress(), requestPath);

            try {

                BasicRequestValue requestObject = new BasicRequestValue();

                requestObject.setLoginCredential(loginCredential);
                value = request.doPut(requestObject, new ObjectTokenType<BasicResponseValue<byte[]>>() {
                });


            } catch (WebAccessException e) {

                logger.info("generateSiteHierarchyReport()=> error:  " + e.getMessage());

                if (e.getStatus() == BasicResponseValue.STATUS.ACCESS_TOKEN_EXPIRED) {

                    logger.info("generateSiteHierarchyReport()=> relogin ... ");

                    getUserManager().logonToOrca(getAuthUser());

                    loginCredential = user.getContext(AppCtx.ORCA_INSTANCE).getLoginCredential();

                    if (loginCredential != null) {

                        logger.info("generateSiteHierarchyReport()=> relogin, OK!");

                        BasicRequestValue requestObject = new BasicRequestValue();
                        requestObject.setLoginCredential(loginCredential);
                        request = new RestRequest(loginCredential.getHostAddress(), requestPath);

                        value = request.doPut(requestObject, new ObjectTokenType<BasicResponseValue<byte[]>>() { });

                    }

                } else {

                    throw e;
                }
            }

        }

        logger.info("generateSiteHierarchyReport()=> END.");


        return value;
    }

    private WebErrors handleWebAccessResponseException(WebAccessResponseException e, WebErrors errors) {
        errors.putError("admin.account.siteHierarchy.error.downloadReport,reportNotAvailable");
        return errors;
    }


    private String handleValidationException(WebRequest request, ValidationException ex) {
        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new SiteHierarchyWebUpdateExceptionResolver());
        return "account/locationHierarchy";
    }


    @ModelAttribute(SessionKey.ACCOUNT_SITE_HIERARCHY)
    public AccountSiteHierarchyForm init(@PathVariable(IdPathKey.ACCOUNT_ID) Long accountId) {
        return new AccountSiteHierarchyForm();
    }
    
    private boolean checkAllLevelsEmpty(List<SiteHierarchyLevelForm> levels) {
        boolean levelIsSet = false;
        for (SiteHierarchyLevelForm level : levels) {
            if (Utility.isSet(level.getName())) {
                levelIsSet = true;
                break;
            }
        }
        return !levelIsSet;
    }

}

