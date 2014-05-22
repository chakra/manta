package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTextValidationResolver implements ValidationCodeResolver {


    private String fieldKey;
    private String defaulFieldName;

    public AbstractTextValidationResolver(String fieldKey, String defaulFieldName) {
        this.fieldKey = fieldKey;
        this.defaulFieldName = defaulFieldName;
    }

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        for(ValidationCode code : codes) {
            switch (code.getReason()) {
                case VALUE_IS_NOT_SET  : errors.add(isNotSet(code));  break;
                case RANGE_OUT: errors.add(isRangeOut(code));  break;
                case VALUE_IS_NOT_SUPPORTED: errors.add(isNotSupported(code));  break;
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

    public  abstract ArgumentedMessage isNotSet(ValidationCode code) throws ValidationException ;

    public  abstract ArgumentedMessage isRangeOut(ValidationCode code) throws ValidationException ;

    public  abstract ArgumentedMessage isNotSupported(ValidationCode code) throws ValidationException ;
}
