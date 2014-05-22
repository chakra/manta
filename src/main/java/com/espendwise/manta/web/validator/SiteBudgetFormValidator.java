package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.arguments.StringArgument;
import com.espendwise.manta.util.arguments.StringI18nArgument;
import com.espendwise.manta.util.arguments.resolvers.AbstractNumberResolver;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.web.forms.BudgetDisplayForm;
import com.espendwise.manta.web.forms.BudgetPeriodAmountForm;
import com.espendwise.manta.web.forms.CostCenterBudgetDisplayForm;
import com.espendwise.manta.web.forms.SiteBudgetForm;
import com.espendwise.manta.web.util.SiteBudgetFormUtil;
import com.espendwise.manta.web.util.WebErrors;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SiteBudgetFormValidator extends AbstractFormValidator {

    private static final Logger logger = Logger.getLogger(SiteBudgetFormValidator.class);

    public SiteBudgetFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        logger.info("validate()=> BEGIN");

        WebErrors errors = new WebErrors();

        SiteBudgetForm valueObj = (SiteBudgetForm) obj;

        if(checkForBudgetErrors(valueObj.getCostCenterBudget())){

        SortedMap<String, CosCenterProblem> wrongCostCenterPeriods = determineWrongPeriods(valueObj);


        if (valueObj.getUsedSiteBudgetThreshold()) {
            wrongCostCenterPeriods = determineWrongThreshold(valueObj,
                    wrongCostCenterPeriods
            );
        }

        if (Utility.isSet(wrongCostCenterPeriods)) {

            for (Map.Entry<String, CosCenterProblem> e : wrongCostCenterPeriods.entrySet()) {

                if (Utility.isSet(e.getValue())) {

                    if (Utility.isSet(e.getValue().getWrongPeriods())) {

                        errors.putError(
                                "validation.web.error.wrongLocationBudgetAmounts",
                                new StringArgument(e.getKey()),
                                new StringI18nArgument("admin.site.budgets.label.amount"),
                                new StringArgument(SiteBudgetFormUtil.wrongValuesCommaString(e.getValue().getWrongPeriods(), e.getValue().getWrongValues()))
                        );
                    }

                    if (e.getValue().isWrongThreshold()) {
                        errors.putError(
                                "validation.web.error.wrongBudgetThreshold",
                                 new StringArgument(e.getKey()),
                                new StringI18nArgument("admin.site.budgets.label.threshold")
                        );
                    }
                }
            }
        }

        }  else {

            errors.putError("validation.web.error.wrongBudgetTypesForSite" );

        }

        logger.info("validate()=> END.");

        return new MessageValidationResult(errors.get());

    }

    private SortedMap<String, CosCenterProblem> determineWrongThreshold(SiteBudgetForm valueObj, SortedMap<String, CosCenterProblem> wrongCostCenterPeriods) {

        if (Utility.isSet(valueObj.getCostCenterBudget())) {

            for (CostCenterBudgetDisplayForm costCenterBudget : valueObj.getCostCenterBudget().values()) {

                if (Utility.isSet(costCenterBudget.getBudgets())) {

                    BudgetDisplayForm budget = costCenterBudget.getBudgets().get(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);

                    if (budget != null) {


                        if (Utility.isSet(budget.getThresholdPercent())) {

                            Inner3CodeNumberRelover resolver = new Inner3CodeNumberRelover();

                            PercentIntValidator validator = Validators.getPercentIntValidator();
                            CodeValidationResult vr = validator.validate(budget.getThresholdPercent(), resolver);

                            if (vr != null) {

                                vr.getResult();

                                if (resolver.isInvalidNumberFormat() || resolver.isInvalidPositive()) {
                                    addErrorThreshold(wrongCostCenterPeriods, costCenterBudget.getCostCenterName());
                                }
                            }
                        }


                    }

                }
            }
        }

        return wrongCostCenterPeriods;

    }

    private SortedMap<String, CosCenterProblem> determineWrongPeriods(SiteBudgetForm valueObj) {

        SortedMap<String, CosCenterProblem> wrongCostCenterPeriods = new TreeMap<String, CosCenterProblem>();

        if (Utility.isSet(valueObj.getCostCenterBudget())) {

            for (CostCenterBudgetDisplayForm costCenterBudget : valueObj.getCostCenterBudget().values()) {

                if (Utility.isSet(costCenterBudget.getBudgets())) {

                    BudgetDisplayForm budget = costCenterBudget.getBudgets().get(RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET);

                    if (budget != null) {

                        for (BudgetPeriodAmountForm period : budget.getPeriodAmount().values()) {

                            if (Utility.isSet(period.getInputForm())) {

                                Inner3CodeNumberRelover resolver = new Inner3CodeNumberRelover();

                                AmountValidator validator = Validators.getAmountValidator(20, 8);
                                CodeValidationResult vr = validator.validate(period.getInputForm(), resolver);

                                if (vr != null) {

                                    vr.getResult();

                                    if (resolver.isInvalidNumberFormat() ||
                                        resolver.isInvalidPositive() ||
                                        resolver.isRangeOut()) {
                                        addErrorPeriod(wrongCostCenterPeriods, costCenterBudget.getCostCenterName(), period.getPeriod(), period.getInputForm());
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

        return wrongCostCenterPeriods;
    }


    private static boolean checkForBudgetErrors(Map<Long, CostCenterBudgetDisplayForm> costCenterBudgets) {

        boolean isSiteBudgetSet = false;
        boolean isAccountBudgetSet = false;

        if (Utility.isSet(costCenterBudgets)) {
            for (CostCenterBudgetDisplayForm costCenterBudget : costCenterBudgets.values()) {
                if (RefCodeNames.BUDGET_TYPE_CD.SITE_BUDGET.equals(costCenterBudget.getCostCenterType())) {
                    isSiteBudgetSet = true;
                } else if (RefCodeNames.BUDGET_TYPE_CD.ACCOUNT_BUDGET.equals(costCenterBudget.getCostCenterType())) {
                    isAccountBudgetSet = true;
                }
            }
        }


        return !(isSiteBudgetSet && isAccountBudgetSet);
    }

    private void addErrorPeriod(SortedMap<String, CosCenterProblem> wrongCostCenterPeriods, String costCenterName, Integer period, String value) {
        CosCenterProblem l = wrongCostCenterPeriods.get(costCenterName);
        if (l == null) {
            l = new CosCenterProblem();
            wrongCostCenterPeriods.put(costCenterName, l);
        }
        l.getWrongPeriods().add(period);
        l.getWrongValues().add(value);
    }

    private void addErrorThreshold(SortedMap<String, CosCenterProblem> wrongCostCenterPeriods, String costCenterName) {
        CosCenterProblem l = wrongCostCenterPeriods.get(costCenterName);
        if (l == null) {
            l = new CosCenterProblem();
            wrongCostCenterPeriods.put(costCenterName, l);
        }
        l.setWrongThreshold(true);
    }

    private class Inner3CodeNumberRelover extends AbstractNumberResolver {

        private boolean rangeOut;
        private boolean invalidNumberFormat;
        private boolean invalidPositive;
        private boolean notSet;

        @Override
        public ArgumentedMessage isInvalidNumberFormat(ValidationCode code) throws ValidationException {
            invalidNumberFormat = true;
            return null;
        }

        @Override
        public ArgumentedMessage isInvalidPositive(ValidationCode code) throws ValidationException {
            invalidPositive = true;
            return null;
        }

        @Override
        public ArgumentedMessage isNotSet(ValidationCode code) throws ValidationException {
            notSet = true;
            return null;
        }
        
        @Override
        public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {

            List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

            for(ValidationCode code : codes) {
                switch (code.getReason()) {
                    case    VALUE_IS_NOT_SET  : errors.add(isNotSet(code));                 break;
                    case    INVALID_POSITIVE_VALUE : errors.add(isInvalidPositive(code));   break;
                    case    WRONG_NUMBER_FORMAT: errors.add(isInvalidNumberFormat(code));   break;
                    case    RANGE_OUT: errors.add(isRangeOut(code));                        break;
                    default: break;
                }
            }

            return errors;
        }

        public boolean isInvalidNumberFormat() {
            return invalidNumberFormat;
        }

        public void setInvalidNumberFormat(boolean invalidNumberFormat) {
            this.invalidNumberFormat = invalidNumberFormat;
        }

        public boolean isInvalidPositive() {
            return invalidPositive;
        }

        public void setInvalidPositive(boolean invalidPositive) {
            this.invalidPositive = invalidPositive;
        }

        public boolean isNotSet() {
            return notSet;
        }

        public void setNotSet(boolean notSet) {
            this.notSet = notSet;
        }

        public boolean isRangeOut() {
            return rangeOut;
        }
        
        public ArgumentedMessage isRangeOut(ValidationCode code) throws ValidationException {
            rangeOut = true;
            return null;
        }

        public void setRangeOut(boolean rangeOut) {
            this.rangeOut = rangeOut;
        }

    }

    private class CosCenterProblem {

        private List<Integer> wrongPeriods;
        private List<String> wrongValues;
        private boolean wrongThreshold;

        private CosCenterProblem() {
            this.wrongPeriods = Utility.emptyList(Integer.class);
            this.wrongValues = Utility.emptyList(String.class);
            this.wrongThreshold = false;
        }

        public List<Integer> getWrongPeriods() {
            return wrongPeriods;
        }

        public void setWrongPeriods(List<Integer> wrongPeriods) {
            this.wrongPeriods = wrongPeriods;
        }

        public boolean isWrongThreshold() {
            return wrongThreshold;
        }

        public void setWrongThreshold(boolean wrongThreshold) {
            this.wrongThreshold = wrongThreshold;
        }

        public List<String> getWrongValues() {
            return wrongValues;
        }

        public void setWrongValues(List<String> wrongValues) {
            this.wrongValues = wrongValues;
        }

        @Override
        public String toString() {
            return "CosCenterProblem{" +
                    "wrongPeriods=" + wrongPeriods +
                    ", wrongValues=" + wrongValues +
                    ", wrongThreshold=" + wrongThreshold +
                    '}';
        }
    }
}
