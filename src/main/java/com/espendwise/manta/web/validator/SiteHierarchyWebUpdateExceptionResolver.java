package com.espendwise.manta.web.validator;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.arguments.ObjectArgument;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.validation.resolvers.AbstractSiteHierarchyValidationCodeResolver;

import java.util.Map;


public class SiteHierarchyWebUpdateExceptionResolver extends AbstractSiteHierarchyValidationCodeResolver {

    @Override
    protected ArgumentedMessage errorOverlapingSiteConfig(ApplicationExceptionCode code, Map map) {
        return new ArgumentedMessageImpl("validation.web.error.siteHierarchy.error.overlapingLocations",new ObjectArgument<Map>(map));
    }

    @Override
    protected ArgumentedMessage errorTopLevelConfig(ApplicationExceptionCode code, String s) {
        return new ArgumentedMessageImpl("validation.web.error.siteHierarchy.error.topLevelConfigured", Args.typed(s));
    }

    @Override
    protected ArgumentedMessage errorHigherLevelConfig(ApplicationExceptionCode code, String s) {
        return new ArgumentedMessageImpl("validation.web.error.siteHierarchy.error.higherLevelIsConfigured", Args.typed(s));
    }

    @Override
    protected ArgumentedMessage errorSubLevelConfig(ApplicationExceptionCode code, String s) {
        return new ArgumentedMessageImpl("validation.web.error.siteHierarchy.error.subLevelConfigured", Args.typed(s));
    }

    @Override
    protected ArgumentedMessage duplicatedName(ApplicationExceptionCode code, String name) {
        return new ArgumentedMessageImpl("validation.web.error.siteHierarchy.error.uniqueName", Args.typed(name));
    }

    @Override
    protected ArgumentedMessage multipleSiteConfiguration(ApplicationExceptionCode code, Long locationId) {
        return new ArgumentedMessageImpl("validation.web.error.siteHierarchy.error.site.multipleConfiguration");
    }


}
