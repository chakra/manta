package com.espendwise.manta.util.validation.resolvers;

import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.List;

public class ExceptionResolver  implements ValidationCodeResolver {

    @Override
    public List<? extends ArgumentedMessage> resolve(ValidationCode[] codes) throws ValidationException {
        throw new ValidationException(codes);
    }
}
