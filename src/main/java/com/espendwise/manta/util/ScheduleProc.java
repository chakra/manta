package com.espendwise.manta.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.model.data.ScheduleData;
import com.espendwise.manta.model.data.ScheduleDetailData;
import com.espendwise.manta.model.view.ScheduleJoinView;
import com.espendwise.manta.model.view.ScheduleOrderDatesView;
import com.espendwise.manta.model.view.SiteDeliveryScheduleView;


public class ScheduleProc {

    private static final Logger logger = Logger.getLogger(ScheduleProc.class);

    int WEEKLY = 1;
    int DAY_MONTHLY = 2;
    int WEEK_MONTHLY = 3;

    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat timeFormat24hour = new SimpleDateFormat(Constants.SYSTEM_TIME_PATTERN);
    SimpleDateFormat dayFormat = new SimpleDateFormat(Constants.SYSTEM_DATE_PATTERN);

    Calendar setupCal = Calendar.getInstance();

    //Schedule related
    ScheduleData scheduleHeader;
    List<ScheduleDetailData> scheduleDetails;
    SiteDeliveryScheduleView siteDeliverySchedule;


    int[] weekFilter;
    boolean applyWeekFilter;
    Long scheduleId;
    int scheduleRule;
    Date effDate;
    GregorianCalendar effDateWkBegCal = null;
    Date exdate;
    Date[] alsoDates;
    Date[] exceptDates;
    Date[] holidayDates;
    Long cycle;
    int[] deliveryDays;
    int weekDayInd;
    int cutoffDay;
    private Date cutoffTime;
    int accountCutoffDays;
    boolean initialisedFlag;
    String errorMessage;

    //Changing parameters
    Date curAlsoDate;
    int curAlsoInd;
    Date curExceptDate;
    int curExceptInd;
    Date curHolidayDate;
    int curHolidayInd;
    GregorianCalendar begCal;
    GregorianCalendar wkBegCal;
    GregorianCalendar endCal;
    Date begTime;
    GregorianCalendar curDeliveryCal;
    int curCalendarIndex;
    GregorianCalendar curBegWeekCal; //for weekly schedule
    boolean exceptionFlag;
    GregorianCalendar firstWeekMonthCal; //for week-monthly schedule
    private int[][] siteDeliveryWeekFilter;
    private int currMonth = -1;

    int weekOfDel = 0;


    public ScheduleProc(ScheduleJoinView pSchedule, SiteDeliveryScheduleView pSiteDeliverySchedule, int pAccountCutoffDays) {
        this.scheduleHeader = pSchedule.getSchedule();
        this.scheduleDetails = pSchedule.getScheduleDetail();
        this.siteDeliverySchedule = pSiteDeliverySchedule;
        this.initialisedFlag = false;
        this.errorMessage = null;
        this.accountCutoffDays = pAccountCutoffDays;
    }

    public ScheduleProc(ScheduleJoinView pSchedule, SiteDeliveryScheduleView pSiteDeliverySchedule) {
        this.scheduleHeader = pSchedule.getSchedule();
        this.scheduleDetails = pSchedule.getScheduleDetail();
        this.siteDeliverySchedule = pSiteDeliverySchedule;
        this.initialisedFlag = false;
        this.errorMessage = null;
        this.accountCutoffDays = 0;
    }

    public void initSchedule() throws Exception {

        String scheduleRule = this.scheduleHeader.getScheduleRuleCd();
       
        this.scheduleId = this.scheduleHeader.getScheduleId();
        this.cycle = this.scheduleHeader.getCycle();
        
         if (null != this.siteDeliverySchedule) {
            logger.info("initSchedule()=> delivery schedule: " + this.siteDeliverySchedule.toString());
        }

        this.effDate = this.scheduleHeader.getEffDate();

        Calendar effCall = GregorianCalendar.getInstance();
        effCall.setTime(this.effDate);
        int effDateWeekDay = effCall.get(Calendar.DAY_OF_WEEK);

        this.effDateWkBegCal = (GregorianCalendar) effCall.clone();
        this.effDateWkBegCal.add(Calendar.DAY_OF_MONTH, 1 - effDateWeekDay);

        if (this.cycle == 2 && RefCodeNames.SCHEDULE_RULE_CD.WEEK.equals(scheduleRule)) {
            //if is year is current
            int effYear = effCall.get(GregorianCalendar.YEAR);
            this.weekOfDel = effCall.get(Calendar.WEEK_OF_YEAR);
            logger.info("initSchedule()=> OK this.cycle=" + this.cycle + " scheduleRule=" + scheduleRule);
        } else if (this.cycle != 1) {
            this.errorMessage = "Can't process schedule if schedule cycle is not 1. Schedule id = " + this.scheduleId;
            throw new Exception(this.errorMessage);
        }

        this.exdate = this.scheduleHeader.getExpDate();
        if (this.siteDeliverySchedule == null) {
            this.applyWeekFilter = false;
            logger.info("initSchedule()=> No site delivery schedule provided");
        } else if (this.siteDeliverySchedule.getSiteScheduleType().startsWith("Any")) {
            this.applyWeekFilter = false;
        } else {
            //Week filter
            this.weekFilter = new int[5];
            this.weekFilter[0] = (this.siteDeliverySchedule.isWeek1ofMonth()) ? 0 : -1;
            this.weekFilter[1] = (this.siteDeliverySchedule.isWeek2ofMonth()) ? 0 : -1;
            this.weekFilter[2] = (this.siteDeliverySchedule.isWeek3ofMonth()) ? 0 : -1;
            this.weekFilter[3] = (this.siteDeliverySchedule.isWeek4ofMonth()) ? 0 : -1;
            this.weekFilter[4] = (this.siteDeliverySchedule.isLastWeekofMonth()) ? 0 : -1;

            this.applyWeekFilter = false;
            for (int ii = 0; ii < 5; ii++) {
                if (this.weekFilter[ii] == -1) {
                    this.applyWeekFilter = true;
                    break;
                }
            }
        }

        if (RefCodeNames.SCHEDULE_RULE_CD.DAY_MONTH.equals(scheduleRule)) {
            this.scheduleRule = DAY_MONTHLY;
        } else if (RefCodeNames.SCHEDULE_RULE_CD.WEEK.equals(scheduleRule)) {
            this.scheduleRule = WEEKLY;
        } else if (RefCodeNames.SCHEDULE_RULE_CD.WEEK_MONTH.equals(scheduleRule)) {
            this.scheduleRule = WEEK_MONTHLY;
        }

        //Details
        List<Integer> deliveryDays = new ArrayList<Integer>();
        List<Date> alsoDates = new ArrayList<Date>();
        List<Date> exceptDates = new ArrayList<Date>();
        List<Date> holidayDates = new ArrayList<Date>();

        for (ScheduleDetailData scheduleDetail : this.scheduleDetails) {

            String code = scheduleDetail.getScheduleDetailCd();
            String value = scheduleDetail.getValue();

            if (RefCodeNames.SCHEDULE_DETAIL_CD.MONTH_WEEK.equals(code) && this.scheduleRule == WEEK_MONTHLY) {

                int weekInd = Integer.parseInt(value);
                if (weekInd <= 5) {
                    deliveryDays.add(weekInd);
                }

            } else if (RefCodeNames.SCHEDULE_DETAIL_CD.WEEK_DAY.equals(code) && this.scheduleRule == WEEK_MONTHLY) {

                this.weekDayInd = Integer.parseInt(value);

            } else if (RefCodeNames.SCHEDULE_DETAIL_CD.WEEK_DAY.equals(code) && this.scheduleRule == WEEKLY) {

                int weekDayInd = Integer.parseInt(value);
                logger.info("initSchedule()=> this.weekOfDel=" + this.weekOfDel);
                deliveryDays.add(weekDayInd);

            } else if (RefCodeNames.SCHEDULE_DETAIL_CD.MONTH_DAY.equals(code) && this.scheduleRule == DAY_MONTHLY) {

                int monthDayInd = Integer.parseInt(value);
                deliveryDays.add(monthDayInd);

            } else if (RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_DAY.equals(code)) {

                this.cutoffDay = Integer.parseInt(value);
                this.cutoffDay += this.accountCutoffDays;

            } else if (RefCodeNames.SCHEDULE_DETAIL_CD.CUTOFF_TIME.equals(code)) {

                this.cutoffTime = this.scheduleHeader.getScheduleTypeCd().equals(RefCodeNames.SCHEDULE_TYPE_CD.CORPORATE) ? timeFormat24hour.parse(value) : timeFormat.parse(value);

            } else if (RefCodeNames.SCHEDULE_DETAIL_CD.ALSO_DATE.equals(code)) {

                Date valueDate = dayFormat.parse(value);
                alsoDates.add(valueDate);

            } else if (RefCodeNames.SCHEDULE_DETAIL_CD.EXCEPT_DATE.equals(code)) {

                Date valueDate = dayFormat.parse(value);
                exceptDates.add(valueDate);

            } else if (RefCodeNames.SCHEDULE_DETAIL_CD.HOLIDAY.equals(code)) {

                Date valueDate = dayFormat.parse(value);
                holidayDates.add(valueDate);

            }
        }

        //Arrange arrays
        //delivery days
        this.deliveryDays = sortInt(deliveryDays);

        //dates
        this.alsoDates = sortDates(alsoDates);
        this.curAlsoInd = 0;

        this.exceptDates = sortDates(exceptDates);
        this.curExceptInd = 0;

        this.holidayDates = sortDates(holidayDates);
        this.curHolidayInd = 0;

        //return
        this.initialisedFlag = true;
    }


    //Account cutoff days
    public void setAccountCutoffDays(int pVal) {
        this.accountCutoffDays = pVal;
    }

    public int getAccountCutoffDays() {
        return this.accountCutoffDays;

    }

    //---------------------------------------------------------------------------
    //Get first date
    public GregorianCalendar getFirstDeliveryDate(Date pBegDate, Date pEndDate) throws Exception {
        calcFirstDeliveryDate(pBegDate, pEndDate);
        return this.curDeliveryCal;
    }

    //Get first date
    public GregorianCalendar getFirstDeliveryDate(Date pBegDate, Date pEndDate, boolean pMoveToEffDateFl) throws Exception {
        Date begDate = pBegDate;
        if (pMoveToEffDateFl) {
            if (this.effDate != null && this.effDate.after(pBegDate)) {
                begDate = this.effDate;
            }
        }
        calcFirstDeliveryDate(begDate, pEndDate);
        return this.curDeliveryCal;
    }

    //Get next date
    public GregorianCalendar getNextDeliveryDate() throws Exception {
        calcNextDeliveryDate();
        return this.curDeliveryCal;
    }

    //get order delivery date
    public GregorianCalendar getOrderDeliveryDate(Date pOrderDate, Date pOrderTime) throws Exception {
        calcOrderDeliveryDate(pOrderDate, pOrderTime);
        return this.curDeliveryCal;
    }

    //get order delivery pair
    public ScheduleOrderDatesView getFirstOrderDeliveryDates(Date pOrderDate, Date pOrderTime, boolean pMoveToEffDateFl) throws Exception {

        Date begDate = pOrderDate;
        Date begTime = pOrderTime;

        if (this.effDate != null && this.effDate.after(pOrderDate)) {
            begDate = this.effDate;
            begTime = timeFormat.parse("0:0 AM");
        }

        calcOrderDeliveryDate(begDate, begTime);

        if (this.curDeliveryCal == null) return null;

        Date cutoffDate = calcCutoffDate();

        return new ScheduleOrderDatesView(this.curDeliveryCal.getTime(), cutoffDate, null, null, null);
    }

    //get order delivery pair
    public ScheduleOrderDatesView getOrderDeliveryDates(Date pOrderDate, Date pOrderTime) throws Exception {

        calcOrderDeliveryDate(pOrderDate, pOrderTime);

        if (this.curDeliveryCal == null) {
            logger.info("getOrderDeliveryDates()=> ScheduleProc WE AREEE returning NULL");
            return null;
        }

        Date cutoffDate = calcCutoffDate();

        return new ScheduleOrderDatesView(this.curDeliveryCal.getTime(), cutoffDate, null, null, null);
    }

    //get order delivery pair
    public ScheduleOrderDatesView getNextOrderDeliveryDates() throws Exception {

        calcNextDeliveryDate();
        if (this.curDeliveryCal == null) return null;

        Date cutoffDate = calcCutoffDate();

        return new ScheduleOrderDatesView(this.curDeliveryCal.getTime(), cutoffDate, null, null, null);
    }

    //-----------------------------------------------------------------------------
    private Date calcCutoffDate() throws Exception {

        if (this.curDeliveryCal == null) {
            return null;
        }

        GregorianCalendar cutoffCal = (GregorianCalendar) this.curDeliveryCal.clone();
        if (this.cutoffDay == 0) {
            return cutoffCal.getTime();
        }

        mmm:
        for (int ii = 0; ii < this.cutoffDay; ) {

            int wd = cutoffCal.get(Calendar.DAY_OF_WEEK);
            Date dd = cutoffCal.getTime();

            if (wd == 1 || wd == 7) {
                cutoffCal.add(Calendar.DATE, -1);
                continue;
            }

            for (int holidayInd = this.curHolidayInd; holidayInd >= 0 && holidayInd < this.holidayDates.length; holidayInd--) {
                int cr = dd.compareTo(this.holidayDates[holidayInd]);
                if (cr == 0) {
                    cutoffCal.add(Calendar.DATE, -1);
                    continue mmm;
                }
                if (cr > 0) break;
            }

            for (int exceptInd = this.curExceptInd; exceptInd >= 0 && exceptInd < this.exceptDates.length; exceptInd--) {
                int cr = dd.compareTo(this.exceptDates[exceptInd]);
                if (cr == 0) {
                    cutoffCal.add(Calendar.DATE, -1);
                    continue mmm;
                }
                if (cr > 0) break;
            }

            cutoffCal.add(Calendar.DATE, -1);

            ii++;
        }

        //Scroll if not working day
        mmm1:
        while (true) {

            int wd = cutoffCal.get(Calendar.DAY_OF_WEEK);
            Date dd = cutoffCal.getTime();
            if (wd == 1 || wd == 7) {
                cutoffCal.add(Calendar.DATE, -1);
                continue;
            }

            for (int holidayInd = this.curHolidayInd; holidayInd >= 0 && holidayInd < this.holidayDates.length; holidayInd--) {
                int cr = dd.compareTo(this.holidayDates[holidayInd]);
                if (cr == 0) {
                    cutoffCal.add(Calendar.DATE, -1);
                    continue mmm1;
                }
                if (cr > 0) break;
            }

            for (int exceptInd = this.curExceptInd; exceptInd >= 0 && exceptInd < this.exceptDates.length; exceptInd--) {
                int cr = dd.compareTo(this.exceptDates[exceptInd]);
                if (cr == 0) {
                    cutoffCal.add(Calendar.DATE, -1);
                    continue mmm1;
                }
                if (cr > 0) break;
            }
            break;
        }

        return cutoffCal.getTime();
    }

    //-----------------------------------------------------------------------------
    private void calcOrderDeliveryDate(Date pOrderDate, Date pOrderTime) throws Exception {

        Date orderDate = dayFormat.parse(dayFormat.format(pOrderDate));
        Date orderTime = timeFormat.parse(timeFormat.format(pOrderTime));

        GregorianCalendar earliestDateCal = new GregorianCalendar();
        earliestDateCal.setTime(orderDate);

        if (orderTime.after(this.cutoffTime)) {
            earliestDateCal.add(Calendar.DATE, 1);
        }

        scrollHolidayDates(earliestDateCal.getTime());
        scrollExceptDates(earliestDateCal.getTime());

        for (int ii = 0; ii < this.cutoffDay; ) {
            int wd = earliestDateCal.get(Calendar.DAY_OF_WEEK);
            Date dd = earliestDateCal.getTime();
            if (wd == 1 || wd == 7 || isHolidayDate(dd) || isExceptDate(dd)) {
                earliestDateCal.add(Calendar.DATE, 1);
                continue;
            }
            earliestDateCal.add(Calendar.DATE, 1);
            ii++;
        }

        while (true) {

            int wd = earliestDateCal.get(Calendar.DAY_OF_WEEK);
            Date dd = earliestDateCal.getTime();

            if (wd == 1 || wd == 7 || isHolidayDate(dd) || isExceptDate(dd)) {
                earliestDateCal.add(Calendar.DATE, 1);
                continue;
            }

            break;
        }

        calcFirstDeliveryDate(earliestDateCal.getTime(), null);
    }

    //------------------------------------------------------------------------------
    private void calcFirstDeliveryDate(Date pBegDate, Date pEndDate) throws Exception {

        Date caltFirstDelD = (this.curDeliveryCal == null) ? null : this.curDeliveryCal.getTime();

        logger.info("calcFirstDeliveryDate()=> this.curDeliveryCal  in the beggining of calcFirstDeliveryDate:" + caltFirstDelD);
        logger.info("calcFirstDeliveryDate()=> this.pBegDate : " + pBegDate);
        logger.info("calcFirstDeliveryDate()=> pEndDate : " + pEndDate);
        logger.info("calcFirstDeliveryDate()=> this.effDate: " + this.effDate);

        if (pBegDate == null) {
            this.curDeliveryCal = null;
            return;
        }

        if (this.effDate != null && new Date().before(this.effDate)) {
            this.curDeliveryCal = null;
            return;
        } else {
            setupCal.setTime(pBegDate);
        }

        this.begCal =
                new GregorianCalendar(setupCal.get(Calendar.YEAR),
                        setupCal.get(Calendar.MONTH),
                        setupCal.get(Calendar.DAY_OF_MONTH));

        this.begCal.setFirstDayOfWeek(Calendar.SUNDAY);
        this.begTime = timeFormat.parse(timeFormat.format(setupCal.getTime()));

        if (pEndDate == null && this.exdate != null) {
            setupCal.setTime(this.exdate);
        } else if (pEndDate == null) {
            setupCal.add(Calendar.YEAR, 10);
        } else if (this.exdate != null && this.exdate.before(pEndDate)) {
            setupCal.setTime(this.exdate);
        } else {
            setupCal.setTime(pEndDate);
        }

        this.endCal =
                new GregorianCalendar(setupCal.get(Calendar.YEAR),
                        setupCal.get(Calendar.MONTH),
                        setupCal.get(Calendar.DAY_OF_MONTH));

        this.endCal.setFirstDayOfWeek(Calendar.SUNDAY);

        if (this.endCal.before(this.begCal)) {
            return;
        }

        //Setup exceptions
        Date begDate = this.begCal.getTime();
        scrollAlsoDates(begDate);
        scrollExceptDates(begDate);
        scrollHolidayDates(begDate);
        GregorianCalendar wrkCal = new GregorianCalendar();

        //Create schedule dates
        boolean foundFl = false;
        if (this.scheduleRule == WEEKLY) {

            int weekDay = this.begCal.get(Calendar.DAY_OF_WEEK);

            this.curBegWeekCal = (GregorianCalendar) this.begCal.clone();
            this.curBegWeekCal.add(Calendar.DATE, 1 - weekDay);

            mmm:
            for (int wkCount = 0; foundFl == false && wkCount <= 6; wkCount++) {

                logger.info("calcFirstDeliveryDate()=> foundFl:" + foundFl + " wkCount:" + wkCount);

                if (wkCount != 0) {
                    this.curBegWeekCal.add(Calendar.DATE, 7);
                }

                logger.info("calcFirstDeliveryDate()=> this.deliveryDays.length: " + this.deliveryDays.length);

                for (this.curCalendarIndex = 0; this.curCalendarIndex < this.deliveryDays.length; this.curCalendarIndex++) {
                    if (wkCount > 0 || this.deliveryDays[this.curCalendarIndex] >= weekDay) {
                        wrkCal = (GregorianCalendar) this.curBegWeekCal.clone();
                        wrkCal.add(Calendar.DATE, this.deliveryDays[this.curCalendarIndex] - 1);
                        foundFl = testScheduleDate(wrkCal);
                        if (foundFl) break mmm;
                    }
                }

            }

        } else if (this.scheduleRule == WEEK_MONTHLY) {
            //  this.weekDayInd

            this.firstWeekMonthCal = (GregorianCalendar) this.begCal.clone();
            int day = this.begCal.get(Calendar.DAY_OF_MONTH);
            this.firstWeekMonthCal.set(Calendar.DAY_OF_MONTH, 1);

            mmm:
            for (int mntCount = 0; mntCount < 3; mntCount++) {

                if (mntCount != 0) {
                    this.firstWeekMonthCal.set(Calendar.DAY_OF_MONTH, 1);
                    this.firstWeekMonthCal.add(Calendar.MONTH, 1);
                }

                int weekDay = this.firstWeekMonthCal.get(Calendar.DAY_OF_WEEK);
                int month = this.firstWeekMonthCal.get(Calendar.MONTH);
                if (weekDay < this.weekDayInd) this.firstWeekMonthCal.add(Calendar.DATE, this.weekDayInd - weekDay);
                if (weekDay > this.weekDayInd) this.firstWeekMonthCal.add(Calendar.DATE, 7 + (this.weekDayInd - weekDay));

                for (this.curCalendarIndex = 0; this.curCalendarIndex < this.deliveryDays.length; this.curCalendarIndex++) {
                    wrkCal = (GregorianCalendar) this.firstWeekMonthCal.clone();
                    int weekNum = this.deliveryDays[this.curCalendarIndex] - 1;
                    if (weekNum > 0) {
                        wrkCal.add(Calendar.DATE, weekNum * 7);
                    }
                    if (weekNum >= 4 && month != wrkCal.get(Calendar.MONTH)) {
                        wrkCal.add(Calendar.DATE, -7);
                    }
                    if (wrkCal.get(Calendar.DAY_OF_MONTH) < day) continue;
                    foundFl = testScheduleDate(wrkCal);
                    if (foundFl) break mmm;
                }
            }
        }

        caltFirstDelD = (this.curDeliveryCal == null) ? null : this.curDeliveryCal.getTime();
        logger.info("calcFirstDeliveryDate()=> this.curDeliveryCal  in the end of calcFirstDeliveryDate:" + caltFirstDelD);
        //Check also dates
        foundFl |= assignAlsoDate();
        //check the end of the interval
        if (foundFl) {
            if (this.curDeliveryCal.after(this.endCal)) {
                foundFl = false;
                this.curDeliveryCal = null;
            }
        } else {
            this.curDeliveryCal = null;
        }
        if (this.curDeliveryCal != null) {
            this.currMonth = this.curDeliveryCal.get(Calendar.MONTH);
        }

    }

    //Get next date
    private void calcNextDeliveryDate() throws Exception {

        if (this.curDeliveryCal == null) {
            return;
        }

        GregorianCalendar wrkCal = new GregorianCalendar();

        //Create schedule dates
        boolean foundFl = false;
        if (this.scheduleRule == WEEKLY) {
            if (!this.exceptionFlag) this.curCalendarIndex++;
            mmm:
            for (int wkCount = 0; wkCount < 50; wkCount++) {
                if (wkCount != 0) this.curBegWeekCal.add(Calendar.DATE, 7);
                for (; this.curCalendarIndex < this.deliveryDays.length; this.curCalendarIndex++) {
                    wrkCal = (GregorianCalendar) this.curBegWeekCal.clone();
                    wrkCal.add(Calendar.DATE, this.deliveryDays[this.curCalendarIndex] - 1);

                    if (this.currMonth == -1 || this.currMonth - wrkCal.get(Calendar.MONTH) != 0) {

                        this.currMonth = wrkCal.get(Calendar.MONTH);
                        initSiteDevileryWeekFilter();
                    }
                    foundFl = testScheduleDate(wrkCal);
                    if (foundFl) break mmm;
                }
                this.curCalendarIndex = 0;
            }
        } else if (this.scheduleRule == WEEK_MONTHLY) {
            if (!this.exceptionFlag) this.curCalendarIndex++;
            mmm:
            for (int mntCount = 0; mntCount < 3; mntCount++) {
                int month = this.firstWeekMonthCal.get(Calendar.MONTH);
                for (; this.curCalendarIndex < this.deliveryDays.length; this.curCalendarIndex++) {
                    wrkCal = (GregorianCalendar) this.firstWeekMonthCal.clone();
                    int weekNum = this.deliveryDays[this.curCalendarIndex] - 1;
                    if (weekNum > 0) {
                        wrkCal.add(Calendar.DATE, weekNum * 7);
                    }
                    if (weekNum >= 4 && month != wrkCal.get(Calendar.MONTH)) {
                        wrkCal.add(Calendar.DATE, -7);
                    }
                    foundFl = testScheduleDate(wrkCal);
                    if (foundFl) break mmm;
                }
                this.curCalendarIndex = 0;
                this.firstWeekMonthCal.set(Calendar.DAY_OF_MONTH, 1);
                this.firstWeekMonthCal.add(Calendar.MONTH, 1);
                int weekDay = this.firstWeekMonthCal.get(Calendar.DAY_OF_WEEK);
                if (weekDay < this.weekDayInd) this.firstWeekMonthCal.add(Calendar.DATE, this.weekDayInd - weekDay);
                if (weekDay > this.weekDayInd) this.firstWeekMonthCal.add(Calendar.DATE, 7 + (this.weekDayInd - weekDay));
            }
        } else {
            this.curDeliveryCal = null;
        }
        foundFl |= assignAlsoDate();
        //check the end of the interval
        if (foundFl) {
            if (this.curDeliveryCal.after(this.endCal)) {
                foundFl = false;
                this.curDeliveryCal = null;
            }
        } else {
            this.curDeliveryCal = null;
        }
        return;
    }

    private void initSiteDevileryWeekFilter() {

        this.siteDeliveryWeekFilter = new int[7][5];
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 5; j++)
                this.siteDeliveryWeekFilter[i][j] = -1;
    }

    private boolean testScheduleDate(GregorianCalendar pCal) {
        if (isWeekOk(pCal)) {
            if (isHolidayDate(pCal.getTime()) ||
                    isExceptDate(pCal.getTime())) {
                pCal = getDateSubst(pCal);
                if (pCal != null) {
                    this.curDeliveryCal = (GregorianCalendar) pCal.clone();
                    this.exceptionFlag = false;
                    return true;
                }
            } else {
                this.curDeliveryCal = (GregorianCalendar) pCal.clone();
                this.exceptionFlag = false;
                int thisWeek = this.curDeliveryCal.get(Calendar.WEEK_OF_YEAR);
                if (2 == this.cycle) {
                    if (this.scheduleRule == WEEKLY) {
                        long effMills = this.effDateWkBegCal.getTime().getTime();
                        long currMills = this.curDeliveryCal.getTime().getTime();
                        long diffWeeks = (currMills - effMills) / (1000 * 3600 * 24 * 7);
                        if ((diffWeeks % this.cycle) == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        this.curDeliveryCal = null;
        return false;
    }

    //----------------------------------------------------------------------------
    //Check also dates
    private boolean assignAlsoDate() {
        boolean assignedFl = false;
        GregorianCalendar wrkCal = new GregorianCalendar();
        while (this.curAlsoDate != null) {
            if (this.curDeliveryCal != null && !this.curAlsoDate.before(this.curDeliveryCal.getTime())) break;
            wrkCal.setTime(this.curAlsoDate);
            if (isWeekOk(wrkCal)) {
                this.curDeliveryCal = new GregorianCalendar();
                this.curDeliveryCal.setTime(this.curAlsoDate);
                this.exceptionFlag = true;
                nextAlsoDate();
                assignedFl = true;
                break;
            }
            nextAlsoDate();
        }
        return assignedFl;
    }

    //----------------------------------------------------------------------------
    private boolean isWeekOk(GregorianCalendar pCal) {
        if (isHolidayDate(pCal.getTime()) || isExceptDate(pCal.getTime())) {
            return false;
        }

        if (this.siteDeliverySchedule != null) {
            String intervalS = this.siteDeliverySchedule.getIntervWeek();
            int interval = 0;
            if (Utility.isSet(intervalS)) {
                try {
                    interval = Integer.parseInt(intervalS);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
            if (interval > 0) {
                long curMills = pCal.getTime().getTime();
                long begMills = this.effDateWkBegCal.getTime().getTime();
                long diffDays = (curMills - begMills) / (1000 * 60 * 60 * 24);
                long diffWeeks = diffDays / 7;
                long daysInWeek = (diffDays % 7) - 1;
                if ((diffWeeks % interval) == 0) {
                    return true;
                }

            }
        }
        if (!this.applyWeekFilter) return true;

        int day = pCal.get(Calendar.DAY_OF_MONTH);

        int week = 0;
        if (day <= 7) {
            week = 0;
        } else if (day <= 14) {
            week = 1;
        } else if (day <= 21) {
            week = 2;
        } else if (day <= 28) {
            week = 3;
        } else {
            week = 4;
        }

        if (this.weekFilter[week] >= 0) {
            return true;
        }

        if (this.weekFilter[4] >= 0) {
            int month = pCal.get(Calendar.MONTH);
            GregorianCalendar wrkCal = (GregorianCalendar) pCal.clone();
            wrkCal.add(Calendar.DATE, 7);
            if (month != wrkCal.get(Calendar.MONTH)) {
                return true;
            }
        }

        if (this.cycle == 2 && this.scheduleRule == WEEKLY) {
            if (week >= 1 && this.weekFilter[week - 1] >= 0) {
                return true;
            }
            if (week == 0 && this.weekFilter[4] >= 0) {
                return true;
            }
            if (week == 0 && this.weekFilter[3] >= 0) {
                GregorianCalendar wrkCal = (GregorianCalendar) pCal.clone();
                wrkCal.add(Calendar.DATE, -7);
                int dd = wrkCal.get(Calendar.DAY_OF_MONTH);
                if (dd <= 28) {
                    return true;
                }
            }
        }

        return false;

    }

    public boolean isSiteDevilery(int week, GregorianCalendar pCal) {

        int i = this.cycle.intValue();

        while (i <= week) {

            if (this.weekFilter[week - i] == 0 && this.siteDeliveryWeekFilter[pCal.get(Calendar.DAY_OF_WEEK) - 1][week - i] != 0) {
                if (!(isHolidayDate(pCal.getTime()) || isExceptDate(pCal.getTime()))) {
                    this.siteDeliveryWeekFilter[pCal.get(Calendar.DAY_OF_WEEK) - 1][week - i] = 0;
                    return true;
                }
            }

            i = i + this.cycle.intValue();
        }

        return false;
    }

    //---------------------------------------------------------------------------
    private GregorianCalendar getDateSubst(GregorianCalendar pCal) {
        return null;
    }

    //---------------------------------------------------------------------------
    private void scrollAlsoDates(Date earlyDate) {
        this.curAlsoDate = null;
        for (; this.curAlsoInd < this.alsoDates.length; this.curAlsoInd++) {
            if (!earlyDate.after(this.alsoDates[this.curAlsoInd])) {
                this.curAlsoDate = this.alsoDates[this.curAlsoInd];
                this.curAlsoInd++;
                break;
            }
        }
    }

    private void nextAlsoDate() {
        this.curAlsoDate = null;
        if (this.curAlsoInd < this.alsoDates.length) {
            this.curAlsoDate = this.alsoDates[this.curAlsoInd];
            this.curAlsoInd++;
        }
    }

    //---------------------------------------------------------------------------
    private boolean isExceptDate(Date pCurDate) {
        if (this.curExceptDate == null) return false;
        if (this.curExceptDate.before(pCurDate)) {
            scrollExceptDates(pCurDate);
        }
        return this.curExceptDate != null && this.curExceptDate.equals(pCurDate);
    }

    //---------------------------------------------------------------------------
    private void scrollExceptDates(Date earlyDate) {
        if (this.curExceptInd == 0) this.curExceptDate = null;
        for (; this.curExceptInd < this.exceptDates.length; this.curExceptInd++) {
            if (!earlyDate.after(this.exceptDates[this.curExceptInd])) {
                this.curExceptDate = this.exceptDates[this.curExceptInd];
                this.curExceptInd++;
                break;
            }
        }
    }


    private boolean isHolidayDate(Date pCurDate) {
        if (this.curHolidayDate == null) return false;
        if (this.curHolidayDate.before(pCurDate)) {
            scrollHolidayDates(pCurDate);
        }
        return this.curHolidayDate != null && this.curHolidayDate.equals(pCurDate);
    }


    private void scrollHolidayDates(Date earlyDate) {
        this.curHolidayDate = null;
        for (; this.curHolidayInd < this.holidayDates.length; this.curHolidayInd++) {
            if (!earlyDate.after(this.holidayDates[this.curHolidayInd])) {
                this.curHolidayDate = this.holidayDates[this.curHolidayInd];
                break;
            }
        }
    }

    //---------------------------------------------------------------------------
    private Date[] sortDates(List<Date> dates) {

        if (dates == null) {
            return new Date[0];
        }

        Date[] dateA = new Date[dates.size()];
        for (int i = 0; i < dateA.length; i++) {
            dateA[i] = dates.get(i);
        }

        if (dateA.length <= 1) {
            return dateA;
        }

        for (int i = 0; i < dateA.length - 1; i++) {

            boolean endFl = true;

            for (int j = 0; j < dateA.length - i - 1; j++) {
                Date dd1 = dateA[j];
                Date dd2 = dateA[j + 1];
                if (dd1.after(dd2)) {
                    endFl = false;
                    dateA[j] = dd2;
                    dateA[j + 1] = dd1;
                }
            }

            if (endFl) {
                break;
            }
        }

        return dateA;
    }

    private int[] sortInt(List<Integer> pValues) {
        if (pValues == null) return new int[0];
        int[] intA = new int[pValues.size()];
        for (int ii = 0; ii < intA.length; ii++) {
            Integer valI = pValues.get(ii);
            intA[ii] = valI.intValue();
        }
        if (intA.length <= 1) return intA;
        for (int ii = 0; ii < intA.length - 1; ii++) {
            boolean endFl = true;
            for (int jj = 0; jj < intA.length - ii - 1; jj++) {
                int dd1 = intA[jj];
                int dd2 = intA[jj + 1];
                if (dd1 > dd2) {
                    endFl = false;
                    intA[jj] = dd2;
                    intA[jj + 1] = dd1;
                }
            }
            if (endFl) break;
        }
        return intA;
    }

    public Date getCutoffTime() {
        return this.cutoffTime;
    }

}
