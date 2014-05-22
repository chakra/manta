package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.resolvers.AbstractSiteHierarchyValidationCodeResolver;
import com.espendwise.manta.util.validation.resolvers.AbstractWorkflowValidationCodeResolver;

import java.util.Set;


public class WorkflowWebUpdateExceptionResolver extends AbstractWorkflowValidationCodeResolver {


    @Override
    protected ArgumentedMessage workflowRuleInUseDel(ApplicationExceptionCode code, Set ruleIds) {
        return new ArgumentedMessageImpl("validation.web.error.workflow.error.rulesInUseDel", new ObjectArgument<Set>(ruleIds));
    }
    @Override
    protected ArgumentedMessage workflowRuleInUseUpd(ApplicationExceptionCode code, Set ruleIds) {
        return new ArgumentedMessageImpl("validation.web.error.workflow.error.rulesInUseUpd", new ObjectArgument<Set>(ruleIds));
    }

    @Override
    protected ArgumentedMessage workflowNoRulesSelected(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.workflow.error.noRulesSelected");
    }

    @Override
    protected ArgumentedMessage workflowRuleSkuNotFound(ApplicationExceptionCode code, Set ruleIds) {
        return new ArgumentedMessageImpl("validation.web.error.workflow.error.rulesSkuNotFound", new ObjectArgument<Set>(ruleIds));
    }

    @Override
    protected ArgumentedMessage workflowNotUnique(ApplicationExceptionCode code, String workflowName) {
        return new ArgumentedMessageImpl("validation.web.error.workflow.error.workflowNotUnique", new ObjectArgument<String>(workflowName));
    }

}
