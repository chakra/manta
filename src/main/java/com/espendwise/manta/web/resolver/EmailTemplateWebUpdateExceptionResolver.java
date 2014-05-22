package com.espendwise.manta.web.resolver;

import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractEmailTemplateValidationCodeResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;


public class EmailTemplateWebUpdateExceptionResolver extends AbstractEmailTemplateValidationCodeResolver {

    @Override
    protected ArgumentedMessage isTemplateNameNotUnique(ApplicationExceptionCode code, EmailTemplateIdentView template) {
        return new ArgumentedMessageImpl("validation.web.error.emailTemplateNotUnique");
    }

    @Override
    protected ArgumentedMessage isEmailTemplateCantBeRenamed(ApplicationExceptionCode code, EmailTemplateIdentView template, String originalName) {
        return new ArgumentedMessageImpl("validation.web.error.emailTemplateCantBeRenamed", Args.typed(originalName));
    }


}
