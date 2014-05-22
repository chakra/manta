package com.espendwise.manta.util;

import com.espendwise.manta.model.data.FiscalCalenderData;
import com.espendwise.manta.model.data.FiscalCalenderDetailData;
import com.espendwise.manta.model.view.FiscalCalendarIdentView;
import com.espendwise.manta.model.view.FiscalCalendarListView;
import com.espendwise.manta.model.view.FiscalCalendarPhysicalView;
import com.espendwise.manta.util.parser.Parse;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;


public class FiscalCalendarUtility {

    private static final Logger logger = Logger.getLogger(FiscalCalendarUtility .class);

    public static String getTailPeriodByDate(Date pExpDate) {

        String res = "";

        if (pExpDate != null) {

            Calendar c = new GregorianCalendar();

            c.setTime(pExpDate);
            c.add(Calendar.DAY_OF_MONTH, 1);

            res = new SimpleDateFormat("M/d").format(c.getTime());
        }

        return res;

    }

    public static FiscalCalenderDetailData createTailPeriod(FiscalCalenderData calendarData, int num) {

        FiscalCalenderDetailData p = new FiscalCalenderDetailData();
        p.setFiscalCalenderId(calendarData.getFiscalCalenderId());
        p.setPeriod(num);
        p.setMmdd(getTailPeriodByDate(calendarData.getExpDate()));

        return p;
    }

    public static SortedMap<Integer, FiscalCalenderDetailData> toPeriodMap(List<FiscalCalenderDetailData> periods) {
        SortedMap<Integer, FiscalCalenderDetailData> m = new TreeMap<Integer, FiscalCalenderDetailData>();
        for (FiscalCalenderDetailData p : periods) {
            m.put(p.getPeriod(), p);
        }
        return m;
    }

    public static Date getMmDdDate(FiscalCalendarIdentView calendar, int period) {
        return getMmDdDate(calendar.getFiscalCalendarData(), period, toPeriodMap(calendar.getPeriods()));
    }

    public static Date getMmDdDate(FiscalCalenderData calendar, List<FiscalCalenderDetailData> periods, int period) {
        return getMmDdDate(calendar, period, toPeriodMap(periods));
    }

    public static Date getMmDdDate(FiscalCalenderData calendar,  int period, SortedMap<Integer, FiscalCalenderDetailData> periodMsp) {
        return getMmDdDate(calendar.getEffDate(), period, periodMsp);
    }

    public static Date getMmDdDate(Date effDate, int period, SortedMap<Integer, FiscalCalenderDetailData> periodMsp) {

        ArrayList<Integer> periodIndexes = new ArrayList<Integer>(periodMsp.keySet());

        int year = getYearForDate(effDate);

        if (periodIndexes.size() == 1) {
            return Parse.parseMonthWithDay(
                    getMmdd(periodMsp, periodIndexes.get(0)),
                    year,
                    Constants.SYSTEM_MONTH_WITH_DAY_PATTERN
            );
        }

        for (int i = 0; i < periodIndexes.size() - 1; i++) {

            String mmdd1 = getMmdd(periodMsp, periodIndexes.get(i));


            Date date1 = Utility.isSet(mmdd1) ? Parse.parseMonthWithDay(mmdd1, year, Constants.SYSTEM_MONTH_WITH_DAY_PATTERN) : null;

            String mmdd2 = getMmdd(periodMsp, periodIndexes.get(i + 1));
            Date date2 = Utility.isSet(mmdd2) ? Parse.parseMonthWithDay(mmdd2, year, Constants.SYSTEM_MONTH_WITH_DAY_PATTERN) : null;

            if (date2 != null && date1 != null && date1.compareTo(date2) > 0) {
                year = year + 1;
                date2 = Parse.parseMonthWithDay(getMmdd(periodMsp, periodIndexes.get(i + 1)), year, Constants.SYSTEM_MONTH_WITH_DAY_PATTERN);
            }

            if (period == periodIndexes.get(i)) {
                return date1;
            }

            if (period == periodIndexes.get(i + 1)) {
                return date2;
            }


        }

        return null;
    }

    private static String getMmdd(SortedMap<Integer, FiscalCalenderDetailData> periodMsp, Integer period) {
        return periodMsp.get(period).getMmdd();
    }


    public static int getYearForDate(Date pDate) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(pDate);
        return cal.get(Calendar.YEAR);
    }

    public static SortedMap<Integer, Date> toPeriodDateMap(FiscalCalendarIdentView calendar) {
        SortedMap<Integer, Date> x = new TreeMap<Integer, Date>();
        SortedMap<Integer, FiscalCalenderDetailData> periodsMap = toPeriodMap(calendar.getPeriods());
        for (Integer period : periodsMap.keySet()) {
            x.put(period, getMmDdDate(calendar.getFiscalCalendarData(),
                    period,
                    periodsMap
            )
            );
        }
        return x;
    }

    public static SortedMap<Integer, Date> toPeriodDateMap(FiscalCalendarPhysicalView calendarPhysical) {

        SortedMap<Integer, Date> x = new TreeMap<Integer, Date>();
        SortedMap<Integer, FiscalCalenderDetailData> periodsMap = toPeriodMap(calendarPhysical.getPeriods());

        for (Integer period : periodsMap.keySet()) {

            x.put(period,
                    getMmDdDate(
                            getPhysicalEffDate(calendarPhysical),
                            period,
                            periodsMap
                    )
            );
        }

        return x;

    }

    public static SortedMap<Integer, Integer> signedPeriodsIndexSet(FiscalCalendarPhysicalView calendarPhysical) {

        SortedMap<Integer, Integer> x = new TreeMap<Integer, Integer>();

        SortedMap<Integer, FiscalCalenderDetailData> periodsMap = toPeriodMap(calendarPhysical.getPeriods());
        if(calendarPhysical.isServiceScheduleCalendar()) {
            periodsMap.remove(periodsMap.lastKey());
        }

        int i = 0;
        for (Integer period : periodsMap.keySet()) {
            x.put(i++, period);
        }

        return x;
    }

    public static Date getPhysicalEffDate(FiscalCalendarPhysicalView calendarPhysical) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(calendarPhysical.getEffDate());

        if (Utility.intNN(calendarPhysical.getCalendatFiscalYear()) == 0) {

            return new GregorianCalendar(calendarPhysical.getPhysicalFiscalYear(),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            ).getTime();

        } else {

            return cal.getTime();

        }
    }

    public static int getPhysicalYear(Integer fiscalYear, Date date) {
        return Utility.intNN(fiscalYear) == 0 ? getYearForDate(date) : fiscalYear;
    }


    public static Date calcExpDate(FiscalCalendarListView calendar) {
        return calcExpDate(calendar.getFiscalYear());
    }

    public static Date calcExpDate(Integer fiscalYear) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int year = c.get(Calendar.YEAR);
        year = (fiscalYear > 0) ? fiscalYear : year;
        try {
            dt = Parse.parseDate("12/31/" + year, Constants.SYSTEM_DATE_PATTERN);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dt;
    }

    public static Date calcExpDate(Integer fiscalYear, Date expDate) {
        return expDate == null
                ? Parse.parseDate("12/31/" + getPhysicalYear(fiscalYear, new Date()), Constants.SYSTEM_DATE_PATTERN)
                : expDate;
    }


    public static Integer getPeriodOrNull(FiscalCalendarPhysicalView calendarPhysical, Date date) {

        //logger.info("getPeriodOrNull()=> BEGIN, date: " + date);

        Date today = Utility.setToMidnight(date);

        SortedMap<Integer, Date> periodDateMap = toPeriodDateMap(calendarPhysical);
        ArrayList<Integer> periodIndexes = new ArrayList<Integer>(periodDateMap.keySet());

        Date effDate = getPhysicalEffDate(calendarPhysical);
        Date expDate = calendarPhysical.getExpDate();

        //logger.info("getPeriodOrNull()=> effDate: " + calendarPhysical.getEffDate());
        //logger.info("getPeriodOrNull()=> physicalDate: " + effDate);
       // logger.info("getPeriodOrNull()=> expDate: " + expDate);

        Set<Integer> keys = periodDateMap.keySet();

      //  logger.info("getPeriodOrNull()=> periodDateMap: " + periodDateMap);
       // logger.info("getPeriodOrNull()=> periodIndexes: " + periodIndexes);
        //logger.info("getPeriodOrNull()=> keys: " + keys);

        Integer period = 0;

        for (int i = 0; i < periodIndexes.size() - 1; i++) {

            Date from = periodDateMap.get(periodIndexes.get(i));
            Date to = periodDateMap.get(periodIndexes.get(i + 1));

           // logger.info("getPeriodOrNull()=> check[" + i + "]  " + new DateArgument(from).resolve() + " > " + new DateArgument(date).resolve() + "  < " + new DateArgument(to).resolve());

            if (to != null) { // if calendar valid and was created by manta
                if (from.compareTo(today) <= 0 && to.compareTo(today) > 0) {
                    return periodIndexes.get(i);
                }
            } else if (i == periodIndexes.size()) {  // maybe  created using old ui and last period is null
                if (expDate != null) {   // old calendars  have  expDate is null
                    if (from.compareTo(today) <= 0 && expDate.compareTo(today) > 0 || from.compareTo(today) <= 0) {
                        return periodIndexes.get(i);
                    }
                }
            }

            period = periodIndexes.get(i);

        }


        return period;

    }


    public static boolean isPeriodAfter(Date periodA, Date periodB, boolean ignoreYear) {

        Calendar calPeriodA = Calendar.getInstance();
        calPeriodA.setTime(periodA);

        Calendar calPeriodB = Calendar.getInstance();
        calPeriodB.setTime(periodB);

        if (!ignoreYear) {
            logger.info(" calPeriodB.getTime(): "+calPeriodB.getTime()+" after "+calPeriodA.getTime());

            return calPeriodB.getTime().after(calPeriodA.getTime());

        } else {

            calPeriodA.set(Calendar.YEAR, Constants.LEAP_YEAR);
            calPeriodB.set(Calendar.YEAR, Constants.LEAP_YEAR);


            logger.info(" calPeriodB.getTime(): "+calPeriodB.getTime()+" after(ignoreYear) "+calPeriodA.getTime());

            return calPeriodB.getTime().after(calPeriodA.getTime());
        }

    }
}
