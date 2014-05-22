package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ExceptionReason;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractSiteHierarchyValidationCodeResolver  implements ValidationResolver<ValidationException> {


    public List<? extends ArgumentedMessage> resolve(ValidationException exception) {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        ApplicationExceptionCode[] codes = exception.getExceptionCodes();

        if (codes != null) {

            for (ApplicationExceptionCode code : codes) {

                if (code.getReason() instanceof ExceptionReason.SiteHierarchyUpdateReason) {

                    switch ((ExceptionReason.SiteHierarchyUpdateReason) code.getReason()) {
                        case MULTIPLE_SITE_CONFIGURATION:  errors.add(multipleSiteConfiguration(code, (Long) code.getArguments()[0].get()));       break;
                        case DUPLICATED_NAME:  errors.add(duplicatedName(code, (String) code.getArguments()[0].get())); break;
                        case SITE_HIER_ERROR_SUB_LEVEL_CONFIG:  errors.add(errorSubLevelConfig(code, (String) code.getArguments()[0].get()));       break;
                        case SITE_HIER_ERROR_TOP_LEVEL_CONFIG:  errors.add(errorTopLevelConfig(code, (String) code.getArguments()[0].get()));       break;
                        case SITE_HIER_ERROR_HIGHER_LEVEL_CONFIG:  errors.add(errorHigherLevelConfig(code, (String) code.getArguments()[0].get()));       break;
                        case SITE_HIER_ERROR_OVERLAPING_SITE_CONFIG:  errors.add(errorOverlapingSiteConfig(code, (Map) code.getArguments()[0].get()));       break;

                    }

                }

            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage errorOverlapingSiteConfig(ApplicationExceptionCode code, Map map);

    protected abstract ArgumentedMessage errorTopLevelConfig(ApplicationExceptionCode code, String s);

    protected abstract ArgumentedMessage errorHigherLevelConfig(ApplicationExceptionCode code, String s);

    protected abstract ArgumentedMessage errorSubLevelConfig(ApplicationExceptionCode code, String s);

    protected abstract ArgumentedMessage duplicatedName(ApplicationExceptionCode code, String name);

    protected abstract ArgumentedMessage multipleSiteConfiguration(ApplicationExceptionCode code, Long locationId);

}
