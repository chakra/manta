package com.espendwise.manta.util.validation;


public interface ApplicationValidator<T> {
    public ValidationResult validate(T obj) throws ValidationException;
}
