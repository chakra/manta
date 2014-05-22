package com.espendwise.manta.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Clean {

    boolean ignoreErrors()  default false;

    boolean before()  default false;

    String[] value() default {};  }
