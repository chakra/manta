package com.espendwise.manta.web.resolver;

import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.validation.resolvers.AbstractBatchOrderSaveExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class BatchOrderWebSaveExceptionResolver extends AbstractBatchOrderSaveExceptionResolver {

    @Override
    protected ArgumentedMessage isErrorOnLine(ApplicationExceptionCode code, String lineNum, String errorMsg) {
        return new ArgumentedMessageImpl("validation.web.batchOrder.error.errorOnLine", Args.typed(lineNum, errorMsg));
    }
    
    @Override
    protected ArgumentedMessage isGeneralException(ApplicationExceptionCode code, String errorMsg) {
        return new ArgumentedMessageImpl("validation.web.error.errorMessage", Args.typed(errorMsg));
    }

}
