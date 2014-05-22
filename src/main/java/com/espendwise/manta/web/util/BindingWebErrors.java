package com.espendwise.manta.web.util;

import com.espendwise.manta.util.Utility;
import com.espendwise.manta.util.arguments.Args;
import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;


public class BindingWebErrors extends WebErrors {

    private BindingResult bindingResult;

    public BindingWebErrors(BindingResult bindingResult) {
        super();
        this.bindingResult = bindingResult;
        if (this.bindingResult != null) {
            for (ObjectError error : this.bindingResult.getAllErrors()) {
                super.add(MessageType.ERROR, Utility.toList(new WebError(error.getCode(), Args.typed(error.getArguments()), error.getDefaultMessage())));
            }
        }
    }


    @Override
    public void add(MessageType messageType, List<? extends DisplayMessage> messageList) {
        if (this.bindingResult != null) {
            super.add(messageType, messageList);
            for (DisplayMessage m : messageList) {
                bindingResult.addError(new ObjectError(messageType.name(), m.getCodes(), m.getArguments(), m.getDefaultMessage()));
            }
        }
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
