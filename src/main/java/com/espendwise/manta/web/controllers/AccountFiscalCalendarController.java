package com.espendwise.manta.web.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.espendwise.manta.i18n.I18nUtil;
import com.espendwise.manta.model.data.BudgetDetailData;
import com.espendwise.manta.model.data.FiscalCalenderData;
import com.espendwise.manta.model.view.BudgetView;
import com.espendwise.manta.model.view.FiscalCalendarIdentView;
import com.espendwise.manta.model.view.FiscalCalendarListView;
import com.espendwise.manta.model.view.FiscalCalendarPhysicalView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.spi.SuccessMessage;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.FiscalCalendarUtility;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.parser.AppParserException;
import com.espendwise.manta.util.parser.AppParserFactory;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.validation.ValidationException;
import com.espendwise.manta.web.forms.AccountFiscalCalendarForm;
import com.espendwise.manta.web.forms.FiscalCalendarForm;
import com.espendwise.manta.web.resolver.FiscalCalendarWEbUpdateExceptionResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.IdPathKey;
import com.espendwise.manta.web.util.SessionKey;
import com.espendwise.manta.web.util.WebAction;
import com.espendwise.manta.web.util.WebErrors;
import com.espendwise.manta.web.util.WebFormUtil;
import com.espendwise.manta.web.validator.CreateNewFiscalCalendarValidator;
import com.espendwise.manta.web.validator.FiscalCalendarFormValidator;

@Controller
@RequestMapping(UrlPathKey.ACCOUNT.FISCAL_CALENDAR)
public class AccountFiscalCalendarController extends BaseController {

    private static final Logger logger = Logger.getLogger(AccountFiscalCalendarController.class);

    private AccountService accountService;

    @Autowired
    public AccountFiscalCalendarController(AccountService accountService) {
        this.accountService = accountService;
    }

    public String handleValidationException(ValidationException ex, WebRequest request) {

        WebErrors webErrors = new WebErrors(request);
        webErrors.putErrors(ex, new FiscalCalendarWEbUpdateExceptionResolver());

        return "account/fiscalCalendar";
    }


    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String initShow(HttpServletRequest request, @ModelAttribute(SessionKey.ACCOUNT_FISCAL_CALENDAR) AccountFiscalCalendarForm form, @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId) {
        return "account/fiscalCalendar";
    }

    @RequestMapping(value = "/{fiscalCalendarId}", method = RequestMethod.GET)
    public String show(HttpServletRequest request,
                       @ModelAttribute(SessionKey.ACCOUNT_FISCAL_CALENDAR) AccountFiscalCalendarForm form,
                       @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                       @PathVariable(IdPathKey.FISCAL_CALENDAR_ID) Long fiscalCalendarId) {

        logger.info("show()=> BEGIN" +
                ", accountId: " + accountId +
                ", fiscalCalendarId: " + fiscalCalendarId);

        if (fiscalCalendarId != null) {
            FiscalCalendarIdentView calendarToEdit = accountService.findCalendarIdent(accountId, fiscalCalendarId);
            form.setCalendarToEdit(WebFormUtil.createFiscalCalendarForm(getAppLocale(), calendarToEdit));
        }

        logger.info("show()=> END");

        return "account/fiscalCalendar";

    }

    @SuccessMessage
    @RequestMapping(value = "/{fiscalCalendarId}/save", method = RequestMethod.POST)
    public String save(WebRequest request,
                       @ModelAttribute(SessionKey.ACCOUNT_FISCAL_CALENDAR) AccountFiscalCalendarForm form,
                       @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                       @PathVariable(IdPathKey.FISCAL_CALENDAR_ID) Long fiscalCalendarId) {

        logger.info("save()=> BEGIN" +
                ", accountId: " + accountId +
                ", fiscalCalendarId: " + fiscalCalendarId);


        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form, new FiscalCalendarFormValidator());
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            logger.info("save()=> ERRORS: " + webErrors);
            return "account/fiscalCalendar";
        }

        FiscalCalendarIdentView calendar = new FiscalCalendarIdentView();
        if (!form.isNew()) {
            calendar = accountService.findCalendarIdent(accountId, fiscalCalendarId);
        }

        calendar.setFiscalCalendarData(WebFormUtil.createCalendarData(getAppLocale(), calendar.getFiscalCalendarData(), form));
        calendar.setPeriods(WebFormUtil.createFiscalCalendarPeriods(getAppLocale(), calendar.getFiscalCalendarData(), calendar.getPeriods(), form));
        calendar.setServiceScheduleCalendar(form.getCalendarToEdit().isServiceScheduleCalendar());

        try {

            calendar = accountService.saveFiscalCalendar(calendar);

        } catch (ValidationException e) {

            return handleValidationException(e, request);

        }

        logger.info("save()=> END, redirect to " + calendar.getFiscalCalendarData().getFiscalCalenderId());

        return redirect("../" + calendar.getFiscalCalendarData().getFiscalCalenderId());

    }
    
    @RequestMapping(value = "/{fiscalCalendarId}/setForAll", method = RequestMethod.POST)
    public String setForAll(WebRequest request,
                       @ModelAttribute(SessionKey.ACCOUNT_FISCAL_CALENDAR) AccountFiscalCalendarForm form,
                       @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId,
                       @PathVariable(IdPathKey.FISCAL_CALENDAR_ID) Long fiscalCalendarId) {

        logger.info("setForAll()=> BEGIN" +
                ", accountId: " + accountId +
                ", fiscalCalendarId: " + fiscalCalendarId);
        
        FiscalCalendarForm calendarToEdit = form.getCalendarToEdit();
        calendarToEdit.setFiscalYear(AppI18nUtil.getMessage("admin.account.fiscalCalendar.all"));
        
        String dateEdit = "";
        Integer year = 0;
        Date dateValue = null;
        GregorianCalendar grC;
            
        // set effective date
        if (form.getCalendars().isEmpty()) {
            dateEdit = Utility.ignorePattern(calendarToEdit.getEffDate(), AppI18nUtil.getDatePatternPrompt());
            if (Utility.isSet(dateEdit)) {
                try {
                    dateValue = AppParserFactory.getParser(Date.class).parse(dateEdit, AppI18nUtil.getDatePattern());
                } catch (AppParserException e) {}
            }
            if (dateValue != null) {
                year = FiscalCalendarUtility.getYearForDate(dateValue);
            } else {
                if (form.getCurrentFiscalYear() != null) {
                    year = form.getCurrentFiscalYear();
                } else {
                    //No FiscalCalender exist .. MANTA-415
                    year = Utility.getYearForDate(new Date());
                }
            }
        } else {
            Date oldestDate = form.getCalendars().get(0).getEffDate();
            for (FiscalCalendarListView lastCalendar : form.getCalendars()) {
                if (lastCalendar.getExpDate() != null) {
                    if (oldestDate.before(lastCalendar.getExpDate())) {
                        oldestDate = lastCalendar.getExpDate();
                    }
                } else {
                    if (oldestDate.before(lastCalendar.getEffDate())) {
                        oldestDate = lastCalendar.getEffDate();
                    }
                }
            }
            year = FiscalCalendarUtility.getYearForDate(oldestDate) + 1;
        }
        grC = new GregorianCalendar(year, Calendar.JANUARY, 1);
        calendarToEdit.setEffDate(AppI18nUtil.formatDate(getAppLocale(), grC.getTime()));

        // set expiration date
        dateValue = null;
        if (form.getCalendars().isEmpty()) {
            dateEdit = Utility.ignorePattern(calendarToEdit.getExpDate(), AppI18nUtil.getDatePatternPrompt());
            if (Utility.isSet(dateEdit)) {
                try {
                    dateValue = AppParserFactory.getParser(Date.class).parse(dateEdit, AppI18nUtil.getDatePattern());
                } catch (AppParserException e) {}
            }
        }
        if (dateValue != null) {
            year = FiscalCalendarUtility.getYearForDate(dateValue);
        } else {
            year = 2500;
        }
        grC = new GregorianCalendar(year, Calendar.DECEMBER, 31);
        calendarToEdit.setExpDate(AppI18nUtil.formatDate(getAppLocale(), grC.getTime()));

        //set periods
        boolean periodsEmpty = true;
        Iterator<Integer> it = calendarToEdit.getPeriods().keySet().iterator();
        String periodStartDate = "";
        while (it.hasNext()) {
            periodStartDate = calendarToEdit.getPeriods().get(it.next());
            if (Utility.isSet(periodStartDate)) {
                periodsEmpty = false;
                break;
            }
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(I18nUtil.getDayWithMonthPattern(getAppLocale()));
        grC = new GregorianCalendar(year, Calendar.JANUARY, 1);
        String period = sdf.format(grC.getTime());
        SortedMap<Integer, String> periods = calendarToEdit.getPeriods();
        periods.put(periods.firstKey(), period);
        if (periodsEmpty) {
            int inc = 365 / periods.size();
            int dayOfMonth = 0;     
            int m = (periods.size() / 13) + 2;
            int n = 31 / m;
            for (int i = periods.firstKey() + 1, j = inc; i <= periods.size(); i++, j += inc) {
                grC.set(Calendar.DAY_OF_YEAR, j);
                dayOfMonth = grC.get(Calendar.DAY_OF_MONTH);
                int k;
                for (k = 1; k < m; k++) {
                    if (dayOfMonth <= k * n) {
                        if (k > 1) {
                            grC.set(Calendar.DAY_OF_MONTH, k*n - n/2);
                        } else {
                            grC.set(Calendar.DAY_OF_MONTH, 1);
                        }
                        break;
                    }
                }
                if (k == m) {
                    grC.add(Calendar.MONTH, 1);
                    grC.set(Calendar.DAY_OF_MONTH, 1);
                }
                periods.put(i, sdf.format(grC.getTime()));
            }
        }

        logger.info("setForAll()=> END");

        return "account/fiscalCalendar";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(HttpServletRequest request, @ModelAttribute(SessionKey.ACCOUNT_FISCAL_CALENDAR) AccountFiscalCalendarForm form) {

        WebErrors webErrors = new WebErrors(request);

        List<? extends ArgumentedMessage> validationErrors = WebAction.validate(form, new CreateNewFiscalCalendarValidator());
        if (!validationErrors.isEmpty()) {
            webErrors.putErrors(validationErrors);
            return "account/fiscalCalendar";
        }


        form.setCalendarToEdit(new FiscalCalendarForm());
        form.getCalendarToEdit().setPeriods(new TreeMap<Integer, String>());
        form.getCalendarToEdit().setEditable(true);
        form.getCalendarToEdit().setServiceScheduleCalendar(form.getIsServiceScheduleCalendar());

        for (int i = 0; i < Parse.parseInt(form.getNumberOfBudgetPeriods()); i++) {
            form.getCalendarToEdit().getPeriods().put(i + 1, Constants.EMPTY);
        }

        return "account/fiscalCalendar";

    }

    @SuccessMessage
    @RequestMapping(value = "/copyBudget", method = RequestMethod.POST)
    public String copyBudgetForward(HttpServletRequest request,
                                    @ModelAttribute(SessionKey.ACCOUNT_FISCAL_CALENDAR) AccountFiscalCalendarForm form,
                                    @PathVariable(IdPathKey.ACCOUNT_ID) Long accountId) {
        //Copies the current fiscal year budget to next year for all sites
        WebErrors webErrors = new WebErrors(request);
        GregorianCalendar cal;
        //Get Current fiscal year
        FiscalCalenderData currentCalendar = accountService.findCurrentFiscalCalendar(accountId);

        Integer currentFiscalYear = 0;
        if (currentCalendar != null) {
            currentFiscalYear = currentCalendar.getFiscalYear();
        }
        if (currentFiscalYear == 0) {
            cal = new GregorianCalendar();
            cal.setTime(new Date());
            currentFiscalYear = cal.get(Calendar.YEAR);
        }	    
        Integer nextFiscalYear = currentFiscalYear + 1;
        Integer fiscalCalendarPeriods = 0;
	    
        //Get Fiscal Calendar for next fiscal year
        cal = new GregorianCalendar(nextFiscalYear, 11, 31);
        Date nextFiscalYearDate = cal.getTime();
        
        FiscalCalendarPhysicalView nextFCalView = accountService.findCalendarForDate(accountId, nextFiscalYearDate);
        if (nextFCalView == null) {
            webErrors.putError("admin.account.fiscalCalendar.error.fiscalCalendarForNextYearMissing", Args.typed(nextFiscalYear));
            return "account/fiscalCalendar";
        } else {
            if (Utility.isSet(nextFCalView.getPeriods())) {
                fiscalCalendarPeriods = nextFCalView.getPeriods().size();
            }
        }

        //Get account and site budgets for the next year
        List<BudgetView> nextYearBudgets = accountService.findAccountSiteBudgets(accountId, nextFiscalYear);

        if (Utility.isSet(nextYearBudgets)) {			
            boolean accountBudgetPresent = false;
            StringBuilder message = new StringBuilder();
            Set<Long> siteIds = new HashSet<Long>();
            for (BudgetView budget : nextYearBudgets) {
                if (RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET.equals(budget.getBudgetData().getBudgetTypeCd())) {
                    siteIds.add(budget.getBudgetData().getBusEntityId());
                } else if (!accountBudgetPresent && RefCodeNames.BUDGET_TYPE_CD.ACCOUNT_BUDGET.equals(budget.getBudgetData().getBudgetTypeCd())) {
                    accountBudgetPresent = true;
                }
            }

            if (accountBudgetPresent) {
                message.append(AppI18nUtil.getMessage("admin.account.fiscalCalendar.message.account"));
                message.append(": ");
                message.append(accountId);
            }
            
            if (!siteIds.isEmpty()) {
                Iterator<Long> it = siteIds.iterator();
                message.append(" and ");
                message.append(AppI18nUtil.getMessage("admin.account.fiscalCalendar.message.sites"));
                message.append(": ");
                int i = 0;
                while (true) {
                    message.append(it.next());
                    if (it.hasNext()) {
                        if (i <= 9) {
                            message.append(", ");
                        } else {
                            message.append("...");
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
            }
            webErrors.putError("admin.account.fiscalCalendar.error.budgetsForNextYearExist",
                               new NumberArgument(currentFiscalYear),
                               new NumberArgument(nextFiscalYear),
                               new StringArgument(message.toString()));
        } else { 
            //Get account and site budgets for the current year
            List<BudgetView> currentYearBudgets = accountService.findAccountSiteBudgets(accountId, currentFiscalYear);
            
            if (Utility.isSet(currentYearBudgets)) {
                for (BudgetView budget : currentYearBudgets) {
                    budget.getBudgetData().setBudgetId(null); 
                    budget.getBudgetData().setBudgetYear(nextFiscalYear);

                    //Set budgets equals to number of fiscal calendar
                    for (BudgetDetailData budgetDetail : budget.getBudgetDetails()) {
                        budgetDetail.setBudgetDetailId(null);
                        if (fiscalCalendarPeriods < budgetDetail.getPeriod() &&
                            budgetDetail.getAmount() != null) {
                            budgetDetail.setAmount(null);
                        }
                    }
                }
                accountService.updateAccountSiteBudgets(currentYearBudgets);
            }
        }

        return "account/fiscalCalendar";
    }

    @RequestMapping(value = "/{fiscalCalendarId}/clone", method = RequestMethod.POST)
    public String clone(HttpServletRequest request, @ModelAttribute(SessionKey.ACCOUNT_FISCAL_CALENDAR) AccountFiscalCalendarForm form) {

        form.getCalendarToEdit().setFiscalCalendarId(null);
        form.getCalendarToEdit().setFiscalYear(null);
        form.getCalendarToEdit().setExpDate(null);
        form.getCalendarToEdit().setEffDate(null);
        form.getCalendarToEdit().setEditable(true);
        form.getCalendarToEdit().setServiceScheduleCalendar(form.getIsServiceScheduleCalendar());

        if (form.getIsServiceScheduleCalendar()) {
            form.getCalendarToEdit().getPeriods().remove(form.getCalendarToEdit().getPeriods().lastKey());
        }

        return "account/fiscalCalendar";
    }

    @ModelAttribute(SessionKey.ACCOUNT_FISCAL_CALENDAR)
    public AccountFiscalCalendarForm initModel(@PathVariable(IdPathKey.ACCOUNT_ID) Long accountId) {

        AccountFiscalCalendarForm form = new AccountFiscalCalendarForm(accountId);
        if (!form.isInitialized()) {

            List<FiscalCalendarListView> calendars = accountService.findFiscalCalendars(accountId);
            boolean isServiceScheduleCalendar = accountService.isServiceScheduleCalendar(accountId);
            FiscalCalenderData currentCalendar = accountService.findCurrentFiscalCalendar(accountId);

            form.setServiceScheduleCalendar(isServiceScheduleCalendar);
            form.setCalendars(calendars);
            Boolean isBudget = false;
            if (currentCalendar != null) {
                int fiscalYear = currentCalendar.getFiscalYear();
                if (fiscalYear <= 0) {
                    fiscalYear = Utility.getYearForDate(new Date());
                }
                form.setCurrentFiscalYear(fiscalYear);
                isBudget = accountService.isBudgetExistsForAccount(accountId, currentCalendar.getFiscalYear());
            }
            form.setShowCopyBudget(isBudget);
            form.initialize();

        }

        return form;

    }

}