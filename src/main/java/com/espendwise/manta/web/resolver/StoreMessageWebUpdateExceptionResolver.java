package com.espendwise.manta.web.resolver;


import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.TypedArgument;
import com.espendwise.manta.util.validation.resolvers.AbstractStoreMessageUpdateExceptionResolver;
import com.espendwise.manta.util.trace.ApplicationExceptionCode;

public class StoreMessageWebUpdateExceptionResolver extends AbstractStoreMessageUpdateExceptionResolver {

    @Override
    protected ArgumentedMessage isEndDateBeforeCurrentDate(ApplicationExceptionCode code) {

        TypedArgument[] args = new TypedArgument[]{
                Args.i18nTyped("admin.message.label.endDate")[0],
                code.getArguments()[0],
                Args.i18nTyped("admin.global.text.error.param.currentDate")[0],
                code.getArguments()[1]
        };

        return new ArgumentedMessageImpl("validation.web.error.wrongDateRange", args);
    }

    @Override
    protected ArgumentedMessage isEndDateBeforePostedDate(ApplicationExceptionCode code) {

        TypedArgument[] args = new TypedArgument[]{
                Args.i18nTyped("admin.message.label.endDate")[0],
                code.getArguments()[0],
                Args.i18nTyped("admin.message.label.postingDate")[0],
                code.getArguments()[1]
        };

        return new ArgumentedMessageImpl("validation.web.error.wrongDateRange", args);

    }

    @Override
    protected ArgumentedMessage isPostedDateBeforeCurrentDate(ApplicationExceptionCode code) {

        TypedArgument[] args = new TypedArgument[]{
                Args.i18nTyped("admin.message.label.postingDate")[0],
                code.getArguments()[0],
                Args.i18nTyped("admin.global.text.error.param.currentDate")[0],
                code.getArguments()[1],
        };

        return new ArgumentedMessageImpl("validation.web.error.wrongDateRange", args);
    }
    
    @Override
    protected ArgumentedMessage isForceReadCountLessThanOriginal(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.countLessThanOriginal", code.getArguments());
    }

    @Override
    protected ArgumentedMessage isMessageTitleNotUnique(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.messageTitleNotUnique", code.getArguments());
    }
    
    @Override
    protected ArgumentedMessage isMessageNameNotUnique(ApplicationExceptionCode code) {
        return new ArgumentedMessageImpl("validation.web.error.messageNameNotUnique", code.getArguments());
    }
}
