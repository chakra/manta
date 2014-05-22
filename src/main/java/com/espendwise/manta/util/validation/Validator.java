package com.espendwise.manta.util.validation;


public interface Validator<T> {
    public ValidationResult validate(T obj) throws ValidationException;
}
