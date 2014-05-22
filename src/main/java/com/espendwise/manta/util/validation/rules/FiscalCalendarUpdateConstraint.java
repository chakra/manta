package com.espendwise.manta.util.validation.rules;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.espendwise.manta.dao.FiscalCalendarDAOImpl;
import com.espendwise.manta.model.view.FiscalCalendarIdentView;
import com.espendwise.manta.model.view.FiscalCalendarListView;
import com.espendwise.manta.model.view.FiscalReportIntersectResView;
import com.espendwise.manta.service.AccountService;
import com.espendwise.manta.support.spring.ServiceLocator;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.FiscalCalendarUtility;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.AppLocale;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.format.AppI18nFormatter;
import com.espendwise.manta.util.parser.Parse;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationRule;
import com.espendwise.manta.util.validation.ValidationRuleResult;

public class FiscalCalendarUpdateConstraint implements ValidationRule {

    private static final Logger logger = Logger.getLogger(FiscalCalendarUpdateConstraint.class);

    private FiscalCalendarIdentView calendar;
    private Calendar currentDate;
    private EntityManager entityManager;

    public FiscalCalendarUpdateConstraint(FiscalCalendarIdentView calendar, EntityManager entityManager) {
        this.calendar = calendar;
        this.currentDate = Calendar.getInstance();
        this.entityManager = entityManager;
    }

    @Override
    public ValidationRuleResult apply() {

        logger.info("apply()=> BEGIN");

        if (getCalendar() == null) {
            return null;
        }

        List<FiscalCalendarListView> accountCalendars = getAccountService().findFiscalCalendars(getCalendar().getFiscalCalendarData().getBusEntityId());

        ValidationRuleResult vrr = new ValidationRuleResult();

        checkDefaultCalendar(vrr, getCalendar(), accountCalendars);
        if (vrr.isFailed()) {
            return vrr;
        }

        checkCalendarCommonRule(vrr, getCalendar());
        if (vrr.isFailed()) {
            return vrr;
        }

        checkCalendarWithYear(vrr, getCalendar(), accountCalendars);
        if (vrr.isFailed()) {
            return vrr;
        }

        checkOnFiscalCalDetailsRange(vrr, getCalendar());
        if (vrr.isFailed()) {
            return vrr;
        }

        checkOnFiscalCalDataRange(vrr, getCalendar(), accountCalendars);
        if (vrr.isFailed()) {
            return vrr;
        }


        vrr.success();

        logger.info("apply()=> END.");

        return vrr;
    }

    private void checkCalendarCommonRule(ValidationRuleResult vrr, FiscalCalendarIdentView calendar) {

        logger.debug("checkCalendarCommonRule()=> BEGIN");

        Date effDate = calendar.getFiscalCalendarData().getEffDate();
        Date expDate = calendar.getFiscalCalendarData().getExpDate();

        logger.info("checkCalendarCommonRule()=> checkRule WRONG_DATE_INTERVAL(effDate.after(expDate))");

        if (expDate != null && effDate.compareTo(expDate) >= 0) {

            vrr.failed(
                    ExceptionReason.FiscalCalendarUpdateReason.WRONG_DATE_INTERVAL,
                    Args.typed(effDate, expDate)
            );

            logger.info("checkCalendarCommonRule()=> checkRule WRONG_DATE_INTERVAL(effDate.after(expDate)) FAILED" +
                    ", effDate: " + fdate(effDate) +
                    ", expDate: " + fdate(expDate));

        }

        logger.debug("checkCalendarCommonRule()=> END.");

    }


    private void checkUnique(ValidationRuleResult result, FiscalCalendarIdentView calendar, List<FiscalCalendarListView> accountCalendars) {

        if (calendar.getFiscalCalendarData().getFiscalYear() >= 0) {

            // check: Fiscal Year name is unique

            logger.info("checkUnique()=> checkRule YEAR_EXIST(fiscalYear: " + calendar.getFiscalCalendarData().getFiscalYear() + ")");

            for (FiscalCalendarListView fcv : accountCalendars) {

                if (fcv.getFiscalYear().intValue() == calendar.getFiscalCalendarData().getFiscalYear()) {

                    if (fcv.getFiscalCalendarId() != Utility.longNN(calendar.getFiscalCalendarData().getFiscalCalenderId())) {

                        result.failed(
                                ExceptionReason.FiscalCalendarUpdateReason.YEAR_EXIST,
                                Args.typed(calendar.getFiscalCalendarData().getFiscalYear())
                        );

                        logger.info("checkUnique()=> checkRule YEAR_EXIST(fiscalYear: " + calendar.getFiscalCalendarData().getFiscalYear() + ") FAILED" +
                                ", calendarId: " + calendar.getFiscalCalendarData().getFiscalCalenderId() + ", " +
                                ", existingCalendarId: " + fcv.getFiscalCalendarId());

                    }
                }
            }

        }

        logger.debug("checkUnique()=> END.");

    }


    private void checkCalendarWithYear(ValidationRuleResult result, FiscalCalendarIdentView calendar, List<FiscalCalendarListView> accountCalendars) {

        logger.debug("checkCalendarWithYear()=> BEGIN, fiscalYear: " + calendar.getFiscalCalendarData().getFiscalYear());

        if (calendar.getFiscalCalendarData().getFiscalYear() > 0) {

            logger.info("checkCalendarWithYear()=> checkRule YEAR_UPDATE_NOT_ALLOWED(fiscalYear < currentYear)");

            if (calendar.getFiscalCalendarData().getFiscalYear() < getCurrentDate().get(Calendar.YEAR)) {

                result.failed(
                        ExceptionReason.FiscalCalendarUpdateReason.YEAR_UPDATE_NOT_ALLOWED,
                        Args.typed(calendar.getFiscalCalendarData().getFiscalYear(), getCurrentDate().get(Calendar.YEAR))
                );

                logger.info("checkCalendarWithYear()=> checkRule YEAR_UPDATE_NOT_ALLOWED(fiscalYear < currentYear) FAILED" +
                        ", fiscalYer: "+calendar.getFiscalCalendarData().getFiscalYear()+
                        ", currentYear: "+getCurrentDate().get(Calendar.YEAR));

                return;
            }

            checkUnique(result, getCalendar(), accountCalendars);
            if (result.isFailed()) {
                return;
            }

            if (calendar.isServiceScheduleCalendar()) {
                check45PercentRule(result, calendar);
            }

        } else {
            
            logger.info("checkCalendarWithYear()=> ignored, fiscal year less than 0");

        }

        logger.debug("checkCalendarWithYear()=> END.");

    }

    private void check45PercentRule(ValidationRuleResult result, FiscalCalendarIdentView calendar) {

        logger.debug("check45PercentRule()=> BEGIN");

        Integer fiscalYearUpd = calendar.getFiscalCalendarData().getFiscalYear();

        Date effDate = calendar.getFiscalCalendarData().getEffDate();
        Date expDate = calendar.getFiscalCalendarData().getExpDate();

        Calendar effStartCalendar = Calendar.getInstance();
        Calendar expEndCalendar = Calendar.getInstance();

        effStartCalendar.setTime(calendar.getFiscalCalendarData().getEffDate());
        expEndCalendar.setTime(calendar.getFiscalCalendarData().getExpDate());

        int yearStart = effStartCalendar.get(Calendar.YEAR);
        int yearEnd = expEndCalendar.get(Calendar.YEAR);

        double MILLIS_TO_DAY_FACTOR = 24 * 60 * 60 * 1000;
        long rangeDays = Math.round((expDate.getTime() - effDate.getTime()) / MILLIS_TO_DAY_FACTOR);

        logger.info("check45PercentRule()=> checkRule [" +
                "WRONG_YEAR_VALUE(...)," +
                "WRONG_YEAR_VALUE_1(...)");

        if ((fiscalYearUpd > yearEnd || fiscalYearUpd < yearStart)) {

            result.failed(
                    ExceptionReason.FiscalCalendarUpdateReason.WRONG_YEAR_VALUE,
                    Args.typed(fiscalYearUpd)
            );

            logger.info("check45PercentRule()=> checkRule WRONG_YEAR_VALUE((fiscalYearUpd > yearEnd || fiscalYearUpd < yearStart)) FAILED" +
                    ", (" + fiscalYearUpd + " > " + yearEnd + " || " + fiscalYearUpd + " < " + yearStart + "))");

        } else if (yearEnd != yearStart) {

            try {

                Date lastYearDate1 = Parse.parseDate("12/31/" + yearStart, Constants.SYSTEM_DATE_PATTERN);
                Date lastYearDate2 = Parse.parseDate("12/31/" + ((yearEnd != yearStart) ? (yearEnd - 1) : yearEnd), Constants.SYSTEM_DATE_PATTERN);

                double daysInYearStartPer = Math.round(((lastYearDate1.getTime() - effDate.getTime()) / MILLIS_TO_DAY_FACTOR) * 100 / rangeDays);
                double daysInYearEndPer = Math.round((((expDate.getTime() - lastYearDate2.getTime()) / MILLIS_TO_DAY_FACTOR) + 1) * 100 / rangeDays);

                logger.debug("check45PercentRule()=> daysInYearStartPer=" + daysInYearStartPer + ", daysInYearEndPer=" + daysInYearEndPer);

                double PERSENT_OF_DAYS_COVER = 45;
                if ((daysInYearStartPer < PERSENT_OF_DAYS_COVER && yearStart == fiscalYearUpd) || (daysInYearEndPer < PERSENT_OF_DAYS_COVER && yearEnd == fiscalYearUpd)) {

                    result.failed(
                            ExceptionReason.FiscalCalendarUpdateReason.WRONG_YEAR_VALUE_1,
                            Args.typed(fiscalYearUpd)
                    );

                    logger.info("check45PercentRule()=> checkRule WRONG_YEAR_VALUE_1(...) FAILED" +
                            ", daysInYearStartPer: " + daysInYearStartPer+
                            ", PERSENT_OF_DAYS_COVER: " + PERSENT_OF_DAYS_COVER+
                            ", yearStart: "+yearStart+
                            ", fiscalYearUpd: "+fiscalYearUpd+
                            ", daysInYearEndPer: "+daysInYearEndPer+
                            ", PERSENT_OF_DAYS_COVER: "+PERSENT_OF_DAYS_COVER);
                }

            } catch (Exception ex) {

                logger.error(ex.getMessage(), ex);

                result.failed(
                        ExceptionReason.FiscalCalendarUpdateReason.EXCEPTION_WHEN_CHECK_WRONG_YEAR_VALUE_1,
                        Args.typed(fiscalYearUpd)
                );

            }
        }

        logger.debug("check45PercentRule()=> END.");

    }

    private void checkOnFiscalCalDetailsRange(ValidationRuleResult result, FiscalCalendarIdentView calendar) {

        logger.debug("checkOnFiscalCalDetailsRange()=> BEGIN");

        Date effDate = calendar.getFiscalCalendarData().getEffDate();
        Date expDate = calendar.getFiscalCalendarData().getExpDate();
        List<Integer> outOfRangePeriods = new ArrayList<Integer>();
        List<Integer> outOfSeqPeriods = new ArrayList<Integer>();

        Calendar effStartCalendar = Calendar.getInstance();
        effStartCalendar.setTime(effDate);


        SortedMap<Integer, Date> periodDatesMap = FiscalCalendarUtility.toPeriodDateMap(calendar);

        // exclude for check45PercentRule() last hidden period
        int lastSignPeriod = isNew(calendar) || !calendar.isServiceScheduleCalendar() ? periodDatesMap.lastKey() : periodDatesMap.lastKey() - 1;
        List<Integer> signPeriods = new ArrayList<Integer>(periodDatesMap.headMap(lastSignPeriod + 1).keySet());


        //check : Effective date match to the first period start date
        Calendar firstPeriod = Calendar.getInstance();
        firstPeriod.setTime(periodDatesMap.get(signPeriods.get(0)));

        logger.info("checkOnFiscalCalDetailsRange()=> signPeriods: " + signPeriods);
        logger.info("checkOnFiscalCalDetailsRange()=> periodDatesMap: " + periodDatesMap);

        logger.info("checkOnFiscalCalDetailsRange()=> checkRule FIRST_PERIOD_RULE(...)");

        if (effStartCalendar.get(Calendar.DAY_OF_MONTH) != firstPeriod.get(Calendar.DAY_OF_MONTH) || effStartCalendar.get(Calendar.MONTH) != firstPeriod.get(Calendar.MONTH)) {

            result.failed(
                    ExceptionReason.FiscalCalendarUpdateReason.FIRST_PERIOD_RULE,
                    Args.typed(firstPeriod.getTime())
            );

            logger.info("checkOnFiscalCalDetailsRange()=> checkRule FIRST_PERIOD_RULE(...) FAILED" +
                    ", effDateDay: "+effStartCalendar.get(Calendar.DAY_OF_MONTH)+
                    ", firstPeriodDay: "+firstPeriod.get(Calendar.DAY_OF_MONTH)+
                    ", effDateMonth: "+effStartCalendar.get(Calendar.MONTH)+
                    ", firstPeriodMonth: "+firstPeriod.get(Calendar.MONTH)
            );

        }


        if (expDate != null) {

            Calendar lastPeriod = Calendar.getInstance();
            lastPeriod.setTime(periodDatesMap.get(signPeriods.get(signPeriods.size() - 1)));

            // expDate > start date of last period
            logger.info("checkOnFiscalCalDetailsRange()=> checkRule LAST_PERIOD_RULE(...)");
            if (lastPeriod.after(expDate) || lastPeriod.getTime().equals(expDate)) {

                result.failed(
                        ExceptionReason.FiscalCalendarUpdateReason.LAST_PERIOD_RULE,
                        Args.typed(lastPeriod.getTime())
                );

                logger.info("checkOnFiscalCalDetailsRange()=> checkRule LAST_PERIOD_RULE(...) FAILED" +
                        ", lastPeriod: " + lastPeriod +
                        ", expDate: " + expDate
                );

            }

        }

        if (result.isFailed()) {
            return;
        }


        logger.info("checkOnFiscalCalDetailsRange()=> checkRule [OUT_OF_RANGE_PERIODS(...), OUT_OF_SEQ_PERIODS(...)");
        for (int j = 0; j < signPeriods.size(); j++) {

            Integer periodNum = signPeriods.get(j);
            Integer nextPeriodNum = signPeriods.size() - 1 > j ? signPeriods.get(j + 1) : null;

            Date periodDateJ = periodDatesMap.get(periodNum);
            Date periodDateNext = nextPeriodNum != null ? periodDatesMap.get(nextPeriodNum) : null;

            logger.debug("checkOnFiscalCalDetailsRange()=> periodDateJ[" + periodNum + "] -> " + fdate(periodDateJ));
            logger.debug("checkOnFiscalCalDetailsRange()=> periodDateNext[" + nextPeriodNum + "] -> " + fdate(periodDateNext));

            if(!calendar.isServiceScheduleCalendar())  {
                if(periodDateNext == null){
                    break;
                }
            }

            if (expDate != null && periodDateJ.after(expDate)) {

                logger.info("checkOnFiscalCalDetailsRange()=> outOfRange," +
                        " param[period: " + periodNum + ", periodDateJ: " + fdate(periodDateJ) +
                        ", expDate:" + fdate(expDate) + "]");

                outOfRangePeriods.add(periodNum);

            } else if (nextPeriodNum!=null &&
                //!FiscalCalendarUtility.isPeriodAfter( periodDateJ,  periodDateNext, expDate == null)) {
                // Manta-457 fiscal calendar must allow calendars to cross years
                !FiscalCalendarUtility.isPeriodAfter( periodDateJ,  periodDateNext, false)) {

                    logger.info("checkOnFiscalCalDetailsRange()=> outOfSeq," +
                            " param[period: " + periodNum + ", periodDateJ: " + fdate(periodDateJ) +
                            ", periodDateNext:" + fdate(periodDateNext) + "]");

                    outOfSeqPeriods.add(nextPeriodNum);

            }


        }

        if (outOfRangePeriods.size() > 0) {

            result.failed(
                    ExceptionReason.FiscalCalendarUpdateReason.OUT_OF_RANGE_PERIODS,
                    Args.typed(Utility.toCommaString(outOfRangePeriods))
            );

            logger.info("checkOnFiscalCalDetailsRange()=> checkRule OUT_OF_RANGE_PERIODS(...) FAILED" +
                    ", outOfRangePeriods: " + outOfRangePeriods);
        }

        if (outOfSeqPeriods.size() > 0) {

            result.failed(
                    ExceptionReason.FiscalCalendarUpdateReason.OUT_OF_SEQ_PERIODS,
                    Args.typed(Utility.toCommaString(outOfSeqPeriods))
            );

            logger.info("checkOnFiscalCalDetailsRange()=> checkRuleOUT_OF_SEQ_PERIODS(...) FAILED" +
                    ", ooutOfSeqPeriods: "+outOfSeqPeriods);

        }

        logger.debug("checkOnFiscalCalDetailsRange()=> END.");

    }


    private void checkDefaultCalendar(ValidationRuleResult result,
                                      FiscalCalendarIdentView calendar,
                                      List<FiscalCalendarListView> accountCalendars) {

        logger.debug("checkDefaultCalendar()=> BEGIN");

        if (calendar.getFiscalCalendarData().getFiscalYear() == 0) {
            
            //check if default calendars already exist            
            if (Utility.isSet(accountCalendars)) {
                for (FiscalCalendarListView element : accountCalendars) {
                    if (Integer.valueOf(0).equals(element.getFiscalYear()) && // found default fiscal calendar
                        (Utility.isNew(calendar) || !element.getFiscalCalendarId().equals(calendar.getFiscalCalendarData().getFiscalCalenderId()))) {
                        result.failed(ExceptionReason.FiscalCalendarUpdateReason.YEAR_OF_ZERO_CALENDAR_EXIST);

                        logger.info("checkDefaultCalendar()=> checkRule YEAR_OF_ZERO_CALENDAR_EXIST(...) FAILED" +
                                                ", existing calendar ID: " + element.getFiscalCalendarId());
                        break;
                    }
                }
            }
            
            Calendar start = Calendar.getInstance();
            start.setTime(calendar.getFiscalCalendarData().getEffDate());

            Calendar end = null;
            if (calendar.getFiscalCalendarData().getExpDate() != null) {
                end = Calendar.getInstance();
                end.setTime(calendar.getFiscalCalendarData().getExpDate());
            }

            logger.info("checkDefaultCalendar()=> checkRule YEAR_OF_ZERO_RULE(...)");

            if (start.get(Calendar.MONTH) != Calendar.JANUARY ||
                start.get(Calendar.DAY_OF_MONTH) != 1 ||
                (end != null &&
                    (end.get(Calendar.MONTH) != Calendar.DECEMBER ||
                        end.get(Calendar.DAY_OF_MONTH) != 31))) {

                result.failed(ExceptionReason.FiscalCalendarUpdateReason.YEAR_OF_ZERO_RULE, 
                		Args.typed(Integer.valueOf(calendar.getFiscalCalendarData().getFiscalYear())));

                logger.info("checkDefaultCalendar()=> checkRuleYEAR_OF_ZERO_RULE(...) FAILED" +
                        ", effDay: " + start.get(Calendar.DAY_OF_MONTH) +
                        ", effMonth: " + start.get(Calendar.MONTH) +
                        ", expDay: " + (end != null ? end.get(Calendar.DAY_OF_MONTH) : "null") +
                        ", expMonth: " + (end != null ? end.get(Calendar.MONTH) : "null")
                );
            }
            
            SortedMap<Integer, Date> periodDatesMap = FiscalCalendarUtility.toPeriodDateMap(calendar);

            // exclude for check45PercentRule() last hidden period
            if (!isNew(calendar) && calendar.isServiceScheduleCalendar()) {
                periodDatesMap = periodDatesMap.headMap(periodDatesMap.lastKey());
            }
            
            //check : First period start on 01/01
            Calendar firstPeriod = Calendar.getInstance();
            firstPeriod.setTime(periodDatesMap.get(periodDatesMap.firstKey()));

            logger.info("checkDefaultCalendar() => checkRule YEAR_OF_ZERO_LAST_PERIOD_START_RULE(...)");

            if (firstPeriod.get(Calendar.MONTH) != Calendar.JANUARY ||
                firstPeriod.get(Calendar.DAY_OF_MONTH) != 1)  {

                result.failed(
                    ExceptionReason.FiscalCalendarUpdateReason.YEAR_OF_ZERO_FIRST_PERIOD_START_RULE
                );

                logger.info("checkDefaultCalendar()=> checkRule YEAR_OF_ZERO_FIRST_PERIOD_START_RULE(...) FAILED" +
                        ", firstPerionMonth: " + firstPeriod.get(Calendar.MONTH) + 1 +
                        ", firstPeriodDay: " + firstPeriod.get(Calendar.DAY_OF_MONTH)
                );
            }
            
            //check : Last period don't start on 12/31
            Calendar lastPeriod = Calendar.getInstance();
            lastPeriod.setTime(periodDatesMap.get(periodDatesMap.lastKey()));

            logger.info("checkDefaultCalendar() => checkRule YEAR_OF_ZERO_LAST_PERIOD_START_RULE(...)");

            if (lastPeriod.get(Calendar.MONTH) == Calendar.DECEMBER &&
                lastPeriod.get(Calendar.DAY_OF_MONTH) == 31)  {

                result.failed(
                    ExceptionReason.FiscalCalendarUpdateReason.YEAR_OF_ZERO_LAST_PERIOD_START_RULE
                );

                logger.info("checkDefaultCalendar()=> checkRule YEAR_OF_ZERO_LAST_PERIOD_START_RULE(...) FAILED" +
                        ", lastPerionMonth: " + lastPeriod.get(Calendar.MONTH) + 1 +
                        ", lastPeriodDay: " + lastPeriod.get(Calendar.DAY_OF_MONTH)
                );
            }

            //check : periods not exceed end of year
            Integer prevYear = null;
            Integer currentYear = null;
            Calendar tmpCalendar = Calendar.getInstance();
            for (int i = periodDatesMap.firstKey(); i <= periodDatesMap.lastKey(); i++) {
                if (Utility.isSet(periodDatesMap.get(i))) {
                    tmpCalendar.setTime(periodDatesMap.get(i));
                    currentYear = tmpCalendar.get(Calendar.YEAR);
                    if (prevYear != null && currentYear > prevYear) {
                        result.failed(
                            ExceptionReason.FiscalCalendarUpdateReason.YEAR_OF_ZERO_PERIOD_EXEED_YEAR_END,
                            Args.typed(Integer.valueOf(i))
                        );
                        logger.info("checkDefaultCalendar()=> checkRule YEAR_OF_ZERO_PERIOD_EXEED_YEAR_END(...) FAILED" +
                                    ", periodNumber: " + i);
                        break;
                    }
                    prevYear = currentYear;
                }
            }
        } else {

            logger.info("checkDefaultCalendar()=> reject, fiscal year != 0");

        }

        logger.debug("checkDefaultCalendar()=> END.");

    }


    private void checkOnFiscalCalDataRange(ValidationRuleResult result, FiscalCalendarIdentView calendar, List<FiscalCalendarListView> accountCalendars) {

        logger.info("checkOnFiscalCalDataRange()=> BEGIN");

        SortedMap<Date, FiscalCalendarValue> tm = new TreeMap<Date, FiscalCalendarValue>();

        Date key;
        FiscalCalendarValue value;
        FiscalReportIntersectResView mmdd = null;
        Long fiscalCalenderId = null;
        Long busEntityId = null;
        Integer FiscalYear = null;
        
        fiscalCalenderId = calendar.getFiscalCalendarData().getFiscalCalenderId();

        logger.info("checkOnFiscalCalDataRange()=> checkRule [DUPLICATED_EFF_DATE(...) fiscalCalenderId="+String.valueOf(fiscalCalenderId));

        for (int i = 0; accountCalendars != null && i < accountCalendars.size(); i++) {

            FiscalCalendarListView accountCalendar = accountCalendars.get(i);
            
            if (accountCalendar.getFiscalCalendarId() == Utility.longNN(calendar.getFiscalCalendarData().getFiscalCalenderId())) {
            	
            	busEntityId = calendar.getFiscalCalendarData().getBusEntityId();
                key = calendar.getFiscalCalendarData().getEffDate();
                FiscalYear  = calendar.getFiscalCalendarData().getFiscalYear();
                value = new FiscalCalendarValue(calendar.getFiscalCalendarData().getFiscalCalenderId(),
                        calendar.getFiscalCalendarData().getFiscalYear(),
                        calendar.getFiscalCalendarData().getEffDate(),
                        calendar.getFiscalCalendarData().getExpDate())
                ;
            } else {

            	busEntityId = accountCalendar.getBusEntityId();
            	FiscalYear  = accountCalendar.getFiscalYear();
                key = accountCalendar.getEffDate();
                value = new FiscalCalendarValue(accountCalendar.getFiscalCalendarId(),
                        accountCalendar.getFiscalYear(),
                        accountCalendar.getEffDate(),
                        accountCalendar.getExpDate())
                ;
            }
            
            FiscalCalendarDAOImpl fiscalCalendarDAO = new FiscalCalendarDAOImpl(this.entityManager);
            
            SortedMap<Integer, Date> periodDatesMap = FiscalCalendarUtility.toPeriodDateMap(calendar);
            Date lastKeyDate = periodDatesMap.get(periodDatesMap.lastKey());
            	
            mmdd = fiscalCalendarDAO.getFiscalCalendarIntersectYear(busEntityId,fiscalCalenderId,calendar.getFiscalCalendarData().getEffDate(),lastKeyDate);

            if (mmdd != null) {
            	    logger.info("Error. new calendar.getFiscalCalendarData().getFiscalYear()="+String.valueOf(calendar.getFiscalCalendarData().getFiscalYear())+" Calendars for years "+fdate(calendar.getFiscalCalendarData().getEffDate())+" and "+String.valueOf(mmdd.getFiscalYear())+" Effective Date = "+fdate(mmdd.getEffDate())+" intersect.");
            	    //logger.info("Error. new calendar mmdd.getEffDate() "+fdate(mmdd.getEffDate())+" mmdd.getFiscalYear()="+String.valueOf(mmdd.getFiscalYear()));

					//result.failed(
					//        ExceptionReason.FiscalCalendarUpdateReason.CALENDAR_INTERSECT,
					//        Args.typed(String.valueOf(mmdd.getFiscalYear()), fdate(mmdd.getEffDate()), String.valueOf(FiscalYear), fdate(key))
					//);
            	    
					result.failed(
					        ExceptionReason.FiscalCalendarUpdateReason.CALENDAR_INTERSECT,
					        Args.typed(String.valueOf(calendar.getFiscalCalendarData().getFiscalYear()), fdate(calendar.getFiscalCalendarData().getEffDate()), String.valueOf(mmdd.getFiscalYear()), fdate(mmdd.getEffDate()))
					);            	    
            
                logger.info("checkOnFiscalCalDataRange()=> checkRule [CALENDAR_INTERSECT(...) FAILED" +
                        ", effDate: " + fdate(mmdd.getEffDate()) +
                        ", existingCalendarId: " + String.valueOf(fiscalCalenderId)
                );
                
                return;                
            }                     
            
            fiscalCalendarDAO = null;           
            
            /*if (tm.containsKey(key)) {

                Integer year = tm.get(key).getFiscalYear();

                result.failed(
                        ExceptionReason.FiscalCalendarUpdateReason.DUPLICATED_EFF_DATE,
                        Args.typed(year, key)
                );

                logger.debug("checkOnFiscalCalDataRange()=> checkRule [DUPLICATED_EFF_DATE(...) FAILED" +
                        ", effDate: " + fdate(key) +
                        ", existingCalendarId: " + tm.get(key).getFiscalCalendarId()
                );

                return;
            }*/

            tm.put(key, value);
        }


        if (Utility.isNew(calendar)) {

            logger.info("checkOnFiscalCalDataRange()=> check for new record");

            key = calendar.getFiscalCalendarData().getEffDate();
            
            fiscalCalenderId = calendar.getFiscalCalendarData().getFiscalCalenderId();
            busEntityId = calendar.getFiscalCalendarData().getBusEntityId();
            
            value = new FiscalCalendarValue(calendar.getFiscalCalendarData().getFiscalCalenderId(),
                    calendar.getFiscalCalendarData().getFiscalYear(),
                    calendar.getFiscalCalendarData().getEffDate(),
                    calendar.getFiscalCalendarData().getExpDate()
            );
            
            
            FiscalCalendarDAOImpl fiscalCalendarDAO = new FiscalCalendarDAOImpl(this.entityManager);
            	
            SortedMap<Integer, Date> periodDatesMap = FiscalCalendarUtility.toPeriodDateMap(calendar);
            Date lastKeyDate = periodDatesMap.get(periodDatesMap.lastKey());
            	
            mmdd = fiscalCalendarDAO.getFiscalCalendarIntersectYear(busEntityId,fiscalCalenderId,key,lastKeyDate);

            if (mmdd != null) {	
            	logger.info("Error. new calendar.getFiscalCalendarData().getFiscalYear()="+String.valueOf(calendar.getFiscalCalendarData().getFiscalYear())+" Calendars for years "+fdate(mmdd.getEffDate())+" and "+String.valueOf(mmdd.getFiscalYear())+" Effective Date = "+fdate(mmdd.getEffDate())+" intersect.");
            	//logger.info("Error. update calendar mmdd.getEffDate() "+fdate(mmdd.getEffDate())+" mmdd.getFiscalYear()="+String.valueOf(mmdd.getFiscalYear()));

				result.failed(
				        ExceptionReason.FiscalCalendarUpdateReason.CALENDAR_INTERSECT,
				        Args.typed(String.valueOf(calendar.getFiscalCalendarData().getFiscalYear()), fdate(calendar.getFiscalCalendarData().getEffDate()), String.valueOf(mmdd.getFiscalYear()), fdate(mmdd.getEffDate()))
				);           

                logger.info("checkOnFiscalCalDataRange()=> checkRule [CALENDAR_INTERSECT(...) FAILED" +
                        ", effDate: " + fdate(mmdd.getEffDate()) +
                        ", existingCalendarId: " + String.valueOf(fiscalCalenderId)
                );
                
                return;
            }                     
            
            fiscalCalendarDAO = null;          

            /*if (tm.containsKey(key)) {

                Integer year = tm.get(key).getFiscalYear();

                result.failed(
                        ExceptionReason.FiscalCalendarUpdateReason.DUPLICATED_EFF_DATE,
                        Args.typed(year, key)
                );

                logger.info("checkOnFiscalCalDataRange()=> checkRule [DUPLICATED_EFF_DATE(...) FAILED" +
                        ", effDate: " + fdate(key) +
                        ", existingCalendarId: " + tm.get(key).getFiscalCalendarId()
                );

            }*/

            tm.put(key, value);

        }

        if (tm.size() == 0) {
            return;
        }

        if(result.isFailed()){
            return;
        }

        if (calendar.isServiceScheduleCalendar()) {

            ArrayList<Date> effDatesList = new ArrayList<Date>(tm.keySet());

            logger.info("checkOnFiscalCalDataRange()=> checkRule [OVERLAP(...), GAP(...)]");
            logger.debug("checkOnFiscalCalDataRange()=> effDatesList: " + effDatesList);

            Date keyCurr = calendar.getFiscalCalendarData().getEffDate();

            Date keyBefore = null;
            Date keyAfter = null;

            if (effDatesList.size() != 1) {

                for (int i = 0; i < effDatesList.size(); i++) {

                    Date keyEl = effDatesList.get(i);

                    if (keyEl.equals(keyCurr)) {

                        if (i == 0) {
                            keyAfter = effDatesList.get(i + 1);
                            break;
                        } else if (i == effDatesList.size() - 1) {
                            keyBefore = effDatesList.get(i - 1);
                            break;
                        } else {
                            keyAfter = effDatesList.get(i + 1);
                            keyBefore = effDatesList.get(i - 1);
                            break;
                        }

                    }
                }
            }

            logger.debug("checkOnFiscalCalDataRange()=> currentEffDate:" + fdate(keyCurr) + "("
                    + "before: " + ((keyBefore == null) ? "" : fdate(keyBefore)) +
                    ", after: " + ((keyAfter == null) ? "" : fdate(keyAfter)) + ")"
            );

            FiscalCalendarValue fckDCurr = tm.get(keyCurr);

            if (keyAfter != null) {

                FiscalCalendarValue fckDAfter = tm.get(keyAfter);
                long daysBetween = ((fckDAfter.getEffDate().getTime() - fckDCurr.getExpDate().getTime()) / (3600000 * 24));

                if (daysBetween < 1) {

                    result.failed(
                            ExceptionReason.FiscalCalendarUpdateReason.OVERLAP,
                            Args.typed(fckDAfter.getFiscalYear())
                    );

                    logger.info("checkOnFiscalCalDataRange()=>  checkRule OVERLAP(...) FAILED" +
                            ", effDate: " + fdate(fckDCurr.getEffDate()) +
                            ", effDateAfter: " + fdate(fckDAfter.getEffDate()) +
                            ", daysBetween: " + daysBetween +
                            ", fiscalYearAfter: " + fckDAfter.getFiscalYear()
                    );


                } else if (daysBetween > 1) {

                    result.failed(
                            ExceptionReason.FiscalCalendarUpdateReason.GAP,
                            Args.typed(fckDAfter.getFiscalYear())
                    );

                    logger.info("checkOnFiscalCalDataRange()=>  checkRule GAP(...) FAILED" +
                            ", effDate: " + fdate(fckDCurr.getEffDate()) +
                            ", effDateAfter: " + fdate(fckDAfter.getEffDate()) +
                            ", daysBetween: " + daysBetween +
                            ", fiscalYearAfter: " + fckDAfter.getFiscalYear()
                    );
                }

            }

            if (keyBefore != null) {

                FiscalCalendarValue fckDBefore = tm.get(keyBefore);

                Date expDateBefore = (fckDBefore.getExpDate() == null) ? FiscalCalendarUtility.calcExpDate(fckDBefore.getFiscalYear()) : fckDBefore.getExpDate();
                long daysBetween = ((fckDCurr.getEffDate().getTime() - expDateBefore.getTime()) / (3600000 * 24));

                if (daysBetween < 1) {

                    result.failed(
                            ExceptionReason.FiscalCalendarUpdateReason.OVERLAP,
                            Args.typed(fckDBefore.getFiscalYear())
                    );

                    logger.info("checkOnFiscalCalDataRange()=>  checkRule OVERLAP(...) FAILED" +
                            ", effDate: " + fdate(fckDCurr.getEffDate()) +
                            ", effDateBefore: " + fdate(fckDBefore.getEffDate()) +
                            ", expDateBefore: " + fdate(expDateBefore) +
                            ", daysBetween: " + daysBetween +
                            ", fiscalYearBefore: " + fckDBefore.getFiscalYear()
                    );

                } else if (daysBetween > 1) {

                    result.failed(
                            ExceptionReason.FiscalCalendarUpdateReason.GAP,
                            Args.typed(fckDBefore.getFiscalYear())
                    );
                    logger.info("checkOnFiscalCalDataRange()=>  checkRule GAP(...) FAILED" +
                            ", effDate: " + fdate(fckDCurr.getEffDate()) +
                            ", effDateBefore: " + fdate(fckDBefore.getEffDate()) +
                            ", expDateBefore: " + fdate(expDateBefore) +
                            ", daysBetween: " + daysBetween +
                            ", fiscalYearBefore: " + fckDBefore.getFiscalYear()
                    );


                }
            }
        }

        logger.debug("checkOnFiscalCalDataRange()=> END.");


    }


    private String fdate(Date date) {
        return date != null ? AppI18nFormatter.formatDate(date, AppLocale.SYSTEM_LOCALE) : Constants.UNK;
    }

    private boolean isNew(FiscalCalendarIdentView calendar) {
        return getId(calendar) <= 0;
    }

    private long getId(FiscalCalendarIdentView calendar) {
        return  Utility.longNN(calendar.getFiscalCalendarData().getFiscalCalenderId());
    }

    public Calendar getCurrentDate() {
        return currentDate;
    }

    public FiscalCalendarIdentView getCalendar() {
        return calendar;
    }

    public AccountService getAccountService() {
        return ServiceLocator.getAccountService();
    }

    private class FiscalCalendarValue {

        private Long fiscalCalendarId;
        private Integer fiscalYear;
        private Date effDate;
        private Date expDate;

        private FiscalCalendarValue(Long fiscalCalendarId, Integer fiscalYear, Date effDate, Date expDate) {
            this.fiscalCalendarId = fiscalCalendarId;
            this.fiscalYear = fiscalYear;
            this.effDate = effDate;
            this.expDate = expDate;
        }

        public Long getFiscalCalendarId() {
            return fiscalCalendarId;
        }

        public void setFiscalCalendarId(Long fiscalCalendarId) {
            this.fiscalCalendarId = fiscalCalendarId;
        }

        public Integer getFiscalYear() {
            return fiscalYear;
        }

        public void setFiscalYear(Integer fiscalYear) {
            this.fiscalYear = fiscalYear;
        }

        public Date getEffDate() {
            return effDate;
        }

        public void setEffDate(Date effDate) {
            this.effDate = effDate;
        }

        public Date getExpDate() {
            return expDate;
        }

        public void setExpDate(Date expDate) {
            this.expDate = expDate;
        }
    }
}
