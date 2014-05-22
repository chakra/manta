package com.espendwise.manta.web.resolver;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractLoaderExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class LoaderWebExceptionResolver extends AbstractLoaderExceptionResolver {
    
    @Override
    protected ArgumentedMessage isGeneralException(ApplicationExceptionCode code, String errorMsg) {
        return new ArgumentedMessageImpl("dummyMessageKey", errorMsg);
    }

}
