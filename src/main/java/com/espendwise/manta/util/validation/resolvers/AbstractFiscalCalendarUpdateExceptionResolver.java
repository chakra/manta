package com.espendwise.manta.util.validation.resolvers;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.dao.FiscalCalendarDAOImpl;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationException;

public abstract class AbstractFiscalCalendarUpdateExceptionResolver implements ValidationResolver<ValidationException> {
	
	private static final Logger logger = Logger.getLogger(FiscalCalendarDAOImpl.class);


    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {
            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.FiscalCalendarUpdateReason) {

                    switch ((ExceptionReason.FiscalCalendarUpdateReason) code.getReason()) {
                        case YEAR_UPDATE_NOT_ALLOWED:
                            errors.add(isYearUpdateNotAllowed(code, (Integer) code.getArguments()[0].get(), (Integer) code.getArguments()[1].get()));
                            break;
                        case YEAR_OF_ZERO_RULE:
                            errors.add(isYearOfZeroRule(code, (Integer)code.getArguments()[0].get()));
                            break;
                        case WRONG_YEAR_VALUE:
                            errors.add(isWrongYearValue(code, (Integer) code.getArguments()[0].get()));
                            break;
                        case WRONG_YEAR_VALUE_1:
                            errors.add(isWrongYearValueOne(code, (Integer) code.getArguments()[0].get()));
                            break;
                        case EXCEPTION_WHEN_CHECK_WRONG_YEAR_VALUE_1:
                            errors.add(isExcWhenCheckWrongYearValueOne(code, (Integer) code.getArguments()[0].get()));
                            break;
                        case LAST_PERIOD_RULE:
                            errors.add(isLastPeriodRule(code, (Date) code.getArguments()[0].get()));
                            break;
                        case OUT_OF_RANGE_PERIODS:
                            errors.add(isOutOfRangePeriods(code, (String) code.getArguments()[0].get()));
                            break;
                        case OUT_OF_SEQ_PERIODS:
                            errors.add(isOutOfSeqPeriods(code, (String) code.getArguments()[0].get()));
                            break;
                        case DUPLICATED_EFF_DATE:
                            errors.add(isDuplicatedEffDate(code, (Integer) code.getArguments()[0].get(), (Date) code.getArguments()[1].get()));
                            break;
                        case GAP:
                            errors.add(isGap(code, (Integer) code.getArguments()[0].get()));
                            break;
                        case OVERLAP:
                            errors.add(isOverlap(code, (Integer) code.getArguments()[0].get()));
                            break;
                        case YEAR_EXIST:
                            errors.add(isYearExist(code, (Integer) code.getArguments()[0].get()));
                            break;
                        case WRONG_DATE_INTERVAL:
                            errors.add(isWrongDateInterval(code, (Date) code.getArguments()[0].get(), (Date) code.getArguments()[1].get()));
                            break;
                        case FIRST_PERIOD_RULE:
                            errors.add(isFirstPeriodRule(code));
                            break;
                        case YEAR_OF_ZERO_FIRST_PERIOD_START_RULE:
                            errors.add(isYZFirstPeriodStartRule(code));
                            break;
                        case YEAR_OF_ZERO_LAST_PERIOD_START_RULE:
                            errors.add(isYZLastPeriodStartRule(code));
                            break;
                        case YEAR_OF_ZERO_PERIOD_EXEED_YEAR_END:
                            errors.add(isYZPeriodExeedYearEnd(code, (Integer) code.getArguments()[0].get()));
                            break;
                        case YEAR_OF_ZERO_CALENDAR_EXIST:
                            errors.add(isYZCalendarExist(code));
                            break;
                        case CALENDAR_INTERSECT:
                            errors.add(isCalendarIntersect(code, (String) code.getArguments()[0].get(), (String) code.getArguments()[1].get(), (String) code.getArguments()[2].get(), (String) code.getArguments()[3].get()));
                            break;                            
                    }

                }
            }
        }

        return errors;
    }
    
    protected abstract ArgumentedMessage isCalendarIntersect(ApplicationExceptionCode code, String fiscalYear1, String effDate1, String fiscalYear2, String effDate2);

    protected abstract ArgumentedMessage isFirstPeriodRule(ApplicationExceptionCode code);

    protected abstract ArgumentedMessage isWrongDateInterval(ApplicationExceptionCode code, Date effDate, Date expDate);

    protected abstract ArgumentedMessage isYearExist(ApplicationExceptionCode code, Integer fiscalYear);

    protected abstract ArgumentedMessage isOverlap(ApplicationExceptionCode code, Integer fiscalYear);

    protected abstract ArgumentedMessage isGap(ApplicationExceptionCode code, Integer fiscalYear);

    protected abstract ArgumentedMessage isDuplicatedEffDate(ApplicationExceptionCode code, Integer fiscalYear, Date effDate);

    protected abstract ArgumentedMessage isOutOfSeqPeriods(ApplicationExceptionCode code, String periods);

    protected abstract ArgumentedMessage isOutOfRangePeriods(ApplicationExceptionCode code, String periods);

    protected abstract ArgumentedMessage isLastPeriodRule(ApplicationExceptionCode code, Date period);

    protected abstract ArgumentedMessage isExcWhenCheckWrongYearValueOne(ApplicationExceptionCode code, Integer fiscalYear);

    protected abstract ArgumentedMessage isWrongYearValueOne(ApplicationExceptionCode code, Integer fiscalYear);

    protected abstract ArgumentedMessage isWrongYearValue(ApplicationExceptionCode code, Integer fiscalYear);

    protected abstract ArgumentedMessage isYearOfZeroRule(ApplicationExceptionCode code, Integer fiscalYear);

    protected abstract ArgumentedMessage isYearUpdateNotAllowed(ApplicationExceptionCode code, Integer fiscalYear, Integer currentYear);
    
    protected abstract ArgumentedMessage isYZFirstPeriodStartRule(ApplicationExceptionCode code);
    
    protected abstract ArgumentedMessage isYZLastPeriodStartRule(ApplicationExceptionCode code);
    
    protected abstract ArgumentedMessage isYZPeriodExeedYearEnd(ApplicationExceptionCode code, Integer periodNumber);
    
    protected abstract ArgumentedMessage isYZCalendarExist(ApplicationExceptionCode code);

}

