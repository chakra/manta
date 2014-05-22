package com.espendwise.manta.util.validation;


import com.espendwise.manta.spi.ValidationResolver;

public interface CustomValidator<T, R extends ValidationResolver> extends ApplicationValidator<T> {

    public ValidationResult validate(T obj, R resolver) throws ValidationException;
}
