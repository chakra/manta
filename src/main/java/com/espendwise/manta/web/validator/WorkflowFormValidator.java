package com.espendwise.manta.web.validator;


import java.util.HashSet;

import com.espendwise.manta.util.AppResourceHolder;
import com.espendwise.manta.util.Constants;
import com.espendwise.manta.util.RefCodeNames;
import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.MessageI18nArgument;
import com.espendwise.manta.util.validation.*;
import com.espendwise.manta.util.validation.rules.WorkflowUniqueConstraint;
import com.espendwise.manta.web.forms.WorkflowForm;
import com.espendwise.manta.web.resolver.TextErrorWebResolver;
import com.espendwise.manta.web.util.WebErrors;
import org.apache.log4j.Logger;

public class WorkflowFormValidator extends AbstractFormValidator{

    private static final Logger logger = Logger.getLogger(WorkflowFormValidator.class);

    public WorkflowFormValidator() {
        super();
    }

    @Override
    public ValidationResult validate(Object obj) {

        WebErrors errors = new WebErrors();

        WorkflowForm valueObj = (WorkflowForm) obj;

        ValidationResult vr;
        HashSet<String> supportedTypes = new HashSet<String>();
        supportedTypes.add(RefCodeNames.WORKFLOW_TYPE_CD.ORDER_WORKFLOW);

        TextValidator shortDescValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH);
        TextValidator typeValidator = Validators.getTextValidator(Constants.VALIDATION_FIELD_CRITERIA.DB_CODE_LENGTH, true, supportedTypes);

        vr = shortDescValidator.validate(valueObj.getWorkflowName(), new TextErrorWebResolver("admin.account.workflowEdit.label.workflowName"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = shortDescValidator.validate(valueObj.getWorkflowStatus(), new TextErrorWebResolver("admin.account.workflowEdit.label.workflowStatus"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }

        vr = typeValidator.validate(valueObj.getWorkflowTypeCd(), new TextErrorWebResolver("admin.account.workflowEdit.label.workflowType"));
        if (vr != null) {
            errors.putErrors(vr.getResult());
        }
        if (errors.isEmpty()) {
	        //  validate unique if Workflow Name
			ServiceLayerValidation validation = new ServiceLayerValidation();
	        validation.addRule(new WorkflowUniqueConstraint(valueObj.getAccountId(), valueObj.getWorkflowName(), valueObj.getWorkflowId(), valueObj.getWorkflowTypeCd()));
	        validation.validate();
        }
 
         return new MessageValidationResult(errors.get());
    }


}
