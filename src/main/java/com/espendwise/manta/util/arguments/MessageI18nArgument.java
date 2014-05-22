package com.espendwise.manta.util.arguments;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.ArgumentedMessageImpl;
import com.espendwise.manta.util.arguments.resolvers.ResolverFactory;

public class MessageI18nArgument extends I18nArgument<ArgumentedMessage> {

    public MessageI18nArgument(ArgumentedMessage message, ArgumentResolver<ArgumentedMessage> stringArgumentResolver) {
        super(message,stringArgumentResolver, ArgumentType.MESSAGE);
    }

    public MessageI18nArgument(ArgumentedMessage message, String defaultValue, ArgumentResolver<ArgumentedMessage> stringArgumentResolver, ArgumentType argumentType) {
        super(message, defaultValue, stringArgumentResolver, argumentType);
    }

    public MessageI18nArgument(ArgumentedMessage message) {
        super(message, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.MESSAGE, ArgumentedMessage.class), ArgumentType.MESSAGE);
    }

    public MessageI18nArgument(ArgumentedMessage message, String defaultValue) {
        super(message, defaultValue, ResolverFactory.getFactory().getArgumentResolver(ArgumentType.MESSAGE, ArgumentedMessage.class), ArgumentType.MESSAGE);
    }

    public MessageI18nArgument(String message) {
        super(new ArgumentedMessageImpl(message), ResolverFactory.getFactory().getArgumentResolver(ArgumentType.MESSAGE, ArgumentedMessage.class), ArgumentType.MESSAGE);
    }
}