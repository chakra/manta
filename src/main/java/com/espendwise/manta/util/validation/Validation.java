package com.espendwise.manta.util.validation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {

   Class<? extends ApplicationValidator>[] value();

}
