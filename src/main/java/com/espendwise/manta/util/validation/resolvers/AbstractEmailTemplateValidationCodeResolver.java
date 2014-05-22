package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.model.view.EmailTemplateIdentView;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.ValidationException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEmailTemplateValidationCodeResolver implements ValidationResolver<ValidationException> {

    private static final Logger logger = Logger.getLogger(AbstractEmailTemplateValidationCodeResolver.class);

    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.EmailTemplateUpdateReason) {

                    switch ((ExceptionReason.EmailTemplateUpdateReason) code.getReason()) {
                        case EMAIL_TEMPLATE_MUST_BE_UNIQUE:
                            errors.add(isTemplateNameNotUnique(code, (EmailTemplateIdentView) code.getArguments()[0].get()));
                            break;
                        case EMAIL_TEMPLATE_CANT_BE_RENAMED:
                            errors.add(isEmailTemplateCantBeRenamed(code, (EmailTemplateIdentView) code.getArguments()[0].get(), (String) (code.getArguments()[1]).get()));
                            break;
                    }

                }

            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isTemplateNameNotUnique(ApplicationExceptionCode code, EmailTemplateIdentView template);

    protected abstract ArgumentedMessage isEmailTemplateCantBeRenamed(ApplicationExceptionCode code, EmailTemplateIdentView template, String originalName);

}