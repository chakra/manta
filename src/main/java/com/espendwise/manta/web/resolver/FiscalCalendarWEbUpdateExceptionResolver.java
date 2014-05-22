package com.espendwise.manta.web.resolver;


import java.util.Date;

import org.apache.log4j.Logger;

import com.espendwise.manta.dao.FiscalCalendarDAOImpl;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.resolvers.AbstractFiscalCalendarUpdateExceptionResolver;

public class FiscalCalendarWEbUpdateExceptionResolver extends AbstractFiscalCalendarUpdateExceptionResolver {
	
	private static final Logger logger = Logger.getLogger(FiscalCalendarDAOImpl.class);

    @Override
    protected ArgumentedMessage isFirstPeriodRule(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.firstPeriodRule", Args.i18nTyped("admin.account.fiscalCalendar.label.effDate"));
    }

    @Override
    protected ArgumentedMessage isWrongDateInterval(ApplicationExceptionCode code, Date effDate, Date expDate) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarWrongDateInterval", Args.i18nTyped("admin.account.fiscalCalendar.label.effDate", "admin.account.fiscalCalendar.label.expDate"));
    }

    @Override
    protected ArgumentedMessage isYearExist(ApplicationExceptionCode code, Integer fiscalYear) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarYearExists", Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear));
    }

    @Override
    protected ArgumentedMessage isOverlap(ApplicationExceptionCode code, Integer fiscalYear) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarOverlap", Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear));
    }

    @Override
    protected ArgumentedMessage isGap(ApplicationExceptionCode code, Integer fiscalYear) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarGap", Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear));
    }

    @Override
    protected ArgumentedMessage isDuplicatedEffDate(ApplicationExceptionCode code, Integer fiscalYear, Date effDate) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarDuplicateEffDate"
                , Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear, effDate));
    }
    
    @Override
    protected ArgumentedMessage isCalendarIntersect(ApplicationExceptionCode code, String fiscalYear1, String effDate1, String fiscalYear2, String effDate2) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarIntersect", Args.i18nTyped(fiscalYear1.equals("0") ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear1, 
        		effDate1, fiscalYear2.equals("0") ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear2, effDate2));
    }    

    @Override
    protected ArgumentedMessage isOutOfSeqPeriods(ApplicationExceptionCode code, String periods) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarOutOfSeqPeriods", Args.i18nTyped(periods));
    }

    @Override
    protected ArgumentedMessage isOutOfRangePeriods(ApplicationExceptionCode code, String periods) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarOutOfRangePeriods", Args.i18nTyped(periods));
    }

    @Override
    protected ArgumentedMessage isLastPeriodRule(ApplicationExceptionCode code, Date period) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarLastPeriodRule", Args.i18nTyped(period));
    }

    @Override
    protected ArgumentedMessage isExcWhenCheckWrongYearValueOne(ApplicationExceptionCode code, Integer fiscalYear) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarExcWhenCheckWrongYearValue1", Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear));
    }

    @Override
    protected ArgumentedMessage isWrongYearValueOne(ApplicationExceptionCode code, Integer fiscalYear) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarWrongYearValue1", Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear));
    }

    @Override
    protected ArgumentedMessage isWrongYearValue(ApplicationExceptionCode code, Integer fiscalYear) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarWrongYearValue", Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear));
    }

    @Override
    protected ArgumentedMessage isYearOfZeroRule(ApplicationExceptionCode code, Integer fiscalYear) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarYearOfZeroRule"
                , Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear));
    }

    @Override
    protected ArgumentedMessage isYearUpdateNotAllowed(ApplicationExceptionCode code, Integer fiscalYear, Integer currentYear) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarYearUpdateNotAllowed", Args.i18nTyped(Utility.intNN(fiscalYear) == 0 ? new MessageI18nArgument("admin.account.fiscalCalendar.all") : fiscalYear, currentYear));
    }
    
    @Override
    protected ArgumentedMessage isYZFirstPeriodStartRule(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarZeroYearFirstPeriodRule");
    }
    
    @Override
    protected ArgumentedMessage isYZLastPeriodStartRule(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarZeroYearLastPeriodRule");
    }
    
    @Override
    protected ArgumentedMessage isYZPeriodExeedYearEnd(ApplicationExceptionCode code, Integer periodNumber) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarZeroYearPeriodExeedYearEndRule", Args.i18nTyped(periodNumber));
    }
    
    @Override
    protected ArgumentedMessage isYZCalendarExist(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.fiscalCalendarZeroYearCalendarAlreadyExist");
    }

}
