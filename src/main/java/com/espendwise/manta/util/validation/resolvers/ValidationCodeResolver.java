package com.espendwise.manta.util.validation.resolvers;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.spi.ValidationResolver;
import com.espendwise.manta.util.validation.ValidationCode;
import com.espendwise.manta.util.validation.ValidationException;

import java.util.List;

public interface ValidationCodeResolver extends ValidationResolver<ValidationCode[]> {
    @Override
    List<? extends ArgumentedMessage> resolve(ValidationCode[] code) throws ValidationException;
}
