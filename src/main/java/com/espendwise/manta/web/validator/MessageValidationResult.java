package com.espendwise.manta.web.validator;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.TypedMessage;
import com.espendwise.manta.util.validation.ValidationResult;

import java.util.List;

public class MessageValidationResult implements ValidationResult {

    private List<? extends TypedMessage> messages;

    public MessageValidationResult(List<? extends TypedMessage> messages) {
        this.messages = messages;
    }

    @Override
    public List<? extends ArgumentedMessage> getResult() {
        return messages;
    }

}
