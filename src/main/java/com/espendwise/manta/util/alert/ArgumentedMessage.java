package com.espendwise.manta.util.alert;

import com.espendwise.manta.util.arguments.TypedArgument;
import org.springframework.context.MessageSourceResolvable;


public interface ArgumentedMessage extends Message, MessageSourceResolvable {

    public String getKey();

    public TypedArgument[] getArguments();

    public String getDefaultMessage();


}
