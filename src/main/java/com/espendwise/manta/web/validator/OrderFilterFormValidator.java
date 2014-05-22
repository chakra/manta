package com.espendwise.manta.web.validator;


import java.util.List;

import org.apache.log4j.Logger;

import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.arguments.NumberArgument;
import com.espendwise.manta.util.validation.CodeValidationResult;
import com.espendwise.manta.util.validation.DateValidator;
import com.espendwise.manta.util.validation.TextValidator;
import com.espendwise.manta.util.validation.ValidationResult;
import com.espendwise.manta.util.validation.Validators;
import com.espendwise.manta.web.forms.OrderFilterForm;
import com.espendwise.manta.web.resolver.DateErrorWebResolver;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.AppI18nUtil;
import com.espendwise.manta.web.util.WebErrors;
import java.util.Date;

public class OrderFilterFormValidator extends AbstractFormValidator {

    private static final Logger logger = Logger.getLogger(OrderFilterFormValidator.class);

    public OrderFilterFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        OrderFilterForm valueObj = (OrderFilterForm) obj;

        ValidationResult vr;
        
        String datePattern = AppI18nUtil.getDatePattern();
        String datePatternPrompt = AppI18nUtil.getDatePatternPrompt();
        Date fromDate = null;
        Date toDate = null;
        if (Utility.isSetIgnorePattern(valueObj.getOrderFromDate(), datePatternPrompt)) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            vr = dateValidator.validate(valueObj.getOrderFromDate(), new DateErrorWebResolver("admin.order.label.orderFromDate", "Order \"From\" Date"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            fromDate = dateValidator.getParsedDate();
        }

        if (Utility.isSetIgnorePattern(valueObj.getOrderToDate(), datePatternPrompt)) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern);
            vr = dateValidator.validate(valueObj.getOrderToDate(), new DateErrorWebResolver("admin.order.label.orderToDate", "Order \"To\" Date"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
            toDate = dateValidator.getParsedDate();
        }
        
        if (fromDate != null && toDate != null) {
            DateValidator dateValidator = Validators.getDateValidator(datePattern, fromDate, toDate);
            vr = dateValidator.checkRanges(new DateErrorWebResolver("admin.order.label.orderFromDate", "Order \"From\" Date",
                                                                    "admin.order.label.orderToDate", "Order \"To\" Date"));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }

        if (Utility.isSet(valueObj.getAccountFilter())) {
            try {
                List<Long> values = Utility.splitLong(valueObj.getAccountFilter());
                if (values != null && values.size() > Constants.FILTER_RESULT_LIMIT.ACCOUNT) {
                    errors.putError(
                            "validation.web.error.maximumFilterSize",
                            new MessageI18nArgument("admin.global.filter.label.filterByAccounts"),
                            new NumberArgument(Constants.FILTER_RESULT_LIMIT.ACCOUNT)
                    );
                }
            } catch (Exception e) {
                errors.putError("validation.web.error.wrongFilterFormat", new MessageI18nArgument("admin.global.filter.label.filterByAccounts"));
            }
        }
        
        if (Utility.isSet(valueObj.getDistributorFilter())) {
            try {
                List<Long> values = Utility.splitLong(valueObj.getDistributorFilter());
                if (values != null && values.size() > Constants.FILTER_RESULT_LIMIT.DISTRIBUTOR) {
                    errors.putError(
                            "validation.web.error.maximumFilterSize",
                            new MessageI18nArgument("admin.global.filter.label.distributors"),
                            new NumberArgument(Constants.FILTER_RESULT_LIMIT.DISTRIBUTOR)
                    );
                }
            } catch (Exception e) {
                errors.putError("validation.web.error.wrongFilterFormat", new MessageI18nArgument("admin.global.filter.label.distributors"));
            }
        }

        if (Utility.isSet(valueObj.getUserFilter())) {
            try {
                List<Long> values = Utility.splitLong(valueObj.getUserFilter());
                if (values != null && values.size() > Constants.FILTER_RESULT_LIMIT.USER) {
                    errors.putError(
                            "validation.web.error.maximumFilterSize",
                            new MessageI18nArgument("admin.global.filter.label.users"),
                            new NumberArgument(Constants.FILTER_RESULT_LIMIT.USER)
                    );
                }
            } catch (Exception e) {
                errors.putError("validation.web.error.wrongFilterFormat", new MessageI18nArgument("admin.global.filter.label.users"));
            }
        }
        
        if (Utility.isSet(valueObj.getSiteZipCode())) {
            TextValidator validator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.SHORT_DB_CODE_LENGTH);
            vr = validator.validate(valueObj.getSiteZipCode(), new TextErrorWebResolver("admin.global.filter.label.postalCode", null));
            if (vr != null) {
                errors.putErrors(vr.getResult());
            }
        }
        
        return new MessageValidationResult(errors.get());
    }

}
