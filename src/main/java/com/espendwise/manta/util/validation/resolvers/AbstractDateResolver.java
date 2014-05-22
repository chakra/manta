package com.espendwise.manta.util.validation.resolvers;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDateResolver  implements ValidationCodeResolver {


    public AbstractDateResolver() {
    }

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {

        List<ArgumentedMessage> errors = new ArrayList<ArgumentedMessage>();

        for(ValidationCode code : codes) {
            switch ( code.getReason()) {
                case      VALUE_IS_NOT_SET  : errors.add(isNotSet(code));           break;
                case      WRONG_DATE_FORMAT : errors.add(isWrongDateFormat(code));  break;
                case      WRONG_DATE_FORMAT_EXT : errors.add(isWrongDateFormatExt(code));  break;
                case      RANGE_OUT         : errors.add(isRangeOut(code)); break;
                case      DATES_RANGE_OUT   : errors.add(isDatesRangeOut(code)); break;
                case      WRONG_BOUNDS_RIGHT_BEFORE_LEFT : errors.add(isWrongBounds(code)); break;
                default: break;
            }
        }

        return errors;
    }

    protected abstract ArgumentedMessage isRangeOut(ValidationCode code) throws ValidationException;

    protected abstract ArgumentedMessage isDatesRangeOut(ValidationCode code) throws ValidationException;

    protected abstract ArgumentedMessage isWrongDateFormat(ValidationCode code)throws ValidationException ;

    public  abstract ArgumentedMessage isNotSet(ValidationCode code) throws ValidationException ;

    protected abstract ArgumentedMessage isWrongDateFormatExt(ValidationCode code)throws ValidationException ;
    
    protected abstract ArgumentedMessage isWrongBounds(ValidationCode code) throws ValidationException ;
}