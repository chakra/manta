package com.espendwise.manta.web.util;


import com.espendwise.manta.model.data.BudgetData;
import com.espendwise.manta.model.data.BudgetDetailData;
import com.espendwise.manta.model.data.CostCenterData;
import com.espendwise.manta.model.data.FiscalCalenderDetailData;
import com.espendwise.manta.model.entity.BudgetEntity;
import com.espendwise.manta.model.view.*;
import com.espendwise.manta.util.BudgetUtil;
import com.espendwise.manta.util.FiscalCalendarUtility;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.format.AppI18nFormatter;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.wrapper.BudgetEntityWrapper;
import com.espendwise.manta.web.forms.*;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

public class SiteBudgetFormUtil {

    private static final Logger logger = Logger.getLogger(SiteBudgetFormUtil.class);


    public static Map<Long, CostCenterBudgetDisplayForm> createCostCenterBudgetDisplayForm(SiteBudgetYearView budget,
                                                                                           FiscalCalendarPhysicalView currentCalendar,
                                                                                           Map<Long, BudgetSiteSpendView> costCentersSpent,
                                                                                           Date date,
                                                                                           boolean alwaysNewly) {

        logger.info("createCostCenterBudgetDisplayForm()=> BEGIN");

        Map<Long, CostCenterBudgetDisplayForm> x = new HashMap<Long, CostCenterBudgetDisplayForm>();

        List<CostCenterBudgetView> costCenterBudgets = budget.getCostCenterBudgets();

        FiscalCalendarPhysicalView calendar = budget.getFiscalCalendar();

        Integer currentYear = currentCalendar == null ? null : currentCalendar.getPhysicalFiscalYear();
        Integer currentPeriod = currentCalendar == null ? null : FiscalCalendarUtility.getPeriodOrNull(currentCalendar, date);

        if (Utility.isSet(costCenterBudgets)) {

            for (CostCenterBudgetView costCenterBudget : costCenterBudgets) {

                CostCenterData costCenter = costCenterBudget.getCostCenter();

                if (costCenter != null) {

                    BudgetEntity siteBudget = costCenterBudget.getSiteBudget();
                    BudgetEntity accountBudget = costCenterBudget.getAccountBudget();
                    BudgetSiteSpendView spent = costCentersSpent != null ? costCentersSpent.get(costCenter.getCostCenterId()) : null;

                    CostCenterBudgetDisplayForm costCenterBudgetDisplayForm = new CostCenterBudgetDisplayForm();

                    costCenterBudgetDisplayForm.setCostCenterId(costCenter.getCostCenterId());
                    costCenterBudgetDisplayForm.setCostCenterName(costCenter.getShortDesc());
                    costCenterBudgetDisplayForm.setCostCenterType(costCenter.getCostCenterTypeCd());
                    costCenterBudgetDisplayForm.setBudgets(new HashMap<String, BudgetDisplayForm>());

                    if (siteBudget == null && accountBudget == null) {

                        BudgetDisplayForm budgetForm = new BudgetDisplayForm();
                        budgetForm.setBudgetType(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);
                        budgetForm.setPeriodAmount(createBudgetPeriodsForm(calendar, siteBudget));
                        costCenterBudgetDisplayForm.getBudgets().put(budgetForm.getBudgetType(), budgetForm);

                    }


                    if (siteBudget != null) {

                        BudgetDisplayForm budgetForm = new BudgetDisplayForm();

                        budgetForm.setBudgetId(siteBudget != null ? alwaysNewly ? null : siteBudget.getBudgetId() : null);
                        budgetForm.setBudgetType(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);
                        budgetForm.setThresholdPercent(siteBudget != null ? siteBudget.getBudget().getBudgetThreshold() : null);
                        budgetForm.setPeriodAmount(createBudgetPeriodsForm(calendar, siteBudget));
                        budgetForm.setBudgetTotal(siteBudget != null ? Utility.strNN(BudgetUtil.calculateBudgetTotal(siteBudget)) : null);

                        if (spent != null && currentYear != null && currentYear.intValue() == spent.getBudgetYear()) {

                            if (currentPeriod != null && currentPeriod.intValue() == spent.getBudgetPeriod()) {

                                BudgetSpendView v = BudgetUtil.findBudgetSpent(spent.getBudgetSpends(), siteBudget.getBudget().getBudgetId());

                                if (v != null) {

                                    CostCenterBudgetSpentForm costCenterBudgetSpentForm = new CostCenterBudgetSpentForm();
                                    costCenterBudgetSpentForm.setAllocated(v.getAmountAllocated());
                                    costCenterBudgetSpentForm.setPeriod(spent.getBudgetPeriod());
                                    costCenterBudgetSpentForm.setYear(spent.getBudgetYear());
                                    costCenterBudgetSpentForm.setAllocated(v.getAmountAllocated());
                                    costCenterBudgetSpentForm.setUnlimitedBudget(v.isUnlimitedBudget());
                                    costCenterBudgetSpentForm.setSpent(v.getAmountSpent());
                                    costCenterBudgetSpentForm.setRemaining(Utility.subtractAmtNullable(v.getAmountAllocated(), v.getAmountSpent()));

                                    budgetForm.setCostCenterBudgetSpentForm(costCenterBudgetSpentForm);

                                }
                            }

                        }

                      costCenterBudgetDisplayForm.getBudgets().put(budgetForm.getBudgetType(), budgetForm);

                    }

                    if (accountBudget != null) {

                        BudgetDisplayForm budgetForm = new BudgetDisplayForm();

                        budgetForm.setBudgetId(alwaysNewly ? null : accountBudget.getBudgetId());
                        budgetForm.setBudgetType(RefCodeNames.BUDGET_TYPE_CD.ACCOUNT_BUDGET);
                        budgetForm.setThresholdPercent(accountBudget.getBudget().getBudgetThreshold());
                        budgetForm.setPeriodAmount(createBudgetPeriodsForm(calendar, accountBudget));
                        budgetForm.setBudgetTotal(Utility.strNN(BudgetUtil.calculateBudgetTotal(accountBudget)));

                        if (spent != null && currentYear != null && currentYear == calendar.getPhysicalFiscalYear()) {

                            if (currentPeriod != null && currentPeriod.intValue() == spent.getBudgetPeriod()) {

                                BudgetSpendView v = BudgetUtil.findBudgetSpent(spent.getBudgetSpends(), accountBudget.getBudget().getBudgetId());

                                if (v != null) {

                                    CostCenterBudgetSpentForm costCenterBudgetSpentForm = new CostCenterBudgetSpentForm();

                                    costCenterBudgetSpentForm.setAllocated(v.getAmountAllocated());
                                    costCenterBudgetSpentForm.setPeriod(spent.getBudgetPeriod());
                                    costCenterBudgetSpentForm.setYear(spent.getBudgetYear());
                                    costCenterBudgetSpentForm.setAllocated(v.getAmountAllocated());
                                    costCenterBudgetSpentForm.setUnlimitedBudget(v.isUnlimitedBudget());
                                    costCenterBudgetSpentForm.setSpent(v.getAmountSpent());
                                    costCenterBudgetSpentForm.setRemaining(Utility.subtractAmtNullable(v.getAmountAllocated(), v.getAmountSpent()));

                                    budgetForm.setCostCenterBudgetSpentForm(costCenterBudgetSpentForm);

                                }

                            }
                        }

                        costCenterBudgetDisplayForm.getBudgets().put(budgetForm.getBudgetType(), budgetForm);

                    }

                    x.put(costCenter.getCostCenterId(), costCenterBudgetDisplayForm);

                }


            }
        }

        logger.info("createCostCenterBudgetDisplayForm()=> END");

        return x;
    }

    private static Map<Integer, BudgetPeriodAmountForm> createBudgetPeriodsForm(FiscalCalendarPhysicalView calendarPhysical, BudgetEntity budgetEntity) {

        Map<Integer, BudgetPeriodAmountForm> budgetAmounts = new HashMap<Integer, BudgetPeriodAmountForm>();

        BudgetEntityWrapper budget = new BudgetEntityWrapper(budgetEntity);

        for (FiscalCalenderDetailData period : calendarPhysical.getPeriods()) {

            BudgetPeriodAmountForm periodAmountForm = new BudgetPeriodAmountForm();

            BigDecimal amount = budget.getBudget() != null ? budget.getAmount(period.getPeriod()) : null;

            periodAmountForm.setPeriod(period.getPeriod());
            periodAmountForm.setMmdd(period.getMmdd());
            periodAmountForm.setInputForm(amount != null ? amount.toString() : null);

            budgetAmounts.put(period.getPeriod(), periodAmountForm);

        }

        return budgetAmounts;

    }

    public static SortedMap<Integer, Integer> createDisplayFiscalYears(List<FiscalCalendarListView> accountCalendars, FiscalCalendarPhysicalView currentFiscalCalendarPhysical, Date time) {

        logger.info("createDisplayFiscalYears()=> BEGIN");

        SortedSet<Integer> fiscalYears = new TreeSet<Integer>();


        int currentFiscalYear = currentFiscalCalendarPhysical == null
                ? FiscalCalendarUtility.getYearForDate(time)
                : currentFiscalCalendarPhysical.getPhysicalFiscalYear();


        logger.info("createDisplayFiscalYears()=> currentFiscalYear: " + currentFiscalYear);


        SortedMap<Date, FiscalCalendarListView> candidates = new TreeMap<Date, FiscalCalendarListView>();
        for (FiscalCalendarListView calendar : accountCalendars) {
            candidates.put(calendar.getEffDate(), calendar);
        }

        List<Date> indexes = new ArrayList<Date>(new TreeSet<Date>(candidates.keySet()));

        for (int i = 0; i < indexes.size(); i++) {

            FiscalCalendarListView calendar = candidates.get(indexes.get(i));
            FiscalCalendarListView nextCalendar = i + 1 < indexes.size() ? candidates.get(indexes.get(i + 1)) : null;

            int yearStart = FiscalCalendarUtility.getYearForDate(calendar.getEffDate());

            Integer yearEnd = nextCalendar == null
                    ? calendar.getExpDate() != null ? FiscalCalendarUtility.getYearForDate(calendar.getExpDate()) : null
                    : Integer.valueOf(FiscalCalendarUtility.getYearForDate(nextCalendar.getEffDate()));

            if (yearEnd == null) {
                yearEnd = FiscalCalendarUtility.getYearForDate(calendar.getEffDate());
                yearEnd += (Utility.intNN(calendar.getFiscalYear()) == 0 ? 1 : 0);
            }

            if (yearStart >= currentFiscalYear && currentFiscalYear <= yearEnd) {
                if (yearEnd >= yearStart) {
                    for (int y = yearEnd; yearStart <= y; y--) {
                        if (y >= currentFiscalYear) {
                            fiscalYears.add(y);
                        }
                    }
                }
            }

            if (nextCalendar == null) {
                break;
            }

        }

        if (currentFiscalCalendarPhysical != null && Utility.intNN(currentFiscalCalendarPhysical.getCalendatFiscalYear()) == 0) {
            fiscalYears.add(currentFiscalCalendarPhysical.getPhysicalFiscalYear());
            fiscalYears.add(currentFiscalCalendarPhysical.getPhysicalFiscalYear() + 1);
        }

        if (Utility.isSet(accountCalendars)) {

            if (fiscalYears.size() == 0 && currentFiscalCalendarPhysical!=null && currentFiscalYear > 0) {// multiple calendar exists and most recent calendar year is 0
                fiscalYears.add(currentFiscalYear);
                fiscalYears.add(currentFiscalYear + 1);
            }
        }


        logger.info("createDisplayFiscalYears()=> fiscalYears: " + fiscalYears);

        SortedMap<Integer, Integer> fiscalIndexYears = new TreeMap<Integer, Integer>();

        int i = 0;
        for (Integer k : fiscalYears) {
            fiscalIndexYears.put(i++, k);
        }

        logger.info("createDisplayFiscalYears()=> END,  " + fiscalIndexYears);

        return fiscalIndexYears;

    }

    public static List<BudgetView> createSiteBudgets(AppLocale appLocale, Long locationId, SiteBudgetForm form, List<BudgetView> budgetIdents) {

        logger.info("createSiteBudgets()=> BEGIN, locationId:  " + locationId);

        List<BudgetView> x = new ArrayList<BudgetView>();

        if (Utility.isSet(form.getCostCenterBudget())) {

            Map<Long, BudgetView> budgetIdentsMap = Utility.toMap(budgetIdents);

            for (CostCenterBudgetDisplayForm costCenterBudget : form.getCostCenterBudget().values()) {

                if (Utility.isSet(costCenterBudget.getBudgets())) {

                    BudgetDisplayForm budgetForm = costCenterBudget.getBudgets().get(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);

                    if (budgetForm != null) {

                        BudgetView budget = budgetIdentsMap.get(budgetForm.getBudgetId());

                        if (RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET.equals(costCenterBudget.getCostCenterType())) {

                            if (budget == null) {

                                budget = new BudgetView();

                                budget.setBudgetData(new BudgetData());
                                budget.getBudgetData().setBusEntityId(locationId);
                                budget.getBudgetData().setCostCenterId(costCenterBudget.getCostCenterId());
                                budget.getBudgetData().setBudgetYear(form.getYear());
                                budget.getBudgetData().setBudgetTypeCd(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);
                                budget.setBudgetDetails(Utility.emptyList(BudgetDetailData.class));


                            }

                            BudgetData budgetData = budget.getBudgetData();

                            String threshold = Utility.isSet(budgetForm.getThresholdPercent())
                                    ? !form.getUsedSiteBudgetThreshold()
                                    ? null
                                    : AppI18nFormatter.formatInteger(Parse.parsePercentInt(budgetForm.getThresholdPercent()), AppLocale.SYSTEM_LOCALE)
                                    : null;

                            budgetData.setBudgetThreshold(threshold);
                            budgetData.setBudgetStatusCd(RefCodeNames.BUDGET_STATUS_CD.ACTIVE);

                            List<BudgetDetailData> periods = new ArrayList<BudgetDetailData>();
                            Map<Integer, BudgetDetailData> budgetIdentPeriodMap = BudgetUtil.toMapByPeriod(budget.getBudgetDetails());
                            Map<Integer, BudgetPeriodAmountForm> periodAmount = budgetForm.getPeriodAmount();

                            for (BudgetPeriodAmountForm amount : periodAmount.values()) {

                                logger.info("createSiteBudgets()=> processing amount of budget " + budgetForm.getBudgetId()
                                        + ", \n      period: " + amount.getPeriod()
                                        + ", \n cost center: " + costCenterBudget.getCostCenterId()
                                        + ", \n      amount: " + amount.getInputForm()
                                );

                                BudgetDetailData budgetPeriod = budgetIdentPeriodMap.get(amount.getPeriod());
                                if (budgetPeriod == null) {
                                    budgetPeriod = new BudgetDetailData();
                                }
                                budgetPeriod.setAmount(AppI18nUtil.parseAmountNN(appLocale, amount.getInputForm()));
                                budgetPeriod.setPeriod(amount.getPeriod());

                                periods.add(budgetPeriod);

                            }

                            x.add(new BudgetView(budgetData, periods));

                        }


                    }

                }

            }

        }

        logger.info("createSiteBudgets()=> END.");

        return x;

    }


    public static List<Long> retreiveSiteBudgetIds(SiteBudgetForm form) {
        List<Long> budgetIds = new ArrayList<Long>();
        if (Utility.isSet(form.getCostCenterBudget())) {
            for (CostCenterBudgetDisplayForm costCenterBudget : form.getCostCenterBudget().values()) {
                if (Utility.isSet(costCenterBudget.getBudgets())) {
                    BudgetDisplayForm budgetForm = costCenterBudget.getBudgets().get(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);
                    if (budgetForm != null && Utility.isSet(budgetForm.getBudgetId())) {
                        budgetIds.add(budgetForm.getBudgetId());
                    }
                }
            }
        }

        return budgetIds;

    }

    public static SiteBudgetForm eraseAllPeriodAmounts(SiteBudgetForm form) {
        if (Utility.isSet(form.getCostCenterBudget())) {
            for (CostCenterBudgetDisplayForm costCenterBudget : form.getCostCenterBudget().values()) {
                if (Utility.isSet(costCenterBudget.getBudgets())) {
                    BudgetDisplayForm budgetForm = costCenterBudget.getBudgets().get(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);
                    if (budgetForm != null) {
                        if (Utility.isSet(budgetForm.getPeriodAmount())) {
                            for (BudgetPeriodAmountForm periodEntry : budgetForm.getPeriodAmount().values()) {
                                periodEntry.setInputForm(null);
                            }
                        }
                    }
                }
            }

        }
        return form;
    }

    public static boolean isUnlimited(Map<Long, CostCenterBudgetDisplayForm> costCenterBudgets) {

        boolean notunlimited = true;

        if (Utility.isSet(costCenterBudgets)) {
            for (CostCenterBudgetDisplayForm costCenterBudget : costCenterBudgets.values()) {
                if (Utility.isSet(costCenterBudget.getBudgets())) {
                    Collection<BudgetDisplayForm> budgets = costCenterBudget.getBudgets().values();
                    for (BudgetDisplayForm b : budgets) {
                        notunlimited = notunlimited && !(Utility.longNN(b.getBudgetId()) > 0 && Utility.isSet(b.getBudgetTotal() == null));
                    }
                }
            }
        }

        return !notunlimited;

    }

    public static BigDecimal getBudgetsTotal(Map<Long, CostCenterBudgetDisplayForm> costCenterBudgets) {

        BigDecimal total = null;

        if (Utility.isSet(costCenterBudgets)) {
            for (CostCenterBudgetDisplayForm costCenterBudget : costCenterBudgets.values()) {
                if (Utility.isSet(costCenterBudget.getBudgets())) {
                    Collection<BudgetDisplayForm> budgets = costCenterBudget.getBudgets().values();
                    for (BudgetDisplayForm b : budgets) {
                        total = Utility.addAmtNullable(total, Utility.isSet(b.getBudgetTotal()) ? new BigDecimal(b.getBudgetTotal()) : null);
                    }
                }
            }
        }

        return total;

    }

    public static List<Long> getSortedByNameCostCenterIds(Map<Long, CostCenterBudgetDisplayForm> costCenterBudgets) {
        List<Long> x = new ArrayList<Long>();
        if (Utility.isSet(costCenterBudgets)) {
            List<CostCenterBudgetDisplayForm> costCenters = new ArrayList<CostCenterBudgetDisplayForm>(costCenterBudgets.values());
            Collections.sort(costCenters, CostCenterBudgetDisplayForm.COMPARE_BY_NAME);
            for (CostCenterBudgetDisplayForm f : costCenters) {
                x.add(f.getCostCenterId());
            }
        }
        return x;
    }

    public static boolean isEditable(Map<Long, CostCenterBudgetDisplayForm> costCenterBudget) {

        if (Utility.isSet(costCenterBudget)) {

            for (Map.Entry<Long, CostCenterBudgetDisplayForm> costCenter : costCenterBudget.entrySet()) {

                if (RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET.equals(costCenter.getValue().getCostCenterType())) {
                    return true;
                }

                if (!Utility.isSet(costCenter.getValue().getBudgets())) {
                    if (RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET.equals(costCenter.getValue().getCostCenterType())) {
                        return true;
                    }
                } else {
                    if (Utility.isSet(costCenter.getValue().getBudgets().get(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET))
                            && RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET.equals(costCenter.getValue().getCostCenterType())) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
    
    public static String wrongValuesCommaString(List<Integer> numbers, List<String> values) {
        StringBuilder message = new StringBuilder();
        
        if (Utility.isSet(numbers) && Utility.isSet(values)) {
            int size = Math.min(numbers.size(), values.size());
            
            for (int i = 0; i < size; i++) {
                message.append(numbers.get(i));
                message.append(" (");
                message.append(values.get(i).trim());
                message.append("), ");
            }
            message.setLength(message.length() - 2);
        }
        
        return message.toString();
    }
}
