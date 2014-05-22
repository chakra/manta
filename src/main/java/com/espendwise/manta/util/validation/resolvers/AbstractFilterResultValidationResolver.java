package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilterResultValidationResolver implements ValidationCodeResolver {


    private String fieldKey;
    private String defaulFieldName;

    public AbstractFilterResultValidationResolver(String fieldKey, String defaulFieldName) {
        this.fieldKey = fieldKey;
        this.defaulFieldName = defaulFieldName;
    }

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        for(ValidationCode code : codes) {
            switch (code.getReason()) {
                case RESULT_SIZE_LIMIT_OUT: errors.add(isSizeOut(code));  break;
                default: break;
            }
        }

        return errors;
    }


    public String getFieldKey() {
        return fieldKey;
    }

    public String getDefaulFieldName() {
        return defaulFieldName;
    }

    public  abstract ArgumentedMessage isSizeOut(ValidationCode code) throws ValidationException ;
}
