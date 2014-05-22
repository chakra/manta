package com.espendwise.manta.web.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.FiscalCalendarUtility;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.IntegerValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.AccountFiscalCalendarForm;
import com.espendwise.manta.web.forms.FiscalCalendarForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.NumberErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebError;
import com.espendwise.manta.web.util.WebErrors;


public class FiscalCalendarFormValidator extends AbstractFormValidator {

    private static final Logger logger = Logger.getLogger(FiscalCalendarFormValidator.class);

    @Override
    public ValidationResult validate(Object obj) {

        logger.info("validate()=> BEGIN");

        WebErrors errors = new WebErrors();
        ValidationResult vr;

        AccountFiscalCalendarForm form = (AccountFiscalCalendarForm) obj;

        FiscalCalendarForm calendar = form.getCalendarToEdit();

        DateValidator dateValidator = Validators.getDateValidator(AppI18nUtil.getDatePattern());
        vr = dateValidator.validate(getDateValue(calendar.getEffDate()), new DateErrorWebResolver("admin.account.fiscalCalendar.label.effDate"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        Date effDate = dateValidator.getParsedDate();

        Date expDate = null;
        if (form.getIsServiceScheduleCalendar()) {

            dateValidator = Validators.getDateValidator(AppI18nUtil.getDatePattern());
            vr = dateValidator.validate(getDateValue(calendar.getExpDate()), new DateErrorWebResolver("admin.account.fiscalCalendar.label.expDate"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }

            expDate = dateValidator.getParsedDate();

        }


        String fiscalYearInput = Utility.strNN(calendar.getFiscalYear()).equalsIgnoreCase(AppI18nUtil.getMessage("admin.account.fiscalCalendar.all"))
                ? String.valueOf(0)
                : calendar.getFiscalYear();


        IntegerValidator fiscalYearValidator = Validators.getIntegerValidator();
        vr = fiscalYearValidator.validate(fiscalYearInput, new NumberErrorWebResolver("admin.account.fiscalCalendar.label.fiscalYear"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        Integer fiscalYear = fiscalYearValidator.getParsedValue();

        logger.info("validate()=> effDate: "+effDate+", expDate: "+expDate+", fiscalYear: "+fiscalYear);

        if (fiscalYear != null && effDate != null) {

            if (IsYearValid(fiscalYear)) {

                if (Utility.isSet(calendar.getPeriods()) && fiscalYear >= 0) {

                    List<Integer> errorPeriods = checkPeriodFormat(form, effDate);
                    if (Utility.isSet(errorPeriods)) {
                        errors.putError(
                                new WebError(
                                        "validation.web.error.wrongArrayPeriodFormat",
                                        new StringI18nArgument("admin.account.fiscalCalendar.label.periodStartDate"),
                                        new StringArgument( Utility.toCommaString(errorPeriods))
                                )
                        );
                    } else {
                    }
                }
            } else {

                errors.putError(
                        new WebError(
                                "validation.web.error.wrongYearRange",
                                Args.i18nTyped(
                                        "admin.account.fiscalCalendar.label.fiscalYear",
                                        Constants.VALIDATION_FIELD_CRITERIA.MIN_YEAR,
                                        Constants.VALIDATION_FIELD_CRITERIA.MAX_YEAR
                                )
                        )
                );

            }
        }

        logger.info("validate()=> END, errors.size: " + errors.size());

        return new MessageValidationResult(errors.get());

    }

    private List<Integer> checkPeriodNotEmpty(AccountFiscalCalendarForm form) {

        Set<Integer> errorPeriods = new HashSet<Integer>();

        FiscalCalendarForm calendar = form.getCalendarToEdit();

        logger.info("checkPeriodNotEmpty()=> calendar.getPeriods(): "+calendar.getPeriods());

        for (int i = calendar.getPeriods().firstKey(); i < calendar.getPeriods().size(); i++) {
            if (!Utility.isSet(calendar.getPeriods().get(i))) {
                errorPeriods.add(i);
            }
        }

        if (!calendar.getIsServiceScheduleCalendar() || form.isNew()) {

            String lastMmddStr = calendar.getPeriods().get(calendar.getPeriods().lastKey());
            if (calendar.getPeriods().size() > 1 && !Utility.isSet(lastMmddStr)) {
                errorPeriods.add(calendar.getPeriods().lastKey());
            }
        }


        return new ArrayList(errorPeriods);
    }

    private List<Integer> checkPeriodFormat(AccountFiscalCalendarForm form, Date effDate) {

        Set<Integer> errorPeriods = new HashSet<Integer>();

        FiscalCalendarForm calendar = form.getCalendarToEdit();
        Integer  year = FiscalCalendarUtility.getYearForDate(effDate);

        logger.info("checkPeriodFormat()=> calendar.getPeriods().size(): "+calendar.getPeriods().size());

        String prevMMDD = null;
        for (int i = 13; i <= calendar.getPeriods().size(); i++) {
            String mmDd = calendar.getPeriods().get(i);
            if (Utility.isSet(mmDd)) {
                if (i>13 && prevMMDD == null) {
                    errorPeriods.add(i - 1);
                }
                prevMMDD = mmDd;
            }
        }
        if (Utility.isSet(errorPeriods)) {
            return new ArrayList(errorPeriods);
        }

        Date prevDate = null;
        for (int i = calendar.getPeriods().firstKey(); i < calendar.getPeriods().size(); i++) {
            String mmddStr = calendar.getPeriods().get(i);
            if (!Utility.isSet(mmddStr)) {
                errorPeriods.add(i);
                continue;
            }
            Date mmddDate = AppI18nUtil.parseMonthWithDay(mmddStr, year, true);
            if (!Utility.isSet(mmddDate)) {
                errorPeriods.add(i);
            } else if (prevDate != null && mmddDate.after(prevDate)) {
                year = year + 1;
            }
            prevDate = mmddDate;
        }

        if (!calendar.getIsServiceScheduleCalendar() || form.isNew()) {

            String lastMmddStr = calendar.getPeriods().get(calendar.getPeriods().lastKey());
            if (calendar.getPeriods().size() > 1 && !Utility.isSet(lastMmddStr)) {
                errorPeriods.add(calendar.getPeriods().lastKey());
            }
        }

        String lastMmddStr = calendar.getPeriods().get(calendar.getPeriods().lastKey());
        if (Utility.isSet(lastMmddStr)) {
            Date mmddDate = AppI18nUtil.parseMonthWithDay(lastMmddStr, year, true);
            if (!Utility.isSet(mmddDate)) {
                errorPeriods.add(calendar.getPeriods().lastKey());
            }
        }

        return new ArrayList(errorPeriods);

    }

    private boolean IsYearValid(Integer year) {
        return year == 0
                || !(year < Constants.VALIDATION_FIELD_CRITERIA.MIN_YEAR
                || year > Constants.VALIDATION_FIELD_CRITERIA.MAX_YEAR);

    }


    private String getDateValue(String date) {
        return Utility.ignorePattern(
                date,
                AppI18nUtil.getDatePatternPrompt()
        );
    }

}
