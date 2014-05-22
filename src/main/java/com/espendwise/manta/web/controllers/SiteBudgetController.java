package com.espendwise.manta.web.controllers;

import com.espendwise.manta.model.view.*;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.service.IllegalDataStateException;
import com.espendwise.manta.service.SiteService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.BudgetUtil;
import com.espendwise.manta.util.FiscalCalendarUtility;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.CostCenterBudgetDisplayForm;
import com.espendwise.manta.web.forms.SiteBudgetForm;
import com.espendwise.manta.web.util.*;
import com.espendwise.manta.web.validator.MessageValidationResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Controller
@RequestMapping(UrlPathKey.SITE.BUDGETS)
public class SiteBudgetController extends BaseController {

    private static final Logger logger = Logger.getLogger(SiteBudgetController.class);

    private SiteService siteService;
    private AccountService accountService;

    @Autowired
    public SiteBudgetController(SiteService siteService, AccountService accountService) {
        this.siteService = siteService;
        this.accountService = accountService;
    }

    @Override
    public void initBinder(DataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(String.class,
                new PatternEditor(
                        AppI18nUtil.getMessage("admin.site.budgets.pattern.unlimited"),
                        true
                ));
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {
        WebErrors webErrors = new WebErrors(request);
        return "site/budgets";
    }


    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String show(WebRequest request, @ModelAttribute(SessionKey.SITE_BUDGET) SiteBudgetForm form, @PathVariable(IdPathKey.LOCATION_ID) Long locationId) throws Exception {

        SiteHeaderView site = siteService.findSiteHeader(getStoreId(), locationId);


        FiscalCalendarPhysicalView currentFiscalCalendar = accountService.findCalendarForDate(
                site.getAccountId(),
                new Date()
        );

        return redirect(
                UrlPathAssistent.createPath(request,
                        UrlPathKey.SITE.BUDGETS) + "/" + (currentFiscalCalendar == null ? 0 : currentFiscalCalendar.getPhysicalFiscalYear())
        );


    }

    @RequestMapping(value = "/{year}", method = {RequestMethod.GET})
    public String show(WebRequest request,
                       @ModelAttribute(SessionKey.SITE_BUDGET) SiteBudgetForm form,
                       @PathVariable(IdPathKey.LOCATION_ID) Long locationId,
                       @PathVariable(IdPathKey.YEAR) Integer year) throws Exception {

        logger.info("show()=> BEGIN, year: " + year);

        WebErrors errors = new WebErrors(request);

        Date time = new Date();

        SiteHeaderView site = siteService.findSiteHeader(getStoreId(), locationId);

        if (site != null) {

            FiscalCalendarPhysicalView currentFiscalCalendar = accountService.findCalendarForDate(
                    site.getAccountId(),
                    time
            );

            List<FiscalCalendarListView> accountCalendars = accountService.findFiscalCalendars(site.getAccountId());

            logger.info("show()=> currentFiscalCalendar: " + currentFiscalCalendar);


            form.setYear(year);
            form.setFiscalYears(
                    SiteBudgetFormUtil.createDisplayFiscalYears(
                            accountCalendars,
                            currentFiscalCalendar,
                            time
                    )
            );


            errors.putErrors(
                    provideBudgetsForYear(
                            site,
                            accountCalendars,
                            form,
                            year,
                            time,
                            currentFiscalCalendar
                    ).get()
            );

        }


        logger.info("show()=> END.");

        return "site/budgets";

    }

    private WebErrors provideBudgetsForYear(SiteHeaderView site, List<FiscalCalendarListView> calendars, SiteBudgetForm form, Integer year, Date time, FiscalCalendarPhysicalView currentFiscalCalendar) throws IllegalDataStateException {

        WebErrors errors = new WebErrors();

        if (year > 0) {

            SortedMap<Integer, SiteBudgetYearView> budgets = siteService.findBudgetForSiteByYear(site,
                    calendars,
                    Utility.toList(year, year - 1),
                    time
            );


            logger.info("show()=> budgets: " + budgets.keySet() + ", budgets.size: " + budgets.size());

            if (year > 0 && !budgets.keySet().isEmpty()) {

                if (!budgets.keySet().contains(year)) {
                    errors.putError("exception.web.error.fiscalCalendarNotFoundForYear", Args.i18nTyped(year));
                    return errors;
                }

                SiteBudgetYearView budget = budgets.get(year);
                SiteBudgetYearView previousBudget = budgets.get(year - 1);

                if (budget != null) {

                    if (budget.getFiscalCalendar() != null) {

                        Integer currentPeriod = null;
                        Map<Long, BudgetSiteSpendView> costCentersSpent = null;

                        if (currentFiscalCalendar != null && currentFiscalCalendar.getPhysicalFiscalYear() == year) {

                            currentPeriod = FiscalCalendarUtility.getPeriodOrNull(currentFiscalCalendar, time);

                            costCentersSpent = siteService.calculateCostCentersBudgetSpentsForSite(
                                    budget,
                                    currentPeriod
                            );
                        }


                        boolean isUsedSiteBudgetThreshold = siteService.isUsedSiteBudgetThreshold(getStoreId(),
                                site.getAccountId(),
                                site.getSiteId()
                        );


                        form.setFiscalYearPeriods(FiscalCalendarUtility.signedPeriodsIndexSet(budget.getFiscalCalendar()));
                        form.setYear(year);
                        form.setCurrentPeriod(String.valueOf(currentPeriod));
                        form.setUsedSiteBudgetThreshold(isUsedSiteBudgetThreshold);
                        form.setEnableCopyBudgets(previousBudget!= null && BudgetUtil.hasSiteBudget(previousBudget));
                        form.setCostCenterBudget(
                                SiteBudgetFormUtil.createCostCenterBudgetDisplayForm(
                                        budget,
                                        currentFiscalCalendar,
                                        costCentersSpent,
                                        time,
                                        false
                                )
                        );

                    }

                    logger.info("show()=> budget: " + budget);

                }

            }
        }

        return errors;
    }

    @SuccessMessage
    @RequestMapping(value = "/{fiscalYear}", method = {RequestMethod.POST})
    public String update(WebRequest request, @ModelAttribute(SessionKey.SITE_BUDGET) SiteBudgetForm form,
                         @PathVariable("fiscalYear") Integer fiscalYear,
                         @PathVariable(IdPathKey.LOCATION_ID) Long locationId) throws Exception {


        logger.info("update()=> BEGIN, action: " + form.getSelectedActionValue());

        WebErrors errors = new WebErrors(request);

        try {

            if (SiteBudgetForm.ACTION.COPY_LAST_YEAR.equals(form.getSelectedActionValue())) {

                errors = copyLastYear(request,
                        form,
                        form.getYear(),
                        locationId
                );

            } else if (SiteBudgetForm.ACTION.SET_UNLIMITED.equals(form.getSelectedActionValue())) {

                if (form.isEditable()) {

                    errors = setUnlimited(request,
                            form,
                            form.getYear(),
                            locationId
                    );

                }

            } else if (SiteBudgetForm.ACTION.SAVE_CHANGES.equals(form.getSelectedActionValue())) {

                if (form.isEditable()) {

                    errors = saveChanges(request,
                            form,
                            form.getYear(),
                            locationId
                    );

                }

            } else {

                errors.add(new WebError("validation.web.error.noActionSelected"));

            }

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }

        boolean withErrors = errors != null && !errors.isEmpty();

        logger.info("update()=> END, " + (withErrors ? "Errors Detected" : "OK!"));

        return errors != null && !errors.isEmpty() ? "site/budgets" : redirectToYear(request, fiscalYear);

    }

    private WebErrors saveChanges(WebRequest request, SiteBudgetForm form, Integer fiscalYear, Long locationId) throws Exception {

        logger.info("saveChanges()=> BEGIN" +
                ", locationId: " + locationId +
                ", fiscalYear: " + fiscalYear
        );

        WebErrors errors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form);
        if (!validationErrors.isEmpty()) {
            errors.putErrors(validationErrors);
            return errors;
        }


        List<BudgetView> budgetIdents = siteService.findSiteBudgets(locationId,
                SiteBudgetFormUtil.retreiveSiteBudgetIds(form)
        );

        List<BudgetView> budgets = SiteBudgetFormUtil.createSiteBudgets(getAppLocale(),
                locationId,
                form,
                budgetIdents
        );

        siteService.updateSiteBudgets(getStoreId(),
                locationId,
                budgets
        );


        logger.info("saveChanges()=> END");

        return errors;

    }

    private WebErrors setUnlimited(WebRequest request, SiteBudgetForm form, Integer fiscalYear, Long locationId) throws Exception {

        logger.info("setUnlimited()=> BEGIN" +
                ", locationId: " + locationId +
                ", fiscalYear: " + fiscalYear
        );

        WebErrors webErrors = saveChanges(
                request,
                SiteBudgetFormUtil.eraseAllPeriodAmounts(form),
                fiscalYear,
                locationId
        );

        logger.info("setUnlimited()=> END.");

        return webErrors;
    }

    private WebErrors copyLastYear(WebRequest request, SiteBudgetForm form, Integer fiscalYear, Long locationId) throws Exception {

        logger.info("copyLastYear()=> BEGIN" +
                ", locationId: " + locationId +
                ", fiscalYear: " + fiscalYear
        );

        WebErrors webErrors = new WebErrors(request);

        if (form.getEnableCopyBudgets()) {

            SiteHeaderView site = siteService.findSiteHeader(getStoreId(), locationId);

            Date time = new Date();

            List<FiscalCalendarListView> accountCalendars = accountService.findFiscalCalendars(site.getAccountId());

            SortedMap<Integer, SiteBudgetYearView> budgets = siteService.findBudgetForSiteByYear(site,
                    accountCalendars,
                    Utility.toList(fiscalYear - 1),
                    new Date()
            );

            SiteBudgetYearView previousBudget = budgets.get(fiscalYear-1);


            if (previousBudget != null) {

                Map<Long, CostCenterBudgetDisplayForm> previousForm = SiteBudgetFormUtil.createCostCenterBudgetDisplayForm(
                        previousBudget,
                        null,
                        null,
                        time,
                        true
                );

                form.setCostCenterBudget(previousForm);

            }

            webErrors = saveChanges(
                    request,
                    form,
                    fiscalYear,
                    locationId
            );


        }

        logger.info("copyLastYear()=> END.");

        return webErrors;

    }


    @ModelAttribute(SessionKey.SITE_BUDGET)
    public SiteBudgetForm initModel() {

        SiteBudgetForm form = new SiteBudgetForm();
        form.initialize();

        return form;

    }

    private String redirectToYear(WebRequest request, Integer fiscalYear) {
        return redirect(
                UrlPathAssistent.urlPath(
                        UrlPathAssistent.createPath(
                                request,
                                UrlPathKey.SITE.BUDGETS),
                        String.valueOf(fiscalYear)
                )
        );
    }


}
